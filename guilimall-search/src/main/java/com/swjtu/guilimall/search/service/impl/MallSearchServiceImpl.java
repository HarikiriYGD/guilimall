package com.swjtu.guilimall.search.service.impl;

import com.swjtu.common.utils.Query;
import com.swjtu.guilimall.search.config.GuilimallElasticsearchConfig;
import com.swjtu.guilimall.search.constant.EsConstant;
import com.swjtu.guilimall.search.service.MallSearchService;
import com.swjtu.guilimall.search.vo.SearchParam;
import com.swjtu.guilimall.search.vo.SearchResult;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.swing.*;
import java.io.IOException;

/**
 * @Author: Lil_boat
 * @Date: 2022/9/2 18:02
 * @Description:
 */
@Service
public class MallSearchServiceImpl implements MallSearchService {

    @Autowired
    private RestHighLevelClient client;

    /**
     * 去es进行检索
     */
    @Override
    public SearchResult search(SearchParam param) {
        // 动态构建出查询需要的DSL语句
        SearchResult result = null;
        // 1. 准备检索请求
        SearchRequest searchRequest = buildSearchRequest(param);
        try {
            // 2. 执行检索请求
            SearchResponse response = client.search(searchRequest, GuilimallElasticsearchConfig.COMMON_OPTIONS);

            // 3. 分析响应数据封装成我们需要的格式
            result = buildSearchResult(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 准备检索请求
     * 模糊匹配，过滤(按照属性、分类、品牌、价格区间、库存)，排序，分页，高亮，聚合分析
     *
     * @return
     */
    private SearchRequest buildSearchRequest(SearchParam param) {
        // 构建DSL语句
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 查询：模糊匹配，过滤(按照属性、分类、品牌、价格区间、库存)
        // 1. 构建bool-query
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 1.1 must-模糊匹配
        if (StringUtils.hasLength(param.getKeyword())) {
            boolQuery.must(QueryBuilders.matchQuery("skuTitle", param.getKeyword()));
        }
        // 1.2 bool-filter - 按照三级分类id查询
        if (param.getCatalog3Id() != null) {
            boolQuery.filter(QueryBuilders.termQuery("catalogId", param.getCatalog3Id()));
        }
        // 1.2 bool-filter - 按照品牌id查询
        if (param.getBrandId() != null && param.getBrandId().size() > 0) {
            boolQuery.filter(QueryBuilders.termQuery("brandId", param.getBrandId()));
        }
        // 1.2 bool-filter - 按照所有指定的属性进行查询
        if (param.getAttrs() != null && param.getAttrs().size() > 0) {
            for (String attrStr : param.getAttrs()) {
                BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
                String[] s = attrStr.split("_");
                // 检索的属性id
                String attrId = s[0];
                // 这个属性的检索用的值
                String[] attrValues = s[1].split(":");
                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrValue", attrValues));
                // 每一个都得生成一个nested查询
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedBoolQuery, ScoreMode.None);
                boolQuery.filter(nestedQuery);
            }
        }

        // 1.2 bool-filter - 按照库存是否有进行查询
        boolQuery.filter(QueryBuilders.termQuery("hasStock", param.getHasStock() == 1));

        // 1.2 bool-filter - 按照价格区间
        if (StringUtils.hasLength(param.getSkuPrice())) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            // 获取传入的价格区间
            String[] s = param.getSkuPrice().split("_");
            if (s.length == 2) {
                // 这是一个价格区间
                rangeQuery.gte(s[0]).lte(s[1]);
            } else if (s.length == 1) {
                if (param.getSkuPrice().startsWith("_")) {
                    rangeQuery.lte(s[0]);
                }
                if (param.getSkuPrice().endsWith("_")) {
                    rangeQuery.gte(s[1]);
                }
            }
            boolQuery.filter(rangeQuery);
        }
        // 把组合好的所有条件放进query进行查询
        sourceBuilder.query(boolQuery);

        // 排序，分页，高亮
        // 2.1 排序
        if (StringUtils.hasLength(param.getSort())) {
            String sort = param.getSort();
            // sort=hotScore_asc/desc
            String[] s = sort.split("_");
            SortOrder order = s[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC;
            sourceBuilder.sort(s[0], order);
        }
        // 2.2 分页  pageSize:5
        // pageNum:1 from 0 size:5
        // pageNum:2 from 5 size:5
        sourceBuilder.from((param.getPageNum() - 1) * EsConstant.PRODUCT_PAGESIZE);
        sourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);
        // 2.3 高亮
        if (StringUtils.hasLength(param.getKeyword())) {
            HighlightBuilder builder = new HighlightBuilder();
            builder.field("skuTitle");
            builder.preTags("<b style='color:red'>");
            builder.postTags("</b>");
            sourceBuilder.highlighter(builder);
        }

        // 聚合分析
        // 3.1


        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);
        return searchRequest;
    }

    /**
     * 构建结果数据
     *
     * @param response
     * @return
     */
    private SearchResult buildSearchResult(SearchResponse response) {
        return null;
    }


}
