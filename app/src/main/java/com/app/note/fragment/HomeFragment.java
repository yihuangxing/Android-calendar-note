package com.app.note.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.note.R;
import com.app.note.activity.MeetingActivity;
import com.app.note.activity.AddMoodActivity;
import com.app.note.activity.MoodListActivity;
import com.app.note.activity.NoteActivity;
import com.app.note.adapter.ArticleAdapter;
import com.app.note.api.ApiConstants;
import com.app.note.base.BaseFragment;
import com.app.note.entity.NewInfo;
import com.app.note.entity.NewListInfo;
import com.app.note.entity.UserInfo;
import com.app.note.http.HttpStringCallback;
import com.app.note.utils.GsonUtils;
import com.app.note.view.GroupItemDecoration;
import com.app.note.view.GroupRecyclerView;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener {
    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;
    GroupRecyclerView mRecyclerView;

    private int currentIndex = 0;
    private int year;
    private int month;
    private int curDay;
    String[] items = {"会议", "请假", "说说"};
    private String calder_type = "会议";

    Map<String, Calendar> map = new HashMap<>();
    private List<NewInfo> list1 = new ArrayList<>();
    private List<NewInfo> list2 = new ArrayList<>();

    private ArticleAdapter mArticleAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        rootView.findViewById(R.id.mood).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MoodListActivity.class));
            }
        });

        rootView.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("新建");
                builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentIndex = which;
                        calder_type = items[which];

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (currentIndex == 0) {
                            Intent intent = new Intent(getActivity(), MeetingActivity.class);
                            intent.putExtra("year", year);
                            intent.putExtra("month", month);
                            intent.putExtra("curDay", curDay);
                            intent.putExtra("calder_type", calder_type);
                            getActivity().startActivityForResult(intent, 2000);
                        } else if (currentIndex == 2) {
                            Intent intent = new Intent(getActivity(), AddMoodActivity.class);
                            intent.putExtra("year", year);
                            intent.putExtra("month", month);
                            intent.putExtra("curDay", curDay);
                            intent.putExtra("calder_type", calder_type);
                            getActivity().startActivityForResult(intent, 2000);
                        } else if (currentIndex == 1) {
                            Intent intent = new Intent(getActivity(), NoteActivity.class);
                            intent.putExtra("year", year);
                            intent.putExtra("month", month);
                            intent.putExtra("curDay", curDay);
                            intent.putExtra("calder_type", calder_type);
                            getActivity().startActivityForResult(intent, 2000);
                        }

                        currentIndex = 0;
                        calder_type = items[0];
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });


        mTextMonthDay = rootView.findViewById(R.id.tv_month_day);
        mTextYear = rootView.findViewById(R.id.tv_year);
        mTextLunar = rootView.findViewById(R.id.tv_lunar);
        mRelativeTool = rootView.findViewById(R.id.rl_tool);
        mCalendarView = rootView.findViewById(R.id.calendarView);
        mTextCurrentDay = rootView.findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        rootView.findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        mCalendarLayout = rootView.findViewById(R.id.calendarLayout);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));

    }

    @Override
    protected void initData() {
        year = mCalendarView.getCurYear();
        month = mCalendarView.getCurMonth();
        curDay = mCalendarView.getCurDay();


//        map.put(getSchemeCalendar(year, month, 3, "#ff0000", "假").toString(),
//                getSchemeCalendar(year, month, 3, "#ff0000", "假"));
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
//        mCalendarView.setSchemeDate(map);


        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, NewInfo>());


        mArticleAdapter = new ArticleAdapter(getActivity());
        mArticleAdapter.setArticleAdapterListener(new ArticleAdapter.ArticleAdapterListener() {
            @Override
            public void onItem(NewInfo newInfo) {
               AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
               builder.setTitle("确定要删除吗？");
               builder.setMessage("删除后的数据将无法恢复");
               builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                        del(newInfo.getUid());
                   }
               });
               builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               });

               builder.show();
            }
        });
        mRecyclerView.setAdapter(mArticleAdapter);

        getHttpData();
    }

    private void getHttpData() {
        UserInfo userInfo = ApiConstants.getUserInfo();
        if (null != userInfo) {
            getHttpData(userInfo.getUsername());
        }
    }


    private Calendar getSchemeCalendar(int year, int month, int day, String color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(Color.parseColor(color));//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();

        year = calendar.getYear();
        month = calendar.getMonth();
        curDay = calendar.getDay();

        Log.e("onDateSelected", "  -- " + calendar.getYear() +
                "  --  " + calendar.getMonth() +
                "  -- " + calendar.getDay() +
                "  --  " + isClick + "  --   " + calendar.getScheme());
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }


    private void getHttpData(String username) {
        OkGo.<String>get(ApiConstants.QUERY_NEW_URL)
                .params("username", username)
                .execute(new HttpStringCallback(getActivity()) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        NewListInfo newListInfo = GsonUtils.parseJson(response, NewListInfo.class);
                        list1.clear();
                        list2.clear();
                        if (newListInfo.getInfoList().size() > 0) {
                            for (int i = 0; i < newListInfo.getInfoList().size(); i++) {
                                NewInfo info = newListInfo.getInfoList().get(i);
                                if (info.getCalder_type().contains("会议")) {
                                    list1.add(info);
                                    map.put(getSchemeCalendar(info.getYear(), info.getMonth(), info.getCur_day(), "#ff0000", "会").toString(),
                                            getSchemeCalendar(info.getYear(), info.getMonth(), info.getCur_day(), "#ff0000", "会"));
                                } else if (info.getCalder_type().contains("说说")) {
                                    map.put(getSchemeCalendar(info.getYear(), info.getMonth(), info.getCur_day(), "#ff0000", "心").toString(),
                                            getSchemeCalendar(info.getYear(), info.getMonth(), info.getCur_day(), "#ff0000", "心"));
                                } else if (info.getCalder_type().contains("请假")) {
                                    list2.add(info);
                                    map.put(getSchemeCalendar(info.getYear(), info.getMonth(), info.getCur_day(), "#ff0000", "假").toString(),
                                            getSchemeCalendar(info.getYear(), info.getMonth(), info.getCur_day(), "#ff0000", "假"));

                                }

                                mCalendarView.setSchemeDate(map);
                            }

                            if (null != mArticleAdapter) {
                                mArticleAdapter.setData(list1, list2);
                                mArticleAdapter.resetGroups();
                                mRecyclerView.notifyDataSetChanged();
                            }
                        }

                    }

                    @Override
                    protected void onError(String response) {
                        showToast(response);
                    }
                });
    }


    public void refreshData() {
        getHttpData();
    }

    private void del(int uid){
        OkGo.<String>get(ApiConstants.DEL_URL)
                .params("uid", uid)
                .execute(new HttpStringCallback(getActivity()) {
                    @Override
                    protected void onSuccess(String msg, String response) {
                        showToast(msg);
                        getHttpData();
                    }

                    @Override
                    protected void onError(String response) {
                        showToast(response);
                    }
                });


    }
}

