package com.example.searchapi.aop;

import com.example.searchapi.annotation.AuthCheck;
import com.example.searchapi.common.ErrorCode;
import com.example.searchapi.enums.UserRoleEnum;
import com.example.searchapi.exception.BusinessException;
import com.example.searchapi.model.entity.User;
import com.example.searchapi.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 权限校验aop
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;


    /**
     * 拦截加了AuthCheck注解的方法
     * @param joinPoint
     * @param authCheck
     * @return
     */

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        //获取注解上的用户角色
        String mustRole = authCheck.mustRole();
        //获取HttpServletRequest
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        //获取当前登录的用户
        User loginUer = userService.getLoginUer(request);
        //注解里必须有内容才给通过
        if (StringUtils.isNotBlank(mustRole)){
            //userRoleEnum必须是那三个才行
            UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
            if (userRoleEnum == null)
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            //获取当前登录用户的角色
            String userRole = loginUer.getUserRole();
            if (UserRoleEnum.BAN.equals(userRoleEnum))
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            //必须有管理员权限才行
            if (UserRoleEnum.ADMIN.equals(userRoleEnum))
                if (!mustRole.equals(userRole))
                    throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return joinPoint.proceed();
    }
}
