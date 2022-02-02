package com.app.note.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
 * 新建请假
 */
public class NoteActivity extends BaseActivity {
    private TextView start_time;
    private TextView end_time;
    private TextView content;
    private TimePickerView pvCustomTime;   //年月日

    private int year;
    private int month;
    private int curDay;
    private String calder_type;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_note;
    }

    @Override
    protected void initView() {
        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        content = findViewById(R.id.content);
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvCustomTime.show();
            }
        });
        findViewById(R.id.push).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start_timeStr = start_time.getText().toString().trim();
                String end_timeStr = end_time.getText().toString().trim();
                String contentStr = content.getText().toString().trim();
                if (TextUtils.isEmpty(end_timeStr)) {
                    showToast("请填写请假结束时间");
                } else if (TextUtils.isEmpty(contentStr)) {
                    showToast("请填写请假理由");
                }else {
                    UserInfo userInfo = ApiConstants.getUserInfo();
                    if (null!=userInfo){
                        push(userInfo.getUsername(),year,month,curDay,end_timeStr,contentStr,start_timeStr);
                    }

                }
            }
        });


        //设置数据
        year = getIntent().getIntExtra("year", 0);
        month = getIntent().getIntExtra("month", 0);
        curDay = getIntent().getIntExtra("curDay", 0);

        calder_type = getIntent().getStringExtra("calder_type");

        start_time.setText(year + "-" + month + "-" + curDay);

    }

    @Override
    protected void initData() {
        setStatusBarDarkMode();

        initCustomTimePicker();

    }

    public void initCustomTimePicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2020, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2060, 2, 28);

        pvCustomTime = new TimePickerBuilder(NoteActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                end_time.setText(getTime(date));
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