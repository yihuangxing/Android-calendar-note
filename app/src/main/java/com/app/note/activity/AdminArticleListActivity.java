package com.app.note.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import com.app.note.R;
import com.app.note.adapter.AdminArticleAdapter;
import com.app.note.api.ApiConstants;
import com.app.note.base.BaseActivity;
import com.app.note.entity.NewInfo;
import com.app.note.entity.NewListInfo;
import com.app.note.entity.UserInfo;
import com.app.note.http.HttpStringCallback;
import com.app.note.utils.GsonUtils;
import com.app.note.view.GroupItemDecoration;
import com.app.note.view.GroupRecyclerView;
import com.haibin.calendarview.Calendar;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class AdminArticleListActivity extends BaseActivity {
     private GroupRecyclerView recyclerView;

    private AdminArticleAdapter mArticleAdapter;

    private List<NewInfo> list1 = new ArrayList<>();
    private List<NewInfo> list2 = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_admin_article_list;
    }

    @Override
    protected void initView() {
        setStatusBarDarkMode();
        recyclerView =findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new GroupItemDecoration<String, NewInfo>());


        mArticleAdapter = new AdminArticleAdapter(this);
        recyclerView.setAdapter(mArticleAdapter);

    }

    @Override
    protected void initData() {
        getHttpData();
    }

    private void getHttpData() {
        OkGo.<String>get(ApiConstants.QUERY_NEW_URL)
                .execute(new HttpStringCallback(this) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        NewListInfo newListInfo = GsonUtils.parseJson(response, NewListInfo.class);
                        list1.clear();
                        list2.clear();
                        if (newListInfo.getInfoList().size() > 0) {
                            for (int i = 0; i < newListInfo.getInfoList().size(); i++) {
                                NewInfo info = newListInfo.getInfoList().get(i);
                                if (info.getCalder_type().contains("会议")) {
                                    list1.add(info);
                                }  else if (info.getCalder_type().contains("请假")) {
                                    list2.add(info);
                                }
                            }
                            if (null != mArticleAdapter) {
                                mArticleAdapter.setData(list1, list2);
                                mArticleAdapter.resetGroups();
                                recyclerView.notifyDataSetChanged();
                            }
                        }

                    }

                    @Override
                    protected void onError(String response) {
                        showToast(response);
                    }
                });
    }


}