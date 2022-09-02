package com.swjtu.guilimall.search;

import com.alibaba.nacos.common.utils.JacksonUtils;
import com.qiniu.util.Json;
import com.swjtu.guilimall.search.config.GuilimallElasticsearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

@SpringBootTest
class GuilimallSearchApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    @Test
    void searchData() throws IOException {
        // 1. 创建检索请求
        SearchRequest searchRequest = new SearchRequest();
        // 指定索引
        searchRequest.indices("bank");
        // 指定DSL，检索条件
        // SearchSourceBuilder
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 1.1 构建检索条件
        sourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));
        sourceBuilder.from(0);
        sourceBuilder.size(5);

        AvgAggregationBuilder ageAvg = AggregationBuilders.avg("age");
        sourceBuilder.aggregation(ageAvg);

        searchRequest.source(sourceBuilder);

        // 2. 执行检索
        SearchResponse searchResponse = client.search(searchRequest, GuilimallElasticsearchConfig.COMMON_OPTIONS);

        // 3. 分析结果
        // 3.1 获取所有查到的数据
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            searchHit.getIndex();
            searchHit.getType();
            User user = JacksonUtils.toObj(searchHit.getSourceAsString(), User.class);
            System.out.println("user" + user);
        }

    }


    @Data
    class User {
        private String name;
        private String gender;
        private Integer age;
    }

    /**
     * 测试存储数据
     * 更新也可以
     */
    @Test
    void indexData() throws IOException {
        IndexRequest request = new IndexRequest("users");
        request.id("1");
//        request.source("username", "zhangsan", "age", 18, "gender", "男");
        User user = new User();
        user.setName("zhangsan");
        user.setGender("male");
        user.setAge(18);

        String json = JacksonUtils.toJson(user);
        // 要保存的内容
        request.source(json, XContentType.JSON);
        // 执行保存操作
        IndexResponse index = client.index(request, GuilimallElasticsearchConfig.COMMON_OPTIONS);
        System.out.println(index);
    }

    @Test
    void contextLoads() {
        System.out.println(client);
    }


}
