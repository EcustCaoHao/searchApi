package com.example.searchapi.common;


/**
 * 返回工具类
 */
public class ResultUtils {

    //成功返回数据
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(200,data,"ok");
    }

    //失败
    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse(errorCode);
    }

    //失败
    public static BaseResponse error(int code,String message){
        return new BaseResponse(code,null,message);
    }

    //失败
    public static BaseResponse error(ErrorCode errorCode,String message){
        return new BaseResponse(errorCode.getCode(),null,message);
    }

}
