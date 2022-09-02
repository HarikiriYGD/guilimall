package com.swjtu.guilimall.search.service;

import com.swjtu.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @Author: Lil_boat
 * @Date: 2022/8/28 16:55
 * @Description:
 */
public interface ProductSaveService {
    /**
     * 保存所有的sku数据到es中
     * @param skuEsModels
     */
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
