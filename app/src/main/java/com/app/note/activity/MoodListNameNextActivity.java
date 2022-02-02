package com.app.note.activity;

import androidx.recyclerview.widget.RecyclerView;

import com.app.note.R;
import com.app.note.adapter.AdminMoodListAdapter;
import com.app.note.api.ApiConstants;
import com.app.note.base.BaseActivity;
import com.app.note.entity.NewListInfo;
import com.app.note.http.HttpStringCallback;
import com.app.note.utils.GsonUtils;
import com.lzy.okgo.OkGo;

/**
 * 文件夹下的用户名图文详情
 */
public class MoodListNameNextActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private AdminMoodListAdapter mAdminMoodListAdapter;
    private String username;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mood_list_name_next;
    }


    @Override
    protected void initView() {
        setStatusBarDarkMode();
        recyclerView = findViewById(R.id.recyclerView);

        mAdminMoodListAdapter = new AdminMoodListAdapter();
        recyclerView.setAdapter(mAdminMoodListAdapter);
    }

    @Override
    protected void initData() {
        username = getIntent().getStringExtra("username");
        getHttpData(username);

    }

    private void getHttpData(String username) {
        OkGo.<String>get(ApiConstants.QUERY_ONE_NEW_URL)
                .params("username", username)
                .params("calder_type", "说说")
                .execute(new HttpStringCallback(this) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        NewListInfo newListInfo = GsonUtils.parseJson(response, NewListInfo.class);
                        mAdminMoodListAdapter.setList(newListInfo.getInfoList());
                    }

                    @Override
                    protected void onError(String response) {
                        showToast(response);
                    }
                });
    }
}