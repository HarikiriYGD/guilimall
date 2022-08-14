package com.swjtu.guilimall.member.feign;

import com.swjtu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: Lil_boat
 * @Date: 2022/8/14 16:17
 * @Description: 会员远程调用优惠券服务
 */
@FeignClient(value = "guilimall-coupon")
@Component
public interface CouponFeignService {

    @RequestMapping("/coupon/coupon/member/list")
    public R memberCoupons();

}
