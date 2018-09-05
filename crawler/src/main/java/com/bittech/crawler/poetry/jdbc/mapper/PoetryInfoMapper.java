package com.bittech.crawler.poetry.jdbc.mapper;

import com.bittech.crawler.poetry.jdbc.entity.PoetryInfo;

import java.util.List;

/**
 * Author: secondriver
 * Created: 2018/1/15 0015
 */
public interface PoetryInfoMapper {
    
    int insertPoetryInfo(PoetryInfo poetryInfo);
    
    List<PoetryInfo> queryPoetryInfoByAuthor(String author);
}
