package com.csb.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.format.Time;
import cn.trinea.android.common.util.TimeUtils;

public class CalendarUtils {
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	private static SimpleDateFormat sdfDate = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static SimpleDateFormat sdfTime = new SimpleDateFormat(
			"HH:mm");
	private static String calanderURL = "";
	private static String calanderEventURL = "";
	private static String calanderRemiderURL = "";
	// 为了兼容不同版本的日历,2.2以后url发生改变
	static {
		if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
			calanderURL = "content://com.android.calendar/calendars";
			calanderEventURL = "content://com.android.calendar/events";
			calanderRemiderURL = "content://com.android.calendar/reminders";

		} else {
			calanderURL = "content://calendar/calendars";
			calanderEventURL = "content://calendar/events";
			calanderRemiderURL = "content://calendar/reminders";
		}
	}

	public static void writeEvent(Context context, String dateStr,
			String contentStr) throws Exception{
		String calId = "";
		Cursor userCursor = context.getContentResolver().query(
				Uri.parse(calanderURL), null, null, null, null);
		if (userCursor.getCount() > 0) {
			userCursor.moveToFirst();
			calId = userCursor.getString(userCursor.getColumnIndex("_id"));

		}
		ContentValues event = new ContentValues();
		event.put("title", contentStr);
		event.put("description", contentStr);
		event.put("calendar_id", calId);
		Date date = sdf.parse(dateStr);

		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(date.getTime());
		long start = mCalendar.getTime().getTime();
		// mCalendar.set(Calendar.HOUR_OF_DAY,
		// mCalendar.get(Calendar.HOUR_OF_DAY) + 1);
		// long end = mCalendar.getTime().getTime();

		event.put("dtstart", start);
		event.put("dtend", start);
		// event.put("hasAlarm", 1);
		event.put("eventTimezone", Time.getCurrentTimezone());

		Uri newEvent = context.getContentResolver().insert(
				Uri.parse(calanderEventURL), event);
		long id = Long.parseLong(newEvent.getLastPathSegment());
		ContentValues values = new ContentValues();
		values.put("event_id", id);
		values.put("minutes", 0);
		values.put("method", 1);
		context.getContentResolver().insert(Uri.parse(calanderRemiderURL),
				values);
	}

	public static ArrayList<HashMap<String, String>> queryEventList(Context context, Date date) throws Exception {
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(date.getTime());
		mCalendar.set(Calendar.HOUR_OF_DAY, 00);
		mCalendar.set(Calendar.MINUTE, 00);
		mCalendar.set(Calendar.SECOND, 00);
		long startTime = mCalendar.getTimeInMillis();
		mCalendar.set(Calendar.HOUR_OF_DAY, 23);
		mCalendar.set(Calendar.MINUTE, 59);
		mCalendar.set(Calendar.SECOND, 59);
		long endTime = mCalendar.getTimeInMillis();;
		
		Cursor eventCursor = context.getContentResolver().query(
				Uri.parse(calanderEventURL), new String[]{"title", "dtstart"}, "dtstart>=? and dtstart<=?", new String[]{String.valueOf(startTime), String.valueOf(endTime)}, null);
		ArrayList<HashMap<String, String>> resultList= new ArrayList<HashMap<String, String>>();
		while (eventCursor.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>(); 
			String eventTitle = eventCursor.getString(eventCursor
					.getColumnIndex("title"));
			Long eventDate = eventCursor.getLong(eventCursor
					.getColumnIndex("dtstart"));
			String time = TimeUtils.getTime(eventDate, sdfTime);
			map.put("title", eventTitle);
			map.put("time", time);
			resultList.add(map);
		}
		
		return resultList;
	}
	
	
	public static String queryEvents(Context context, Date date) throws Exception {
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(date.getTime());
		mCalendar.set(Calendar.HOUR_OF_DAY, 00);
		mCalendar.set(Calendar.MINUTE, 00);
		mCalendar.set(Calendar.SECOND, 00);
		long startTime = mCalendar.getTimeInMillis();
		mCalendar.set(Calendar.HOUR_OF_DAY, 23);
		mCalendar.set(Calendar.MINUTE, 59);
		mCalendar.set(Calendar.SECOND, 59);
		long endTime = mCalendar.getTimeInMillis();;
		
		Cursor eventCursor = context.getContentResolver().query(
				Uri.parse(calanderEventURL), new String[]{"title", "dtstart"}, "dtstart>=? and dtstart<=?", new String[]{String.valueOf(startTime), String.valueOf(endTime)}, null);
		StringBuffer result = new StringBuffer();
		while (eventCursor.moveToNext()) {
			String eventTitle = eventCursor.getString(eventCursor
					.getColumnIndex("title"));
			Long eventDate = eventCursor.getLong(eventCursor
					.getColumnIndex("dtstart"));
			String time = TimeUtils.getTime(eventDate, sdfTime);
			result.append(time).append(" ").append(eventTitle).append("\n");
		}
		
		return result.toString();
	}
	
	public static ArrayList<Date> queryEventDayList(Context context, int year, int month) throws Exception {
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.set(year, month-1, 1);
		mCalendar.set(Calendar.HOUR_OF_DAY, 00);
		mCalendar.set(Calendar.MINUTE, 00);
		mCalendar.set(Calendar.SECOND, 00);
		long startTime = mCalendar.getTimeInMillis();
		mCalendar.set(year, month-1, 31);
		mCalendar.set(Calendar.HOUR_OF_DAY, 23);
		mCalendar.set(Calendar.MINUTE, 59);
		mCalendar.set(Calendar.SECOND, 59);
		long endTime = mCalendar.getTimeInMillis();
		Cursor eventCursor = context.getContentResolver().query(
				Uri.parse(calanderEventURL), new String[]{"title", "dtstart"}, "dtstart>=? and dtstart<=?", new String[]{String.valueOf(startTime), String.valueOf(endTime)}, null);
		ArrayList<Date> resultList= new ArrayList<Date>();
		while (eventCursor.moveToNext()) {
			Long eventDate = eventCursor.getLong(eventCursor
					.getColumnIndex("dtstart"));
			resultList.add(new Date(eventDate));
		}
		
		return resultList;
	}
}
