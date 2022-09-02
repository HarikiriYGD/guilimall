package com.swjtu.guilimall.product.feign;

import com.swjtu.common.to.SkuReductionTo;
import com.swjtu.common.to.SpuBoundTo;
import com.swjtu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: Lil_boat
 * @Date: 2022/8/25 22:15
 * @Description:
 */
@FeignClient("guilimall-coupon")
public interface CouponFeignService {

    /**
     * 远程调用优惠服务的controller
     * @param spuBoundTo
     * @return
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);


    /**
     * 远程调用优惠服务，存储满减信息
     * @param skuReductionTo
     * @return
     */
    @PostMapping("/coupon/skufullreduction/saveInfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
