package com.example.searchapi.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;
    private T data;
    private String message;

    //全参数返回
    public BaseResponse(int code,T data,String message){
        this.code =code;
        this.message = message;
        this.data = data;
    }

    //错误返回
    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),null, errorCode.getMessage());
    }

    //正确返回
    public BaseResponse(int code,T data){
        this(code,data,"");
    }


}
