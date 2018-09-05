package com.bittech.crawler.poetry.process;

import com.bittech.crawler.poetry.CrawlerSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.handler.SubPageProcessor;

/**
 * Author: secondriver
 * Created: 2017/12/23
 */
public class GushiwenListPageProcessor implements SubPageProcessor {
    
    private Logger logger = LoggerFactory.getLogger(GushiwenListPageProcessor.class);
    private final CrawlerSettings settings;
    
    public GushiwenListPageProcessor(CrawlerSettings settings) {
        this.settings = settings;
    }
    
    @Override
    public MatchOther processPage(Page page) {
        try {
            page.addTargetRequests(page.getHtml().xpath("//div[@class='typecont']/").links().all());
        } catch (Exception e) {
            logger.error("Process page {} exception {}.", page, e.getMessage());
        }
        return MatchOther.YES;
    }
    
    @Override
    public boolean match(Request page) {
        for (String url : settings.getListUrl()) {
            if (page.getUrl().startsWith(url)) {
                return true;
            }
        }
        return false;
    }
}
