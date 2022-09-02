package com.swjtu.guilimall.ware.vo;

import lombok.Data;

/**
 * @Author: Lil_boat
 * @Date: 2022/8/26 16:33
 * @Description:
 */
@Data
public class PurchaseItemDoneVo {

    /**
     * 采购项id
     */
    private Long itemId;
    /**
     * 是否完成
     */
    private Integer status;
    /**
     * 原因
     */
    private String reason;

}
