package com.swjtu.guilimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swjtu.common.utils.PageUtils;
import com.swjtu.guilimall.ware.entity.WareSkuEntity;
import com.swjtu.guilimall.ware.vo.SkuHasStockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author Lil_Boat
 * @email GuodanYang_nn@163.com
 * @date 2022-08-14 14:59:21
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     *
     * @param skuId sku_id
     * @param wareId 仓库的id
     * @param skuNum sku的数量
     */
    void addStock(Long skuId, Long wareId, Integer skuNum);

    /**
     * 查询是否有库存
     * @param skuIds
     * @return
     */
    List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds);
}

