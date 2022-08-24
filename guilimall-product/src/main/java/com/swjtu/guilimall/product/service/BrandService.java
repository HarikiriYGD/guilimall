package com.swjtu.guilimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swjtu.common.utils.PageUtils;
import com.swjtu.guilimall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author Lil_Boat
 * @email GuodanYang_nn@163.com
 * @date 2022-08-14 10:14:03
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateDetail(BrandEntity brand);
}

