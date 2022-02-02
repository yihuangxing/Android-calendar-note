package com.app.note.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.app.note.R;
import com.app.note.adapter.MoodListAdapter;
import com.app.note.api.ApiConstants;
import com.app.note.base.BaseActivity;
import com.app.note.entity.NewInfo;
import com.app.note.entity.NewListInfo;
import com.app.note.entity.UserInfo;
import com.app.note.http.HttpStringCallback;
import com.app.note.utils.GsonUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.List;


public class MoodListActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private MoodListAdapter mListAdapter;
    private String[] items = {"加锁", "收藏","编辑", "删除"};
    private String[] items1 = {"删除"};
    private int currentIndex;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mood_list;
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recyclerView);

    }

    @Override
    protected void initData() {
        setStatusBarDarkMode();
        mListAdapter = new MoodListAdapter();
        mListAdapter.setMoodListListener(new MoodListAdapter.MoodListListener() {
            @Override
            public void onItem(NewInfo info) {
                Intent intent = new Intent(MoodListActivity.this, DialogActivity.class);
                intent.putExtra("info", info);
                intent.putExtra("from", false);
                startActivityForResult(intent, 4000);
            }
        });
        mListAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                NewInfo info = mListAdapter.getData().get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MoodListActivity.this);
                builder.setTitle("操作");
                if (TextUtils.isEmpty(info.getLock_status())) {
                    builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentIndex = which;
                        }
                    });
                } else {
                    builder.setSingleChoiceItems(items1, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentIndex = which;
                        }
                    });
                }

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(info.getLock_status())){
                            if (currentIndex == 0) {
                                Intent intent = new Intent(MoodListActivity.this, DialogActivity.class);
                                intent.putExtra("from", true);
                                intent.putExtra("info", info);
                                startActivityForResult(intent, 4000);
                            } else if (currentIndex == 1) {
                                //收藏
                                CollData(info);

                            } else if (currentIndex == 2) {
                                Intent intent = new Intent(MoodListActivity.this, EditMoodActivity.class);
                                intent.putExtra("info", info);
                                startActivityForResult(intent, 4000);

                            }else if (currentIndex==3){
                                delItem(info.getUid());
                            }
                        }else {
                            delItem(info.getUid());
                        }


                        currentIndex = 0;
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

                return true;
            }
        });
        recyclerView.setAdapter(mListAdapter);

        getHttpData();
    }




    private void getHttpData() {
        UserInfo userInfo = ApiConstants.getUserInfo();
        if (userInfo != null) {
            getHttpData(userInfo.getUsername());
        }
    }

    private void getHttpData(String username) {
        OkGo.<String>get(ApiConstants.QUERY_ONE_NEW_URL)
                .params("username", username)
                .params("calder_type", "说说")
                .execute(new HttpStringCallback(this) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        NewListInfo newListInfo = GsonUtils.parseJson(response, NewListInfo.class);
                        mListAdapter.setList(newListInfo.getInfoList());
                    }

                    @Override
                    protected void onError(String response) {
                        showToast(response);
                    }
                });
    }

    private void delItem(int uid) {
        OkGo.<String>get(ApiConstants.DEL_URL)
                .params("uid", uid)
                .execute(new HttpStringCallback(this) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        showToast(msg);
                        getHttpData();
                    }

                    @Override
                    protected void onError(String response) {
                        showToast(response);
                    }
                });
    }

    private void CollData(NewInfo info) {
        OkGo.<String>get(ApiConstants.COLL_URL)
                .params("uid", info.getUid())
                .params("username", info.getUsername())
                .params("year", info.getYear())
                .params("month", info.getMonth())
                .params("cur_day", info.getCur_day())
                .params("create_time", info.getCreate_time())
                .params("title", info.getTitle())
                .params("content", info.getContent())
                .params("img_url", info.getImg_url())

                .execute(new HttpStringCallback(this) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        showToast(msg);
                    }

                    @Override
                    protected void onError(String response) {
                        showToast(response);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 4000) {
            getHttpData();
        }
    }
}