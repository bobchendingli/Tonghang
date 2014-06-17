package com.csb.ui.login;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.csb.R;
import com.csb.bean.UserBean;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.GlobalContext;
import com.csb.utils.ToastUtils;

public class RegisterActivity extends Activity implements
		android.view.View.OnClickListener {
	public static final int REGION_SELECT = 1;
	private TextView tv_top_title;
	private Button btn_title_left, btn_title_right;

	private EditText et_code;
	private EditText et_birthday;
	private EditText et_gender;
	private EditText et_region;
	private EditText et_post;
	private EditText et_departments;
	private EditText et_hospital;
	private EditText et_username;

	private Context context = GlobalContext.getInstance();
	private UserBean userBean;

	public static Intent newIntent() {
		Intent intent = new Intent(GlobalContext.getInstance(),
				RegisterActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		return intent;
	}

	public static Intent newIntent(UserBean bean) {
		Intent intent = newIntent();
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

		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		btn_title_right.setText("下一步");
		btn_title_right.setOnClickListener(this);

		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setOnClickListener(this);

		et_code = (EditText) findViewById(R.id.et_code);
		et_birthday = (EditText) findViewById(R.id.et_birthday);
		et_birthday.setOnClickListener(this);
		et_gender = (EditText) findViewById(R.id.et_gender);
		et_gender.setOnClickListener(this);
		et_region = (EditText) findViewById(R.id.et_region);
		et_post = (EditText) findViewById(R.id.et_post);
		et_departments = (EditText) findViewById(R.id.et_departments);
		et_hospital = (EditText) findViewById(R.id.et_hospital);
		et_username = (EditText) findViewById(R.id.et_username);

		if (userBean != null) {
			et_code.setText(userBean.getCode());
			et_birthday.setText(userBean.getBirthday());
			et_gender.setText(userBean.getGender());
			et_region.setText(userBean.getRegion());
			et_post.setText(userBean.getPost());
			et_departments.setText(userBean.getDepartments());
			et_hospital.setText(userBean.getHospital());
			et_username.setText(userBean.getUsername());
		}
	}

	/**
	 * 重写了onCreateDialog方法来创建一个列表对话框
	 */
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		switch (id) {
		case REGION_SELECT:
			final Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("请选择性别");
			builder.setSingleChoiceItems(// 第一个参数是要显示的列表，用数组展示；第二个参数是默认选中的项的位置；
					// 第三个参数是一个事件点击监听器
					new String[] { "男", "女" }, 0, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							switch (which) {
							case 0:
								et_gender.setText("男");

								break;
							case 1:
								et_gender.setText("女");
								break;
							}
							dismissDialog(REGION_SELECT);// 想对话框关闭
						}
					});
			return builder.create();
		}
		return null;
	}

	private UserBean getUser() {
		final String username = et_username.getText().toString();
		final String hospital = et_hospital.getText().toString();
		final String departments = et_departments.getText().toString();
		final String post = et_post.getText().toString();
		final String region = et_region.getText().toString();
		final String gender = et_gender.getText().toString();
		final String birthday = et_birthday.getText().toString();
		final String code = et_code.getText().toString();
		StringBuffer msg = new StringBuffer();
		if (TextUtils.isEmpty(username)) {
			msg.append("姓名  ");
		}
		if (TextUtils.isEmpty(hospital)) {
			msg.append("医院  ");
		}
		if (TextUtils.isEmpty(departments)) {
			msg.append("科室  ");
		}
		if (TextUtils.isEmpty(post)) {
			msg.append("职务  ");
		}
		if (TextUtils.isEmpty(region)) {
			msg.append("地区  ");
		}
		if (TextUtils.isEmpty(gender)) {
			msg.append("姓别  ");
		}
		if (TextUtils.isEmpty(birthday)) {
			msg.append("生日  ");
		}
		// if(TextUtils.isEmpty(code)) {
		// msg.append("证件号  ");
		// }
		if (msg.length() > 0) {
			msg.append("字段不能为空!");
			ToastUtils.show(context, msg);
			return null;
		} else {
			if(userBean == null){
				userBean = new UserBean();
			}
			userBean.setUsername(username);
			userBean.setHospital(hospital);
			userBean.setDepartments(departments);
			userBean.setPost(post);
			userBean.setRegion(region);
			userBean.setGender(gender);
			userBean.setBirthday(birthday);
			userBean.setCode(code);
			return userBean;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.et_gender:
			showDialog(REGION_SELECT);
			break;
		case R.id.et_birthday:
			showDateDialog();
			break;
		case R.id.btn_title_right:
			UserBean bean = getUser();
			if (bean != null) {
				Intent intent = RegisterConfirmActivity.newIntent(bean);
				startActivity(intent);
			}
			break;
		case R.id.btn_title_left:
			RegisterActivity.this.finish();
			break;
		}
	}

	private Dialog dialog = null;

	private void showDateDialog() {
		Calendar calendar = Calendar.getInstance();
		final String dateStr = et_birthday.getText().toString();
		if (!TextUtils.isEmpty(dateStr)) {
			String[] dateArr = dateStr.split("-");
			if (dateArr.length == 3) {
				calendar.set(Integer.valueOf(dateArr[0]),
						Integer.valueOf(dateArr[1]) - 1,
						Integer.valueOf(dateArr[2]));
			}
		}
		dialog = new DatePickerDialog(this, dateListener,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		dialog.show();
	}

	DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
		final String dateFormat = "%04d-%02d-%02d";

		@Override
		public void onDateSet(DatePicker datePicker, int year, int month,
				int dayOfMonth) {
			// Calendar月份是从0开始,所以month要加1
			et_birthday.setText(String.format(dateFormat, year, month + 1,
					dayOfMonth));
		}
	};

}
