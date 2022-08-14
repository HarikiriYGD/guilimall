package com.swjtu.guilimall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: Lil_boat
 * @Date: 16:01 2022/8/14
 * @Description: 会员服务的主启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.swjtu.guilimall.member.feign")
public class GuilimallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuilimallMemberApplication.class, args);
    }

}
