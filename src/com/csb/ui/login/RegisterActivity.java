package com.csb.ui.login;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.csb.R;
import com.csb.bean.UserBean;
import com.csb.support.lib.MyAsyncTask;
import com.csb.support.settinghelper.SettingUtility;
import com.csb.ui.main.MainActivity;
import com.csb.ui.task.RegisterAsyncTask;
import com.csb.ui.task.RequestSmsAsyncTask;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.ClassPathResource;
import com.csb.utils.GlobalContext;
import com.csb.utils.ToastUtils;
import com.csb.utils.Utility;

/**
 * 注册提交
 * 
 * @author bobo
 * 
 */
public class RegisterActivity extends Activity implements
		OnClickListener {
	private static final String TAG = RegisterActivity.class
			.getSimpleName();
	private Button btn_get_verificationno, btn_title_left, btn_title_right;
	private TextView tv_top_title;
	private MyCount mc = new MyCount(30000, 1000);
	private EditText et_verificationno;
//	private EditText et_mail;
//	private EditText et_password;
	private EditText et_phone;

	private UserBean userBean;
	private Context context = GlobalContext.getInstance();
	private RequestSmsAsyncTask requestSmsAsyncTask = null;
	private RegisterAsyncTask registerAsyncTask = null;

	public static Intent newIntent() {
		return new Intent(GlobalContext.getInstance(),
				RegisterActivity.class);
	}

	public static Intent newIntent(UserBean bean) {
		Intent intent = newIntent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.putExtra(BundleArgsConstants.USERBEAN_EXTRA, bean);
		return intent;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(BundleArgsConstants.USERBEAN_EXTRA, userBean);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			userBean = savedInstanceState
					.getParcelable(BundleArgsConstants.USERBEAN_EXTRA);
		} else {
			Intent intent = getIntent();
			userBean = intent
					.getParcelableExtra(BundleArgsConstants.USERBEAN_EXTRA);
		}
		setContentView(R.layout.register);
		initView();
	}

	private void initView() {
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("注册");
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setOnClickListener(this);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		btn_title_right.setText("下一步");;
		btn_title_right.setOnClickListener(this);
		btn_get_verificationno = (Button) findViewById(R.id.btn_get_verificationno);
		btn_get_verificationno.setOnClickListener(this);

		et_verificationno = (EditText) findViewById(R.id.et_verificationno);
		et_phone = (EditText) findViewById(R.id.et_phone);
		
		if(userBean != null){
			et_verificationno.setText(userBean.getVerificationno());
			et_phone.setText(userBean.getPhone());
		}
	}

	/** 自定义一个继承CountDownTimer的内部类，用于实现计时器的功能 */
	class MyCount extends CountDownTimer {
		/**
		 * MyCount的构造方法
		 * 
		 * @param millisInFuture
		 *            要倒计时的时间
		 * @param countDownInterval
		 *            时间间隔
		 */
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 在进行倒计时的时候执行的操作
			long second = millisUntilFinished / 1000;
			btn_get_verificationno.setEnabled(false);
			btn_get_verificationno.setText("等待" + second + "秒");
		}

		@Override
		public void onFinish() {// 倒计时结束后要做的事情
			btn_get_verificationno.setEnabled(true);
			btn_get_verificationno.setText("获取验证码");
		}

	}

	private void doGetVerificationNo() {
		final String phone = et_phone.getText().toString();
		if (!ClassPathResource.isMobileNO(phone)) {
			ToastUtils.show(context, "正确填写手机号，我们将向您发送一条验证码短信");
		} else {
			mc.start();
			if (Utility.isTaskStopped(requestSmsAsyncTask)) {
				requestSmsAsyncTask = new RequestSmsAsyncTask(phone);
				requestSmsAsyncTask
						.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
			}

		}
	}

	private void jumpRegisterConfirmActivity() {
		final String phone = et_phone.getText().toString();
		final String verificationno = et_verificationno.getText().toString();
		StringBuffer msg = new StringBuffer();
		if (TextUtils.isEmpty(phone)) {
			msg.append("手机号  ");
		}
		if (TextUtils.isEmpty(verificationno)) {
			msg.append("验证码  ");
		}
		if (msg.length() > 0) {
			msg.append("字段不能为空!");
			ToastUtils.show(context, msg);
		} else {
			if (!ClassPathResource.isMobileNO(phone)) {
				ToastUtils.show(context, "正确填写手机号，我们将向您发送一条验证码短信");
				return;
			} 
			if (userBean == null) {
				userBean = new UserBean();
			}
			userBean.setPhone(phone);
			userBean.setVerificationno(verificationno);
			startActivity(RegisterConfirmActivity.newIntent(userBean));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_get_verificationno:
			doGetVerificationNo();
			break;
		case R.id.btn_title_right:
			jumpRegisterConfirmActivity();
			break;
		case R.id.btn_title_left:
			finish();
			break;
		}
	}
	

	private ProgressDialog mDialog;

    private static final int DIALOG_REQUESTSMS_KEY = 0;
    private static final int DIALOG_REGISTER_KEY = 1;
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_REQUESTSMS_KEY: {
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("正在获取验证码,请稍候...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(true);
                return dialog;
            }
            case DIALOG_REGISTER_KEY: {
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("正在注册,请稍候...");
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
				showDialog(DIALOG_REGISTER_KEY);
				break;
			case BundleArgsConstants.REQUEST_SUCC:
				userBean = (UserBean) msg.obj;
				onSuccess();
				break;
			case BundleArgsConstants.REQUEST_FAIL:
				dismissDialog(DIALOG_REGISTER_KEY);
			default:
				break;
			}
		}
	};
	
	
	private void onSuccess() {
		dismissDialog(DIALOG_REGISTER_KEY);
		GlobalContext.getInstance().setUserBean(userBean);
		SettingUtility.setDefaultUserBean(GlobalContext.getInstance().getUserBean());
		jumpMainActivity();
	}

	private void jumpMainActivity() {
		Intent i = new Intent(RegisterActivity.this, MainActivity.class);
		startActivity(i);
		this.finish();
		return;
	}
	
	

}
