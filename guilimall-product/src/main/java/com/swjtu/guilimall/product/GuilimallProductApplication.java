package com.swjtu.guilimall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author: Lil_boat
 * @Date: 16:02 2022/8/14
 * @Description: 商品的主启动类
 *
 *
 * 逻辑删除：
 *  1. 配置全局的逻辑删除规则
 *  2. 配置逻辑删除的组件(mybatis-plus v3.1.1之前)
 *  3. 加上逻辑删除注解
 *
 *  云存储对象信息存储步骤：
 *  1. 引入oss-starter
 *  2. 配置key，endpoint相关信息
 *  3. 使用OSSClient进行操作
 *
 *  JSR303
 *    1. 给Bean添加校验注解：javax.validation.constraints，并定义自己的message提示
 *    2. 开启校验功能@Valid：校验错误以后会有默认的响应
 *    3. 给校验的bean后紧跟一个BindingResult，就可以获取到校验的结果
 *    4. 分组校验
 *      4.1 给校验注解标注什么情况需要进行校验
 *      4.2 app
 *    5. 自定义校验
 *      5.1 编写一个自定义的校验注解
 *      5.2 编写一个自定义的校验器
 *      5.3 关联自定义的校验器和自定义的校验注解
 *
 *  统一异常处理
 * @ControllerAdvice
 *
 *  Spring-Cache的不足：
 *  1、读模式：
 *      缓存穿透：查询一个null的数据。解决：缓存空数据，cache-null-values=true
 *      缓存击穿：大量并发进来查询一个正好过期的key。解决：加锁
 *      缓存雪崩：大量的key同时过期。：解决：随机加时间
 *  2. 写模式：（缓存与数据库一致）
 *      (1)、读写加锁
 *      (2)、引入canal，感知到MySQL的更新去更新数据库
 *      (3)、读多写多，直接去数据库查询就行
 *
 */
@EnableFeignClients(basePackages = "com.swjtu.guilimall.product.feign")
@SpringBootApplication
@EnableDiscoveryClient
public class GuilimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuilimallProductApplication.class, args);
    }

}
