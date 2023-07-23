package com.example.searchapi.model.dto.search;

import lombok.Data;

import java.io.Serializable;

/**
 * 搜索内容
 */
@Data
public class SearchAllRequest implements Serializable {


    private String searchText;

    private static final long serialVersionUID = 1L;


}
