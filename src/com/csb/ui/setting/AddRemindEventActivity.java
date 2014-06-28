package com.csb.ui.setting;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.csb.R;
import com.csb.utils.CalendarUtils;
import com.csb.utils.ToastUtils;

public class AddRemindEventActivity extends Activity implements
		View.OnClickListener {
	private Button btn_title_left;
	private Button btn_title_right;
	private TextView tv_top_title;
	private EditText et_remind_date;
	private EditText et_remind_content;
	private LinearLayout ll_remind_date;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_calendar_add_remind);
		initView();
	}

	private void initView() {
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("添加提醒事件");
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setText("返回");
		btn_title_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AddRemindEventActivity.this.finish();
			}
		});
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		btn_title_right.setText("确定");
		btn_title_right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String remindDate = et_remind_date.getText().toString();
				final String remindContent = et_remind_content.getText()
						.toString();
				StringBuffer msg = new StringBuffer();
				if (TextUtils.isEmpty(remindDate)) {
					msg.append("提醒时间 ");
				}
				if (TextUtils.isEmpty(remindContent)) {
					msg.append("提醒内容  ");
				}
				if (msg.length() > 0) {
					msg.append("字段不能为空!");
					ToastUtils.show(AddRemindEventActivity.this, msg);
				} else {
					
					try {
						CalendarUtils.writeEvent(AddRemindEventActivity.this, remindDate, remindContent);
						Toast.makeText(AddRemindEventActivity.this, "添加提醒事件成功!", Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						e.printStackTrace();
					}
					AddRemindEventActivity.this.finish();
				}
			}
		});

		et_remind_date = (EditText) findViewById(R.id.et_remind_date);
		et_remind_content = (EditText) findViewById(R.id.et_remind_content);

		ll_remind_date = (LinearLayout) findViewById(R.id.ll_remind_date);
		ll_remind_date.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						AddRemindEventActivity.this);
				View view = View.inflate(AddRemindEventActivity.this,
						R.layout.date_time_dialog, null);
				final DatePicker datePicker = (DatePicker) view
						.findViewById(R.id.date_picker);
				final TimePicker timePicker = (android.widget.TimePicker) view
						.findViewById(R.id.time_picker);
				builder.setView(view);

				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(System.currentTimeMillis());
				try {
					if (!TextUtils.isEmpty(et_remind_date.getText())) {
						final Date date = sdf.parse(et_remind_date.getText()
								.toString());
						cal.setTimeInMillis(date.getTime());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				datePicker.init(cal.get(Calendar.YEAR),
						cal.get(Calendar.MONTH),
						cal.get(Calendar.DAY_OF_MONTH), null);

				timePicker.setIs24HourView(true);
				timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
				timePicker.setCurrentMinute(cal.get(Calendar.MINUTE));
				builder.setPositiveButton("确  定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								StringBuffer sb = new StringBuffer();
								sb.append(String.format(
										"%d-%02d-%02d %02d:%02d",
										datePicker.getYear(),
										datePicker.getMonth() + 1,
										datePicker.getDayOfMonth(),
										timePicker.getCurrentHour(),
										timePicker.getCurrentMinute()));
								// sb.append("  ");
								// sb.append(timePicker.getCurrentHour())
								// .append(":").append(timePicker.getCurrentMinute());

								et_remind_date.setText(sb);
								et_remind_content.requestFocus();
								dialog.cancel();
							}
						});
				Dialog dialog = builder.create();
				dialog.show();
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	
}
