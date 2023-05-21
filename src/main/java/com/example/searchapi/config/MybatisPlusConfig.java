package com.example.searchapi.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MP配置文件
 */
@Configuration
@MapperScan("com.example.searchapi.mapper")
public class MybatisPlusConfig {

}
