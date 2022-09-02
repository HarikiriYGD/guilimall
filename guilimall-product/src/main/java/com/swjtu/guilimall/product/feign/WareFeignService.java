package com.swjtu.guilimall.product.feign;

import com.swjtu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author: Lil_boat
 * @Date: 2022/8/28 16:31
 * @Description:
 */
@FeignClient("guilimall-ware")
public interface WareFeignService {

    /**
     * 远程调用ware查询是否有库存
     * @param skuIds
     * @return
     */
    @PostMapping("/ware/waresku/hasstock")
    R getSkusHasStock(@RequestBody List<Long> skuIds);

}
