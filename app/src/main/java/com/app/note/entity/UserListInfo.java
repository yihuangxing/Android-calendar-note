package com.app.note.entity;

import java.util.List;

/**
 * desc   :
 */
public class UserListInfo {
    private List<UserInfo> userList;

    public List<UserInfo> getUserList() {
        return userList;
    }

    public void setUserList(List<UserInfo> userList) {
        this.userList = userList;
    }

    public UserListInfo(List<UserInfo> userList) {
        this.userList = userList;
    }


}
