package com.swjtu.guilimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: Lil_boat
 * @Date: 2022/8/30 15:09
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catelog2Vo {

    /**
     * 一级分类的id
     */
    private String catalog1Id;
    /**
     * 三级子分类
     */
    private List<Catelog3Vo> catalog3List;
    private String id;
    private String name;

    /**
     * 三级分类vo
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Catelog3Vo{
        private String catalog2Id;
        private String name;
        private String id;
    }

}
