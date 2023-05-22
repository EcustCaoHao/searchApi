package com.example.searchapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.searchapi.common.BaseResponse;
import com.example.searchapi.common.ResultUtils;
import com.example.searchapi.mapper.UserMapper;
import com.example.searchapi.model.entity.User;
import com.example.searchapi.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @GetMapping("/findAll")
    public BaseResponse<List<User>> getAllUser(){
        List<User> users = userMapper.selectList(new QueryWrapper<>());
        return ResultUtils.success(users);
    }

    @GetMapping("/find")
    public BaseResponse<List<User>> getUsers(){
        List<User> users = userService.list();
        return ResultUtils.success(users);
    }


}
