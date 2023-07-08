package com.example.searchapi.model.dto.picture;

import com.example.searchapi.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 查询图片的请求体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PictureQueryRequest extends PageRequest implements Serializable {
    /**
     * 标题
     */
    private String searchText;

    private static final long serialVersionUID = 1L;
}
