package com.example.searchapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.searchapi.common.BaseResponse;
import com.example.searchapi.common.ErrorCode;
import com.example.searchapi.common.ResultUtils;
import com.example.searchapi.exception.BusinessException;
import com.example.searchapi.mapper.PostMapper;
import com.example.searchapi.model.entity.Post;
import com.example.searchapi.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Resource
    private PostService postService;
    @Resource
    private PostMapper postMapper;

    @GetMapping("/hao")
    public BaseResponse success(){
        return ResultUtils.success("曹昊-徐莺");
    }

    @GetMapping("/xu")
    public BaseResponse error(){
        return ResultUtils.error(ErrorCode.NO_AUTH_ERROR);
    }

    @GetMapping("/post")
    public BaseResponse<List<Post>> getAllPost(){
        List<Post> list = postService.list();
        return ResultUtils.success(list);
    }

    @GetMapping("/find")
    public BaseResponse<List<Post>> getPost(){
        return ResultUtils.success(postMapper.selectList(new QueryWrapper<>()));
    }

    @GetMapping("/error")
    public BaseResponse getError(){
        int i = 1 / 0;
        return ResultUtils.success("11");
    }

    @GetMapping("/error1")
    public BaseResponse getError1(){
        throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
    }


}
