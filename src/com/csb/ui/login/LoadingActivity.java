package com.csb.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.csb.R;
import com.csb.support.settinghelper.SettingUtility;

/**
 * 登陆界面
 * 
 * @author bobo
 * 
 */
public class LoadingActivity extends Activity {

	private void jumpWelcomeActivity() {
		Intent i = new Intent(LoadingActivity.this, WelcomeActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(i);
		this.finish();
		return;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (SettingUtility.isFirstStart()) {
			jumpWelcomeActivity();
			finish();
		}
		setContentView(R.layout.loading);
		
		if (!SettingUtility.isFirstStart()) {
			Handler x = new Handler();// 定义一个handle对象
	
			x.postDelayed(new Runnable() {
				@Override
				public void run() {
					jumpLoginActivity();// 这个线程的作用3秒后就是进入到你的主界面
					LoadingActivity.this.finish();// 把当前的LaunchActivity结束掉
				}
			}, 1000);// 设置3秒钟延迟执行splashhandler线程。其实你这里可以再新建一个线程去执行初始化工作，如判断SD,网络状态等
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	private void jumpLoginActivity() {
		Intent i = new Intent(this, LoginActivity.class);
		i.putExtra("login", true);
		startActivity(i);
		finish();
		return;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
