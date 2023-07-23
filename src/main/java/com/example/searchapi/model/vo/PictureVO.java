package com.example.searchapi.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PictureVO implements Serializable {

    private String title;
    private String url;

    private static final long serialVersionUID = 1L;


}
