package com.example.searchapi.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 帖子
 * 图片
 * 用户
 */
@Data
public class SearchVO implements Serializable {

    private List<UserVO> userVOList;

    private List<PostVO> postVOList;

    private List<PictureVO> pictureVOList;

    private static final long serialVersionUID = 1L;

}
