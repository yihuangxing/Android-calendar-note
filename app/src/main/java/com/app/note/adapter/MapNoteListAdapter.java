package com.app.note.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.app.note.R;
import com.app.note.entity.MapNoteInfo;
import com.app.note.utils.GlideEngine;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

/**
 * desc   :
 */
public class MapNoteListAdapter extends BaseQuickAdapter<MapNoteInfo, BaseViewHolder> {
    private ImageView mImageView;
    public MapNoteListAdapter() {
        super(R.layout.map_note_list_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, MapNoteInfo mapNoteInfo) {
        mImageView = baseViewHolder.getView(R.id.imageview);

        baseViewHolder.setText(R.id.title,mapNoteInfo.getTitle());
        baseViewHolder.setText(R.id.content,mapNoteInfo.getContent());

        GlideEngine.createGlideEngine().loadImage(getContext().getApplicationContext(), mapNoteInfo.getImg_url(), mImageView);
    }
}
