package com.app.note.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.note.R;
import com.app.note.entity.NewInfo;
import com.app.note.view.GroupRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 适配器
 * Created by huanghaibin on 2017/12/4.
 */

public class ArticleAdapter extends GroupRecyclerAdapter<String, NewInfo> {

    private RequestManager mLoader;
    LinkedHashMap<String, List<NewInfo>> map = new LinkedHashMap<>();
    List<String> titles = new ArrayList<>();

    private List<NewInfo> list1 = new ArrayList<>();
    private List<NewInfo> list2 = new ArrayList<>();


    public ArticleAdapter(Context context, List<NewInfo> list1, List<NewInfo> list2) {
        super(context);
        mLoader = Glide.with(context.getApplicationContext());
        map.put("最近会议", list1);
        map.put("最近请假", list2);
        titles.add("最近会议");
        titles.add("最近请假");
        resetGroups(map, titles);
    }



    public ArticleAdapter(Context context) {
        super(context);
        mLoader = Glide.with(context.getApplicationContext());
    }


    public void setData(List<NewInfo> list1, List<NewInfo> list2) {
        this.list1 = list1;
        this.list2 = list2;
    }

    public void resetGroups(){
        map.put("最近会议", list1);
        map.put("最近请假", list2);
        titles.add("最近会议");
        titles.add("最近请假");
        resetGroups(map, titles);
    }


    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ArticleViewHolder(mInflater.inflate(R.layout.item_list_article, parent, false));
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, NewInfo item, int position) {
        ArticleViewHolder h = (ArticleViewHolder) holder;
        h.mTextContent.setText(item.getContent());
        if (item.getCalder_type().equals("请假")){
            h.mImageView.setImageResource(R.mipmap.img_14);
            h.mTextTitle.setText(item.getCreate_time()+"到"+item.getTitle()+"结束");
            h.mTimepicker.setVisibility(View.GONE);
        }else {
            h.mImageView.setImageResource(R.mipmap.img_13);
            h.mTextTitle.setText(item.getTitle());
            h.mTimepicker.setText("会议时间："+item.getCreate_time());
            h.mTimepicker.setVisibility(View.VISIBLE);
        }

    }

    private static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextTitle, mTextContent,mTimepicker;
        private ImageView mImageView;

        private ArticleViewHolder(View itemView) {
            super(itemView);
            mTextTitle = itemView.findViewById(R.id.tv_title);
            mTextContent = itemView.findViewById(R.id.tv_content);
            mImageView = itemView.findViewById(R.id.imageView);
            mTimepicker = itemView.findViewById(R.id.timepicker);

        }
    }


}
