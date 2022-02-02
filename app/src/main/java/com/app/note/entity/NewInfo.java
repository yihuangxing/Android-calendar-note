package com.app.note.entity;

import java.io.Serializable;

/**
 * 新建事件
 */
public class NewInfo implements Serializable {
    private int uid;
    private String username;
    private int year;
    private int month;
    private int cur_day;
    private String create_time;
    private String title;
    private String content;
    private String img_url;
    private String calder_type;
    private String lock_status;   //上锁状态

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getCur_day() {
        return cur_day;
    }

    public void setCur_day(int cur_day) {
        this.cur_day = cur_day;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getCalder_type() {
        return calder_type;
    }

    public void setCalder_type(String calder_type) {
        this.calder_type = calder_type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLock_status() {
        return lock_status;
    }

    public void setLock_status(String lock_status) {
        this.lock_status = lock_status;
    }
}
