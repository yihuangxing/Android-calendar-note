package com.app.note.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.note.R;
import com.app.note.adapter.MoodListAdapter;
import com.app.note.api.ApiConstants;
import com.app.note.base.BaseFragment;
import com.app.note.entity.NewInfo;
import com.app.note.entity.NewListInfo;
import com.app.note.entity.UserInfo;
import com.app.note.http.HttpStringCallback;
import com.app.note.utils.GsonUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.lzy.okgo.OkGo;


public class CollectionFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private MoodListAdapter mListAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_collection;
    }

    @Override
    protected void initView() {
        recyclerView = rootView.findViewById(R.id.recyclerView);
        mListAdapter = new MoodListAdapter();
        mListAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("确定取消收藏？");
                builder.setMessage("取消收藏后的数据将无法恢复");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mListAdapter != null && ApiConstants.getUserInfo() != null) {
                            NewInfo info = mListAdapter.getData().get(position);
                            mListAdapter.removeAt(position);
                            delColl(info.getUid());
                        }


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

    }

    @Override
    protected void initData() {
        getHttpData();
    }


    /**
     * 刷新数据
     */
    public void refreshData() {
        getHttpData();
    }

    public void getHttpData() {
        UserInfo userInfo = ApiConstants.getUserInfo();
        if (null != userInfo) {
            OkGo.<String>get(ApiConstants.COLL_LIST_URL)
                    .params("username", userInfo.getUsername())
                    .execute(new HttpStringCallback(getActivity()) {
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
    }

    /**
     * 取消收藏
     */
    public void delColl(int uid) {
        OkGo.<String>get(ApiConstants.DEL_COLL_URL)
                .params("uid", uid)
                .execute(new HttpStringCallback(getActivity()) {
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
}