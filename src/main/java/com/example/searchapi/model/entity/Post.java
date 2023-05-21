package com.example.searchapi.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("post")
public class Post implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


    private String title;


    private String content;


    private String tags;


    private Integer thumbNum;


    private Integer favourNum;


    private Long userId;


    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
