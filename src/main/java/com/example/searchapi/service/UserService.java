package com.example.searchapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.searchapi.model.dto.user.UserQueryRequest;
import com.example.searchapi.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.searchapi.model.vo.LoginUserVO;
import com.example.searchapi.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author caohao
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2023-05-22 21:00:12
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 确认密码
     * @return
     */
    long userRegister(String userAccount,String userPassword,String checkPassword);

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param request
     * @return
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取脱敏的用户信息
     * @param user
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     * @param userList
     * @return
     */
    List<LoginUserVO> getLoginUserVO(List<User> userList);

    /**
     * 获取脱敏的用户信息
     * @param userList
     * @return
     */
    List<UserVO> getListUserVO(List<User> userList);

    /**
     * 退出登录
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取当前登录的用户
     * @param request
     * @return
     */
    User getLoginUer(HttpServletRequest request);

    /**
     * 根据查询条件生成QueryWrapper
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 获取脱敏的用户信息 用户视图
     * @param user
     * @return
     */
    UserVO getUserVO(User user);
}
