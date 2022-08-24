package com.swjtu.guilimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swjtu.common.utils.PageUtils;
import com.swjtu.guilimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author Lil_Boat
 * @email GuodanYang_nn@163.com
 * @date 2022-08-14 10:14:03
 */
public interface CategoryService extends IService<CategoryEntity> {

    /**
     * 封装
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询所有的商品服务
     * @return
     */
    List<CategoryEntity> listWithTree();

    /**
     * 批量删除菜单
     * @param asList
     */
    void removeMenuByIds(List<Long> asList);

    /**
     * 找到catelogId的完整路径
     * [父/子/后代]
     * @param catelogId
     * @return
     */
    Long[] findCatelogPath(Long catelogId);

    void updateCascade(CategoryEntity category);
}

