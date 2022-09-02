package com.swjtu.guilimall.product.web;

import com.swjtu.guilimall.product.entity.CategoryEntity;
import com.swjtu.guilimall.product.service.CategoryService;
import com.swjtu.guilimall.product.vo.Catelog2Vo;
import org.checkerframework.checker.index.qual.GTENegativeOne;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Lil_boat
 * @Date: 2022/8/30 14:47
 * @Description:
 */
@Controller

public class IndexController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private RedissonClient redisson;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        // TODO 查出所有的一级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Category();

        // 视图解析器进来拼串
        // classpath:/templates/ .html
        model.addAttribute("categorys", categoryEntities);
        return "index";
    }

    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        Map<String, List<Catelog2Vo>> map = categoryService.getCatalogJson();
        return map;
    }


    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        // 1. 获取一把锁，只要锁的名字一样，就是同一把锁
        RLock lock = redisson.getLock("my-lock");
        // 2. 加锁  阻塞式等待 lock.lock();

        // 自动解锁时间一定要大于业务执行时间
        // 存在的问题：不能自动续期
        // 如果我们传递了锁的超时时间，就发送给redis执行lua脚本，进行占锁，默认超时就是我们指定的时间
        // 如果我们未指定锁的超时时间，就使用30 * 1000 millSeconds【LockWatchdogTimeout看门狗的默认时间】
        // 只要占锁成功，就会启动一个定时任务【重新给锁设置过期时间，新的过期时间就是看门狗的默认时间】

        // 每隔10s 续期到满时间
        // internalLockLeaseTime【看门狗时间】 / 3 = 10s

        lock.lock(10, TimeUnit.SECONDS);
        // 锁的自动续期，如果业务超长，运行期间自动给锁续上新的30s。
        // 加锁的业务只要运行完成，就不会给当前锁续期，即使不手动解锁，锁默认在30s后自动解锁
        try {
            System.out.println("加锁成功，执行业务");
            Thread.sleep(30000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 3. 解锁
            lock.unlock();
        }
        return "hello";
    }

    /**
     * 读 + 读： 相当于没有锁 因为是共享锁
     * 写 + 写： 要等待前一个写锁之后才能继续写 互斥锁
     * 写 + 读： 要等写锁释放完之后，才可以读
     * 读 + 写： 要等读完之后，才可以写
     * @return
     */
    @GetMapping("/write")
    @ResponseBody
    public String writeValue() {
        RReadWriteLock lock = redisson.getReadWriteLock("rw-lock");
        String s = "";
        RLock rLock = lock.writeLock();
        try {
            // 1. 改数据加写锁，读数据加读锁
            rLock.lock();
            s = UUID.randomUUID().toString();
            Thread.sleep(30000);
            redisTemplate.opsForValue().set("writeValue", s);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return s;
    }

    @GetMapping("/read")
    @ResponseBody
    public String readValue() {
        RReadWriteLock lock = redisson.getReadWriteLock("rw-lock");
        String s = "";
        // 加读锁
        RLock rLock = lock.readLock();
        try {
            rLock.lock();
            Thread.sleep(30000);
            s = redisTemplate.opsForValue().get("writeValue");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return s;
    }

    /**
     * 车库停车
     * 3车位
     */
    @GetMapping("/park")
    @ResponseBody
    public String park() throws InterruptedException {
        RSemaphore park = redisson.getSemaphore("park");
        // 获取一个信号，获取一个值
        boolean b = park.tryAcquire();
        return "ok" + b;
    }

    @GetMapping("/go")
    @ResponseBody
    public String go(){
        RSemaphore park = redisson.getSemaphore("park");
        // 释放一个信号
        park.release();
        return "ok";
    }

    /**
     * 放假，锁门
     *
     * @return
     */
    @GetMapping("/lockDoor")
    @ResponseBody
    public String lockDoor() throws InterruptedException {
        RCountDownLatch door = redisson.getCountDownLatch("door");
        door.trySetCount(5);
        // 等待闭锁完成
        door.await();
        return "放假了";
    }

    @GetMapping("/gogogo/{id}")
    @ResponseBody
    public String gogogo(@PathVariable("id") Long id){
        RCountDownLatch door = redisson.getCountDownLatch("door");
        // 计数减一
        door.countDown();
        return id + "班的人走了";
    }


}
