package com.csb.ui.login;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import cn.trinea.android.common.util.ImageUtils;

import com.csb.R;
import com.csb.bean.ResultUploadPicBean;
import com.csb.bean.UserBean;
import com.csb.support.lib.MyAsyncTask;
import com.csb.support.settinghelper.SettingUtility;
import com.csb.ui.main.MainActivity;
import com.csb.ui.task.RegisterAsyncTask;
import com.csb.ui.task.UploadPicAsyncTask;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.ClassPathResource;
import com.csb.utils.GlobalContext;
import com.csb.utils.ToastUtils;
import com.csb.utils.Utility;

public class RegisterConfirmActivity extends Activity implements
		android.view.View.OnClickListener, OnWheelChangedListener {
	public static final int GENDER_SELECT = 1;
	public static final int AGE_SELECT = 2;
	public static final int DEPARTMENTS_SELECT = 3;
	public static final int POST_SELECT = 4;
	public static final int XUIWEI_SELECT = 5;

	public static final int REQUESTCODE_XUESHI = 6;
	public static final int REQUESTCODE_XINGQUEDIAN = 7;
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
	private EditText et_xuiwei;
	private EditText et_xueshui;
	private EditText et_xingquedian;
	private EditText et_mail;
	private EditText et_password;
	private EditText et_password_cofirm;
	private LinearLayout ll_gender;
	private LinearLayout ll_region;
	private LinearLayout ll_birthday;
	private LinearLayout ll_departments;
	private LinearLayout ll_post;
	private LinearLayout ll_xuiwei;
	private LinearLayout ll_xueshui;
	private LinearLayout ll_xingquedian;

	private TextView tv_mail;
	private TextView tv_password;
	private TextView tv_username;
	private TextView tv_gender;

	private ImageView iv_doctorpic;

	private Context context;
	private UserBean userBean;

	private RegisterAsyncTask registerAsyncTask = null;
	private static String SCALE_IMAGE_PATH = Environment
			.getExternalStorageDirectory() + File.separator + "temp_scale.jpg";

	public static Intent newIntent() {
		Intent intent = new Intent(GlobalContext.getInstance(),
				RegisterConfirmActivity.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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
		context = this;
		if (savedInstanceState != null) {
			userBean = savedInstanceState
					.getParcelable(BundleArgsConstants.USERBEAN_EXTRA);
		} else {
			Intent intent = getIntent();
			userBean = intent
					.getParcelableExtra(BundleArgsConstants.USERBEAN_EXTRA);
		}
		setContentView(R.layout.register_confirm);
		initView();
		initDialogView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Utility.cancelTasks(registerAsyncTask, uploadPicAsyncTask);
	}

	private void initDialogView() {
		initJsonData();
		initDatas();
	}

	private void initView() {
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("注册");

		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		btn_title_right.setText("提交");
		btn_title_right.setOnClickListener(this);

		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setOnClickListener(this);

		iv_doctorpic = (ImageView) findViewById(R.id.iv_doctorpic);
		iv_doctorpic.setOnClickListener(this);

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

		et_xuiwei = (EditText) findViewById(R.id.et_xuiwei);
		et_xueshui = (EditText) findViewById(R.id.et_xueshui);
		et_xingquedian = (EditText) findViewById(R.id.et_xingquedian);
		et_mail = (EditText) findViewById(R.id.et_mail);
		et_password = (EditText) findViewById(R.id.et_password);
		et_password_cofirm = (EditText) findViewById(R.id.et_password_cofirm);

		tv_mail = (TextView) findViewById(R.id.tv_mail);
		tv_mail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				et_mail.requestFocus();
			}
		});
		tv_password = (TextView) findViewById(R.id.tv_password);
		tv_password.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				et_password.requestFocus();
			}
		});
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_username.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				et_username.requestFocus();
			}
		});
		tv_gender = (TextView) findViewById(R.id.tv_gender);
		tv_gender.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				et_gender.requestFocus();
			}
		});

		et_mail.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(et_mail.getText().toString())) {
					tv_mail.setVisibility(View.VISIBLE);
				} else {
					tv_mail.setVisibility(View.GONE);
				}
			}
		});
		et_password.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(et_password.getText().toString())) {
					tv_password.setVisibility(View.VISIBLE);
				} else {
					tv_password.setVisibility(View.GONE);
				}
			}
		});

		et_username.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(et_username.getText().toString())) {
					tv_username.setVisibility(View.VISIBLE);
				} else {
					tv_username.setVisibility(View.GONE);
				}
			}
		});

		et_gender.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(et_gender.getText().toString())) {
					tv_gender.setVisibility(View.VISIBLE);
				} else {
					tv_gender.setVisibility(View.GONE);
				}
			}
		});

		ll_gender = (LinearLayout) findViewById(R.id.ll_gender);
		ll_gender.setOnClickListener(this);

		ll_region = (LinearLayout) findViewById(R.id.ll_region);
		ll_region.setOnClickListener(this);
		et_region.setOnClickListener(this);

		ll_birthday = (LinearLayout) findViewById(R.id.ll_birthday);
		ll_birthday.setOnClickListener(this);

		ll_departments = (LinearLayout) findViewById(R.id.ll_departments);
		ll_departments.setOnClickListener(this);
		et_departments.setOnClickListener(this);
		ll_post = (LinearLayout) findViewById(R.id.ll_post);
		ll_post.setOnClickListener(this);
		et_post.setOnClickListener(this);

		ll_xuiwei = (LinearLayout) findViewById(R.id.ll_xuiwei);
		ll_xuiwei.setOnClickListener(this);
		et_xuiwei.setOnClickListener(this);
		ll_xueshui = (LinearLayout) findViewById(R.id.ll_xueshui);
		ll_xueshui.setOnClickListener(this);
		et_xueshui.setOnClickListener(this);

		ll_xingquedian = (LinearLayout) findViewById(R.id.ll_xingquedian);
		ll_xingquedian.setOnClickListener(this);
		et_xingquedian.setOnClickListener(this);

		if (userBean != null) {
			if (!TextUtils.isEmpty(userBean.getMail()))
				et_mail.setText(userBean.getMail());
			if (!TextUtils.isEmpty(userBean.getPassword()))
				et_password.setText(userBean.getPassword());
			if (!TextUtils.isEmpty(userBean.getPasswordCofirm()))
				et_password_cofirm.setText(userBean.getPasswordCofirm());
			et_code.setText(userBean.getCode());
			et_birthday.setText(userBean.getAge());
			if (!TextUtils.isEmpty(userBean.getGender()))
				et_gender.setText(userBean.getGender());
			et_region.setText(userBean.getRegion());
			et_post.setText(userBean.getPost());
			et_departments.setText(userBean.getDepartments());
			et_hospital.setText(userBean.getHospital());
			if (!TextUtils.isEmpty(userBean.getUsername()))
				et_username.setText(userBean.getUsername());
			et_xuiwei.setText(userBean.getDegree());
			et_xueshui.setText(userBean.getPlanning());
			et_xingquedian.setText(userBean.getInterest());
		}
	}

	private AlertDialog postDialog;
	private static final String[] zyzcArr = { "主任医师", "副主任医师", "主治医师", "住院医师" };
	private static final String[] jzArr = { "教授", "副教授", "讲师", "助教" };
	private String mZyzc;
	private String mJz;

	private void showPostDailog() {
		if (postDialog != null) {
			postDialog.dismiss();
		}
		LayoutInflater factory = LayoutInflater.from(this);
		View postView = factory.inflate(R.layout.spinner_post, null);
		Spinner spinner_zyzc = (Spinner) postView
				.findViewById(R.id.spinner_zyzc);
		Spinner spinner_jz = (Spinner) postView.findViewById(R.id.spinner_jz);
		ArrayAdapter<String> zyzcAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, zyzcArr);
		ArrayAdapter<String> jzAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, jzArr);

		// 设置下拉列表风格
		zyzcAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter添加到spinner中
		spinner_zyzc.setAdapter(zyzcAdapter);
		// 添加Spinner事件监听
		spinner_zyzc
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						mZyzc = zyzcArr[arg2];
						// 设置显示当前选择的项
						arg0.setVisibility(View.VISIBLE);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}

				});

		// 设置下拉列表风格
		jzAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter添加到spinner中
		spinner_jz.setAdapter(jzAdapter);
		// 添加Spinner事件监听
		spinner_jz
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						mJz = jzArr[arg2];
						// 设置显示当前选择的项
						arg0.setVisibility(View.VISIBLE);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}

				});

		postDialog = new AlertDialog.Builder(this)
				.setView(postView)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								et_post.setText(mZyzc + " " + mJz);
								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mZyzc = "";
								mJz = "";
								et_post.setText("");
								dialog.dismiss();
							}
						}).create();
		postDialog.show();
	}

	/**
	 * 重写了onCreateDialog方法来创建一个列表对话框
	 */
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		Dialog dialog = null;
		switch (id) {
		case GENDER_SELECT:
			Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("请选择性别");
			final String[] regionArr = new String[] { "男", "女" };
			builder.setSingleChoiceItems(regionArr, 0, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					et_gender.setText(regionArr[which]);
					dismissDialog(GENDER_SELECT);// 想对话框关闭
				}
			});
			dialog = builder.create();
			break;
		case AGE_SELECT:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("请选择年龄范围");
			final String[] ageArr = new String[] { "30岁以下", "30-35", "36-40",
					"41-45", "46-50", "50岁以上" };
			builder.setSingleChoiceItems(// 第一个参数是要显示的列表，用数组展示；第二个参数是默认选中的项的位置；
					// 第三个参数是一个事件点击监听器
					ageArr, 0, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							et_birthday.setText(ageArr[which]);
							dismissDialog(AGE_SELECT);// 想对话框关闭
						}
					});
			dialog = builder.create();
			break;
		case DEPARTMENTS_SELECT:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("请选择所属科室");
			final String[] depArr = new String[] { "心脏内科", "心电图/扇超室",
					"普通内科（未分科）", "其他专科" };
			builder.setSingleChoiceItems(depArr, 0, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					et_departments.setText(depArr[which]);
					dismissDialog(DEPARTMENTS_SELECT);
				}
			});
			dialog = builder.create();
			break;
		case XUIWEI_SELECT:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("请选择学位");
			final String[] xwArr = new String[] { "博士", "博士后", "硕士", "学士",
					"大专", "其他" };
			builder.setSingleChoiceItems(// 第一个参数是要显示的列表，用数组展示；第二个参数是默认选中的项的位置；
					// 第三个参数是一个事件点击监听器
					xwArr, 0, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							et_xuiwei.setText(xwArr[which]);
							dismissDialog(XUIWEI_SELECT);// 想对话框关闭
						}
					});
			dialog = builder.create();
			break;
		}

		return dialog;
	}

	private AlertDialog wheelDialog;

	private void showWheelDailog() {
		if (wheelDialog != null) {
			wheelDialog.dismiss();
		}
		LayoutInflater factory = LayoutInflater.from(this);
		View wheelView = factory.inflate(R.layout.alert_dialog_citys, null);
		mProvince = (WheelView) wheelView.findViewById(R.id.id_province);
		mCity = (WheelView) wheelView.findViewById(R.id.id_city);
		mArea = (WheelView) wheelView.findViewById(R.id.id_area);

		mProvince.setViewAdapter(new ArrayWheelAdapter<String>(this,
				mProvinceDatas));
		// 添加change事件
		mProvince.addChangingListener(this);
		// 添加change事件
		mCity.addChangingListener(this);
		// 添加change事件
		mArea.addChangingListener(this);

		mProvince.setVisibleItems(5);
		mCity.setVisibleItems(5);
		mArea.setVisibleItems(5);

		if (mCurrentProviceName != null) {
			int proviceIndex = -1;
			for (int i = 0; i < mProvinceDatas.length; i++) {
				if (mCurrentProviceName.equals(mProvinceDatas[i])) {
					proviceIndex = i;
				}
			}
			if (proviceIndex > -1) {
				mProvince.setCurrentItem(proviceIndex);
			}
		}
		updateCities();
		updateAreas();
		wheelDialog = new AlertDialog.Builder(this)
				// .setIconAttribute(android.R.attr.alertDialogIcon)
				// .setTitle("选择地区")
				.setView(wheelView)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								final String region = mCurrentProviceName
										+ mCurrentCityName + mCurrentAreaName;
								et_region.setText(region);
								mCurrentAreaName = "";
								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								et_region.setText("");
								dialog.dismiss();
							}
						}).create();
		wheelDialog.show();
	}

	private UserBean getUser() {
		final String mail = et_mail.getText().toString();
		final String password = et_password.getText().toString();
		final String passwordCofirm = et_password_cofirm.getText().toString();
		final String username = et_username.getText().toString();
		final String hospital = et_hospital.getText().toString();
		final String departments = et_departments.getText().toString();
		final String post = et_post.getText().toString();
		final String region = et_region.getText().toString();
		final String gender = et_gender.getText().toString();
		final String birthday = et_birthday.getText().toString();
		final String code = et_code.getText().toString();
		final String xuewei = et_xuiwei.getText().toString();
		final String xueshu = et_xueshui.getText().toString();
		final String xingquedian = et_xingquedian.getText().toString();
		userBean.setMail(mail);
		userBean.setPassword(password);
		userBean.setUsername(username);
		userBean.setHospital(hospital);
		userBean.setDepartments(departments);
		userBean.setPost(post);
		userBean.setRegion(region);
		userBean.setGender(gender);
		userBean.setAge(birthday);
		userBean.setCode(code);
		userBean.setPassword(password);
		userBean.setPlanning(xueshu);
		userBean.setDegree(xuewei);
		userBean.setInterest(xingquedian);
		StringBuffer msg = new StringBuffer();
		if (TextUtils.isEmpty(mail)) {
			msg.append("邮箱 ");
		}
		if (TextUtils.isEmpty(password)) {
			msg.append("密码 ");
		}
		if (TextUtils.isEmpty(passwordCofirm)) {
			msg.append("确认密码 ");
		}
		if (TextUtils.isEmpty(username)) {
			msg.append("真实姓名 ");
		}
		if (TextUtils.isEmpty(birthday)) {
			msg.append("年龄范围 ");
		}
		if (TextUtils.isEmpty(gender)) {
			msg.append("姓别 ");
		}
		if (TextUtils.isEmpty(region)) {
			msg.append("单位信息 ");
		}
		if (TextUtils.isEmpty(hospital)) {
			msg.append("单位 ");
		}
		if (TextUtils.isEmpty(departments)) {
			msg.append("科室 ");
		}
		if (TextUtils.isEmpty(post)) {
			msg.append("职称 ");
		}
		if (TextUtils.isEmpty(xuewei)) {
			msg.append("学位 ");
		}
		if (TextUtils.isEmpty(xueshu)) {
			msg.append("学术与职业生涯规划需求 ");
		}
		if (TextUtils.isEmpty(xingquedian)) {
			msg.append("学术领域兴趣点 ");
		}

		// if(TextUtils.isEmpty(code)) {
		// msg.append("证件号  ");
		// }
		if (msg.length() > 0) {
			msg.append("字段不能为空!");
			ToastUtils.show(context, msg);
			return null;
		} else {
			if (!ClassPathResource.isEmail(mail)) {
				ToastUtils.show(context, "邮箱格式不正确！");
				return null;
			}
			if (!password.equals(passwordCofirm)) {
				ToastUtils.show(context, "密码与确认密码信息不一致！");
				return null;
			}

			return userBean;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_gender:
		case R.id.et_gender:
			showDialog(GENDER_SELECT);
			break;
		case R.id.et_birthday:
		case R.id.ll_birthday:
			showDialog(AGE_SELECT);
			break;
		case R.id.et_departments:
		case R.id.ll_departments:
			showDialog(DEPARTMENTS_SELECT);
			break;
		case R.id.et_post:
		case R.id.ll_post:
			showPostDailog();
			break;
		case R.id.et_xuiwei:
		case R.id.ll_xuiwei:
			showDialog(XUIWEI_SELECT);
			break;
		case R.id.et_xueshui:
		case R.id.ll_xueshui:
			Intent intent = XueshuActivity.newIntent();
			intent.putExtra(BundleArgsConstants.XUESHU_EXTRA, et_xueshui.getText().toString());
			startActivityForResult(intent, REQUESTCODE_XUESHI);
			break;
		case R.id.et_xingquedian:
		case R.id.ll_xingquedian:
			intent = XingquedianActivity.newIntent();
			intent.putExtra(BundleArgsConstants.XINGQUEDIAN_EXTRA, et_xingquedian.getText().toString());
			startActivityForResult(intent, REQUESTCODE_XINGQUEDIAN);
			break;
		case R.id.btn_title_right:
			UserBean bean = getUser();
			if (bean != null) {
				if (Utility.isTaskStopped(registerAsyncTask)) {
					registerAsyncTask = new RegisterAsyncTask(userBean,
							myHandler);
					registerAsyncTask
							.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
				}
			}
			break;
		case R.id.btn_title_left:
			final String mail = et_mail.getText().toString();
			final String password = et_password.getText().toString();
			final String passwordCofirm = et_password_cofirm.getText()
					.toString();
			final String username = et_username.getText().toString();
			final String hospital = et_hospital.getText().toString();
			final String departments = et_departments.getText().toString();
			final String post = et_post.getText().toString();
			final String region = et_region.getText().toString();
			final String gender = et_gender.getText().toString();
			final String birthday = et_birthday.getText().toString();
			final String code = et_code.getText().toString();
			final String xuewei = et_xuiwei.getText().toString();
			final String xueshu = et_xueshui.getText().toString();
			final String xingquedian = et_xingquedian.getText().toString();
			userBean.setMail(mail);
			userBean.setPassword(password);
			userBean.setPasswordCofirm(passwordCofirm);
			userBean.setUsername(username);
			userBean.setHospital(hospital);
			userBean.setDepartments(departments);
			userBean.setPost(post);
			userBean.setRegion(region);
			userBean.setGender(gender);
			userBean.setAge(birthday);
			userBean.setCode(code);
			userBean.setDegree(xuewei);
			userBean.setPlanning(xueshu);
			userBean.setInterest(xingquedian);

			this.startActivity(RegisterActivity.newIntent(userBean));

			RegisterConfirmActivity.this.finish();
			break;
		case R.id.et_region:
		case R.id.ll_region:
			showWheelDailog();
			break;
		case R.id.iv_doctorpic:
			showChooseDialog();
			break;
		}
	}

	/**
	 * 上传图片
	 */
	private UploadPicAsyncTask uploadPicAsyncTask;

	private static final String IMAGE_FILE_LOCATION = "file:///"
			+ Environment.getExternalStorageDirectory() + "/temp.jpg";
	private static Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);
	private static String mImageCapturePath;

	private static final int FROM_FILE = 1;
	private static final int FROM_CAMERA = 2;
	private static final int CROP_BIG_PICTURE = 4;
	private static final int CROP_SMALL_PICTURE = 5;

	private void showChooseDialog() {
		final String[] items = new String[] { "拍照", "文件" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("选择图片");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				if (item == 0) {
					try {
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(
								android.provider.MediaStore.EXTRA_OUTPUT,
								imageUri);
						intent.putExtra("return-data", false);
						startActivityForResult(intent, FROM_CAMERA);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Intent i = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, FROM_FILE);
				}
			}
		}).create().show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUESTCODE_XUESHI) {
				if (data != null && !TextUtils.isEmpty(data.getAction())) {
					et_xueshui.setText(data.getAction());
				}
				return;
			}

			if (requestCode == REQUESTCODE_XINGQUEDIAN) {
				if (data != null && !TextUtils.isEmpty(data.getAction())) {
					et_xingquedian.setText(data.getAction());
				}
				return;
			}

			if (requestCode == FROM_FILE && null != data) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = context.getContentResolver().query(
						selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				mImageCapturePath = cursor.getString(columnIndex);
				cursor.close();
			} else if (requestCode == FROM_CAMERA) {
				if (imageUri != null) {
					ImageUtils.scaleImage(imageUri.getPath(), SCALE_IMAGE_PATH);
					mImageCapturePath = SCALE_IMAGE_PATH;
					// cropImageUri(imageUri, 150, 150, CROP_SMALL_PICTURE);
				}
			} else if (requestCode == CROP_SMALL_PICTURE) {
				if (imageUri != null) {
					mImageCapturePath = imageUri.getPath();
				}
			}

			if (!TextUtils.isEmpty(mImageCapturePath)) {
				Bitmap bitmap = BitmapFactory.decodeFile(mImageCapturePath);
				iv_doctorpic.setImageBitmap(bitmap);
			}
		}
		// String picturePath contains the path of selected Image
	}

	private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}

	private ProgressDialog dialog = null;

	private void showDialog() {
		dialog = new ProgressDialog(context);
		dialog.setMessage("执照图片上传中,请稍候...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.show();
	}

	private void showRegisterDialog() {
		dialog = new ProgressDialog(this);
		dialog.setMessage("正在注册,请稍候...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.show();
	}

	private void dismissDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case BundleArgsConstants.REGISTER_START:
				showRegisterDialog();
				break;
			case BundleArgsConstants.REQUEST_SUCC:
				dismissDialog();
				if (msg.obj != null) {
					UserBean bean = (UserBean) msg.obj;
					userBean.setUserid(bean.getUserid());
				}
				onSuccess();
				break;
			case BundleArgsConstants.REQUEST_START:
				showDialog();
				break;
			case BundleArgsConstants.UPLOAD_HEAD_PIC_SUCC:
			case BundleArgsConstants.UPLOAD_CODE_PIC_SUCC:
				dismissDialog();
				if (msg.obj != null) {
					ResultUploadPicBean bean = (ResultUploadPicBean) msg.obj;
					userBean.setLicenseimgurl(bean.getImageurl());
					ToastUtils.show(context, "图片上传成功!");
				}
				onUploadPicSuccess();
				break;
			case BundleArgsConstants.REQUEST_FAIL:
				if (msg.obj != null)
					ToastUtils.show(context, msg.obj.toString());
				dismissDialog();
			default:
				break;
			}
		}
	};

	protected void onUploadPicSuccess() {
		jumpMainActivity();
	}

	private void jumpMainActivity() {
		GlobalContext.getInstance().setUserBean(userBean);
		SettingUtility.setDefaultUserBean(GlobalContext.getInstance()
				.getUserBean());
		Intent i = new Intent(RegisterConfirmActivity.this, MainActivity.class);
		startActivity(i);
		this.finish();
		return;
	}

	protected void onSuccess() {
		if (!TextUtils.isEmpty(mImageCapturePath)) {
			GlobalContext.getInstance().setUserBean(userBean);
			SettingUtility.setDefaultUserBean(GlobalContext.getInstance()
					.getUserBean());
			if (Utility.isTaskStopped(uploadPicAsyncTask)) {
				uploadPicAsyncTask = new UploadPicAsyncTask(GlobalContext
						.getInstance().getUserBean().getUserid(),
						mImageCapturePath,
						UploadPicAsyncTask.UPLOAD_DOCTOR_CODE_PIC, myHandler);
				uploadPicAsyncTask
						.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
			}
		} else {
			jumpMainActivity();
		}
	}

	/**
	 * 选择日期
	 */
	private Dialog datePickerDialog = null;

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
		datePickerDialog = new DatePickerDialog(this, dateListener,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.show();
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

	/**
	 * 选择地区
	 */
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		try {
			if (wheel == mProvince) {
				updateCities();
			} else if (wheel == mCity) {
				updateAreas();
			} else if (wheel == mArea) {
				mCurrentAreaName = mAreaDatasMap.get(mCurrentCityName)[newValue];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mAreaDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		} else {
			mCurrentAreaName = areas[0];
		}
		mArea.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mArea.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mCity.setCurrentItem(0);
		updateAreas();
	}

	/**
	 * 解析整个Json对象，完成后释放Json对象的内存
	 */
	private void initDatas() {
		try {
			if (mProvinceDatas != null && mCitisDatasMap != null
					&& mAreaDatasMap != null)
				return;
			JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
			mProvinceDatas = new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
				String province = jsonP.getString("p");// 省名字

				mProvinceDatas[i] = province;

				JSONArray jsonCs = null;
				try {
					/**
					 * Throws JSONException if the mapping doesn't exist or is
					 * not a JSONArray.
					 */
					jsonCs = jsonP.getJSONArray("c");
				} catch (Exception e1) {
					continue;
				}
				String[] mCitiesDatas = new String[jsonCs.length()];
				for (int j = 0; j < jsonCs.length(); j++) {
					JSONObject jsonCity = jsonCs.getJSONObject(j);
					String city = jsonCity.getString("n");// 市名字
					mCitiesDatas[j] = city;
					JSONArray jsonAreas = null;
					try {
						/**
						 * Throws JSONException if the mapping doesn't exist or
						 * is not a JSONArray.
						 */
						jsonAreas = jsonCity.getJSONArray("a");
					} catch (Exception e) {
						continue;
					}

					String[] mAreasDatas = new String[jsonAreas.length()];// 当前市的所有区
					for (int k = 0; k < jsonAreas.length(); k++) {
						String area = jsonAreas.getJSONObject(k).getString("s");// 区域的名称
						mAreasDatas[k] = area;
					}
					mAreaDatasMap.put(city, mAreasDatas);
				}

				mCitisDatasMap.put(province, mCitiesDatas);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		mJsonObj = null;
	}

	/**
	 * 从assert文件夹中读取省市区的json文件，然后转化为json对象
	 */
	private void initJsonData() {
		try {
			if (mJsonObj == null) {
				StringBuffer sb = new StringBuffer();
				InputStream is = getAssets().open("city.json");
				int len = -1;
				byte[] buf = new byte[1024];
				while ((len = is.read(buf)) != -1) {
					sb.append(new String(buf, 0, len, "gbk"));
				}
				is.close();
				mJsonObj = new JSONObject(sb.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
	 */
	private JSONObject mJsonObj;
	/**
	 * 省的WheelView控件
	 */
	private WheelView mProvince;
	/**
	 * 市的WheelView控件
	 */
	private WheelView mCity;
	/**
	 * 区的WheelView控件
	 */
	private WheelView mArea;

	/**
	 * 所有省
	 */
	private String[] mProvinceDatas;
	/**
	 * key - 省 value - 市s
	 */
	private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区s
	 */
	private Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();

	/**
	 * 当前省的名称
	 */
	private String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	private String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	private String mCurrentAreaName = "";

}
