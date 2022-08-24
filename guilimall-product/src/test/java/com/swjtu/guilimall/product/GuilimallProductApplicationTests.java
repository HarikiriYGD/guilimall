package com.swjtu.guilimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.swjtu.guilimall.product.entity.BrandEntity;
import com.swjtu.guilimall.product.service.BrandService;
import com.swjtu.guilimall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Slf4j
class GuilimallProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Test
    public void testFindPath() {
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        log.info("完整路径:{}", Arrays.asList(catelogPath));
    }


    @Test
    void contextLoads() {

        // BrandEntity brandEntity = new BrandEntity();

        // brandEntity.setName("华为");

        // brandService.save(brandEntity);
        // System.out.println("保存成功");

        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("brand_id", 1L);
        List<BrandEntity> list = brandService.list(queryWrapper);
        list.forEach(System.out::println);
    }

}
