package com.example.searchapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.searchapi.model.dto.post.PostQueryRequest;
import com.example.searchapi.model.entity.Post;
import com.example.searchapi.model.vo.PostVO;

import javax.servlet.http.HttpServletRequest;

public interface PostService extends IService<Post> {
    /**
     * 检验帖子的合规性
     * @param post
     * @param add
     */
    void validPost(Post post, boolean add);

    PostVO getPostVO(Post post, HttpServletRequest request);

    QueryWrapper<Post> getQueryWrapper(PostQueryRequest postQueryRequest);

    Page<PostVO> getPostVOPage(Page<Post> page,HttpServletRequest request);

    Page<PostVO> listPostByPage(PostQueryRequest postQueryRequest,HttpServletRequest request);

}
