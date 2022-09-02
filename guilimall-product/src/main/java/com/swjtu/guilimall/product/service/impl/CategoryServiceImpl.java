package com.swjtu.guilimall.product.service.impl;

import com.alibaba.nacos.common.utils.JacksonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.swjtu.guilimall.product.service.CategoryBrandRelationService;
import com.swjtu.guilimall.product.vo.Catelog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swjtu.common.utils.PageUtils;
import com.swjtu.common.utils.Query;

import com.swjtu.guilimall.product.dao.CategoryDao;
import com.swjtu.guilimall.product.entity.CategoryEntity;
import com.swjtu.guilimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import static java.util.concurrent.TimeUnit.SECONDS;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    private static final String DB_LOCK = "DB_LOCK";

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 查询所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        // 组装父子的树形结构
        // 找到所有的一级父类
        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0
        ).map((menu) -> {
            menu.setChildren(getChildren(menu, entities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return level1Menus;
    }


    /**
     * 递归查询所有菜单的子菜单
     *
     * @param root
     * @param all
     * @return
     */
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid().equals(root.getCatId());
        }).map(categoryEntity -> {
            // 找到子菜单
            categoryEntity.setChildren(getChildren(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            // menu1.getSort().compareTo(menu2.getSort());
            // 菜单的排序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return children;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        // TODO

        // 逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }


    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        // 1. 收集当前节点id
        paths.add(catelogId);
        // 2. 查询父亲节点的全部信息
        CategoryEntity byId = this.getById(catelogId);
        // 3. 如果还存父亲节点，递归查找
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }

    /**
     * 级联更新所有关联的数据
     *
     * @CacheEvict: 缓存失效 属性allEntries = true则表示全部删除
     * @Caching: 同时失效多种
     * @param category
     */
    @Transactional(rollbackFor = Exception.class)
//    @Caching(evict = {
//            @CacheEvict(value = "category", key = "'getLevel1Category'"),
//            @CacheEvict(value = "category", key = "'getCatalogJson'")
//    })
    @CacheEvict(value = "category", allEntries = true)
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
        // TODO 同时修改缓存中的数据
    }

    /**
     * 每一个需要缓存的数据都来指定放到哪个名字的缓存【缓存的分区（按照业务的名字区分）】
     * <p>
     * 代表当前方法的结果需要缓存，如果缓存中有，方法不用调用。
     * 如果缓存中没有，会调用方法，最后将方法的结果存入缓存
     * <p>
     * 默认行为：
     * 1)、如果缓存中有，方法不调用
     * 2)、key默认自动生成，缓存的名字：simpleKey
     * 3)、缓存的value的值，默认使用jdk序列化机制，将序列化后的数据存到redis
     * 4)、默认ttl时间 -1
     *
     * @return
     */
    @Cacheable(value = {"category"}, key = "#root.method.name")
    @Override
    public List<CategoryEntity> getLevel1Category() {
        System.out.println("come in........");
        return this.baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));

    }

    @Cacheable(value = {"category"}, key = "#root.method.name")
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        System.out.println("查询了数据库");
        // 查询所有的分类
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        // 1. 查询所有的分类
        List<CategoryEntity> level1Category = getParent_cid(selectList, 0L);
        // 2. 封装数据
        Map<String, List<Catelog2Vo>> parent_cid = level1Category.stream().collect(Collectors.toMap(key -> key.getCatId().toString(), value -> {
            // 遍历每一个一级分类，查找这个一级分类的二级分类
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, value.getCatId());
            // 封装上面的数据
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(level2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo();
                    catelog2Vo.setCatalog1Id(value.getCatId().toString());
                    catelog2Vo.setId(level2.getCatId().toString());
                    catelog2Vo.setName(level2.getName());
                    // 查找当前二级分类的三级分类
                    List<CategoryEntity> level3Catelog = getParent_cid(selectList, level2.getCatId());
                    if (level3Catelog != null) {
                        // 封装数据
                        List<Catelog2Vo.Catelog3Vo> catelog3Vos = level3Catelog.stream().map(level3 -> {
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo();
                            catelog3Vo.setCatalog2Id(level2.getCatId().toString());
                            catelog3Vo.setId(level3.getCatId().toString());
                            catelog3Vo.setName(level3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(catelog3Vos);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));
        return parent_cid;
    }

    /**
     * TODO 产生堆外内存溢出：OutOfDirectMemoryError
     * 1. springboot2.0以后默认使用lettuce作为操作redis的客户端。它使用netty进行网络通信。
     * 2. lettuce的bug导致netty堆外内存溢出 -Xmx300m:netty如果没有它指定堆外内存，默认使用-Xmx300m
     * 可以通过-Dio.netty.maxDirectMemory进行设置
     * <p>
     * 解决方案：不能使用-Dio.netty.maxDirectMemory只取调大堆外内存。
     * 1. 升级Lettuce客户端
     * 2. 切换使用jedis
     * <p>
     * redisTemplate:
     * lettuce、jedis操作redis的底层客户端。spring再次封装redisTemplate
     *
     * @return 返回封装好的数据
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonOld() {
        /**
         * 1、空结果缓存：解决缓存穿透
         * 2、设置过期时间（加随机值）:解决缓存雪崩
         * 3、加锁：解决缓存击穿
         */

        // 1. 加入缓存逻辑
        String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
        if (!StringUtils.hasLength(catalogJSON)) {
            // 2. 缓存中没有
            Map<String, List<Catelog2Vo>> catalogJsonFromDb = getCatalogJsonFromDbWithRedisLock();
            // 3. 查到的数据放入缓存,将对象转为json放在缓存中

            return catalogJsonFromDb;
        }
        // 给缓存中放json字符串，拿出的json字符串，还要逆转为能有的对象类型：【序列化与反序列化】
        Map<String, List<Catelog2Vo>> result = JacksonUtils.toObj(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });
        return result;
    }

    /**
     * 缓存里面的数据如何和数据库保持一致
     * 缓存数据一致性问题：
     * 解决方案一：【双写模式】修改数据库的同时，也修改缓存中的数据
     * 解决方案二：【失效模式】修改数据的同时，把缓存中的数据删除
     *
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithRedissonLock() {
        // 1. 占分布式锁，去redis占坑
        // 锁的粒度，越细越快
        RLock lock = redissonClient.getLock(DB_LOCK);
        lock.lock(30, TimeUnit.SECONDS);
        Map<String, List<Catelog2Vo>> dataFromDb;
        try {
            dataFromDb = getDataFromDb();
        } finally {
            lock.unlock();
        }
        return dataFromDb;
    }


    /**
     * 从数据库查询并封装分类数据
     * 使用redis实现分布式锁
     *
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithRedisLock() {

        // 1. 占分布式锁 去redis占坑
        // 2. 自动设置过去时间    原子命令
        String uuid = DB_LOCK + UUID.randomUUID().toString();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(DB_LOCK, uuid, 30, SECONDS);

        if (lock) {
            System.out.println("获取分布式锁成功");
            Map<String, List<Catelog2Vo>> dataFromDb = null;
            try {
                // 加锁成功...执行业务
                dataFromDb = getDataFromDb();

            } finally {
                // 删锁也要保证原子操作 不然也可以删除别人的锁
                // lua脚本 ||  redis的事务也可以
                String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
                // 删除锁
                Long execute = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script), Arrays.asList(DB_LOCK), uuid);
            }
            return dataFromDb;
        } else {
            // 加锁失败....重试synchronized ()
            // 自旋方式 可以休眠100ms
            System.out.println("获取分布式锁失败...等待重试");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getCatalogJsonFromDbWithLocalLock();
        }
    }


    /**
     * 从数据库中查询数据
     *
     * @return
     */
    private Map<String, List<Catelog2Vo>> getDataFromDb() {
        String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
        if (StringUtils.hasLength(catalogJSON)) {
            // 缓存不为空，直接返回结果
            Map<String, List<Catelog2Vo>> result = JacksonUtils.toObj(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
            return result;
        }

        // 查询所有的分类
        List<CategoryEntity> selectList = baseMapper.selectList(null);

        // 1. 查询所有的分类
        List<CategoryEntity> level1Category = getParent_cid(selectList, 0L);

        // 2. 封装数据
        Map<String, List<Catelog2Vo>> parent_cid = level1Category.stream().collect(Collectors.toMap(key -> key.getCatId().toString(), value -> {
            // 遍历每一个一级分类，查找这个一级分类的二级分类
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, value.getCatId());
            // 封装上面的数据
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(level2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo();
                    catelog2Vo.setCatalog1Id(value.getCatId().toString());
                    catelog2Vo.setId(level2.getCatId().toString());
                    catelog2Vo.setName(level2.getName());
                    // 查找当前二级分类的三级分类
                    List<CategoryEntity> level3Catelog = getParent_cid(selectList, level2.getCatId());
                    if (level3Catelog != null) {
                        // 封装数据
                        List<Catelog2Vo.Catelog3Vo> catelog3Vos = level3Catelog.stream().map(level3 -> {
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo();
                            catelog3Vo.setCatalog2Id(level2.getCatId().toString());
                            catelog3Vo.setId(level3.getCatId().toString());
                            catelog3Vo.setName(level3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(catelog3Vos);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));

        String json = JacksonUtils.toJson(parent_cid);
        stringRedisTemplate.opsForValue().set("catalogJSON", json, 1, TimeUnit.DAYS);

        return parent_cid;
    }

    /**
     * 从数据库查询并封装分类数据
     * 使用本地锁实现的分布式锁
     * 单机版可以
     * 分布式锁不住
     *
     * @return
     */
    public synchronized Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithLocalLock() {

        /**
         * 优化：1.将数据库的多次查询变为一次查询
         *      2. 整合redis
         *          2.1 synchronized(this):SpringBoot所有的组件在容器中都是单例的
         *          2.2 得到锁之后，我们应该再去缓存中确定一次，如果没有才需要继续查询
         *
         *
         */
        // TODO 本地锁：synchronized, JUC(Lock),在分布式情况下，必须使用分布式锁
        return getDataFromDb();
    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList, Long parent_cid) {
        List<CategoryEntity> collect = selectList.stream().filter(item -> item.getParentCid().equals(parent_cid)).collect(Collectors.toList());
        return collect;
    }
}