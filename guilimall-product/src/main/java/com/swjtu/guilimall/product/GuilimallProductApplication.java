package com.swjtu.guilimall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
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
 *      4.2 controller
 *    5. 自定义校验
 *      5.1 编写一个自定义的校验注解
 *      5.2 编写一个自定义的校验器
 *      5.3 关联自定义的校验器和自定义的校验注解
 *
 *  统一异常处理
 * @ControllerAdvice
 *
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GuilimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuilimallProductApplication.class, args);
    }

}
