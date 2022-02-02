package com.app.note.entity;

/**
 * desc   :
 */
public  class MoodInfo {
    private int uid;
    private String username;

    public MoodInfo(int uid, String username) {
        this.uid = uid;
        this.username = username;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
