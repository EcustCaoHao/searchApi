package com.example.searchapi.model.dto.user;

import com.example.searchapi.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户查询请求(分页查询)
 * 组合条件查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryRequest extends PageRequest implements Serializable {
    /**
     * 根据id
     */
    private Long id;
    /**
     * 根据开放平台id
     */
    private String unionId;
    /**
     * 根据微信公众号查询
     */
    private String mpOpenId;
    /**
     * 根据用户昵称查询
     */
    private String userName;
    /**
     * 根据简介查询
     */
    private String userProfile;
    /**
     * 根据用户角色 user/admin/ban
     */
    private String userRole;

    private static final long serialVersionUID = 1L;

}
