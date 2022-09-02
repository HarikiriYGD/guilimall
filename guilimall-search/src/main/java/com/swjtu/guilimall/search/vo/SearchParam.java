package com.swjtu.guilimall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: Lil_boat
 * @Date: 2022/9/2 18:00
 * @Description: 封装页面所有可能传递过来的查询条件
 */
@Data
public class SearchParam {


    /**
     * 页面传递过来的全文匹配关键词
     */
    private String keyword;

    /**
     * 三级分类Id
     */
    private Long catalog3Id;

    /**
     * 排序条件
     * sort = saleCount_asc/desc
     * sort = hotScore_asc/desc
     * sort = skuPrice_asc/desc
     */
    private String sort;

    /**
     * 过滤条件
     * hasStock(是否有货)、 skuPrice(价格区间) brandId attrs
     * hasStock:0/1
     * skuPrice:1_500/_500/500_
     * brandId=1  品牌Id  允许多选
     * attrs=2_5存:6寸
     */
    private Integer hasStock = 1;

    private String skuPrice;

    private List<Long> brandId;
    private List<String> attrs;

    /**
     * 页码
     */
    private Integer pageNum = 1;

}
