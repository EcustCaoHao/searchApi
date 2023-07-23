package com.example.searchapi.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.searchapi.common.ErrorCode;
import com.example.searchapi.enums.SearchTypeEnum;
import com.example.searchapi.exception.BusinessException;
import com.example.searchapi.model.dto.picture.PictureQueryRequest;
import com.example.searchapi.model.dto.post.PostQueryRequest;
import com.example.searchapi.model.dto.search.SearchAllRequest;
import com.example.searchapi.model.dto.user.UserQueryRequest;
import com.example.searchapi.model.entity.Picture;
import com.example.searchapi.model.entity.Post;
import com.example.searchapi.model.entity.User;
import com.example.searchapi.model.vo.PictureVO;
import com.example.searchapi.model.vo.PostVO;
import com.example.searchapi.model.vo.SearchVO;
import com.example.searchapi.model.vo.UserVO;
import com.example.searchapi.service.PictureService;
import com.example.searchapi.service.PostService;
import com.example.searchapi.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
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
    private PictureService pictureService;

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    public SearchVO listAll(@RequestBody SearchAllRequest searchAllRequest, HttpServletRequest request){
        //获取搜索内容
        String searchText = searchAllRequest.getSearchText();
        //返回内容
        SearchVO searchVO = new SearchVO();

        String type = searchAllRequest.getType();
        //不传type那就是全部查
        if (type == null){
            //使用异步编排的方式来改造原有的代码
            CompletableFuture<List<PictureVO>> pictureTask = CompletableFuture.supplyAsync(() -> {
                //搜索出图片
                PictureQueryRequest pictureQueryRequest = new PictureQueryRequest();
                pictureQueryRequest.setSearchText(searchText);
                Page<Picture> picturePage = pictureService.searchPicture(pictureQueryRequest.getSearchText(), pictureQueryRequest.getCurrent(), pictureQueryRequest.getPageSize());
                List<PictureVO> pictureList = new ArrayList<>();
                List<Picture> records = picturePage.getRecords();
                for (Picture picture : records)
                    pictureList.add(pictureService.getPictureVO(picture,request));
                return pictureList;
            });

            CompletableFuture<List<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
                //搜索出用户
                UserQueryRequest userQueryRequest = new UserQueryRequest();
                userQueryRequest.setUserName(searchText);
                Page<User> userPage = userService.page(new Page<>(userQueryRequest.getCurrent(), userQueryRequest.getPageSize()),
                        userService.getQueryWrapper(userQueryRequest));
                List<UserVO> userVOList = userService.getListUserVO(userPage.getRecords());
                return userVOList;
            });

            CompletableFuture<List<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {
                //搜索出帖子
                PostQueryRequest postQueryRequest = new PostQueryRequest();
                postQueryRequest.setContent(searchText);
                Page<Post> page = postService.page(new Page<>(postQueryRequest.getCurrent(), postQueryRequest.getPageSize()), postService.getQueryWrapper(postQueryRequest));
                List<PostVO> postVOList = postService.getPostVOPage(page, request).getRecords();
                return postVOList;
            });

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
            switch (enumByValue){
                case USER:
                    //搜索出用户
                    UserQueryRequest userQueryRequest = new UserQueryRequest();
                    userQueryRequest.setUserName(searchText);
                    Page<User> userPage = userService.page(new Page<>(userQueryRequest.getCurrent(), userQueryRequest.getPageSize()),
                            userService.getQueryWrapper(userQueryRequest));
                    List<UserVO> userVOList = userService.getListUserVO(userPage.getRecords());
                    searchVO.setUserVOList(userVOList);
                    break;
                case POST:
                    //搜索出帖子
                    PostQueryRequest postQueryRequest = new PostQueryRequest();
                    postQueryRequest.setContent(searchText);
                    Page<Post> page = postService.page(new Page<>(postQueryRequest.getCurrent(), postQueryRequest.getPageSize()), postService.getQueryWrapper(postQueryRequest));
                    List<PostVO> postVOList = postService.getPostVOPage(page, request).getRecords();
                    searchVO.setPostVOList(postVOList);
                    break;
                case PICTURE:
                    //搜索出图片
                    PictureQueryRequest pictureQueryRequest = new PictureQueryRequest();
                    pictureQueryRequest.setSearchText(searchText);
                    Page<Picture> picturePage = pictureService.searchPicture(pictureQueryRequest.getSearchText(), pictureQueryRequest.getCurrent(), pictureQueryRequest.getPageSize());
                    List<PictureVO> pictureList = new ArrayList<>();
                    List<Picture> records = picturePage.getRecords();
                    for (Picture picture : records)
                        pictureList.add(pictureService.getPictureVO(picture,request));
                    searchVO.setPictureVOList(pictureList);
                    break;
            }
            return searchVO;
        }
    }

}
