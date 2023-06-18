package com.example.searchapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.searchapi.annotation.AuthCheck;
import com.example.searchapi.common.BaseResponse;
import com.example.searchapi.common.DeleteRequest;
import com.example.searchapi.common.ErrorCode;
import com.example.searchapi.common.ResultUtils;
import com.example.searchapi.exception.BusinessException;
import com.example.searchapi.mapper.UserMapper;
import com.example.searchapi.model.dto.user.*;
import com.example.searchapi.model.entity.User;
import com.example.searchapi.model.vo.LoginUserVO;
import com.example.searchapi.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.example.searchapi.constant.UserConstant.ADMIN_ROLE;


@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        //先校验参数
        if (userRegisterRequest == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        //如果有一个为null或者空的话
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword))
            return null;
        //调用service逻辑
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     * @param userLoginRequest
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        //1.校验
        if (userLoginRequest == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword))
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        //2.登录
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request){
        if (request == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录的用户
     * @param request
     * @return
     */
    @GetMapping("/get/user")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request){
        if (request == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        User loginUer = userService.getLoginUer(request);
        //还是只返回用户脱敏后的信息
        return ResultUtils.success(userService.getLoginUserVO(loginUer));
    }

    /**
     * 更新用户信息
     * @param userUpdateMyRequest
     * @param request
     * @return
     */
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,HttpServletRequest request){
        //1.校验
        if (userUpdateMyRequest == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        //2.当前登录用户
        User loginUer = userService.getLoginUer(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest,user);
        user.setId(loginUer.getId());
        boolean result = userService.updateById(user);
        if (!result)
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据查询条件分页展示用户
     * @param userQueryRequest
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<LoginUserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        Page<LoginUserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<LoginUserVO> userVO = userService.getLoginUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return ResultUtils.success(userVOPage);
    }

    /**
     * 管理员创建用户
     * @param userAddRequest
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest){
        if (userAddRequest == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtils.copyProperties(userAddRequest,user);
        boolean result = userService.save(user);
        if (!result)
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 管理员删除用户
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest){
        if (deleteRequest == null || deleteRequest.getId() <= 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        boolean b = userService.removeById(deleteRequest.getId());
        if (!b)
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 作为管理员更新某个用户
     * @param userUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest){
        if (userUpdateRequest == null || userUpdateRequest.getId()<=0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest,user);
        boolean result = userService.updateById(user);
        if (!result)
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 管理员分页获取用户
     * @param userQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest){
        //这里不做参数校验了 写的简单点
        long current = userQueryRequest.getCurrent();
        long pageSize = userQueryRequest.getPageSize();
        Page<User> page = userService.page(new Page<>(current, pageSize), userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(page);
    }

    /**
     * 管理员根据id获取用户
     * @param id
     * @return
     */
    @AuthCheck(mustRole = ADMIN_ROLE)
    @GetMapping("/get")
    public BaseResponse<User> getUserById(long id){
        if (id <= 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        if (user == null)
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }



}
