package com.example.searchapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.searchapi.model.entity.PostFavour;
import com.example.searchapi.service.PostFavourService;
import com.example.searchapi.mapper.PostFavourMapper;
import org.springframework.stereotype.Service;

/**
* @author caohao
* @description 针对表【post_favour(帖子收藏)】的数据库操作Service实现
* @createDate 2023-05-22 21:27:16
*/
@Service
public class PostFavourServiceImpl extends ServiceImpl<PostFavourMapper, PostFavour>
    implements PostFavourService{

}




