package com.csb.ui.setting;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import cn.trinea.android.common.util.ImageUtils;

import com.csb.R;
import com.csb.bean.ResultUploadPicBean;
import com.csb.bean.UserBean;
import com.csb.dao.URLHelper;
import com.csb.support.lib.MyAsyncTask;
import com.csb.ui.login.XingquedianActivity;
import com.csb.ui.login.XueshuActivity;
import com.csb.ui.task.UpdateUserAsyncTask;
import com.csb.ui.task.UploadPicAsyncTask;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.ClassPathResource;
import com.csb.utils.GlobalContext;
import com.csb.utils.ToastUtils;
import com.csb.utils.Utility;

public class MyInfoActivity extends Activity implements OnClickListener,
		OnWheelChangedListener {
	private Button btn_title_left, btn_title_right;
	private TextView tv_top_title;
	private EditText tv_hospital;
	private EditText tv_deparments;
	private EditText tv_post;
	private EditText tv_region;
	private EditText tv_phone;
	private EditText tv_mail;
	private EditText tv_password;
	private EditText tv_password_cofirm;
	private EditText tv_gender;
	private EditText tv_birthday;
	private EditText tv_code;
	private EditText tv_username;
	private EditText tv_xuiwei;
	private EditText tv_xueshui;
	private EditText tv_xingquedian;
	private ImageView iv_headpic;
	private ImageView iv_doctorpic;
	private Context context = null;
	private UserBean userBean;
	private UpdateUserAsyncTask updateUserAsyncTask = null;
	private static final int RESULT_LOAD_HEAD_IMAGE = 0;
	private static final int RESULT_LOAD_CODE_IMAGE = 1;
	private static final int RESULT_LOAD_HEAD_IMAGE_FROM_CAMERA = 2;
	private static final int RESULT_LOAD_CODE_IMAGE_CAMERA = 3;
	private static final int CROP_BIG_PICTURE = 4;
	private static final int CROP_SMALL_PICTURE = 5;
	public static final int REQUESTCODE_XUESHI = 6;
	public static final int REQUESTCODE_XINGQUEDIAN = 7;
	// dialog
	public static final int GENDER_SELECT = 1;
	public static final int AGE_SELECT = 2;
	public static final int DEPARTMENTS_SELECT = 3;
	public static final int POST_SELECT = 4;
	public static final int XUIWEI_SELECT = 5;

	private UploadPicAsyncTask uploadPicAsyncTask;

	private boolean isEnabled = false;

	private static final String IMAGE_FILE_LOCATION = "file:///"
			+ Environment.getExternalStorageDirectory() + "/temp.png";
	private static Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);

	private static String mImageCapturePath;
	private static String SCALE_IMAGE_PATH = Environment
			.getExternalStorageDirectory() + File.separator + "temp_scale.png";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_info);
		context = this;
		initView();
	}

	private void showChooseDialog(final int type) {
		final String[] items = new String[] { "拍照", "文件" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
						int requestCode = (type == UploadPicAsyncTask.UPLOAD_HEAD_PIC ? RESULT_LOAD_HEAD_IMAGE_FROM_CAMERA
								: RESULT_LOAD_CODE_IMAGE_CAMERA);
						startActivityForResult(intent, requestCode);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					/*
					 * Intent intent = new Intent();
					 * 
					 * intent.setType("image/*");
					 * intent.setAction(Intent.ACTION_GET_CONTENT);
					 * 
					 * startActivityForResult(Intent.createChooser(intent,
					 * "Complete action using"), PICK_FROM_FILE);
					 */
					Intent i = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					int requestCode = (type == UploadPicAsyncTask.UPLOAD_HEAD_PIC ? RESULT_LOAD_HEAD_IMAGE
							: RESULT_LOAD_CODE_IMAGE);
					startActivityForResult(i, requestCode);
				}
				// dialog.dismiss();
			}
		}).create().show();
	}

	private void changeViewEnabled(boolean status) {
		tv_username.setEnabled(status);
		tv_hospital.setEnabled(status);
		tv_deparments.setEnabled(status);
		tv_post.setEnabled(status);
		tv_region.setEnabled(status);
		tv_phone.setEnabled(status);
//		tv_mail.setEnabled(status);
		tv_password.setEnabled(status);
		// tv_password_cofirm.setEnabled(status);
		tv_xuiwei.setEnabled(status);
		tv_xueshui.setEnabled(status);
		tv_xingquedian.setEnabled(status);
		tv_code.setEnabled(status);
		tv_gender.setEnabled(status);
		tv_birthday.setEnabled(status);
		if (status)
			btn_title_right.setText("提交");
		else
			btn_title_right.setText("重新编辑");
	}

	@SuppressWarnings("deprecation")
	private void initView() {
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("我的信息");
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setText("返回");
		btn_title_left.setOnClickListener(this);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		btn_title_right.setText("重新编辑");
		btn_title_right.setOnClickListener(this);

		userBean = GlobalContext.getInstance().getUserBean();

		tv_xuiwei = (EditText) findViewById(R.id.tv_xuiwei);
		tv_xuiwei.setText(userBean.getDegree());
		tv_xuiwei.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(XUIWEI_SELECT);
			}
		});

		tv_xueshui = (EditText) findViewById(R.id.tv_xueshui);
		tv_xueshui.setText(userBean.getPlanning());
		tv_xueshui.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = XueshuActivity.newIntent();
				intent.putExtra(BundleArgsConstants.XUESHU_EXTRA, tv_xueshui
						.getText().toString());
				MyInfoActivity.this.startActivityForResult(intent,
						REQUESTCODE_XUESHI);

			}
		});

		tv_xingquedian = (EditText) findViewById(R.id.tv_xingquedian);
		tv_xingquedian.setText(userBean.getInterest());
		tv_xingquedian.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = XingquedianActivity.newIntent();
				intent.putExtra(BundleArgsConstants.XINGQUEDIAN_EXTRA,
						tv_xingquedian.getText().toString());
				MyInfoActivity.this.startActivityForResult(intent,
						REQUESTCODE_XINGQUEDIAN);
			}
		});

		tv_hospital = (EditText) findViewById(R.id.tv_hospital);
		tv_hospital.setText(userBean.getHospital());
		tv_username = (EditText) findViewById(R.id.tv_username);
		tv_username.setText(userBean.getUsername());
		tv_deparments = (EditText) findViewById(R.id.tv_deparments);
		tv_deparments.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DEPARTMENTS_SELECT);
			}
		});
		tv_deparments.setText(userBean.getDepartments());
		tv_post = (EditText) findViewById(R.id.tv_post);
		tv_post.setText(userBean.getPost());
		tv_post.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showPostDailog();
			}
		});
		tv_region = (EditText) findViewById(R.id.tv_region);
		tv_region.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showWheelDailog();
			}
		});
		tv_region.setText(userBean.getRegion());
		tv_phone = (EditText) findViewById(R.id.tv_phone);
		tv_phone.setText(userBean.getPhone());
		tv_mail = (EditText) findViewById(R.id.tv_mail);
		tv_mail.setText(userBean.getMail());
		tv_password = (EditText) findViewById(R.id.tv_password);
		tv_password_cofirm = (EditText) findViewById(R.id.tv_password_cofirm);
		tv_password.setText(userBean.getPassword());
		tv_gender = (EditText) findViewById(R.id.tv_gender);
		tv_gender.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(GENDER_SELECT);
			}
		});
		tv_gender.setText("1".equals(userBean.getGender()) ? "男" : "女");
		tv_birthday = (EditText) findViewById(R.id.tv_birthday);
		tv_birthday.setText(userBean.getAge());
		tv_birthday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(AGE_SELECT);
			}
		});
		
		tv_code = (EditText) findViewById(R.id.tv_code);
		tv_code.setText(userBean.getCode());
		iv_headpic = (ImageView) findViewById(R.id.iv_headpic);
		iv_doctorpic = (ImageView) findViewById(R.id.iv_doctorpic);
		iv_doctorpic.setOnClickListener(this);
		iv_headpic.setOnClickListener(this);

		if (!TextUtils.isEmpty(userBean.getImageurl())) {
			GlobalContext.IMAGE_CACHE.get(URLHelper.URL_SERVER_UPLOAD
					+ userBean.getImageurl(), iv_headpic);
		}

		if (!TextUtils.isEmpty(userBean.getLicenseimgurl())) {
			GlobalContext.IMAGE_CACHE.get(URLHelper.URL_SERVER_UPLOAD
					+ GlobalContext.getInstance().getUserBean()
							.getLicenseimgurl(), iv_doctorpic);
		}
		changeViewEnabled(isEnabled);

		initDialogView();
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUESTCODE_XUESHI) {
				if (data != null && !TextUtils.isEmpty(data.getAction())) {
					tv_xueshui.setText(data.getAction());
				}
				return;
			}

			if (requestCode == REQUESTCODE_XINGQUEDIAN) {
				if (data != null && !TextUtils.isEmpty(data.getAction())) {
					tv_xingquedian.setText(data.getAction());
				}
				return;
			}

			Bitmap bitmap = null;
			if (requestCode == RESULT_LOAD_HEAD_IMAGE
					|| requestCode == RESULT_LOAD_CODE_IMAGE) {
				if (data != null) {
					mImageCapturePath = getPicturePath(data);
				}
			} else if (requestCode == RESULT_LOAD_HEAD_IMAGE_FROM_CAMERA
					|| requestCode == RESULT_LOAD_CODE_IMAGE_CAMERA) {
				if (imageUri != null) {
					// if (requestCode == RESULT_LOAD_HEAD_IMAGE_FROM_CAMERA) {
					// cropImageUri(imageUri, 150, 150, CROP_SMALL_PICTURE);
					// } else if (requestCode == RESULT_LOAD_CODE_IMAGE_CAMERA)
					// {
					// cropImageUri(imageUri, 300, 300, CROP_BIG_PICTURE);
					// }
					ImageUtils.scaleImage(imageUri.getPath(), SCALE_IMAGE_PATH);
					mImageCapturePath = SCALE_IMAGE_PATH;
					// return;
				}

			} else if (requestCode == CROP_SMALL_PICTURE
					|| requestCode == CROP_BIG_PICTURE) {
				mImageCapturePath = imageUri.getPath();
			}
			if (!TextUtils.isEmpty(mImageCapturePath)) {

				bitmap = ImageUtils.getLoacalBitmap(mImageCapturePath);// BitmapFactory.decodeFile(mImageCapturePath);

				int type = UploadPicAsyncTask.UPLOAD_HEAD_PIC;
				if (requestCode == RESULT_LOAD_HEAD_IMAGE
						|| requestCode == CROP_SMALL_PICTURE
						|| requestCode == RESULT_LOAD_HEAD_IMAGE_FROM_CAMERA) {
					iv_headpic.setImageBitmap(bitmap);
					iv_headpic.refreshDrawableState();
					type = UploadPicAsyncTask.UPLOAD_HEAD_PIC;
				} else if (requestCode == RESULT_LOAD_CODE_IMAGE
						|| requestCode == CROP_BIG_PICTURE
						|| requestCode == RESULT_LOAD_CODE_IMAGE_CAMERA) {
					iv_doctorpic.setImageBitmap(bitmap);
					type = UploadPicAsyncTask.UPLOAD_DOCTOR_CODE_PIC;
				}

				if (Utility.isTaskStopped(uploadPicAsyncTask)) {
					uploadPicAsyncTask = new UploadPicAsyncTask(GlobalContext
							.getInstance().getUserBean().getUserid(),
							mImageCapturePath, type, myHandler);
					uploadPicAsyncTask
							.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
				}
			}
		}
	}

	private String getPicturePath(Intent data) {
		String picturePath = null;
		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		Cursor cursor = context.getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		picturePath = cursor.getString(columnIndex);
		cursor.close();
		return picturePath;
	}

	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.btn_title_right:
			if (isEnabled) {
				doUpdateUser();
			} 
			changeViewEnabled(!isEnabled);
			isEnabled = !isEnabled;
			break;
		case R.id.iv_doctorpic:
			showChooseDialog(UploadPicAsyncTask.UPLOAD_DOCTOR_CODE_PIC);
			// i = new Intent(
			// Intent.ACTION_PICK,
			// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			// startActivityForResult(i, RESULT_LOAD_CODE_IMAGE);
			break;
		case R.id.iv_headpic:
			// i = new Intent(
			// Intent.ACTION_PICK,
			// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			// startActivityForResult(i, RESULT_LOAD_HEAD_IMAGE);
			showChooseDialog(UploadPicAsyncTask.UPLOAD_HEAD_PIC);
			break;
		case R.id.btn_title_left:
			MyInfoActivity.this.finish();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Utility.cancelTasks(updateUserAsyncTask);
		GlobalContext.getInstance().saveImgDataToDb();
	}

	private void doUpdateUser() {
		final String email = tv_mail.getText().toString();
		final String phone = tv_phone.getText().toString();
		final String password = tv_password.getText().toString();
		final String username = tv_username.getText().toString();
		final String hospital = tv_hospital.getText().toString();
		final String departments = tv_deparments.getText().toString();
		final String post = tv_post.getText().toString();
		final String region = tv_region.getText().toString();
		final String gender = tv_gender.getText().toString();
		final String birthday = tv_birthday.getText().toString();
		final String code = tv_code.getText().toString();
		final String xuewei = tv_xuiwei.getText().toString();
		final String xueshu = tv_xueshui.getText().toString();
		final String xingquedian = tv_xingquedian.getText().toString();
		
		StringBuffer msg = new StringBuffer();
		if (TextUtils.isEmpty(username)) {
			msg.append("姓名  ");
		}
		if (TextUtils.isEmpty(hospital)) {
			msg.append("单位  ");
		}
		if (TextUtils.isEmpty(departments)) {
			msg.append("科室  ");
		}
		if (TextUtils.isEmpty(post)) {
			msg.append("职务  ");
		}
		if (TextUtils.isEmpty(region)) {
			msg.append("单位信息  ");
		}
		if (TextUtils.isEmpty(gender)) {
			msg.append("姓别  ");
		}
		if (TextUtils.isEmpty(birthday)) {
			msg.append("年龄范围   ");
		}
		if (TextUtils.isEmpty(phone)) {
			msg.append("手机号  ");
		}
		if (TextUtils.isEmpty(email)) {
			msg.append("邮箱  ");
		}
		if (TextUtils.isEmpty(password)) {
			msg.append("密码  ");
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
		if (msg.length() > 0) {
			msg.append("字段不能为空!");
			ToastUtils.show(context, msg);
		} else {
			if (!ClassPathResource.isMobileNO(phone)) {
				msg.append("手机号  字段不合法，请重新输入！");
				ToastUtils.show(context, msg);
			} else if (!ClassPathResource.isEmail(email)) {
				msg.append("邮箱  字段不合法，请重新输入！");
				ToastUtils.show(context, msg);
			} else {
				boolean isNotUpdate = phone.equals(userBean.getPhone())
						&& password.equals(userBean.getPassword())
						&& username.equals(userBean.getUsername())
						&& hospital.equals(userBean.getHospital())
						&& departments.equals(userBean.getDepartments())
						&& post.equals(userBean.getPost())
						&& region.equals(userBean.getRegion())
						&& gender.equals(userBean.getGender())
						&& birthday.equals(userBean.getAge())
						&& code.equals(userBean.getCode())
						&& xuewei.equals(userBean.getDegree())
						&& xueshu.equals(userBean.getPlanning())
						&& xingquedian.equals(userBean.getInterest());
				if (!isNotUpdate) {
					userBean.setUsername(username);
					userBean.setHospital(hospital);
					userBean.setDepartments(departments);
					userBean.setPost(post);
					userBean.setRegion(region);
					userBean.setGender(gender);
					userBean.setAge(birthday);
					userBean.setCode(code);
					userBean.setMail(email);
					userBean.setPhone(phone);
					userBean.setPassword(password);
					userBean.setDegree(xuewei);
					userBean.setPlanning(xueshu);
					userBean.setInterest(xingquedian);
					if (Utility.isTaskStopped(updateUserAsyncTask)) {
						updateUserAsyncTask = new UpdateUserAsyncTask(userBean,
								myHandler);
						updateUserAsyncTask
								.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
					}
				} else {
					 ToastUtils.show(context, "修改成功!");
				}
			}
		}
	}

	private ProgressDialog progressDialog;
	private Dialog dialog = null;

	private static final int SHOW_PROGRESS_DIALOG = 100;
	private static final int SHOW_PROGRESS_DIALOG_PIC = 101;

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case AGE_SELECT:
			Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("请选择年龄范围");
			final String[] ageArr = new String[] { "30岁以下", "30-35", "36-40",
					"41-45", "46-50", "50岁以上" };
			builder.setSingleChoiceItems(// 第一个参数是要显示的列表，用数组展示；第二个参数是默认选中的项的位置；
					// 第三个参数是一个事件点击监听器
					ageArr, 0, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							tv_birthday.setText(ageArr[which]);
							dismissDialog(AGE_SELECT);// 想对话框关闭
						}
					});
			dialog = builder.create();
			return dialog;
		case XUIWEI_SELECT:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("请选择学位");
			final String[] xwArr = new String[] { "博士", "博士后", "硕士", "学士",
					"大专", "其他" };
			builder.setSingleChoiceItems(// 第一个参数是要显示的列表，用数组展示；第二个参数是默认选中的项的位置；
					// 第三个参数是一个事件点击监听器
					xwArr, 0, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							tv_xuiwei.setText(xwArr[which]);
							dismissDialog(XUIWEI_SELECT);// 想对话框关闭
						}
					});
			dialog = builder.create();
			return dialog;
		case GENDER_SELECT:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("请选择性别");
			final String[] regionArr = new String[] { "男", "女" };
			builder.setSingleChoiceItems(regionArr, 0,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							tv_gender.setText(regionArr[which]);
							dismissDialog(GENDER_SELECT);// 想对话框关闭
						}
					});
			dialog = builder.create();
			return dialog;
		case DEPARTMENTS_SELECT:
			builder = new AlertDialog.Builder(this);
			builder.setTitle("请选择所属科室");
			final String[] depArr = new String[] { "心脏内科", "心电图/扇超室",
					"普通内科（未分科）", "其他专科" };
			builder.setSingleChoiceItems(depArr, 0,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							tv_deparments.setText(depArr[which]);
							dismissDialog(DEPARTMENTS_SELECT);
						}
					});
			dialog = builder.create();
			return dialog;
		case SHOW_PROGRESS_DIALOG:
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在提交,请稍候...");
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(true);
			return progressDialog;

		case SHOW_PROGRESS_DIALOG_PIC:
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("图片正在提交,请稍候...");
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(true);
			return progressDialog;
		}
		return null;
	}

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case BundleArgsConstants.REQUEST_START:
				showDialog(SHOW_PROGRESS_DIALOG);
				break;
			case BundleArgsConstants.REQUEST_SUCC:
				onSuccess();
				break;
			case BundleArgsConstants.REQUEST_FAIL:
				btn_title_right.setEnabled(true);
				dismissDialog(SHOW_PROGRESS_DIALOG);
			case BundleArgsConstants.UPLOAD_HEAD_PIC_SUCC:
				dismissDialog(SHOW_PROGRESS_DIALOG);
				if (msg.obj != null) {
					ResultUploadPicBean bean = (ResultUploadPicBean) msg.obj;
					onUploadPicSuccess(bean,
							BundleArgsConstants.UPLOAD_HEAD_PIC_SUCC);
				}
			case BundleArgsConstants.UPLOAD_CODE_PIC_SUCC:
				dismissDialog(SHOW_PROGRESS_DIALOG);
				if (msg.obj != null) {
					ResultUploadPicBean bean = (ResultUploadPicBean) msg.obj;
					onUploadPicSuccess(bean,
							BundleArgsConstants.UPLOAD_CODE_PIC_SUCC);
				}
			default:
				break;
			}
		}
	};

	private void onSuccess() {
		btn_title_right.setEnabled(true);
		dismissDialog(SHOW_PROGRESS_DIALOG);
		GlobalContext.getInstance().setUserBean(userBean);
	}

	protected void onUploadPicSuccess(ResultUploadPicBean bean, int type) {
		UserBean userBean = GlobalContext.getInstance().getUserBean();
		if (type == BundleArgsConstants.UPLOAD_HEAD_PIC_SUCC)
			userBean.setImageurl(bean.getImageurl());
		else
			userBean.setLicenseimgurl(bean.getImageurl());
		GlobalContext.getInstance().setUserBean(userBean);
		ToastUtils.show(context, "图片上传成功!");

	}

	/**
	 * 选择职称
	 */
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
								tv_post.setText(mZyzc + " " + mJz);
								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mZyzc = "";
								mJz = "";
								tv_post.setText("");
								dialog.dismiss();
							}
						}).create();
		postDialog.show();
	}

	/**
	 * 选择地区
	 */
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
								tv_region.setText(region);
								mCurrentAreaName = "";
								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								tv_region.setText("");
								dialog.dismiss();
							}
						}).create();
		wheelDialog.show();
	}

	private void initDialogView() {
		initJsonData();
		initDatas();
	}

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
