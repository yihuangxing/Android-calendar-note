package com.app.note.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.app.note.R;
import com.app.note.api.ApiConstants;
import com.app.note.base.BaseActivity;
import com.app.note.entity.UserInfo;
import com.app.note.http.HttpStringCallback;
import com.lzy.okgo.OkGo;

public class UpdatePwdActivity extends BaseActivity {
     private EditText password;
     private EditText new_password;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_pwd;
    }

    @Override
    protected void initView() {
        setStatusBarDarkMode();

        password =findViewById(R.id.password);
        new_password =findViewById(R.id.new_password);

        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = password.getText().toString().trim();
                String cpwd = new_password.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    showToast("请输入密码");
                } else if (TextUtils.isEmpty(cpwd)) {
                    showToast("请输入确定密码");
                } else if (!pwd.equals(cpwd)) {
                    showToast("两次密码不一致");
                } else {
                    UserInfo userInfo = ApiConstants.getUserInfo();
                    if (null != userInfo) {
                        updatePwd(userInfo.getUid(), pwd);
                    }

                }
            }
        });

    }

    @Override
    protected void initData() {


    }

    public void updatePwd(int uid, String password) {
        OkGo.<String>get(ApiConstants.UPDATE_EDIT_URL)
                .params("uid", uid)
                .params("password", password)
                .execute(new HttpStringCallback(this) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        showToast(msg);
                        setResult(2000);
                        finish();

                    }

                    @Override
                    protected void onError(String response) {
                        showToast(response);
                    }
                });
    }
}