package com.example.searchapi.common;

import com.example.searchapi.constant.CommonConstant;
import lombok.Data;

/**
 * 分页请求
 */
@Data
public class PageRequest {

    /**
     * 当前页码
     */
    private long current = 1;
    /**
     * 页面条数
     */
    private long pageSize = 10;
    /**
     * 排序字段
     */
    private String sortField;
    /**
     * 默认按照排序字段升序排序
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
