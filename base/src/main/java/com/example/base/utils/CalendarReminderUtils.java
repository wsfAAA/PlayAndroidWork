package com.example.base.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class CalendarReminderUtils {
    private static String CALENDER_URL = "content://com.android.calendar/calendars";
    private static String CALENDER_EVENT_URL = "content://com.android.calendar/events";
    private static String CALENDER_REMINDER_URL = "content://com.android.calendar/reminders";

    private static String CALENDARS_NAME = "test";
    private static String CALENDARS_ACCOUNT_NAME = "Test@163.com";
    private static String CALENDARS_ACCOUNT_TYPE = "com.android.test";
    private static String CALENDARS_DISPLAY_NAME = "Test账户";

    /**
     * 检查是否已经添加了日历账户，如果没有添加先添加一个日历账户再查询
     * 获取账户成功返回账户id，否则返回-1
     */
    private static int checkAndAddCalendarAccount(Context context) {
        int oldId = checkCalendarAccount(context);
        if (oldId >= 0) {
            return oldId;
        } else {
            long addId = addCalendarAccount(context);
            if (addId >= 0) {
                return checkCalendarAccount(context);
            } else {
                return -1;
            }
        }
    }

    /**
     * 检查是否存在现有账户，存在则返回账户id，否则返回-1
     */
    private static int checkCalendarAccount(Context context) {
        Cursor userCursor = context.getContentResolver().query(Uri.parse(CALENDER_URL), null, null, null, null);
        try {
            if (userCursor == null) { //查询返回空值
                return -1;
            }
            int count = userCursor.getCount();
            if (count > 0) { //存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }

    /**
     * 添加日历账户，账户创建成功则返回账户id，否则返回-1
     */
    private static long addCalendarAccount(Context context) {
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Uri.parse(CALENDER_URL);
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
                .build();

        Uri result = context.getContentResolver().insert(calendarUri, value);
        long id = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }

    /**
     * 添加日历事件
     */
    public static void addRepeatCalendarEvent(Context context, String title, String description, long startTime, long endTime) {
        if (context == null) {
            return;
        }
        int calId = checkAndAddCalendarAccount(context); //获取日历账户的id
        if (calId < 0) { //获取账户id失败直接返回，添加日历事件失败
            return;
        }

        //添加日历事件
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(startTime);   //设置开始时间
        long start = mCalendar.getTime().getTime();
        mCalendar.setTimeInMillis(endTime);     //设置终止时间，
        long end = mCalendar.getTime().getTime();
        if (!checkCalendarEvent(context, title, description, startTime, endTime)) {

            Log.e("wsf", TimeUtils.millis2String(start) + "     end time:  " + TimeUtils.millis2String(end) + "      时区：  " + TimeZone.getDefault().getID()+"     "+Thread.currentThread().getName());

            ContentValues event = new ContentValues();
            event.put(CalendarContract.Events.TITLE, title);      // 事件标题
            event.put(CalendarContract.Events.DESCRIPTION, description);    // 事件描述(对应手机系统日历备注栏)
            event.put(CalendarContract.Events.CALENDAR_ID, calId); //插入账户的id
            event.put(CalendarContract.Events.DTSTART, start);       //	事件开始时间
            event.put(CalendarContract.Events.DTEND, end);           //  事件结束时间
            event.put(CalendarContract.Events.RRULE, "FREQ=DAILY;INTERVAL=1");  //事件的重复发生规则格式。
            event.put(CalendarContract.Events.HAS_ALARM, 1);    //设置有闹钟提醒
            event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());   //事件的时区。
            Uri newEvent = context.getContentResolver().insert(Uri.parse(CALENDER_EVENT_URL), event); //添加事件
            if (newEvent == null) { //添加日历事件失败直接返回
                return;
            }

            //事件提醒的设定
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent));
            values.put(CalendarContract.Reminders.MINUTES, 0);   // 设置提醒提前的时间(0：准时  -1：使用系统默认)
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);  // 设置事件提醒方式为通知警报
            Uri uri = context.getContentResolver().insert(Uri.parse(CALENDER_REMINDER_URL), values);
            if (uri == null) { //添加事件提醒失败直接返回
                return;
            }

        }
    }

    /**
     * 删除日历事件
     */
    public static void deleteCalendarEvent(Context context, String title) {
        if (context == null) {
            return;
        }
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null);
        try {
            if (eventCursor == null) { //查询返回空值
                return;
            }
            if (eventCursor.getCount() > 0) {
                //遍历所有事件，找到title跟需要查询的title一样的项
                for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()) {
                    String eventTitle = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.TITLE));
                    if (!TextUtils.isEmpty(title) && title.equals(eventTitle)) {
                        int id = eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID));//取得id
                        Uri deleteUri = ContentUris.withAppendedId(Uri.parse(CALENDER_EVENT_URL), id);
                        int rows = context.getContentResolver().delete(deleteUri, null, null);
                        if (rows == -1) { //事件删除失败
                            return;
                        }
                    }
                }
            }
        } finally {
            if (eventCursor != null) {
                eventCursor.close();
            }
        }
    }

    /**
     * 检查日历事件
     *
     * @param context
     * @param title
     */
    public static boolean checkCalendarEvent(Context context, String title, String description, long startTime, long endTime) {
        if (context == null) {
            return false;
        }
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null);
        try {
            if (eventCursor == null) { //查询返回空值
                return false;
            }
            if (eventCursor.getCount() > 0) {
                //遍历所有事件，找到title跟需要查询的title一样的项
                String eventTitle = "";
                String eventDescription = "";
                long eventStartTime;
                long eventEndTime;
                while (eventCursor.moveToNext()) {
                    eventTitle = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.TITLE));
                    eventDescription = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.DESCRIPTION));
                    eventStartTime = Long.parseLong(eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.DTSTART)));
                    eventEndTime = Long.parseLong(eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.DTEND)));
                    if ((title != null && title.equals(eventTitle)) && (description != null && description.equals(eventDescription))/** && (startTime == eventStartTime) && (endTime == eventEndTime)**/) {
                        return true;
                    }
                }
            }
        } finally {
            if (eventCursor != null) {
                eventCursor.close();
            }
        }
        return false;
    }

    /**
     * 获取日历事件
     *
     * @param context
     * @return
     */
    private static JSONArray getCalendarEvent(Context context) {
        String startTime = "";
        String endTime = "";
        String eventTitle = "";
        String description = "";
        String location = "";

        JSONArray arr = new JSONArray();
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null);
        while (eventCursor.moveToNext()) {
            JSONObject json = new JSONObject();
            eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"));
            description = eventCursor.getString(eventCursor.getColumnIndex("description"));
            location = eventCursor.getString(eventCursor.getColumnIndex("eventLocation"));
            startTime = timeStamp2Date(Long.parseLong(eventCursor.getString(eventCursor.getColumnIndex("dtstart"))));
            endTime = timeStamp2Date(Long.parseLong(eventCursor.getString(eventCursor.getColumnIndex("dtend"))));
            try {
                json.put("eventTitle", eventTitle);
                json.put("description", description);
                json.put("location", location);
                json.put("startTime", startTime);
                json.put("endTime", endTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            arr.put(json);
        }
        return arr;
    }

    /**
     * 时间戳转换为字符串
     *
     * @param time:时间戳
     * @return
     */
    private static String timeStamp2Date(long time) {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }
}
