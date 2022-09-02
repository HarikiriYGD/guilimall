package com.swjtu.guilimall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: Lil_boat
 * @Date: 2022/8/26 16:32
 * @Description: 完成采购的VO
 */
@Data
public class PurchaseDoneVo {

    @NotNull
    private Long id;

    private List<PurchaseItemDoneVo> items;
}
