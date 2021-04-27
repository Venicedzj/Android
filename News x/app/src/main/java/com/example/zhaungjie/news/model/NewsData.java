package com.example.zhaungjie.news.model;

/**
 * Created by zhaungjie on 17-4-9.
 */

public class NewsData {
    private String newsTitle;
    private String newsDate;
    private String newsImgUrl;
    private String newsUrl;

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public String getNewsImgUrl() {
        return newsImgUrl;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    public void setNewsImgUrl(String newsImgUrl) {
        this.newsImgUrl = newsImgUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }
}
