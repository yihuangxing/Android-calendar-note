package com.app.note.api;

import com.app.note.entity.UserInfo;

public class ApiConstants {

    //        public final static String BASE_URL = "http://192.168.14.13:8080";
    public final static String BASE_URL = "http://192.168.2.103:8080";

    public static UserInfo sUserInfo;

    public static UserInfo getUserInfo() {
        return sUserInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        sUserInfo = userInfo;
    }

    //注册
    public final static String REGISTER_URL = BASE_URL + "/user/register";

    //登录
    public final static String LOGIN_URL = BASE_URL + "/user/login";

    //修改用户信息，密码等功能
    public final static String UPDATE_EDIT_URL = BASE_URL + "/user/edit";

    //新建事项
    public final static String NEWS_EDIT_URL = BASE_URL + "/user/push";


    //查询事件
    public final static String QUERY_NEW_URL = BASE_URL + "/user/queryPush";

    //查询某一单独事件
    public final static String QUERY_ONE_NEW_URL=BASE_URL+"/user/queryOnePush";


    //日记上锁
    public final static String LOCK_URL = BASE_URL + "/user/lock";


    //日记解锁
    public final static String UN_LOCK_URL = BASE_URL + "/user/unLock";


    //删除事件
    public final static String DEL_URL = BASE_URL + "/user/del";

    //修改日记
    public final static String EDIT_NOTE_URL = BASE_URL + "/user/editNote";
}
