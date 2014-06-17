package com.csb.ui.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csb.R;
import com.csb.support.settinghelper.SettingUtility;
import com.csb.ui.view.switchbutton.SwitchButton;

public class SettingsActivity extends Activity implements OnClickListener {
	private Button btn_title_left, btn_title_right;
	private TextView tv_top_title;
	private LinearLayout ll_about;
	private LinearLayout ll_share;
	private SwitchButton cb_voice;
	private SwitchButton cb_vibration;
	private SwitchButton cb_23g_voice;
	private Context context = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		context = this;
		initView();
	}

	private void initView() {
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("设置");
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setText("返回");
		btn_title_left.setOnClickListener(this);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		btn_title_right.setVisibility(View.INVISIBLE);
		ll_about = (LinearLayout) findViewById(R.id.ll_about);
		ll_about.setOnClickListener(this);
		ll_share = (LinearLayout) findViewById(R.id.ll_share);
		ll_share.setOnClickListener(this);
		
		cb_voice = (SwitchButton) findViewById(R.id.cb_voice);
		cb_vibration = (SwitchButton) findViewById(R.id.cb_vibration);
		cb_23g_voice = (SwitchButton) findViewById(R.id.cb_23g_voice);
		cb_voice.setOnClickListener(this);
		cb_vibration.setOnClickListener(this);
		cb_23g_voice.setOnClickListener(this);
		cb_voice.setSwitch(SettingUtility.getDefaultVoice(), 0);
		cb_vibration.setSwitch(SettingUtility.getDefaultVibration(), 0);
		cb_23g_voice.setSwitch(SettingUtility.getDefault23GVoice(), 0);
		
		cb_voice.setOnSwitchListener(new com.csb.ui.view.switchbutton.SwitchButton.OnSwitchListener() {
			@Override
			public boolean onSwitch(SwitchButton v, boolean isRight) {
				SettingUtility.setDefaultVoice(isRight);
				return false;
			}
		});
		
		cb_vibration.setOnSwitchListener(new com.csb.ui.view.switchbutton.SwitchButton.OnSwitchListener() {
			@Override
			public boolean onSwitch(SwitchButton v, boolean isRight) {
				SettingUtility.setDefaultVibration(isRight);
				return false;
			}
		});
		
		cb_23g_voice.setOnSwitchListener(new com.csb.ui.view.switchbutton.SwitchButton.OnSwitchListener() {
			@Override
			public boolean onSwitch(SwitchButton v, boolean isRight) {
				SettingUtility.setDefault23GVoice(isRight);
				return false;
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_about:
			Intent intent = new Intent(context, AboutActivity.class);
			startActivity(intent);
			break;
		case R.id.ll_share:
			doShare();
			break;
		case R.id.btn_title_left:
			SettingsActivity.this.finish();
			break;
		case R.id.cb_voice:
			SettingUtility.setDefaultVoice(cb_voice.isOnRight());
			break;
		case R.id.cb_vibration:
			SettingUtility.setDefaultVibration(cb_vibration.isOnRight());
			break;
		case R.id.cb_23g_voice:
			SettingUtility.setDefault23GVoice(cb_23g_voice.isOnRight());
			break;
		}
	}
	
	public void doShare() {
		Intent intent=new Intent(Intent.ACTION_SEND); 
		intent.setType("image/*"); 
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享"); 
		intent.putExtra(Intent.EXTRA_TEXT, "同行网开通了!!!");  
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		startActivity(Intent.createChooser(intent, getTitle())); 
	}
}
