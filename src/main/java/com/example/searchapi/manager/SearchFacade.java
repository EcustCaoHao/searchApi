package com.example.searchapi.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.searchapi.common.ErrorCode;
import com.example.searchapi.datasource.DataSource;
import com.example.searchapi.datasource.PictureDataSource;
import com.example.searchapi.datasource.PostDataSource;
import com.example.searchapi.datasource.UserDataSource;
import com.example.searchapi.enums.SearchTypeEnum;
import com.example.searchapi.exception.BusinessException;

import com.example.searchapi.model.dto.search.SearchAllRequest;

import com.example.searchapi.model.entity.Picture;

import com.example.searchapi.model.vo.*;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 门面模式
 * 前端不需要关心我后端如何获取不同来源的数据的，只需要通过这个门面获取需要的数据
 * controller里面不写很多的业务代码
 */
@Component
public class SearchFacade {

    @Resource
    private UserDataSource userDataSource;
    @Resource
    private PictureDataSource pictureDataSource;
    @Resource
    private PostDataSource postDataSource;


    public SearchVO listAll(@RequestBody SearchAllRequest searchAllRequest, HttpServletRequest request){
        //获取搜索内容
        String searchText = searchAllRequest.getSearchText();
        //返回内容
        SearchVO searchVO = new SearchVO();
        //页码和页面大小
        long current = searchAllRequest.getCurrent();
        long pageSize = searchAllRequest.getPageSize();
        String type = searchAllRequest.getType();
        //不传type那就是全部查
        if (type == null){
            //使用异步编排的方式来改造原有的代码
            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> {
                //搜索出图片
                return pictureDataSource.doSearch(searchText,current,pageSize);
            });

            CompletableFuture<Page<LoginUserVO>> userTask = CompletableFuture.supplyAsync(() -> {
                //搜索出用户
                return userDataSource.doSearch(searchText,current,pageSize);
            });

            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> postDataSource.doSearch(searchText,current,pageSize));

            //上面三个任务全部执行结束才会继续向下执行
            CompletableFuture.allOf(postTask,pictureTask,userTask).join();

            try {
                searchVO.setPictureVOList(pictureTask.get());
                searchVO.setUserVOList(userTask.get());
                searchVO.setPostVOList(postTask.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return searchVO;
        }
        //传入type
        else {
            SearchTypeEnum enumByValue = SearchTypeEnum.getEnumByValue(type);
            if (enumByValue == null){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"type错误");
            }
            Map<String,DataSource<?>> map = new HashMap<>();
            map.put(SearchTypeEnum.PICTURE.getValue(),pictureDataSource);
            map.put(SearchTypeEnum.USER.getValue(),userDataSource);
            map.put(SearchTypeEnum.POST.getValue(),postDataSource);
            DataSource<?> dataSource = map.get(type);
            searchVO.setDataList(dataSource.doSearch(searchText, current, pageSize));
            return searchVO;
        }
    }

}
