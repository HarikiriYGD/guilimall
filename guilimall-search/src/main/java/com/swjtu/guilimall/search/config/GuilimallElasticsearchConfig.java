package com.swjtu.guilimall.search.config;

import org.apache.http.HttpHost;
import org.apache.lucene.queryparser.surround.parser.Token;
import org.elasticsearch.client.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Lil_boat
 * @Date: 2022/8/28 14:00
 * @Description: 1. 导入依赖
 * 2. 编写配置
 * 3. 参照API文档
 */
@Configuration
public class GuilimallElasticsearchConfig {

    public static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
//        builder.addHeader("Authorization", "Bearer" + Token);
//        builder.setHttpAsyncResponseConsumerFactory(
//                new HttpAsyncResponseConsumerFactory
//                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient esRestClient() {
        RestClientBuilder builder = null;

        builder = RestClient.builder(new HttpHost("192.168.196.131", 9200, "http"));

        RestHighLevelClient client = new RestHighLevelClient(builder);

        return client;
    }

}
