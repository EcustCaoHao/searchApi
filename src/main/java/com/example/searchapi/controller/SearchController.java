package com.example.searchapi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.searchapi.common.BaseResponse;
import com.example.searchapi.common.ResultUtils;
import com.example.searchapi.model.dto.picture.PictureQueryRequest;
import com.example.searchapi.model.dto.post.PostQueryRequest;
import com.example.searchapi.model.dto.search.SearchAllRequest;
import com.example.searchapi.model.dto.user.UserQueryRequest;
import com.example.searchapi.model.entity.Picture;
import com.example.searchapi.model.entity.Post;
import com.example.searchapi.model.entity.User;
import com.example.searchapi.model.vo.*;
import com.example.searchapi.service.PictureService;
import com.example.searchapi.service.PostService;
import com.example.searchapi.service.UserService;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/all")
public class SearchController {

    @Resource
    private PictureService pictureService;

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;


    /**
     * 聚合接口
     * @param searchAllRequest
     * @param request
     * @return
     */
    @PostMapping("/search")
    public BaseResponse<SearchVO> listAll(@RequestBody SearchAllRequest searchAllRequest, HttpServletRequest request){
        //获取搜索内容
        String searchText = searchAllRequest.getSearchText();
        //返回内容
        SearchVO searchVO = new SearchVO();
        //搜索出图片
        PictureQueryRequest pictureQueryRequest = new PictureQueryRequest();
        pictureQueryRequest.setSearchText(searchText);
        Page<Picture> picturePage = pictureService.searchPicture(pictureQueryRequest.getSearchText(), pictureQueryRequest.getCurrent(), pictureQueryRequest.getPageSize());
        List<PictureVO> pictureList = new ArrayList<>();
        List<Picture> records = picturePage.getRecords();
        for (Picture picture : records)
            pictureList.add(pictureService.getPictureVO(picture,request));
        searchVO.setPictureVOList(pictureList);
        //搜索出用户
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        Page<User> userPage = userService.page(new Page<>(userQueryRequest.getCurrent(), userQueryRequest.getPageSize()),
                userService.getQueryWrapper(userQueryRequest));
        List<UserVO> userVOList = userService.getListUserVO(userPage.getRecords());
        searchVO.setUserVOList(userVOList);
        //搜索出帖子
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setContent(searchText);
        Page<Post> page = postService.page(new Page<>(postQueryRequest.getCurrent(), postQueryRequest.getPageSize()), postService.getQueryWrapper(postQueryRequest));
        searchVO.setPostVOList(postService.getPostVOPage(page, request).getRecords());
        return ResultUtils.success(searchVO);
    }

}
