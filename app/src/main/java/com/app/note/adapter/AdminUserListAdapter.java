package com.app.note.adapter;

import android.view.View;

import androidx.annotation.NonNull;

import com.app.note.R;
import com.app.note.entity.UserInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.othershe.cornerlabelview.CornerLabelView;

/**
 * desc   :
 */
public class AdminUserListAdapter extends BaseQuickAdapter<UserInfo, BaseViewHolder> {
    private CornerLabelView cornerLabelView;

    public AdminUserListAdapter() {
        super(R.layout.admin_user_list_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, UserInfo userInfo) {
        cornerLabelView = baseViewHolder.getView(R.id.cornerLabelView);
        baseViewHolder.setText(R.id.username, userInfo.getUsername());
        baseViewHolder.setText(R.id.sex, userInfo.getSex());
        baseViewHolder.setText(R.id.register_time, "注册时间：" + userInfo.getCreate_time());
        if (userInfo.getRegister_type()==1){
            cornerLabelView.setVisibility(View.VISIBLE);
        }else {
            cornerLabelView.setVisibility(View.GONE);
        }

    }
}
