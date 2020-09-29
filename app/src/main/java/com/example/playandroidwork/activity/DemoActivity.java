package com.example.playandroidwork.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.example.base.utils.CalendarReminderUtils;
import com.example.playandroidwork.R;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PermissionUtils.permission(PermissionConstants.CALENDAR)
                        .callback(new PermissionUtils.SimpleCallback() {
                            @Override
                            public void onGranted() {
                                ExecutorService service = Executors.newSingleThreadExecutor();
                                service.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        long startTime = System.currentTimeMillis() + 10000;
                                        long endTime = startTime + 10 * 60 * 1000;
                                        CalendarReminderUtils.addRepeatCalendarEvent(DemoActivity.this, "测试日历", "要记得定时签到哦", startTime, endTime);
                                    }
                                });
                            }

                            @Override
                            public void onDenied() {
                            }
                        }).request();

            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarReminderUtils.deleteCalendarEvent(DemoActivity.this, "测试日历");
            }
        });
    }

    private void OpenCalendar() {
        Calendar beginTime = Calendar.getInstance();//开始时间
        beginTime.clear();
        beginTime.set(2020, 8, 19, 12, 0);//2014年1月1日12点0分(注意：月份0-11，24小时制)
        Calendar endTime = Calendar.getInstance();//结束时间
        endTime.clear();
        endTime.set(2020, 8, 31, 13, 30);//2014年2月1日13点30分(注意：月份0-11，24小时制)
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(Uri.parse("content://com.android.calendar/events"))
                .putExtra("beginTime", beginTime.getTimeInMillis())
                .putExtra("endTime", endTime.getTimeInMillis())
                .putExtra("title", "日历测试。。。")
                .putExtra("description", "签到测试...");
        startActivity(intent);
    }

}