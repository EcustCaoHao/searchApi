package com.example.searchapi.model.dto.search;

import com.example.searchapi.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 搜索内容
 */
@Data
public class SearchAllRequest extends PageRequest implements Serializable {

    /**
     * 搜索词
     */
    private String searchText;
    /**
     * 类型 user/post/picture
     */
    private String type;

    private static final long serialVersionUID = 1L;


}
