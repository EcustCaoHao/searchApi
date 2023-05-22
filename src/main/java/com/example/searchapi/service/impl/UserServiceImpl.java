package com.example.searchapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.searchapi.model.entity.User;
import com.example.searchapi.service.UserService;
import com.example.searchapi.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author caohao
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2023-05-22 21:00:12
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




