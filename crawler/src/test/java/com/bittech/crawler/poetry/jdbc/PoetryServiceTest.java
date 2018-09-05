package com.bittech.crawler.poetry.jdbc;

import com.alibaba.fastjson.JSON;
import com.bittech.crawler.poetry.Poetry;
import com.bittech.crawler.poetry.jdbc.entity.PoetryInfo;
import com.bittech.crawler.poetry.jdbc.service.PoetryService;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Author: secondriver
 * Created: 2018/1/21 0021
 */
public class PoetryServiceTest {
    
    private static List<Poetry> poetryList = new ArrayList<>();
    
    private static PoetryService poetryService = new PoetryService();
    
    @BeforeClass
    public static void beforeClass() {
        String dataPath = System.getProperty("user.dir") + File.separator + "data" + File.separator + "so.gushiwen.org";
        File dataFile = new File(dataPath);
        File[] files = dataFile.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    String poetryText = IOUtils.toString(new FileInputStream(file), "UTF-8");
                    Poetry poetry = JSON.parseObject(poetryText, Poetry.class);
                    poetryList.add(poetry);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @AfterClass
    public static void afterClass() {
        poetryList.clear();
    }
    
    @Test
    public void testPoetrySave() {
        for (Poetry poetry : poetryList) {
            if (poetry == null) {
                continue;
            }
            PoetryInfo poetryInfo = new PoetryInfo();
            poetryInfo.setMetaId(UUID.randomUUID().toString());
            poetryInfo.setMetaUrl(poetry.getBasic().getUrl());
            
            poetryInfo.setMetaCreate(new Date());
            poetryInfo.setAuthorName(poetry.getAuthor().getName());
            poetryInfo.setAuthorDynasty(poetry.getAuthor().getDynasty());
            
            poetryInfo.setContentTitle(poetry.getContent().getTitle());
            poetryInfo.setContentBody(poetry.getContent().getBody());
            
            poetryService.save(poetryInfo);
        }
    }
    
    
    @Test
    public void testPoetryQuery() {
        List<PoetryInfo> poetryInfos = poetryService.query("李白");
        for (PoetryInfo poetryInfo : poetryInfos) {
            System.out.println(poetryInfo);
        }
        System.out.println("李白一共: " + poetryInfos.size() + "首");
    }
}
