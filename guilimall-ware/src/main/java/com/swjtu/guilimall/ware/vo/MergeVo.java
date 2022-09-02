package com.swjtu.guilimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: Lil_boat
 * @Date: 2022/8/26 15:21
 * @Description:
 */
@Data
public class MergeVo {

    private Long purchaseId;
    private List<Long> items;

}
