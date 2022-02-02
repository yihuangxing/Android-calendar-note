package com.app.note.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
 */

public class AdminArticleAdapter extends GroupRecyclerAdapter<String, NewInfo> {

    private RequestManager mLoader;
    LinkedHashMap<String, List<NewInfo>> map = new LinkedHashMap<>();
    List<String> titles = new ArrayList<>();

    private List<NewInfo> list1 = new ArrayList<>();
    private List<NewInfo> list2 = new ArrayList<>();


    public AdminArticleAdapter(Context context, List<NewInfo> list1, List<NewInfo> list2) {
        super(context);
        mLoader = Glide.with(context.getApplicationContext());
        map.put("最近会议", list1);
        map.put("最近请假", list2);
        titles.add("最近会议");
        titles.add("最近请假");
        resetGroups(map, titles);
    }


    public AdminArticleAdapter(Context context) {
        super(context);
        mLoader = Glide.with(context.getApplicationContext());
    }


    public void setData(List<NewInfo> list1, List<NewInfo> list2) {
        this.list1 = list1;
        this.list2 = list2;
    }

    public void resetGroups() {
        map.put("会议记录", list1);
        map.put("请假记录", list2);
        titles.add("会议记录");
        titles.add("请假记录");
        resetGroups(map, titles);
    }


    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ArticleViewHolder(mInflater.inflate(R.layout.admin_item_list_article, parent, false));
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, NewInfo item, int position) {
        ArticleViewHolder h = (ArticleViewHolder) holder;

        if (item.getCalder_type().equals("请假")) {
            h.mImageView.setImageResource(R.mipmap.img_14);
            h.mTextTitle.setText(item.getCreate_time() + "到" + item.getTitle() + "结束");
            h.mTimepicker.setVisibility(View.GONE);
            h.mTextContent.setText("原因：" + item.getContent());
        } else {
            h.mImageView.setImageResource(R.mipmap.img_13);
            h.mTextTitle.setText("会议主题：" + item.getTitle());
            h.mTimepicker.setText("会议时间：" + item.getCreate_time());
            h.mTextContent.setText("会议摘要：" + item.getContent());
            h.mTimepicker.setVisibility(View.VISIBLE);
        }
        h.create_name.setText("创建人："+item.getUsername());

        //点击事件
        h.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != mArticleAdapterListener) {
                    mArticleAdapterListener.onItem(item);
                }
                return true;
            }
        });

    }

    private static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextTitle, mTextContent, mTimepicker,create_name;
        private ImageView mImageView;
        private RelativeLayout rootView;

        private ArticleViewHolder(View itemView) {
            super(itemView);
            mTextTitle = itemView.findViewById(R.id.tv_title);
            mTextContent = itemView.findViewById(R.id.tv_content);
            mImageView = itemView.findViewById(R.id.imageView);
            mTimepicker = itemView.findViewById(R.id.timepicker);
            rootView = itemView.findViewById(R.id.rootView);
            create_name =itemView.findViewById(R.id.create_name);

        }
    }


    private ArticleAdapterListener mArticleAdapterListener;

    public ArticleAdapterListener getArticleAdapterListener() {
        return mArticleAdapterListener;
    }

    public void setArticleAdapterListener(ArticleAdapterListener articleAdapterListener) {
        mArticleAdapterListener = articleAdapterListener;
    }

    public interface ArticleAdapterListener {
        void onItem(NewInfo newInfo);
    }

}
