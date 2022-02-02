package com.app.note.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.app.note.R;
import com.app.note.api.ApiConstants;
import com.app.note.base.BaseActivity;
import com.app.note.entity.NewInfo;
import com.app.note.http.HttpStringCallback;
import com.lzy.okgo.OkGo;

/**
 * 操作界面
 */
public class DialogActivity extends BaseActivity {
    private EditText password;
    private NewInfo info;
    private boolean from;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dialog;
    }

    @Override
    protected void initView() {
        info = (NewInfo) getIntent().getSerializableExtra("info");
        from = getIntent().getBooleanExtra("from", false);

        password = findViewById(R.id.password);
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.compatible).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = password.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    showToast("请输入加锁密码");
                } else {
                    if (from) {
                        //加锁
                        calenderLock(pwd);
                    } else {
                        //解锁
                        unCalenderLOck(pwd);
                    }

                }
            }
        });

    }

    @Override
    protected void initData() {
        if (from) {
            toolbar.setTitle("给日记加锁");
            password.setHint("请输入加锁密码");
        } else {
            toolbar.setTitle("请输入解锁密码");
            password.setHint("请输入解锁密码");
        }

    }

    private void unCalenderLOck(String lock_status) {
        if (info != null) {
            OkGo.<String>get(ApiConstants.UN_LOCK_URL)
                    .params("uid", info.getUid())
                    .params("lock_status", lock_status)
                    .execute(new HttpStringCallback(DialogActivity.this) {
                        @Override
                        protected void onSuccess(String msg, String response) {
                            showToast(msg);
                            setResult(4000);
                            finish();
                        }

                        @Override
                        protected void onError(String response) {
                            showToast(response);
                        }
                    });
        }

    }

    private void calenderLock(String lock_status) {
        if (info != null) {
            OkGo.<String>get(ApiConstants.LOCK_URL)
                    .params("uid", info.getUid())
                    .params("lock_status", lock_status)
                    .execute(new HttpStringCallback(DialogActivity.this) {
                        @Override
                        protected void onSuccess(String msg, String response) {
                            showToast(msg);
                            setResult(4000);
                            finish();
                        }

                        @Override
                        protected void onError(String response) {
                            showToast(response);
                        }
                    });
        }

    }

}