package com.app.note.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.note.R;
import com.app.note.api.ApiConstants;
import com.app.note.base.BaseActivity;
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

public class AddMapNoteActivity extends BaseActivity {
    private ImageView imageview;
    private String title;
    private String detail;
    private Double latitude;
    private Double longitude;

    private TextView tv_detail;
    private EditText et_content;

    private String compressPath = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_map_note;
    }

    @Override
    protected void initView() {
        setStatusBarDarkMode();
        tv_detail = findViewById(R.id.tv_detail);
        imageview = findViewById(R.id.imageview);
        et_content = findViewById(R.id.et_content);

        findViewById(R.id.imageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(AddMapNoteActivity.this)
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
                                    GlideEngine.createGlideEngine().loadImage(AddMapNoteActivity.this, compressPath, imageview);
                                }
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }
        });

        findViewById(R.id.push).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contentStr = et_content.getText().toString();
                if (TextUtils.isEmpty(contentStr)) {
                    showToast("请输入内容");
                } else if (TextUtils.isEmpty(compressPath)) {
                    showToast("请上传图片");
                } else {
                    addMapNote(contentStr);
                }
            }
        });

    }

    @Override
    protected void initData() {
        title = getIntent().getStringExtra("title");
        detail = getIntent().getStringExtra("detail");
        latitude = getIntent().getDoubleExtra("latitude", 0f);
        longitude = getIntent().getDoubleExtra("longitude", 0f);

        tv_detail.setText(detail);

    }

    public void addMapNote(String content) {
        PostRequest<String> post = OkGo.post(ApiConstants.ADD_MAP_NOTE_URL);
        post.params("username", ApiConstants.getUserInfo().getUsername());
        post.params("title", title);
        post.params("content", content);
        post.params("file", new File(compressPath));
        post.params("latitude", latitude);
        post.params("longitude", longitude);
        post.execute(new HttpStringCallback(this) {
            @Override
            protected void onSuccess(String msg, String response) {
                showToast(msg);
                setResult(3000);
                finish();
            }

            @Override
            protected void onError(String response) {
                showToast(response);
            }
        });


    }
}