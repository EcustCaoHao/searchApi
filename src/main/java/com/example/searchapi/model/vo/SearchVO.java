package com.example.searchapi.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.searchapi.model.entity.Picture;
import lombok.Data;

import java.io.Serializable;

/**
 * 帖子
 * 图片
 * 用户
 */
@Data
public class SearchVO implements Serializable {

    private Page<LoginUserVO> userVOList;

    private Page<PostVO> postVOList;

    private Page<Picture> pictureVOList;

    private Page<?> dataList;

    private static final long serialVersionUID = 1L;

}
