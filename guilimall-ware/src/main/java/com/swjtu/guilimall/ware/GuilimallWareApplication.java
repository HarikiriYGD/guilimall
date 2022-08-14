package com.swjtu.guilimall.ware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * @Author: Lil_boat
 * @Date: 16:00 2022/8/14
 * @Description: 仓库的主启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GuilimallWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuilimallWareApplication.class, args);
    }

}
