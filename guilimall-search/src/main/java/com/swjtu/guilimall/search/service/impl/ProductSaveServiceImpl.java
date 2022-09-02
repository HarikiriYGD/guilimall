package com.swjtu.guilimall.search.service.impl;

import com.alibaba.nacos.common.utils.JacksonUtils;
import com.swjtu.common.to.es.SkuEsModel;
import com.swjtu.guilimall.search.config.GuilimallElasticsearchConfig;
import com.swjtu.guilimall.search.constant.EsConstant;
import com.swjtu.guilimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Lil_boat
 * @Date: 2022/8/28 16:57
 * @Description:
 */
@Service
@Slf4j
public class ProductSaveServiceImpl implements ProductSaveService {

    @Resource
    private RestHighLevelClient client;

    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        // 保存到es
        // 1. 给es中建立索引 product 建立映射关系

        // 2. 给es中保存这些数据
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            // 构建保存请求
            IndexRequest request = new IndexRequest(EsConstant.PRODUCT_INDEX);
            // 设置唯一id
            request.id(skuEsModel.getSkuId().toString());
            // 转为json
            String s = JacksonUtils.toJson(skuEsModel);
            request.source(s, XContentType.JSON);

            bulkRequest.add(request);
        }
        BulkResponse bulk = client.bulk(bulkRequest, GuilimallElasticsearchConfig.COMMON_OPTIONS);
        // TODO 如果批量错误
        boolean b = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map(BulkItemResponse::getId).collect(Collectors.toList());
        log.error("商品上架错误：{}", collect);
        return b;
    }
}
