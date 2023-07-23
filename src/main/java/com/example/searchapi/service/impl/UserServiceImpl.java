package com.example.searchapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.searchapi.common.ErrorCode;
import com.example.searchapi.exception.BusinessException;
import com.example.searchapi.model.dto.user.UserQueryRequest;
import com.example.searchapi.model.entity.User;
import com.example.searchapi.model.vo.LoginUserVO;
import com.example.searchapi.model.vo.UserVO;
import com.example.searchapi.service.UserService;
import com.example.searchapi.mapper.UserMapper;
import com.example.searchapi.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.searchapi.constant.CommonConstant.SORT_ORDER_ASC;
import static com.example.searchapi.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author caohao
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2023-05-22 21:00:12
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    private static final String SALT = "cao";

    /**
     * 这才是写业务逻辑的地方
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 确认密码
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验参数
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword))
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        //2.规则
        if (userAccount.length() < 4)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        if (userPassword.length() < 8 || checkPassword.length() < 8)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        //3.检验两次密码是否相同
        if (!userPassword.equals(checkPassword))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次输入的密码不一致");
        //4.账号不可以重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if (count>0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复");
        //5.对用户的密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((userPassword + SALT).getBytes());
        //6.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean save = this.save(user);
        if (!save)
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"注册失败，数据库错误");
        //7.回显id
        return user.getId();
    }

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param request
     * @return
     */
    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if (StringUtils.isAnyBlank(userAccount,userPassword))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数错误");
        if (userAccount.length() < 4)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号错误");
        if (userPassword.length() < 8)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码错误");
        //2.密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((userPassword + SALT).getBytes());
        //3.匹配账号和密码
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名或者密码错误");
        //4.记录登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,user);
        //5.返回脱敏的用户信息
        return getLoginUserVO(user);
    }

    /**
     * 获取脱敏的用户信息
     * @param user
     * @return
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null)
            return null;
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user,loginUserVO);
        return loginUserVO;
    }

    /**
     * 获取脱敏的用户信息
     * @param userList
     * @return
     */
    @Override
    public List<LoginUserVO> getLoginUserVO(List<User> userList) {
        if (CollectionUtils.isEmpty(userList))
            return new ArrayList<>();
        //Java8的语法糖
        return userList.stream().map(this::getLoginUserVO).collect(Collectors.toList());
    }

    /**
     * 获取脱敏的用户信息
     * @param userList
     * @return
     */
    public List<UserVO> getListUserVO(List<User> userList){
        if (CollectionUtils.isEmpty(userList))
            return new ArrayList<>();
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        //1.退出登录的前提是要先登录
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null)
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"未登录");
        //2.移除登录状态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    /**
     * 获取当前登录的用户
     * @param request
     * @return
     */
    @Override
    public User getLoginUer(HttpServletRequest request) {
        //1.先判断是否登录
        User currentUser = (User)request.getSession().getAttribute(USER_LOGIN_STATE);
        //Long 是一个包装类
        if (currentUser == null || currentUser.getId() == null)
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        //2.去数据库中查询
        Long id = currentUser.getId();
        currentUser = getById(id);
        //3.查出来的数据也是需要判断的
        if (currentUser == null)
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        return currentUser;
    }

    /**
     * 根据UserQueryRequest生成QueryWrapper
     * @param userQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        Long id = userQueryRequest.getId();
        String userRole = userQueryRequest.getUserRole();
        String userProfile = userQueryRequest.getUserProfile();
        String userName = userQueryRequest.getUserName();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id!=null,"id",id);
        queryWrapper.eq(StringUtils.isNotBlank(userRole),"userRole",userRole);
        queryWrapper.like(StringUtils.isNotBlank(userProfile),"userProfile",userProfile);
        queryWrapper.like(StringUtils.isNotBlank(userName),"userName",userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),sortOrder.equals(SORT_ORDER_ASC),sortField);
        return queryWrapper;
    }

    /**
     * 获取脱敏的用户视图
     * @param user
     * @return
     */
    @Override
    public UserVO getUserVO(User user) {
        if (user == null)
            return null;
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        return userVO;
    }

}




