package com.bittech.crawler.poetry.pipeline;

import com.bittech.crawler.poetry.Poetry;
import com.bittech.crawler.poetry.jdbc.entity.PoetryInfo;
import com.bittech.crawler.poetry.jdbc.service.PoetryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;
import java.util.UUID;

/**
 * Author: secondriver
 * Created: 2018/1/21 0021
 */
public class JdbcPipeline implements Pipeline {
    
    private final Logger logger = LoggerFactory.getLogger(JdbcPipeline.class);
    
    private PoetryService poetryService = new PoetryService();
    
    @Override
    public void process(ResultItems resultItems, Task task) {
        Object value = resultItems.get("poetry");
        if (value instanceof Poetry) {
            Poetry poetry = (Poetry) value;
            PoetryInfo poetryInfo = new PoetryInfo();
            poetryInfo.setMetaId(UUID.randomUUID().toString());
            poetryInfo.setMetaUrl(poetry.getBasic().getUrl());
            
            poetryInfo.setMetaCreate(new Date());
            poetryInfo.setAuthorName(poetry.getAuthor().getName());
            poetryInfo.setAuthorDynasty(poetry.getAuthor().getDynasty());
            
            poetryInfo.setContentTitle(poetry.getContent().getTitle());
            poetryInfo.setContentBody(poetry.getContent().getBody());
            
            boolean status = poetryService.save(poetryInfo);
            if (!status) {
                logger.error("PoetryInfo {} storage failed.", poetryInfo);
            }
        } else {
            logger.error("Url {} not find value.", resultItems.getRequest().getUrl());
        }
    }
}