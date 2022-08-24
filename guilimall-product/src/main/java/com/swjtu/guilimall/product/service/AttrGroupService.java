package com.swjtu.guilimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swjtu.common.utils.PageUtils;
import com.swjtu.guilimall.product.entity.AttrGroupEntity;
import com.swjtu.guilimall.product.vo.AttrGroupWithAttrsVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author Lil_Boat
 * @email GuodanYang_nn@163.com
 * @date 2022-08-14 10:14:03
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 模糊查询
     * @param params 查询带入的参数
     * @param catelogId 分组属性的id值
     * @return
     */
    PageUtils queryPage(Map<String, Object> params, Long catelogId);

}

