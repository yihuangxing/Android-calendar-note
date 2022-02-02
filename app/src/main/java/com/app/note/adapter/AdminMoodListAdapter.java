package com.app.note.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.note.R;
import com.app.note.entity.NewInfo;
import com.app.note.utils.BlurTransformation;
import com.app.note.utils.GlideEngine;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

/**
 * desc   :
 */
public class AdminMoodListAdapter extends BaseQuickAdapter<NewInfo, BaseViewHolder> {
    private ImageView mImageView;
    private TextView tv_lock;

    public AdminMoodListAdapter() {
        super(R.layout.admin_mood_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, NewInfo newInfo) {
        mImageView = baseViewHolder.getView(R.id.imageview);
        tv_lock = baseViewHolder.getView(R.id.tv_lock);
        baseViewHolder.setText(R.id.title, newInfo.getTitle());
        baseViewHolder.setText(R.id.create_time, newInfo.getCreate_time());
        baseViewHolder.setText(R.id.username, newInfo.getUsername());
        if (TextUtils.isEmpty(newInfo.getLock_status())) {
            GlideEngine.createGlideEngine().loadImage(getContext().getApplicationContext(), newInfo.getImg_url(), mImageView);
            tv_lock.setVisibility(View.GONE);
        } else {
            Glide.with(getContext().getApplicationContext()).load(newInfo.getImg_url())
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(getContext().getApplicationContext(), 25, 3)))
                    .into(mImageView);
            tv_lock.setVisibility(View.VISIBLE);
            tv_lock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mMoodListListener) {
                        mMoodListListener.onItem(newInfo);
                    }
                }
            });

        }
    }


    public void setMoodListListener(MoodListListener moodListListener) {
        mMoodListListener = moodListListener;
    }

    private MoodListListener mMoodListListener;

    public interface MoodListListener {
        void onItem(NewInfo info);
    }

}
