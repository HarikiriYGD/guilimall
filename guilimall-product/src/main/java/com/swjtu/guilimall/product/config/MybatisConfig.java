package com.swjtu.guilimall.product.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: Lil_boat
 * @Date: 2022/8/24 16:14
 * @Description: 分页插件
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.swjtu.guilimall.product.dao")
public class MybatisConfig {

    /**
     * 引入分页插件
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 跳转回首页
        paginationInterceptor.setOverflow(true);
        // 每页的数据
        paginationInterceptor.setLimit(1000);
        return paginationInterceptor;
    }

}
