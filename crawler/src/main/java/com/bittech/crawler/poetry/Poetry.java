package com.bittech.crawler.poetry;

import lombok.Data;

/**
 * 诗词歌赋
 * <p>
 * Author: secondriver
 * Created: 2017/12/23 0023
 */
public class Poetry {
    
    private Basic basic;
    
    private Author author;
    
    private Content content;

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    /**
     * 基本信息
     */
    public static class Basic {
        
        /**
         * 来源地址
         */
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
    
    /**
     * 作者信息
     */
    public static class Author {
        
        /**
         * 作者名称
         */
        private String name;
        
        /**
         * 作者所在朝代
         */
        private String dynasty;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDynasty() {
            return dynasty;
        }

        public void setDynasty(String dynasty) {
            this.dynasty = dynasty;
        }
    }
    
    /**
     * 诗词歌赋内容
     */
    public static class Content {
        
        /**
         * 标题
         */
        private String title;
        
        /**
         * 正文
         */
        private String body;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
    
}
