package com.swjtu.guilimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @Author: Lil_boat
 * @Date: 2022/8/31 17:07
 * @Description: redisson的配置
 */
@Configuration
public class MyRedissonConfig {

    /**
     * 所有对Redisson的使用都是通过RedissonClient对象
     *
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {
        // 创建配置
        Config config = new Config();
        // 单节点模式
        config.useSingleServer().setAddress("redis://192.168.196.131:6379");
        // 创建实例
        return Redisson.create(config);
    }

}
