package com.app.note.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.app.note.R;
import com.app.note.api.ApiConstants;
import com.app.note.base.BaseActivity;

public class AdminMainActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_admin_main;
    }

    @Override
    protected void initView() {
        setStatusBarDarkMode();

        findViewById(R.id.float2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this, AdminMoodListActivity.class));
            }
        });

        findViewById(R.id.float4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this, AdminUserListActivity.class));
            }
        });

        findViewById(R.id.float3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainActivity.this);
                builder.setTitle("确定要退出登录吗?");
                builder.setMessage("退出登录将清空用户信息");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiConstants.setUserInfo(null);
                        showToast("退出成功");
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });

        findViewById(R.id.float1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMainActivity.this, AdminArticleListActivity.class));
            }
        });

    }

    @Override
    protected void initData() {

    }
}