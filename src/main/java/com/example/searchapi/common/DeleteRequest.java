package com.example.searchapi.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求 删除都是根据id删除
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;


    private static final long serialVersionUID = 1L;
}
