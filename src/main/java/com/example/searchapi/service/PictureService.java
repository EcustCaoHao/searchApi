package com.example.searchapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.searchapi.model.dto.picture.PictureQueryRequest;
import com.example.searchapi.model.dto.post.PostQueryRequest;
import com.example.searchapi.model.entity.Picture;
import com.example.searchapi.model.entity.Post;
import com.example.searchapi.model.vo.PictureVO;
import com.example.searchapi.model.vo.PostVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PictureService {

    PictureVO getPictureVO(Picture picture, HttpServletRequest request);

    Page<Picture> searchPicture(String searchText, long pageNum, long pageSize);

}
