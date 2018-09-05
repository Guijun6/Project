package com.bittech.crawler.poetry.jdbc.entity;

import lombok.Data;

import java.util.Date;

/**
 * Author: secondriver
 * Created: 2018/1/15 0015
 */
public class PoetryInfo {
    
    /**
     * 编号
     */
    private String metaId;
    
    /**
     * 来源地址
     */
    private String metaUrl;
    
    
    /**
     * 创建时间
     */
    private Date metaCreate;
    
    /**
     * 作者名称
     */
    private String authorName;
    
    /**
     * 作者所在朝代
     */
    private String authorDynasty;
    
    /**
     * 标题
     */
    private String contentTitle;
    
    /**
     * 正文
     */
    private String contentBody;

    public String getMetaId() {
        return metaId;
    }

    public void setMetaId(String metaId) {
        this.metaId = metaId;
    }

    public String getMetaUrl() {
        return metaUrl;
    }

    public void setMetaUrl(String metaUrl) {
        this.metaUrl = metaUrl;
    }

    public Date getMetaCreate() {
        return metaCreate;
    }

    public void setMetaCreate(Date metaCreate) {
        this.metaCreate = metaCreate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorDynasty() {
        return authorDynasty;
    }

    public void setAuthorDynasty(String authorDynasty) {
        this.authorDynasty = authorDynasty;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContentBody() {
        return contentBody;
    }

    public void setContentBody(String contentBody) {
        this.contentBody = contentBody;
    }
}
