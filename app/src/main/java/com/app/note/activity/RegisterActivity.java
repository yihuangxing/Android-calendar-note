package com.app.note.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.app.note.R;
import com.app.note.api.ApiConstants;
import com.app.note.base.BaseActivity;
import com.app.note.entity.UserInfo;
import com.app.note.http.HttpStringCallback;
import com.app.note.utils.GsonUtils;
import com.lzy.okgo.OkGo;

public class RegisterActivity extends BaseActivity {
    private EditText username;
    private EditText password;

    private RadioGroup radiogroup;
    private String sex = "女";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    showToast("请输入用户名");
                } else if (TextUtils.isEmpty(pwd)) {
                    showToast("请输入密码");
                } else {
                    register(name,pwd,sex);
                }
            }
        });
        radiogroup = findViewById(R.id.radioGroup);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.man:
                        sex = "男";
                        break;
                    case R.id.woman:
                        sex = "女";
                        break;
                }
            }
        });

    }

    @Override
    protected void initData() {
        setStatusBarDarkMode();
    }


    private void register(String username, String password, String sex) {
        OkGo.<String>get(ApiConstants.REGISTER_URL)
                .params("username", username)
                .params("password", password)
                .params("sex", sex)
                .params("nickname", "这个家伙很懒，什么都没有留下~~")
                .params("register_type",0)
                .execute(new HttpStringCallback(this) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        UserInfo userInfo = GsonUtils.parseJson(response, UserInfo.class);
                        if (userInfo != null) {
                            finish();
                        }
                    }

                    @Override
                    protected void onError(String response) {
                      showToast(response);
                    }
                });
    }
}