package com.swjtu.guilimall.ware.feign;

import com.swjtu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: Lil_boat
 * @Date: 2022/8/26 17:07
 * @Description:
 */
@FeignClient("guilimall-product")
public interface ProductFeignService {

    /**
     *
     * @param skuId
     * @return
     */
    @RequestMapping("product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);

}
