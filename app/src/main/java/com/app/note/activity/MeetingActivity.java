package com.app.note.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.note.R;
import com.app.note.api.ApiConstants;
import com.app.note.base.BaseActivity;
import com.app.note.entity.UserInfo;
import com.app.note.http.HttpStringCallback;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.lzy.okgo.OkGo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 新建会议
 */
public class MeetingActivity extends BaseActivity {
    private TextView picker_time;
    private EditText title;
    private EditText content;

    private TimePickerView pvCustomTime;   //年月日

    private int year;
    private int month;
    private int curDay;
    private String calder_type;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_meeting;
    }

    @Override
    protected void initView() {
        setStatusBarDarkMode();
        picker_time = findViewById(R.id.picker_time);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        picker_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.push).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = picker_time.getText().toString();
                String contentStr = content.getText().toString();
                String titleStr = title.getText().toString();
                if (TextUtils.isEmpty(titleStr)) {
                    showToast("请填写会议主题");
                } else if (TextUtils.isEmpty(contentStr)) {
                    showToast("请填写会议摘要");
                } else {
                    UserInfo userInfo = ApiConstants.getUserInfo();
                    if (null != userInfo) {
                        push(userInfo.getUsername(), year, month, curDay, titleStr, contentStr, time);
                    }

                }
            }
        });


        //设置数据
        year = getIntent().getIntExtra("year", 0);
        month = getIntent().getIntExtra("month", 0);
        curDay = getIntent().getIntExtra("curDay", 0);

        calder_type = getIntent().getStringExtra("calder_type");

        picker_time.setText(year + "-" + month + "-" + curDay);


    }

    @Override
    protected void initData() {
    }

    public void initCustomTimePicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2020, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2060, 2, 28);

        pvCustomTime = new TimePickerBuilder(MeetingActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                picker_time.setText(getTime(date));
            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                        TextView ivCancel = v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setContentTextSize(18)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


    private void push(String username, int year, int month, int curDay, String title, String content, String create_time) {
        OkGo.<String>post(ApiConstants.NEWS_EDIT_URL)
                .params("username", username)
                .params("year", year)
                .params("month", month)
                .params("cur_day", curDay)
                .params("title", title)
                .params("content", content)
                .params("create_time", create_time)
                .params("calder_type", calder_type)
                .execute(new HttpStringCallback(this) {
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