package com.example.searchapi.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 帖子视图
 */
@Data
public class PostVO implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * title
     */
    private String title;
    /**
     * content
     */
    private String content;
    /**
     * 点赞数
     */
    private Integer thumbNum;
    /**
     * 收藏数
     */
    private Integer favourNum;
    /**
     * 创建用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 创建人信息
     */
    private UserVO userVO;

    /**
     * 当前用户对该帖子是否已经点赞
     */
    private Boolean hasThumb;
    /**
     * 当前用户对该帖子是否已经收藏
     */
    private Boolean hasFavour;
}
