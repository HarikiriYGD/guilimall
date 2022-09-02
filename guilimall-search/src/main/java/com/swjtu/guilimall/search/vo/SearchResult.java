package com.swjtu.guilimall.search.vo;

import com.swjtu.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.List;

/**
 * @Author: Lil_boat
 * @Date: 2022/9/2 20:16
 * @Description:
 */
@Data
public class SearchResult {

    /**
     * 查询到的所有商品信息
     */
    private List<SkuEsModel> products;

    /**
     * 分页信息
     */
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 总记录数
     */
    private Long total;
    /**
     * 总页码
     */
    private Integer totalPage;

    /**
     *  所有查询到的品牌
     */
    private List<BrandVo> brands;

    /**
     * 查询到的所有分类
     */
    private List<CatalogVo> catalogs;

    /**
     * 所有查询到的属性
     */
    private List<AttrVo> attrs;

    @Data
    public static class BrandVo {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    @Data
    public static class CatalogVo{
        private Long catalogId;
        private String catalogName;
        private String brandImg;
    }

    @Data
    public static class AttrVo{
        private Long attrId;
        private String attrName;
        private String brandImg;
        private List<String> attrValue;
    }

}
