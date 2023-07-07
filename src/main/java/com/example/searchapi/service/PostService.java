package com.example.searchapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
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

}
