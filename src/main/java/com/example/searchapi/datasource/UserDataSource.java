package com.example.searchapi.datasource;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.example.searchapi.model.dto.user.UserQueryRequest;

import com.example.searchapi.model.vo.LoginUserVO;

import com.example.searchapi.service.UserService;

import org.springframework.stereotype.Service;


import javax.annotation.Resource;


@Service
public class UserDataSource implements DataSource<LoginUserVO>{

    @Resource
    private UserService userService;

    @Override
    public Page<LoginUserVO> doSearch(String searchText, long pageNum, long pageSize) {
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        userQueryRequest.setCurrent(pageNum);
        userQueryRequest.setPageSize(pageSize);
        return userService.listUserVOByPage(userQueryRequest);
    }
}




