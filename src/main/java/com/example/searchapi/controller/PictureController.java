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
import com.example.searchapi.model.dto.picture.PictureQueryRequest;
import com.example.searchapi.model.dto.post.PostAddRequest;
import com.example.searchapi.model.dto.post.PostQueryRequest;
import com.example.searchapi.model.dto.post.PostUpdateRequest;
import com.example.searchapi.model.entity.Picture;
import com.example.searchapi.model.entity.Post;
import com.example.searchapi.model.entity.User;
import com.example.searchapi.model.vo.PictureVO;
import com.example.searchapi.model.vo.PostVO;
import com.example.searchapi.service.PictureService;
import com.example.searchapi.service.PostService;
import com.example.searchapi.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/picture")
public class PictureController {

    @Resource
    private PictureService pictureService;

    private final static Gson GSON = new Gson();

    /**
     * 分页获取图片（封装类）
     * @param pictureQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<PictureVO>> listPictureByPage(@RequestBody PictureQueryRequest pictureQueryRequest, HttpServletRequest request){
        long current = pictureQueryRequest.getCurrent();
        long pageSize = pictureQueryRequest.getPageSize();
        Page<Picture> picturePage = pictureService.searchPicture(pictureQueryRequest.getSearchText(), current, pageSize);
        Page<PictureVO> pictureVOPage = new Page<>();
        List<PictureVO> pictureList = new ArrayList<>();
        List<Picture> records = picturePage.getRecords();
        for (Picture picture : records)
            pictureList.add(pictureService.getPictureVO(picture,request));
        pictureVOPage.setRecords(pictureList);
        return ResultUtils.success(pictureVOPage);
    }

}
