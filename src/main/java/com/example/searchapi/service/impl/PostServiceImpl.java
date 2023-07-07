package com.example.searchapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.searchapi.common.ErrorCode;
import com.example.searchapi.exception.BusinessException;
import com.example.searchapi.mapper.PostFavourMapper;
import com.example.searchapi.mapper.PostMapper;
import com.example.searchapi.mapper.PostThumbMapper;
import com.example.searchapi.mapper.UserMapper;
import com.example.searchapi.model.entity.Post;
import com.example.searchapi.model.entity.PostFavour;
import com.example.searchapi.model.entity.PostThumb;
import com.example.searchapi.model.entity.User;
import com.example.searchapi.model.vo.PostVO;
import com.example.searchapi.model.vo.UserVO;
import com.example.searchapi.service.PostService;
import com.example.searchapi.service.UserService;
import com.example.searchapi.utils.PostUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Resource
    private PostMapper postMapper;

    @Resource
    private UserService userService;

    @Resource
    private PostThumbMapper postThumbMapper;

    @Resource
    private PostFavourMapper postFavourMapper;


    /**
     * 创建帖子时的参数校验或者是贴在参数校验
     * @param post
     * @param add
     */
    @Override
    public void validPost(Post post, boolean add) {
        //没有post直接抛出异常
        if (post == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        String title = post.getTitle();
        String content = post.getContent();
        String tags = post.getTags();
        //add 为true就是创建帖子
        if (add)
            if (StringUtils.isAnyBlank(title,content,tags))
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
        //不创建帖子也好，创建帖子也罢，有参数就校验
        if (StringUtils.isNotBlank(title))
            if (title.length() > 80)
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"标题过长");
        if (StringUtils.isNotBlank(content))
            if (content.length() > 8192)
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"内容过长");
    }

    /**
     * 获取帖子
     * @param post
     * @param request
     * @return
     */
    @Override
    public PostVO getPostVO(Post post, HttpServletRequest request) {
        //将Post转为PostVO
        PostVO postVO = PostUtils.objToVo(post);
        //还需要填充3个字段 创建用户的详细信息 当前用户对该帖子是否点赞和收藏
        Long userId = postVO.getUserId();
        User user = null;
        if (userId!=null && userId>0)
            user = userService.getById(userId);
        UserVO userVO = userService.getUserVO(user);
        postVO.setUserVO(userVO);
        User loginUer = userService.getLoginUer(request);
        if (loginUer!=null){
            //设置是否点赞
            QueryWrapper<PostThumb> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("postId",post.getId());
            queryWrapper.eq("userId",loginUer.getId());
            PostThumb postThumb = postThumbMapper.selectOne(queryWrapper);
            postVO.setHasThumb(postThumb != null);
            //设置是否收藏
            QueryWrapper<PostFavour> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("postId",postVO.getId());
            queryWrapper1.eq("userId",loginUer.getId());
            PostFavour postFavour = postFavourMapper.selectOne(queryWrapper1);
            postVO.setHasFavour(postFavour != null);
        }
        return postVO;
    }
}
