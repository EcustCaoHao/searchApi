package com.example.searchapi.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 支持user、post、picture类型的查询
 */
@Getter
public enum SearchTypeEnum {

    USER("用户","user"),
    POST("帖子","post"),
    PICTURE("图片","picture");

    private String text;
    private String value;

    SearchTypeEnum(String text,String value){
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value值来获取枚举值
     * @param val
     * @return
     */
    public static SearchTypeEnum getEnumByValue(String val){
        if (ObjectUtils.isEmpty(val))
            return null;
        for (SearchTypeEnum searchTypeEnum : SearchTypeEnum.values())
            if (searchTypeEnum.value.equals(val))
                return  searchTypeEnum;
        return null;
    }


}
