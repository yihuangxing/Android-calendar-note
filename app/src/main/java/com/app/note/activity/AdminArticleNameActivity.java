package com.app.note.activity;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.note.R;
import com.app.note.adapter.MoodListNameAdapter;
import com.app.note.api.ApiConstants;
import com.app.note.base.BaseActivity;
import com.app.note.entity.MoodInfo;
import com.app.note.entity.NewInfo;
import com.app.note.entity.NewListInfo;
import com.app.note.http.HttpStringCallback;
import com.app.note.utils.GsonUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdminArticleNameActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private MoodListNameAdapter mMoodListNameAdapter;
    private List<MoodInfo> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_admin_article_name;
    }

    @Override
    protected void initView() {

        mRecyclerView = findViewById(R.id.recyclerView);

        mMoodListNameAdapter = new MoodListNameAdapter();
        mMoodListNameAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                MoodInfo moodInfo = mMoodListNameAdapter.getData().get(position);
                Intent intent = new Intent(AdminArticleNameActivity.this, AdminArticleListActivity.class);
                intent.putExtra("username", moodInfo.getUsername());
                startActivity(intent);


            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mMoodListNameAdapter);

    }

    @Override
    protected void initData() {
        setStatusBarDarkMode();

        getHttpData();
    }

    private void getHttpData() {
        OkGo.<String>get(ApiConstants.QUERY_NEW_URL)
                .execute(new HttpStringCallback(this) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        mList.clear();
                        NewListInfo newListInfo = GsonUtils.parseJson(response, NewListInfo.class);
                        for (int i = 0; i < newListInfo.getInfoList().size(); i++) {
                            if (newListInfo.getInfoList().get(i).getCalder_type().contains("会议") || newListInfo.getInfoList().get(i).getCalder_type().contains("请假")) {
                                mList.add(new MoodInfo(newListInfo.getInfoList().get(i).getUid(), newListInfo.getInfoList().get(i).getUsername()));
                            }
                        }
                        List<MoodInfo> moodInfos = removeDuplicate(mList);
                        mMoodListNameAdapter.setList(moodInfos);

                    }

                    @Override
                    protected void onError(String response) {
                        showToast(response);
                    }
                });
    }


    public static List<MoodInfo> removeDuplicate(List<MoodInfo> list) {
        List<MoodInfo> newList = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (MoodInfo user : list) {
            String userName = user.getUsername();
            if (!set.contains(userName)) { //set中不包含重复的
                set.add(userName);
                newList.add(user);
            }
        }
        return newList;
    }

}