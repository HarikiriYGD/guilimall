package com.swjtu.guilimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swjtu.common.utils.PageUtils;
import com.swjtu.guilimall.ware.entity.PurchaseEntity;
import com.swjtu.guilimall.ware.vo.MergeVo;
import com.swjtu.guilimall.ware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author Lil_Boat
 * @email GuodanYang_nn@163.com
 * @date 2022-08-14 14:59:21
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceivePurchase(Map<String, Object> params);

    /**
     * 合并采购单
     * @param vo
     */
    void mergePurchase(MergeVo vo);

    /**
     * 员工领取采购单
     * @param ids
     */
    void received(List<Long> ids);

    /**
     * 完成采购单
     * @param vo
     */
    void done(PurchaseDoneVo vo);
}

