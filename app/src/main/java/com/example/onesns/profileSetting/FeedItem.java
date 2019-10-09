package com.example.onesns.profileSetting;


public class FeedItem {
    private String title;
    private String detail;
    private String days;

    public FeedItem(String title, String detail, String days) {
        this.title = title;
        this.detail = detail;
        this.days = days;
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
}
