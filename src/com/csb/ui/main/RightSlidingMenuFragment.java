package com.csb.ui.main;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.trinea.android.common.util.ImageUtils;

import com.csb.R;
import com.csb.bean.ResultUploadPicBean;
import com.csb.bean.UserBean;
import com.csb.dao.URLHelper;
import com.csb.support.lib.MyAsyncTask;
import com.csb.support.settinghelper.SettingUtility;
import com.csb.ui.login.LoginActivity;
import com.csb.ui.setting.MyCalendarActivity;
import com.csb.ui.setting.MyInfoActivity;
import com.csb.ui.setting.SettingsActivity;
import com.csb.ui.task.UploadPicAsyncTask;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.GlobalContext;
import com.csb.utils.ToastUtils;
import com.csb.utils.Utility;

/**
 * 主要控制右边按钮点击事件
 * 
 * @author Administrator
 * 
 */
public class RightSlidingMenuFragment extends Fragment implements
		OnClickListener {
	private static final String TAG = RightSlidingMenuFragment.class
			.getSimpleName();
	private static final int RESULT_LOAD_IMAGE = 0;
	private View myInfoBtnLayout;
	private View myPublishBtnLayout;
	private View myCalendarBtnLayout;
	private View myBrowserBtnLayout;
	private View myFavBtnLayout;
	private View addressBookBtnLayout;
	private View settingBtnLayout;
	private ImageView headImageView;
	private TextView nameTextView;
	private TextView emailTextView;
	private TextView logoutBtn;
	private View[] menuItemViews = { myInfoBtnLayout, myPublishBtnLayout,
			myCalendarBtnLayout, myBrowserBtnLayout, myFavBtnLayout,
			addressBookBtnLayout, settingBtnLayout };
	private int[] menuItemViewIds = { R.id.myInfoBtnLayout,
			R.id.myPublishBtnLayout, R.id.myCalendarBtnLayout,
			R.id.myBrowserBtnLayout, R.id.myFavBtnLayout,
			R.id.addressBookBtnLayout, R.id.settingBtnLayout };

	private LinearLayout ll_myinfo;

	private Context context;

	private UploadPicAsyncTask uploadPicAsyncTask;

	private static final String IMAGE_FILE_LOCATION = "file:///"
			+ Environment.getExternalStorageDirectory() + "/temp.jpg";
	private static Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);

	private static String mImageCapturePath;

	private static String SCALE_IMAGE_PATH = Environment
			.getExternalStorageDirectory() + File.separator + "temp_scale.jpg";

	private static final int FROM_FILE = 1;
	private static final int FROM_CAMERA = 2;
	private static final int CROP_BIG_PICTURE = 4;
	private static final int CROP_SMALL_PICTURE = 5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_rigth_fragment, container,
				false);
		context = view.getContext();
		initView(view);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (GlobalContext.getInstance().getUserBean() != null
				&& !TextUtils.isEmpty(GlobalContext.getInstance().getUserBean()
						.getImageurl())) {
			GlobalContext.IMAGE_CACHE.get(URLHelper.URL_SERVER_UPLOAD
					+ GlobalContext.getInstance().getUserBean().getImageurl(),
					headImageView);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		GlobalContext.getInstance().saveImgDataToDb();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

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

	private void initView(View view) {

		ll_myinfo = (LinearLayout) view.findViewById(R.id.ll_myinfo);
		ll_myinfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MyInfoActivity.class);
				startActivity(intent);
			}
		});
		headImageView = (ImageView) view.findViewById(R.id.iv_head);
		headImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showChooseDialog();
			}
		});

		nameTextView = (TextView) view.findViewById(R.id.nameTextView);
		emailTextView = (TextView) view.findViewById(R.id.emailTextView);
		logoutBtn = (TextView) view.findViewById(R.id.btn_logout);
		logoutBtn.setOnClickListener(this);

		final UserBean userBean = GlobalContext.getInstance().getUserBean();
		nameTextView.setText(userBean.getUsername());
		emailTextView.setText(userBean.getMail());

		for (int i = 0; i < menuItemViews.length; i++) {
			menuItemViews[i] = view.findViewById(menuItemViewIds[i]);
			menuItemViews[i].setOnClickListener(this);
		}
		/*
		 * myInfoBtnLayout = view.findViewById(R.id.myInfoBtnLayout);
		 * myInfoBtnLayout.setOnClickListener(this); myPublishBtnLayout =
		 * view.findViewById(R.id.myPublishBtnLayout);
		 * myPublishBtnLayout.setOnClickListener(this); myCalendarBtnLayout =
		 * view.findViewById(R.id.myCalendarBtnLayout);
		 * myCalendarBtnLayout.setOnClickListener(this); myBrowserBtnLayout =
		 * view.findViewById(R.id.myBrowserBtnLayout);
		 * myBrowserBtnLayout.setOnClickListener(this); myFavBtnLayout =
		 * view.findViewById(R.id.myFavBtnLayout);
		 * myFavBtnLayout.setOnClickListener(this); addressBookBtnLayout =
		 * view.findViewById(R.id.addressBookBtnLayout);
		 * addressBookBtnLayout.setOnClickListener(this); settingBtnLayout =
		 * view.findViewById(R.id.settingBtnLayout);
		 * settingBtnLayout.setOnClickListener(this);
		 */
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Utility.cancelTasks(uploadPicAsyncTask);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
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
				headImageView.setImageBitmap(bitmap);

				if (Utility.isTaskStopped(uploadPicAsyncTask)) {
					uploadPicAsyncTask = new UploadPicAsyncTask(GlobalContext
							.getInstance().getUserBean().getUserid(),
							mImageCapturePath,
							UploadPicAsyncTask.UPLOAD_HEAD_PIC, myHandler);
					uploadPicAsyncTask
							.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
				}
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

	private void selectedMenuItemView(int id) {
		for (int i = 0; i < menuItemViews.length; i++) {
			if (id == menuItemViews[i].getId())
				menuItemViews[i].setSelected(true);
			else
				menuItemViews[i].setSelected(false);
		}
	}

	protected void doMyCalendarEvent() {
		Intent i = new Intent(getActivity(), MyCalendarActivity.class);
		startActivity(i);
	}
	
	protected void doSystemCalendarEvent() {
		try {
			Intent i = new Intent();
			ComponentName cn = null;
			if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
				cn = new ComponentName("com.android.calendar",
						"com.android.calendar.LaunchActivity");
				
			} else {
				cn = new ComponentName("com.google.android.calendar",
						"com.android.calendar.LaunchActivity");
			}
			i.setComponent(cn);
			startActivity(i);
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		Intent intent = null;
		switch (v.getId()) {
		case R.id.myInfoBtnLayout:
			intent = new Intent(getActivity(), MyInfoActivity.class);
			startActivity(intent);
		case R.id.myPublishBtnLayout:
		case R.id.myBrowserBtnLayout:
		case R.id.myFavBtnLayout:
		case R.id.addressBookBtnLayout:
			break;
		case R.id.settingBtnLayout:
			intent = new Intent(getActivity(), SettingsActivity.class);
			startActivity(intent);
			break;
		case R.id.myCalendarBtnLayout:
			doMyCalendarEvent();
			showContent();
			break;
		case R.id.iv_head:
			// Toast.makeText(getActivity(), "点击头像", Toast.LENGTH_SHORT).show();
			// Intent intent = new Intent(getActivity(), AboutActivity.class);
			// startActivity(intent);
			break;
		case R.id.btn_logout:
			// Intent i = new Intent(Intent.ACTION_MAIN);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// i.addCategory(Intent.CATEGORY_HOME);
			// startActivity(i);
			logout();
			break;
		default:
			break;
		}
	}

	private void jumpLoginActivity() {
		Intent i = new Intent(getActivity(), LoginActivity.class);
		i.putExtra("login", true);
		startActivity(i);
		getActivity().finish();
		return;
	}

	public void logout() {
		Dialog dialog = new AlertDialog.Builder(getActivity()).setTitle("提示")
				.setMessage("确实要注销吗?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						SettingUtility.setDefaultUserBean(new UserBean());
						jumpLoginActivity();
					}

				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}

				}).create();
		dialog.show();
	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		MainActivity ra = (MainActivity) getActivity();
		ra.switchContent(fragment);
	}

	private void showContent() {
		if (getActivity() == null)
			return;
		MainActivity ra = (MainActivity) getActivity();
		ra.showContent();
	}

	private ProgressDialog dialog = null;

	private void showDialog() {
		dialog = new ProgressDialog(context);
		dialog.setMessage("图片上传中,请稍候...");
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
			case BundleArgsConstants.REQUEST_START:
				showDialog();
				break;
			case BundleArgsConstants.UPLOAD_HEAD_PIC_SUCC:
			case BundleArgsConstants.UPLOAD_CODE_PIC_SUCC:
				dismissDialog();
				if (msg.obj != null) {
					ResultUploadPicBean bean = (ResultUploadPicBean) msg.obj;
					onSuccess(bean);
				}

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

	protected void onSuccess(ResultUploadPicBean bean) {
		UserBean userBean = GlobalContext.getInstance().getUserBean();
		userBean.setImageurl(bean.getImageurl());
		GlobalContext.getInstance().setUserBean(userBean);
		ToastUtils.show(context, "图片上传成功!");
	}

}
