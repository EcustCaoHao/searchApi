package com.example.searchapi.datasource;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.example.searchapi.model.dto.post.PostQueryRequest;

import com.example.searchapi.model.vo.PostVO;

import com.example.searchapi.service.PostService;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 这个就是最简单的适配器模式
 */
@Service
public class PostDataSource implements DataSource<PostVO> {

    @Resource
    private PostService postService;

    /**
     * 其实这个改造就是在现有方法中调用原有的方法 参数需要改变
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Page<PostVO> doSearch(String searchText, long pageNum, long pageSize) {
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setContent(searchText);
        postQueryRequest.setCurrent(pageNum);
        postQueryRequest.setPageSize(pageSize);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return postService.listPostByPage(postQueryRequest,request);
    }


}
