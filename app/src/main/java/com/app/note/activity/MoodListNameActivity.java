package com.app.note.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.note.R;
import com.app.note.adapter.MoodListNameAdapter;
import com.app.note.api.ApiConstants;
import com.app.note.base.BaseActivity;
import com.app.note.entity.MoodInfo;
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

/**
 * 创建图文用户目录
 */
public class MoodListNameActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private MoodListNameAdapter mMoodListNameAdapter;
    private List<MoodInfo> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mood_list_name;
    }

    @Override
    protected void initView() {
        setStatusBarDarkMode();
        recyclerView = findViewById(R.id.recyclerView);
        mMoodListNameAdapter = new MoodListNameAdapter();
        mMoodListNameAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                MoodInfo moodInfo = mMoodListNameAdapter.getData().get(position);
                if (null != moodInfo) {
                    Intent intent = new Intent(MoodListNameActivity.this, MoodListNameNextActivity.class);
                    intent.putExtra("username", moodInfo.getUsername());
                    startActivity(intent);
                }

            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(mMoodListNameAdapter);

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
                        mList.clear();
                        NewListInfo newListInfo = GsonUtils.parseJson(response, NewListInfo.class);
                        for (int i = 0; i < newListInfo.getInfoList().size(); i++) {
                            mList.add(new MoodInfo(newListInfo.getInfoList().get(i).getUid(), newListInfo.getInfoList().get(i).getUsername()));
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