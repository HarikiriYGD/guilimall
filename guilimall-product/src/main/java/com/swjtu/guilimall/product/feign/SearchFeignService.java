package com.swjtu.guilimall.product.feign;

import com.swjtu.common.to.es.SkuEsModel;
import com.swjtu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author: Lil_boat
 * @Date: 2022/8/28 17:12
 * @Description:
 */
@FeignClient("guilimall-search")
public interface SearchFeignService {

    @PostMapping("/search/save/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
