package com.example.searchapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.searchapi.mapper.PostMapper;
import com.example.searchapi.model.entity.Post;
import com.example.searchapi.service.PostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Resource
    private PostMapper postMapper;


}
