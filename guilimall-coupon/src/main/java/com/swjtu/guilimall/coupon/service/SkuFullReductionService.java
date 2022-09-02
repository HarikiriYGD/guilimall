package com.swjtu.guilimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swjtu.common.to.SkuReductionTo;
import com.swjtu.common.utils.PageUtils;
import com.swjtu.guilimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author Lil_Boat
 * @email GuodanYang_nn@163.com
 * @date 2022-08-14 14:50:00
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTo skuReductionTo);
}

