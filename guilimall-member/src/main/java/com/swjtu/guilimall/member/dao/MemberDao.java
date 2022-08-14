package com.swjtu.guilimall.member.dao;

import com.swjtu.guilimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author Lil_Boat
 * @email GuodanYang_nn@163.com
 * @date 2022-08-14 14:56:35
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
