package com.example.searchapi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.searchapi.annotation.AuthCheck;
import com.example.searchapi.common.BaseResponse;
import com.example.searchapi.common.DeleteRequest;
import com.example.searchapi.common.ErrorCode;
import com.example.searchapi.common.ResultUtils;
import com.example.searchapi.constant.UserConstant;
import com.example.searchapi.enums.UserRoleEnum;
import com.example.searchapi.exception.BusinessException;
import com.example.searchapi.model.dto.post.PostAddRequest;
import com.example.searchapi.model.dto.post.PostQueryRequest;
import com.example.searchapi.model.dto.post.PostUpdateRequest;
import com.example.searchapi.model.entity.Post;
import com.example.searchapi.model.entity.User;
import com.example.searchapi.model.vo.PostVO;
import com.example.searchapi.service.PostService;
import com.example.searchapi.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Resource
    private UserService userService;
    @Resource
    private PostService postService;

    private final static Gson GSON = new Gson();

    /**
     * 创建帖子
     * @param postAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addPost(@RequestBody PostAddRequest postAddRequest, HttpServletRequest request){
        if (postAddRequest == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        Post post = new Post();
        //title content和copy过来了
        BeanUtils.copyProperties(postAddRequest,post);
        //List<String> tags --->String tags
        List<String> tags = postAddRequest.getTags();
        if (tags!=null){
            //将list的列表转换为json格式的字符串
            post.setTags(GSON.toJson(tags));
        }
        postService.validPost(post,true);
        //帖子是谁创建的
        User loginUer = userService.getLoginUer(request);
        post.setUserId(loginUer.getId());
        post.setFavourNum(0);
        post.setThumbNum(0);
        //插入数据
        boolean result = postService.save(post);
        if (!result)
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(post.getId());
    }

    /**
     * 删除帖子 只有管理员和本人可以删除
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deletePost(@RequestBody DeleteRequest deleteRequest,HttpServletRequest request){
        //没有参数或者是id不对
        if (deleteRequest == null || deleteRequest.getId() <= 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        User loginUer = userService.getLoginUer(request);
        //查看要删除的帖子是不是存在
        Long id = deleteRequest.getId();
        Post post = postService.getById(id);
        if (post == null)
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        //既不是本人也不是管理员就不让删除
        if (!post.getUserId().equals(loginUer.getId()) && !loginUer.getUserRole().equals(UserRoleEnum.ADMIN.getValue()))
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        boolean result = postService.removeById(id);
        if (!result)
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新 只有管理员可以更新帖子
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updatePost(@RequestBody PostUpdateRequest postUpdateRequest){
        if (postUpdateRequest == null || postUpdateRequest.getId() <= 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        Post post = new Post();
        BeanUtils.copyProperties(postUpdateRequest,post);
        List<String> tags = postUpdateRequest.getTags();
        if (tags!=null)
            post.setTags(GSON.toJson(tags));
        postService.validPost(post,false);
        Long id = postUpdateRequest.getId();
        Post oldPost = postService.getById(id);
        if (oldPost == null)
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        boolean result = postService.updateById(post);
        if (!result)
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据id来获取帖子详情
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<PostVO> getPostVOById(long id,HttpServletRequest request){
        if (id <= 0 )
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        Post post = postService.getById(id);
        if (post == null)
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        PostVO postVO = postService.getPostVO(post, request);
        return ResultUtils.success(postVO);
    }

    /**
     * 分页获取帖子（封装类）
     * @param postQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<PostVO>> listPostByPage(@RequestBody PostQueryRequest postQueryRequest,HttpServletRequest request){
        long current = postQueryRequest.getCurrent();
        long pageSize = postQueryRequest.getPageSize();
        Page<Post> page = postService.page(new Page<>(current, pageSize), postService.getQueryWrapper(postQueryRequest));
        return ResultUtils.success(postService.getPostVOPage(page,request));
    }


}
