package com.app.note.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.note.R;
import com.app.note.api.ApiConstants;
import com.app.note.base.BaseActivity;
import com.app.note.entity.UserInfo;
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
 * 新建心情
 */
public class AddMoodActivity extends BaseActivity {
    private ImageView imageview;
    private TextView picker_time;
    private EditText title;

    private int year;
    private int month;
    private int curDay;
    private String calder_type;

    private String compressPath = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mood;
    }

    @Override
    protected void initView() {
        setStatusBarDarkMode();
        picker_time = findViewById(R.id.picker_time);
        title = findViewById(R.id.title);
        imageview = findViewById(R.id.imageview);


        //设置数据
        year = getIntent().getIntExtra("year", 0);
        month = getIntent().getIntExtra("month", 0);
        curDay = getIntent().getIntExtra("curDay", 0);

        calder_type = getIntent().getStringExtra("calder_type");

        picker_time.setText("  " + year + "-" + month + "-" + curDay);

        findViewById(R.id.push).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleStr = title.getText().toString().trim();
                String time = picker_time.getText().toString().trim();
                if (TextUtils.isEmpty(titleStr)){
                    showToast("请吐槽一下内容");
                }else  if (TextUtils.isEmpty(compressPath)){
                    showToast("请上传图片");
                }else {
                    UserInfo userInfo = ApiConstants.getUserInfo();
                    if (null!=userInfo){
                        push(userInfo.getUsername(),year,month,curDay,titleStr,compressPath,time);
                    }

                }

            }
        });

        findViewById(R.id.imageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(AddMoodActivity.this)
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
                                    GlideEngine.createGlideEngine().loadImage(AddMoodActivity.this, compressPath, imageview);
                                }
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }
        });
//
    }

    @Override
    protected void initData() {


    }


    private void push(String username, int year, int month, int curDay, String title, String compressPath, String create_time) {
        PostRequest<String> post = OkGo.<String>post(ApiConstants.NEWS_EDIT_URL);
        post.params("username", username);
        post.params("year", year);
        post.params("month", month);
        post.params("cur_day", curDay);
        post.params("title", title);
        if (!TextUtils.isEmpty(compressPath)){
            post.params("file", new File(compressPath));
        }
        post.params("create_time", create_time);
        post.params("calder_type", calder_type);
        post.execute(new HttpStringCallback(this) {
            @Override
            protected void onSuccess(String msg, String response) {
                showToast(msg);
                setResult(2000);
                finish();
            }

            @Override
            protected void onError(String response) {
                showToast(response);
            }
        });
    }
}