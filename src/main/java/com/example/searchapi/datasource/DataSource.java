package com.example.searchapi.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 制定数据源的规范（任何接入系统的数据源必须实现这个接口）
 * @param <T>
 */
public interface DataSource<T> {

    Page<T> doSearch(String searchText,long pageNum,long pageSize);

}
