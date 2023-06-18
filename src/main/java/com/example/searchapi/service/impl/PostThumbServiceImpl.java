package com.example.searchapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.searchapi.model.entity.PostThumb;
import com.example.searchapi.service.PostThumbService;
import com.example.searchapi.mapper.PostThumbMapper;
import org.springframework.stereotype.Service;

/**
* @author caohao
* @description 针对表【post_thumb(帖子点赞)】的数据库操作Service实现
* @createDate 2023-05-22 21:32:14
*/
@Service
public class PostThumbServiceImpl extends ServiceImpl<PostThumbMapper, PostThumb>
    implements PostThumbService{

}




