package com.bittech.crawler.poetry.jdbc;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * Author: secondriver
 * Created: 2018/1/16 0016
 */
public class JdbcFactory {
    
    private static volatile JdbcFactory mInstance;
    
    private final SqlSessionFactory sqlSessionFactory;
    
    private JdbcFactory() {
        this.sqlSessionFactory = new SqlSessionFactoryBuilder()
                .build(JdbcFactory.class.getClassLoader().getResourceAsStream("mybatis-config.xml"));
    }
    
    
    public static JdbcFactory getInstance() {
        if (mInstance == null) {
            synchronized(JdbcFactory.class) {
                if (mInstance == null) {
                    mInstance = new JdbcFactory();
                }
            }
        }
        return mInstance;
    }
    
    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}