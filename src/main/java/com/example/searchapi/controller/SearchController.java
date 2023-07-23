package com.example.searchapi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.searchapi.common.BaseResponse;
import com.example.searchapi.common.ErrorCode;
import com.example.searchapi.common.ResultUtils;
import com.example.searchapi.enums.SearchTypeEnum;
import com.example.searchapi.exception.BusinessException;
import com.example.searchapi.manager.SearchFacade;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/all")
public class SearchController {
    @Resource
    private SearchFacade searchFacade;


    /**
     * 聚合接口
     * @param searchAllRequest
     * @param request
     * @return
     */
    @PostMapping("/search")
    public BaseResponse<SearchVO> listAll(@RequestBody SearchAllRequest searchAllRequest, HttpServletRequest request){
       return ResultUtils.success(searchFacade.listAll(searchAllRequest,request));
    }
}
