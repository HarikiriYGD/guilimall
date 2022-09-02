package com.swjtu.guilimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swjtu.common.utils.PageUtils;
import com.swjtu.guilimall.product.entity.BrandEntity;
import com.swjtu.guilimall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author Lil_Boat
 * @email GuodanYang_nn@163.com
 * @date 2022-08-14 10:14:03
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    /**
     * 查询所有信息
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存分类和品牌之间的关系
     * @param categoryBrandRelation
     */
    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    /**
     * 更新品牌
     * 如：手机分类下，新增一个ONE-PLUS品牌
     * @param brandId
     * @param name
     */
    void updateBrand(Long brandId, String name);

    /**
     * 更新目录
     * 如：新增一个分类---》电子图书
     * @param catId
     * @param name
     */
    void updateCategory(Long catId, String name);

    /**
     * 查询指定分类下的所有品牌信息
     * 如：手机下游什么品牌
     * @param catId
     * @return
     */
    List<BrandEntity> getBrandsByCatId(Long catId);
}

