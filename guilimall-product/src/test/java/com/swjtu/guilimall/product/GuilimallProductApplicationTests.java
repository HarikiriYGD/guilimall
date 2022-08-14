package com.swjtu.guilimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.swjtu.guilimall.product.entity.BrandEntity;
import com.swjtu.guilimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GuilimallProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Test
    void contextLoads() {

        // BrandEntity brandEntity = new BrandEntity();

        // brandEntity.setName("华为");

        // brandService.save(brandEntity);
        // System.out.println("保存成功");

        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("brand_id",1L);
        List<BrandEntity> list = brandService.list(queryWrapper);
        list.forEach(System.out::println);
    }

}
