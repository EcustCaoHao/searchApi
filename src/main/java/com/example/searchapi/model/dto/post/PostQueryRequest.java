package com.example.searchapi.model.dto.post;

import com.example.searchapi.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询帖子的请求体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PostQueryRequest extends PageRequest implements Serializable {
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 标签
     */
    private List<String> tags;
    /**
     * 创建用户
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}
