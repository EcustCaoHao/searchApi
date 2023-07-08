package com.example.searchapi.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.searchapi.model.entity.Post;
import com.example.searchapi.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class FetchInitPostList implements CommandLineRunner {

    @Resource
    private PostService postService;

    /**
     * 每次启动springboot项目时都会执行这个run方法
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
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
        if (b){
            log.info("获取初始化帖子表成功，数量为={}",list.size());
        }
        else{
            log.error("初始化帖子表失败");
        }
    }
}
