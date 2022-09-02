package com.swjtu.guilimall.search.service;

import com.swjtu.guilimall.search.vo.SearchParam;
import com.swjtu.guilimall.search.vo.SearchResult;

/**
 * @Author: Lil_boat
 * @Date: 2022/9/2 18:02
 * @Description:
 */
public interface MallSearchService {
    /**
     * 检索的所有参数
     * @param param
     * @return 返回检索的结果
     */
    SearchResult search(SearchParam param);
}
