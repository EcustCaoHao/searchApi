package com.example.searchapi.controller;


import com.example.searchapi.common.BaseResponse;
import com.example.searchapi.common.ResultUtils;

import com.example.searchapi.manager.SearchFacade;

import com.example.searchapi.model.dto.search.SearchAllRequest;

import com.example.searchapi.model.vo.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
