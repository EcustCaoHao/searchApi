package com.example.searchapi.utils;

import com.example.searchapi.model.entity.Picture;
import com.example.searchapi.model.entity.Post;
import com.example.searchapi.model.vo.PictureVO;
import com.example.searchapi.model.vo.PostVO;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 将Picture转为PictureVO的包装类
 */
public class PictureUtils {

    private final static Gson GSON = new Gson();

    /**
     * 将json格式的string转为List<String>
     * @param picture
     * @return pictureVO
     */
    public static PictureVO objToVo(Picture picture){
        if (picture == null)
            return null;
        PictureVO pictureVO = new PictureVO();
        BeanUtils.copyProperties(picture,pictureVO);
        return pictureVO;
    }

}
