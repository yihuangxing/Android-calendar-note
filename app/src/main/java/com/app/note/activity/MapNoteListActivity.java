package com.app.note.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.amap.api.services.core.LatLonPoint;
import com.app.note.R;
import com.app.note.adapter.MapNoteListAdapter;
import com.app.note.api.ApiConstants;
import com.app.note.base.BaseActivity;
import com.app.note.entity.MapNoteInfo;
import com.app.note.entity.MapNoteListInfo;
import com.app.note.http.HttpStringCallback;
import com.app.note.utils.GsonUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.lzy.okgo.OkGo;

import java.util.List;

public class MapNoteListActivity extends BaseActivity {
    private MapNoteListAdapter mMapNoteListAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map_note_list;
    }

    @Override
    protected void initView() {
        setStatusBarDarkMode();
        mRecyclerView = findViewById(R.id.recyclerView);
        mMapNoteListAdapter = new MapNoteListAdapter();
        mMapNoteListAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                MapNoteInfo mapNoteInfo = mMapNoteListAdapter.getData().get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MapNoteListActivity.this);
                builder.setTitle("确定删除？");
                builder.setMessage("删除后的数据将无法恢复");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delNote(mapNoteInfo.getUid(), position);
                    }
                });

                builder.show();

                return true;
            }
        });
        mRecyclerView.setAdapter(mMapNoteListAdapter);

    }

    private void delNote(int uid, int position) {
        OkGo.<String>get(ApiConstants.DEL_MAP_NOTE_URL)
                .params("uid", uid)
                .execute(new HttpStringCallback(null) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        showToast(msg);
                        setResult(3000);
                        mMapNoteListAdapter.removeAt(position);
                    }

                    @Override
                    protected void onError(String response) {
                        showToast(response);
                    }
                });
    }

    @Override
    protected void initData() {
        getMarkers();
    }


    public void getMarkers() {
        OkGo.<String>get(ApiConstants.QUERY_MAP_NOTE_URL)
                .params("username", ApiConstants.getUserInfo().getUsername())
                .execute(new HttpStringCallback(null) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        MapNoteListInfo mapNoteListInfo = GsonUtils.parseJson(response, MapNoteListInfo.class);
                        List<MapNoteInfo> list = mapNoteListInfo.getList();
                        mMapNoteListAdapter.setList(list);
                    }

                    @Override
                    protected void onError(String response) {

                    }
                });

    }

}