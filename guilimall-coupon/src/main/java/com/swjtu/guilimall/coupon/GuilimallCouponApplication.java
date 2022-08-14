package com.swjtu.guilimall.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
/**
 * @Author: Lil_boat
 * @Date: 16:01 2022/8/14
 * @Description: 优惠券的主启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.swjtu.guilimall.coupon.dao")
public class GuilimallCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuilimallCouponApplication.class, args);
    }

}
