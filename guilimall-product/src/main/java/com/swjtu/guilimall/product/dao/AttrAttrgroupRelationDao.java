package com.swjtu.guilimall.product.dao;

import com.swjtu.guilimall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author Lil_Boat
 * @email GuodanYang_nn@163.com
 * @date 2022-08-14 10:14:03
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    /**
     * 批量删除属性
     * @param entities
     */
    void deleteBatchRelation(@Param("entities") List<AttrAttrgroupRelationEntity> entities);
}
