package com.app.note.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.note.R;
import com.app.note.api.ApiConstants;
import com.app.note.base.BaseActivity;
import com.app.note.entity.NewInfo;
import com.app.note.http.HttpStringCallback;
import com.app.note.utils.GlideEngine;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.PostRequest;

import java.io.File;
import java.util.List;


/**
 * 编辑说说
 */
public class EditMoodActivity extends BaseActivity {
    private NewInfo info;
    private EditText title;
    private ImageView imageview;

    private String compressPath = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_mood;
    }

    @Override
    protected void initView() {
        setStatusBarDarkMode();
        info = (NewInfo) getIntent().getSerializableExtra("info");
        title = findViewById(R.id.title);
        imageview = findViewById(R.id.imageview);

        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(EditMoodActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .imageEngine(GlideEngine.createGlideEngine())
                        .isCompress(true)
                        .selectionMode(PictureConfig.SINGLE)
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                if (result != null && result.size() > 0) {
                                    LocalMedia localMedia = result.get(0);
                                    compressPath = localMedia.getCompressPath();
                                    GlideEngine.createGlideEngine().loadImage(EditMoodActivity.this, compressPath, imageview);
                                }

                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }
        });


        findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleStr = title.getText().toString().trim();
                if (TextUtils.isEmpty(titleStr)) {
                    showToast("标题不能为空");
                } else {
                    if (null != info) {
                        edit(info.getUid(), titleStr);
                    }

                }
            }
        });

    }

    @Override
    protected void initData() {
        if (null != info) {
            title.setText(info.getTitle());
            GlideEngine.createGlideEngine().loadImage(this, info.getImg_url(), imageview);
        }

    }

    public void edit(int uid, String title) {
        PostRequest<String> post = OkGo.<String>post(ApiConstants.EDIT_NOTE_URL);
        post.params("uid", uid);
        post.params("title", title);
        if (!TextUtils.isEmpty(compressPath)) {
            post.params("file", new File(compressPath));
        }
        post.execute(new HttpStringCallback(this) {
            @Override
            protected void onSuccess(String msg, String response) {
                showToast(msg);
                setResult(4000);
                finish();

            }

            @Override
            protected void onError(String response) {
                showToast(response);

            }
        });
    }
}