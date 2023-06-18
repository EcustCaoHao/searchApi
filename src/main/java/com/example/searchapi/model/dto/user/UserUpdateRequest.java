package com.example.searchapi.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 管理员来更新某个用户
 */
@Data
public class UserUpdateRequest implements Serializable {
    /**
     * 更新的是哪个用户
     */
    private Long id;
    /**
     * 更新用户昵称
     */
    private String userName;
    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 更新简介
     */
    private String userProfile;
    /**
     * 作为管理员更新这个用户的角色
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
