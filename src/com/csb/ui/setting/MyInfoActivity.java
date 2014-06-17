package com.csb.ui.setting;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.csb.R;
import com.csb.bean.ResultUploadPicBean;
import com.csb.bean.UserBean;
import com.csb.dao.URLHelper;
import com.csb.support.lib.MyAsyncTask;
import com.csb.ui.task.UpdateUserAsyncTask;
import com.csb.ui.task.UploadPicAsyncTask;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.ClassPathResource;
import com.csb.utils.GlobalContext;
import com.csb.utils.ToastUtils;
import com.csb.utils.Utility;

public class MyInfoActivity extends Activity implements OnClickListener {
	private Button btn_title_left, btn_title_right;
	private TextView tv_top_title;
	private EditText tv_hospital;
	private EditText tv_deparments;
	private EditText tv_post;
	private EditText tv_region;
	private EditText tv_phone;
	private EditText tv_mail;
	private EditText tv_password;
	private EditText tv_gender;
	private EditText tv_birthday;
	private EditText tv_code;
	private EditText tv_username;
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

	private UploadPicAsyncTask uploadPicAsyncTask;

	private boolean isEnabled = false;

	private static final String IMAGE_FILE_LOCATION = "file:///"
			+ Environment.getExternalStorageDirectory() + "/temp.jpg";
	private static Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);

	private static String mImageCapturePath;

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
		tv_mail.setEnabled(status);
		tv_password.setEnabled(status);
		if (status)
			btn_title_right.setText("提交");
		else
			btn_title_right.setText("重新编辑");
	}

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
		tv_hospital = (EditText) findViewById(R.id.tv_hospital);
		tv_hospital.setText(userBean.getHospital());
		tv_username = (EditText) findViewById(R.id.tv_username);
		tv_username.setText(userBean.getUsername());
		tv_deparments = (EditText) findViewById(R.id.tv_deparments);
		tv_deparments.setText(userBean.getDepartments());
		tv_post = (EditText) findViewById(R.id.tv_post);
		tv_post.setText(userBean.getPost());
		tv_region = (EditText) findViewById(R.id.tv_region);
		tv_region.setText(userBean.getRegion());
		tv_phone = (EditText) findViewById(R.id.tv_phone);
		tv_phone.setText(userBean.getPhone());
		tv_mail = (EditText) findViewById(R.id.tv_mail);
		tv_mail.setText(userBean.getMail());
		tv_password = (EditText) findViewById(R.id.tv_password);
		tv_password.setText(userBean.getPassword());
		tv_gender = (EditText) findViewById(R.id.tv_gender);
		tv_gender.setText(userBean.getGender());
		tv_birthday = (EditText) findViewById(R.id.tv_birthday);
		tv_birthday.setText(userBean.getBirthday());
		tv_code = (EditText) findViewById(R.id.tv_code);
		tv_code.setText(userBean.getCode());
		iv_headpic = (ImageView) findViewById(R.id.iv_headpic);
		iv_doctorpic = (ImageView) findViewById(R.id.iv_doctorpic);
		iv_doctorpic.setOnClickListener(this);
		iv_headpic.setOnClickListener(this);

		if ( !TextUtils.isEmpty(userBean
						.getImageurl())) {
			GlobalContext.IMAGE_CACHE.get(URLHelper.URL_SERVER_UPLOAD
					+ userBean.getImageurl(),
					iv_headpic);
		}

		if (!TextUtils.isEmpty(userBean
						.getLicenseimgurl())) {
			GlobalContext.IMAGE_CACHE.get(URLHelper.URL_SERVER_UPLOAD
					+ GlobalContext.getInstance().getUserBean()
							.getLicenseimgurl(), iv_doctorpic);
		}
		changeViewEnabled(isEnabled);
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
			if (requestCode == RESULT_LOAD_HEAD_IMAGE
					|| requestCode == RESULT_LOAD_CODE_IMAGE) {
				if (data != null) {
					mImageCapturePath = getPicturePath(data);
				}
			} else if (requestCode == RESULT_LOAD_HEAD_IMAGE_FROM_CAMERA
					|| requestCode == RESULT_LOAD_CODE_IMAGE_CAMERA) {
				if (imageUri != null) {
					if (requestCode == RESULT_LOAD_HEAD_IMAGE_FROM_CAMERA) {
						cropImageUri(imageUri, 150, 150, CROP_SMALL_PICTURE);
					} else if (requestCode == RESULT_LOAD_CODE_IMAGE_CAMERA) {
						cropImageUri(imageUri, 300, 300, CROP_BIG_PICTURE);
					}
					return;
				}

			} else if (requestCode == CROP_SMALL_PICTURE || requestCode ==CROP_BIG_PICTURE){
				mImageCapturePath = imageUri.getPath();
			}
			if (!TextUtils.isEmpty(mImageCapturePath)) {
				Bitmap bitmap = BitmapFactory.decodeFile(mImageCapturePath);
				int type = UploadPicAsyncTask.UPLOAD_HEAD_PIC;
				if (requestCode == RESULT_LOAD_HEAD_IMAGE
						|| requestCode == CROP_SMALL_PICTURE) {
					iv_headpic.setImageBitmap(bitmap);
					type = UploadPicAsyncTask.UPLOAD_HEAD_PIC;
				} else if (requestCode == RESULT_LOAD_CODE_IMAGE
						|| requestCode == CROP_BIG_PICTURE) {
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
			if (isEnabled)
				doUpdateUser();
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
		if (TextUtils.isEmpty(phone)) {
			msg.append("手机号  ");
		}
		if (TextUtils.isEmpty(email)) {
			msg.append("邮箱  ");
		}
		if (TextUtils.isEmpty(password)) {
			msg.append("密码  ");
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
						&& birthday.equals(userBean.getBirthday())
						&& code.equals(userBean.getCode());
				if (!isNotUpdate) {
					userBean.setUsername(username);
					userBean.setHospital(hospital);
					userBean.setDepartments(departments);
					userBean.setPost(post);
					userBean.setRegion(region);
					userBean.setGender(gender);
					userBean.setBirthday(birthday);
					userBean.setCode(code);
					userBean.setMail(email);
					userBean.setPhone(phone);
					userBean.setPassword(password);
					if (Utility.isTaskStopped(updateUserAsyncTask)) {
						updateUserAsyncTask = new UpdateUserAsyncTask(userBean,
								myHandler);
						updateUserAsyncTask
								.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
					}
				} else {
					// ToastUtils.show(context, "您没有修改信息!");
				}
			}
		}
	}

	private ProgressDialog mDialog;

	private static final int SHOW_PROGRESS_DIALOG = 0;
	private static final int SHOW_PROGRESS_DIALOG_PIC = 1;

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case SHOW_PROGRESS_DIALOG:
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("正在提交,请稍候...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;

		case SHOW_PROGRESS_DIALOG_PIC:
			dialog = new ProgressDialog(this);
			dialog.setMessage("图片正在提交,请稍候...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
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

}
