package com.app.note.adapter;

import androidx.annotation.NonNull;
import com.app.note.R;
import com.app.note.entity.MoodInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

/**
 * desc   :
 */
public class MoodListNameAdapter extends BaseQuickAdapter<MoodInfo, BaseViewHolder> {
    public MoodListNameAdapter() {
        super(R.layout.mood_name_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, MoodInfo moodInfo) {
        baseViewHolder.setText(R.id.username, moodInfo.getUsername());
    }
}
