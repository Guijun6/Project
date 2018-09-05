package com.bittech.crawler.poetry;

import us.codecraft.webmagic.Site;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: secondriver
 * Created: 2017/12/23 0023
 */
public class CrawlerSettings {
    
    private Site site = Site.me()
            .addHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
            .setRetryTimes(3)
            .setSleepTime(1000);
    
    private Set<String> listUrl = new HashSet<>();
    
    private Set<String> detailUrl = new HashSet<>();
    
    // private String storagePath = System.getProperty("user.home");
    // 设置文件存储路径
    private String storagePath = "C:\\Users\\q1879\\Desktop";


    private CrawlerSettings() {
    
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Set<String> getListUrl() {
        return listUrl;
    }

    public void setListUrl(Set<String> listUrl) {
        this.listUrl = listUrl;
    }

    public Set<String> getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(Set<String> detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public static CrawlerSettings create() {
        return new CrawlerSettings();
    }
    
    public CrawlerSettings site(Site site) {
        if (site != null) {
            this.site = site;
        }
        return this;
    }
    
    
    public CrawlerSettings addListUrl(String url) {
        if (url != null && url.length() != 0) {
            this.listUrl.add(url);
        }
        return this;
    }
    
    public CrawlerSettings addDetailUrl(String url) {
        if (url != null && url.length() != 0) {
            this.detailUrl.add(url);
        }
        return this;
    }
    
    public CrawlerSettings setStoragePath(String storagePath) {
        if (storagePath != null && storagePath.length() != 0) {
            this.storagePath = storagePath;
        }
        return this;
    }
    
}
