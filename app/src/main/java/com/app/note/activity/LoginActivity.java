package com.app.note.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.app.note.IndexActivity;
import com.app.note.R;
import com.app.note.api.ApiConstants;
import com.app.note.base.BaseActivity;
import com.app.note.entity.UserInfo;
import com.app.note.http.HttpStringCallback;
import com.app.note.utils.GsonUtils;
import com.lzy.okgo.OkGo;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity {
    private EditText username;
    private EditText password;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    showToast("请输入用户名");
                } else if (TextUtils.isEmpty(pwd)) {
                    showToast("请输入密码");
                }else {
                     login(name,pwd);
                }
            }
        });
    }

    @Override
    protected void initData() {
        setStatusBarDarkMode();

    }


    private void login(String username, String password) {
        OkGo.<String>get(ApiConstants.LOGIN_URL)
                .params("username", username)
                .params("password", password)
                .execute(new HttpStringCallback(this) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        UserInfo userInfo = GsonUtils.parseJson(response, UserInfo.class);
                        ApiConstants.setUserInfo(userInfo);
                        showToast(msg);
//                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        startActivity(new Intent(LoginActivity.this, IndexActivity.class));
                        finish();

                    }

                    @Override
                    protected void onError(String response) {
                        showToast(response);
                    }
                });
    }
}