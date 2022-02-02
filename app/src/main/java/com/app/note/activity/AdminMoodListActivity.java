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

public class AdminMoodListActivity extends BaseActivity {
    private AdminMoodListAdapter mAdminMoodListAdapter;
    private RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_admin_mood_list;
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
        getHttpData();
    }


    private void getHttpData() {
        OkGo.<String>get(ApiConstants.QUERY_GRAPHIC_URL)
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