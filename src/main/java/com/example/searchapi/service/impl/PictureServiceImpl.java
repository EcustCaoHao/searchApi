package com.example.searchapi.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.searchapi.common.ErrorCode;
import com.example.searchapi.exception.BusinessException;
import com.example.searchapi.mapper.PostFavourMapper;
import com.example.searchapi.mapper.PostMapper;
import com.example.searchapi.mapper.PostThumbMapper;
import com.example.searchapi.model.dto.picture.PictureQueryRequest;
import com.example.searchapi.model.dto.post.PostQueryRequest;
import com.example.searchapi.model.entity.*;
import com.example.searchapi.model.vo.PictureVO;
import com.example.searchapi.model.vo.PostVO;
import com.example.searchapi.model.vo.UserVO;
import com.example.searchapi.service.PictureService;
import com.example.searchapi.service.PostService;
import com.example.searchapi.service.UserService;
import com.example.searchapi.utils.PictureUtils;
import com.example.searchapi.utils.PostUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PictureServiceImpl implements PictureService {

    @Override
    public PictureVO getPictureVO(Picture picture, HttpServletRequest request) {
        return PictureUtils.objToVo(picture);
    }

    @Override
    public Page<Picture> searchPicture(String searchText, long pageNum, long pageSize) {
        long current = (pageNum-1)*pageSize;
        String url = String.format("https://cn.bing.com/images/search?q=%s&first=%s",searchText,current);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = doc.select(".iuscp.isv");
        List<Picture> list = new ArrayList<>();
        for (Element element : elements) {
            Picture picture = new Picture();
            //我需要获取murl 图片的地址
            String m = element.select(".iusc").get(0).attr("m");
            Elements select1 = element.select(".inflnk");
            String title = select1.get(0).attr("aria-label");
            Map<String,Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            picture.setUrl(murl);
            picture.setTitle(title);
            list.add(picture);
        }
        Page<Picture> page = new Page<>(pageNum,pageSize);
        page.setRecords(list);
        return page;
    }

}
