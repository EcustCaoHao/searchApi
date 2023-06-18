package com.example.searchapi.model.dto.user;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserUpdateMyRequest implements Serializable {
    /**
     * 更新用户昵称
     */
    private String userName;
    /**
     * 更新头像
     */
    private String userAvatar;

    /**
     * 更新个人简介
     */
    private String userProfile;

    private static final long serialVersionUID = 1L;
}
