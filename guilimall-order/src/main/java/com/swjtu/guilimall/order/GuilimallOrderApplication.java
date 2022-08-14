package com.swjtu.guilimall.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: Lil_boat
 * @Date: 16:01 2022/8/14
 * @Description: 订单的主启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GuilimallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuilimallOrderApplication.class, args);
    }

}
