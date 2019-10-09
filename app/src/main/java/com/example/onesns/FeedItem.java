package com.example.onesns;


public class FeedItem {
    private String title;
    private String detail;
    private String days;
    private String userName;
    private String post_time;

    public FeedItem(String title, String detail, String days, String userName, String post_time) {
        this.title = title;
        this.detail = detail;
        this.days = days;
        this.userName = userName;
        this.post_time = post_time;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getDays() {
        return days;
    }

    public String getUserName() {
        return userName;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }
}
