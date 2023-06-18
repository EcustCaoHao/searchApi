package com.example.searchapi.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户创建请求实体(作为管理员创建用户用的)
 */
@Data
public class UserAddRequest implements Serializable {
    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 用户账号
     */
    private String userAccount;
    /**
     * 用户密码
     */
    private String userPassword;
    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 用户角色
     */
    private String userRole;

    private static final long serialVersionUID = 1L;

}
