package com.swjtu.guilimall.ware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: Lil_boat
 * @Date: 16:00 2022/8/14
 * @Description: 仓库的主启动类
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.swjtu.guilimall.ware.feign")
public class GuilimallWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuilimallWareApplication.class, args);
    }

}
