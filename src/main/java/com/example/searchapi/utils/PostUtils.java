package com.example.searchapi.utils;

import com.example.searchapi.model.entity.Post;
import com.example.searchapi.model.vo.PostVO;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 将Post转为PostVO的包装类
 */
public class PostUtils {

    private final static Gson GSON = new Gson();

    /**
     * 将json格式的string转为List<String>
     * @param post
     * @return
     */
    public static PostVO objToVo(Post post){
        if (post == null)
            return null;
        PostVO postVO = new PostVO();
        BeanUtils.copyProperties(post,postVO);
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> tagList = GSON.fromJson(post.getTags(), type);
        postVO.setTagList(tagList);
        return postVO;
    }


}
