package com.app.note.activity;

import androidx.recyclerview.widget.RecyclerView;

import com.app.note.R;
import com.app.note.adapter.AdminUserListAdapter;
import com.app.note.api.ApiConstants;
import com.app.note.base.BaseActivity;
import com.app.note.entity.UserListInfo;
import com.app.note.http.HttpStringCallback;
import com.app.note.utils.GsonUtils;
import com.lzy.okgo.OkGo;

public class AdminUserListActivity extends BaseActivity {
    private AdminUserListAdapter mListAdapter;
    private RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_admin_user_list;
    }

    @Override
    protected void initView() {
        setStatusBarDarkMode();
        recyclerView = findViewById(R.id.recyclerView);
        mListAdapter = new AdminUserListAdapter();
        recyclerView.setAdapter(mListAdapter);

    }

    @Override
    protected void initData() {
        getHttData();
    }


    private void getHttData() {
        OkGo.<String>get(ApiConstants.QUERY_USER_LIST)
                .execute(new HttpStringCallback(this) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        UserListInfo userListInfo = GsonUtils.parseJson(response, UserListInfo.class);
                        mListAdapter.setList(userListInfo.getUserList());
                    }

                    @Override
                    protected void onError(String response) {
                        showToast(response);
                    }
                });
    }
}