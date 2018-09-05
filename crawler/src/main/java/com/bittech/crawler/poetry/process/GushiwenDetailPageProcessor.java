package com.bittech.crawler.poetry.process;

import com.bittech.crawler.poetry.CrawlerSettings;
import com.bittech.crawler.poetry.Poetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.handler.SubPageProcessor;

/**
 * Author: secondriver
 * Created: 2017/12/23 0023
 */
public class GushiwenDetailPageProcessor implements SubPageProcessor {
    
    private final Logger logger = LoggerFactory.getLogger(GushiwenDetailPageProcessor.class);
    
    private final CrawlerSettings settings;
    
    public GushiwenDetailPageProcessor(CrawlerSettings settings) {
        this.settings = settings;
    }
    
    @Override
    public MatchOther processPage(Page page) {
        
        try {
            // 基本信息，存的是url
            Poetry.Basic basic = new Poetry.Basic();
            basic.setUrl(page.getUrl().get());

            // 作者信息
            Poetry.Author author = new Poetry.Author();
            author.setDynasty(page.getHtml().xpath("//div[@class='cont']/p[@class='source'][1]/a[1]/text()")
                    .get());
            author.setName(page.getHtml().xpath("//div[@class='cont']/p[@class='source'][1]/a[2]/text()")
                    .get());
            
            // 诗词正文和诗词标题
            Poetry.Content content = new Poetry.Content();
            content.setTitle(page.getHtml()
                    .xpath("//div[@class='cont']/h1/text()")
                    .get());
            content.setBody(page.getHtml()
                    .xpath("//div[@class='cont']/div[@class='contson']/text()")
                    .get());
            
            Poetry poetry = new Poetry();
            poetry.setBasic(basic);
            poetry.setAuthor(author);
            poetry.setContent(content);
            
            page.putField("poetry", poetry);
        } catch (Exception e) {
            logger.error("Process page {} exception {}.", page, e.getMessage());
        }
        return MatchOther.NO;
    }
    
    @Override
    public boolean match(Request page) {
        for (String url : settings.getDetailUrl()) {
            if (page.getUrl().startsWith(url)) {
                return true;
            }
        }
        return false;
    }
}
