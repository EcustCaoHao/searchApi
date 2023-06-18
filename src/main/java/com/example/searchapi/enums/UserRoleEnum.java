package com.example.searchapi.enums;


import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 用户角色的枚举
 */
@Getter
public enum UserRoleEnum {

    USER("用户","user"),
    ADMIN("管理员","admin"),
    BAN("被封号","ban");

   private String text;
   private String value;

   UserRoleEnum(String text,String value){
       this.text = text;
       this.value = value;
   }

    /**
     * 根据value值来获取枚举值
     * @param val
     * @return
     */
   public static UserRoleEnum getEnumByValue(String val){
       if (ObjectUtils.isEmpty(val))
           return null;
       for (UserRoleEnum userRoleEnum : UserRoleEnum.values())
           if (userRoleEnum.value.equals(val))
               return  userRoleEnum;
       return null;
   }

}
