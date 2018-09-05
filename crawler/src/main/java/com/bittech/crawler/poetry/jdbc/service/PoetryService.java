package com.bittech.crawler.poetry.jdbc.service;

import com.bittech.crawler.poetry.jdbc.JdbcFactory;
import com.bittech.crawler.poetry.jdbc.entity.PoetryInfo;
import com.bittech.crawler.poetry.jdbc.mapper.PoetryInfoMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: secondriver
 * Created: 2018/1/21 0021
 */
public class PoetryService {
    
    private SqlSessionFactory factory = JdbcFactory.getInstance().getSqlSessionFactory();
    
    private final Logger logger = LoggerFactory.getLogger(PoetryService.class);
    
    public boolean save(PoetryInfo poetryInfo) {
        try (SqlSession session = factory.openSession()) {
            PoetryInfoMapper mapper = session.getMapper(PoetryInfoMapper.class);
            try {
                int effect = mapper.insertPoetryInfo(poetryInfo);
                session.commit();
                return effect == 1;
            } catch (Exception e) {
                logger.error("Save poetryInfo exception .", e);
                session.rollback();
                return false;
            }
        }
    }
    
    public List<PoetryInfo> query(String author) {
        List<PoetryInfo> result = new ArrayList<>();
        try (SqlSession session = factory.openSession(true)) {
            PoetryInfoMapper mapper = session.getMapper(PoetryInfoMapper.class);
            try {
                result = mapper.queryPoetryInfoByAuthor(author);
            } catch (Exception e) {
                logger.error("Query poetryInfo exception .", e);
            }
        }
        return result;
    }
}