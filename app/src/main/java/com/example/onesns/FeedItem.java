package com.example.onesns;


public class FeedItem {
    private String title;
    private String detail;
    private String days;
    private String userName;
    private String post_time;
    private String user_image;

    public FeedItem(String title, String detail, String days, String userName, String post_time, String user_image) {
        this.title = title;
        this.detail = detail;
        this.days = days;
        this.userName = userName;
        this.post_time = post_time;
        this.user_image = user_image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }
}
