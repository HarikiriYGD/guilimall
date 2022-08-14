package com.swjtu.guilimall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * @Author: Lil_boat
 * @Date: 16:02 2022/8/14
 * @Description: 商品的主启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GuilimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuilimallProductApplication.class, args);
    }

}
