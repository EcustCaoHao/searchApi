package com.example.searchapi.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 返回给前端脱敏的用户视图
 */
@Data
public class UserVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 用户简介
     */
    private String userProfile;
    /**
     * 用户角色 user/admin/ban
     */
    private String userRole;
    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}
