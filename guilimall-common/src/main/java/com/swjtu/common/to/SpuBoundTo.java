package com.swjtu.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Lil_boat
 * @Date: 2022/8/25 22:21
 * @Description:
 */
@Data
public class SpuBoundTo {

    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
