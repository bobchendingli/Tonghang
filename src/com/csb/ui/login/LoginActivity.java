package com.csb.ui.login;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.csb.R;
import com.csb.bean.UserBean;
import com.csb.support.lib.MyAsyncTask;
import com.csb.support.settinghelper.SettingUtility;
import com.csb.ui.main.MainActivity;
import com.csb.ui.task.LoginAsyncTask;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.ClassPathResource;
import com.csb.utils.GlobalContext;
import com.csb.utils.ToastUtils;
import com.csb.utils.Utility;

/**
 * 登陆界面
 * 
 * @author bobo
 * 
 */
public class LoginActivity extends Activity implements OnClickListener {
	private Button btn_login;// 登录按钮
	private Button btn_register;// 注册按钮
	private EditText et_mail;
	private EditText et_password;

	public static final int MENU_PWD_BACK = 1;
	public static final int MENU_HELP = 2;
	public static final int MENU_EXIT = 3;

	private LoginAsyncTask loginAsyncTask = null;
	private UserBean userBean;

	private void jumpWelcomeActivity() {
		Intent i = new Intent(LoginActivity.this, WelcomeActivity.class);
//		i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(i);
		this.finish();
		return;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
//		Intent i = new Intent(Intent.ACTION_MAIN);
//		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		i.addCategory(Intent.CATEGORY_HOME);
//		startActivity(i);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if(!getIntent().getBooleanExtra("login", false)) { 
//			jumpWelcomeActivity();
//		}
//		
//		if(SettingUtility.firstStart()) {
//			jumpWelcomeActivity();
//		}
		
		userBean = SettingUtility.getDefaultUserBean();
		userBean = new UserBean();
		userBean.setUserid("106");
		userBean.setUsername("bb");
		userBean.setMail("11@qq");
		SettingUtility.setDefaultUserBean(userBean);
		if(userBean != null && !TextUtils.isEmpty(userBean.getUserid())) {
			GlobalContext.getInstance().setUserBean(userBean);
			jumpMainActivity();
		}
		
		setContentView(R.layout.login);
		initView();
	}

	private void initView() {
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		btn_register = (Button) findViewById(R.id.btn_register);
		btn_register.setOnClickListener(this);
		et_mail = (EditText) findViewById(R.id.et_mail);
		et_password = (EditText) findViewById(R.id.et_password);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_login:
			doLogin();
			break;
		case R.id.btn_register:
			intent = RegisterActivity.newIntent();
			startActivity(intent);
			break;

		}
	}

	private void doLogin() {
		final String email = et_mail.getText().toString();
		final String password = et_password.getText().toString();
		StringBuffer msg = new StringBuffer();
		if (TextUtils.isEmpty(email)) {
			msg.append("邮箱  ");
		}
		if (TextUtils.isEmpty(password)) {
			msg.append("密码  ");
		}
		if (msg.length() > 0) {
			msg.append("字段不能为空!");
			ToastUtils.show(this, msg);
		} else {
			if (!ClassPathResource.isEmail(email)) {
				msg.append("邮箱  字段不合法，请重新输入！");
				ToastUtils.show(this, msg);
			} else {
				if (Utility.isTaskStopped(loginAsyncTask)) {
					userBean = new UserBean();
					userBean.setPassword(password);
					userBean.setMail(email);
					loginAsyncTask = new LoginAsyncTask(userBean, myHandler);
					loginAsyncTask
							.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		userBean = null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {// 创建系统功能菜单
		menu.add(0, MENU_PWD_BACK, 1, "密码找回").setIcon(R.drawable.menu_findkey);
		menu.add(0, MENU_EXIT, 3, "退出").setIcon(R.drawable.menu_exit);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_PWD_BACK:
//			ToastUtils.show(this, "正在开发中...");
			break;
		case MENU_EXIT:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private ProgressDialog mDialog;

	private static final int DIALOG_LOGIN_KEY = 0;

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_LOGIN_KEY: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("正在登录,请稍候...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return null;
	}

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case BundleArgsConstants.REQUEST_START:
				showDialog(DIALOG_LOGIN_KEY);
				break;
			case BundleArgsConstants.REQUEST_SUCC:
				onSuccess();
				break;
			case BundleArgsConstants.REQUEST_FAIL:
				dismissDialog(DIALOG_LOGIN_KEY);
			default:
				break;
			}
		}
	};

	private void onSuccess() {
		dismissDialog(DIALOG_LOGIN_KEY);
		SettingUtility.setDefaultUserBean(GlobalContext.getInstance().getUserBean());
		jumpMainActivity();
	}

	private void jumpMainActivity() {
		Intent i = new Intent(LoginActivity.this, com.csb.ui.main.MainActivity.class);
		startActivity(i);
		this.finish();
		return;
	}
}
