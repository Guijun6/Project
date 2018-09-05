package com.bittech.crawler.poetry;

import com.bittech.crawler.poetry.pipeline.JdbcPipeline;
import com.bittech.crawler.poetry.pipeline.JsonLocalFilePipeline;
import com.bittech.crawler.poetry.process.GushiwenDetailPageProcessor;
import com.bittech.crawler.poetry.process.GushiwenListPageProcessor;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.handler.CompositePageProcessor;

/**
 * Author: secondriver
 * Created: 2017/12/23 0023
 */
public class PoetryCrawlerApplication {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PoetryCrawlerApplication.class);
    
    public static void main(String[] args) {
        CrawlerSettings settings =
                CrawlerSettings.create()
                        .addListUrl("http://so.gushiwen.org/gushi/tangshi.aspx")
                        .addListUrl("http://so.gushiwen.org/gushi/yuefu.aspx")
                        .addListUrl("http://so.gushiwen.org/gushi/songci.aspx")
                        .addDetailUrl("http://so.gushiwen.org/shiwenv_");
        
        CompositePageProcessor pageProcessor = new CompositePageProcessor(settings.getSite());
        pageProcessor.addSubPageProcessor(new GushiwenListPageProcessor(settings));
        pageProcessor.addSubPageProcessor(new GushiwenDetailPageProcessor(settings));
        
        Spider spider = Spider.create(pageProcessor);

        spider.addUrl(settings.getListUrl().iterator().next());
        
//        for (String url : settings.getListUrl()) {
//            spider.addUrl(url);
//        }
//         spider.addPipeline(new JsonLocalFilePipeline(settings.getStoragePath()))
//         //      spider.addPipeline(new JdbcPipeline())
//                .setSpiderListeners(Lists.newArrayList(new SpiderListener() {
//                    @Override
//                    public void onSuccess(Request request) {
//                        LOGGER.info("On success request url {} .", request.getUrl());
//                    }
//
//                    @Override
//                    public void onError(Request request) {
//                        LOGGER.error("On error request url {} .", request.getUrl());
//                    }
//                }))
//                .thread(3)
//                .run();
        spider.addPipeline(new JsonLocalFilePipeline(settings.getStoragePath())).thread(5).run();
    }
}