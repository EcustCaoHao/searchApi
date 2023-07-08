package com.example.searchapi;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.searchapi.model.entity.Picture;
import com.example.searchapi.model.entity.Post;
import com.example.searchapi.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class CrawlTest {

    @Resource
    private PostService postService;


    @Test
    public void testFetchPicture() throws IOException {
        String url = "https://cn.bing.com/images/search?q=小黑子&first=1";
        Document doc = Jsoup.connect(url).get();
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
        System.out.println(list);
    }

    @Test
    public void testFetch(){
        //抓取某个网页json格式的请求数据
        String json = "{\n" +
                "  \"current\": 1,\n" +
                "  \"pageSize\": 8,\n" +
                "  \"sortField\": \"createTime\",\n" +
                "  \"sortOrder\": \"descend\",\n" +
                "  \"category\": \"文章\",\n" +
                "  \"reviewStatus\": 1\n" +
                "}";
        //请求接口地址
        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String result = HttpRequest.post(url)
                .body(json)
                .execute().body();
        //需要将返回来的这个json格式的数据转换为对象
        Map<String,Object> map= JSONUtil.toBean(result, Map.class);
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");
        List<Post> list = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            Post post = new Post();
            JSONObject o = (JSONObject) records.get(i);
            post.setTitle((String) o.get("title"));
            post.setUserId(1L);
            post.setContent((String) o.get("content"));
            JSONArray tagsArray = (JSONArray) o.get("tags");
            List<String> stringList = tagsArray.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(stringList));
            list.add(post);
        }
        boolean b = postService.saveBatch(list);
        Assertions.assertTrue(b);
    }



}
