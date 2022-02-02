package com.app.note.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.allen.library.SuperTextView;
import com.app.note.R;
import com.app.note.activity.LoginActivity;
import com.app.note.activity.UpdatePwdActivity;
import com.app.note.api.ApiConstants;
import com.app.note.base.BaseFragment;
import com.app.note.entity.UserInfo;


public class MineFragment extends BaseFragment {
    private TextView username;
    private SuperTextView sex;
    private SuperTextView nickname;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        username = rootView.findViewById(R.id.username);
        nickname = rootView.findViewById(R.id.nickname);
        sex = rootView.findViewById(R.id.sex);


        rootView.findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("确定要退出登录吗？");
                builder.setMessage("退出登录将清空登录数据");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiConstants.setUserInfo(null);
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        if (null != getActivity()) {
                            getActivity().finish();
                        }
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

        rootView.findViewById(R.id.updatePwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), UpdatePwdActivity.class), 2000);
            }
        });


    }

    @Override
    protected void initData() {
        UserInfo userInfo = ApiConstants.getUserInfo();
        if (null != userInfo) {
            username.setText(userInfo.getUsername());
            sex.setLeftString("性别：" + userInfo.getSex());
            nickname.setLeftString("昵称：" + userInfo.getNickname());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2000) {
            if (getActivity() != null) {
                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        }
    }
}