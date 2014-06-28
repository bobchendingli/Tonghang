package com.csb.ui.setting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.trinea.android.common.util.TimeUtils;

import com.csb.R;
import com.csb.utils.CalendarUtils;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

@SuppressLint("SimpleDateFormat")
public class MyCalendarActivity extends FragmentActivity {
	final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	final SimpleDateFormat formatterDay = new SimpleDateFormat("dd");
	private Button btn_title_left;
	private Button btn_title_right;
	private TextView tv_top_title;
	private TextView tv_day;
	private EditText et_events;
	private LinearLayout ll_events;
	private CaldroidFragment caldroidFragment;
	private CaldroidFragment dialogCaldroidFragment;
	private Bundle selectDate = new Bundle();

	private void setCustomResourceForDates(int year, int month) {
		ArrayList<Date> eventDays = null;
		try {
			eventDays = CalendarUtils.queryEventDayList(
					getApplicationContext(), year, month);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (eventDays.size() > 0 && caldroidFragment != null) {
			for (Date date : eventDays) {
				caldroidFragment.setBackgroundResourceForDate(R.color.green,
						date);
				caldroidFragment.setTextColorForDate(R.color.white, date);
			}
			if (caldroidFragment != null)
				caldroidFragment.refreshView();
		}
	}

	private void showEvents(Date date) {
		try {
			String events = CalendarUtils.queryEvents(getApplicationContext(),
					date);
			if (!TextUtils.isEmpty(events)) {
				tv_day.setText(String.valueOf(TimeUtils.getTime(date.getTime(),
						formatterDay)));
				et_events.setText(events);
				ll_events.setVisibility(View.VISIBLE);
			} else {
				ll_events.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		int month = selectDate.getInt(CaldroidFragment.MONTH);
		int year = selectDate.getInt(CaldroidFragment.YEAR);
		setCustomResourceForDates(year, month);
		
		Calendar cal = Calendar.getInstance();
		Date blueDate = cal.getTime();
		if (caldroidFragment != null) {
			caldroidFragment.setBackgroundResourceForDate(R.color.blue,
					blueDate);
			caldroidFragment.setTextColorForDate(R.color.white, blueDate);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_calendar);

		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("我的日历");
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setText("返回");
		btn_title_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyCalendarActivity.this.finish();
			}
		});
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		btn_title_right.setText("添加");
		btn_title_right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyCalendarActivity.this,
						AddRemindEventActivity.class);
				MyCalendarActivity.this.startActivity(intent);
			}
		});

		tv_day = (TextView) findViewById(R.id.tv_day);
		et_events = (EditText) findViewById(R.id.et_events);
		ll_events = (LinearLayout) findViewById(R.id.ll_events);

		// Setup caldroid fragment
		// **** If you want normal CaldroidFragment, use below line ****
		caldroidFragment = new CaldroidFragment();

		// //////////////////////////////////////////////////////////////////////
		// **** This is to show customized fragment. If you want customized
		// version, uncomment below line ****
		// caldroidFragment = new CaldroidSampleCustomFragment();

		// Setup arguments

		// If Activity is created after rotation
		if (savedInstanceState != null) {
			caldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
		}
		// If activity is created from fresh
		else {
			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

			// Uncomment this to customize startDayOfWeek
			// args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
			// CaldroidFragment.TUESDAY); // Tuesday
			caldroidFragment.setArguments(args);
			selectDate.putInt(CaldroidFragment.MONTH,
					cal.get(Calendar.MONTH) + 1);
			selectDate.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
		}

		// Attach to the activity
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendar1, caldroidFragment);
		t.commit();

		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
				showEvents(date);
			}

			@Override
			public void onChangeMonth(int month, int year) {
				ll_events.setVisibility(View.GONE);
				selectDate.putInt(CaldroidFragment.MONTH, month);
				selectDate.putInt(CaldroidFragment.YEAR, year);
				setCustomResourceForDates(year, month);
				Calendar cal = Calendar.getInstance();
				Date date = cal.getTime();
				if (caldroidFragment != null) {
					caldroidFragment.setBackgroundResourceForDate(R.color.blue,
							date);
					caldroidFragment.setTextColorForDate(R.color.white, date);
				}
			}

			@Override
			public void onLongClickDate(Date date, View view) {
			}

			@Override
			public void onCaldroidViewCreated() {
				Calendar cal = Calendar.getInstance();
				Date date = cal.getTime();
				showEvents(date);
				
			}

		};

		// Setup Caldroid
		caldroidFragment.setCaldroidListener(listener);

	}

	/**
	 * Save current states of the Caldroid here
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		if (caldroidFragment != null) {
			caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
		}

		if (dialogCaldroidFragment != null) {
			dialogCaldroidFragment.saveStatesToKey(outState,
					"DIALOG_CALDROID_SAVED_STATE");
		}
	}

}
