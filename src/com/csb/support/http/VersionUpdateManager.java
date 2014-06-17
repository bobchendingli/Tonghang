package com.csb.support.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.csb.R;
import com.csb.bean.UpdateVersionBean;
import com.csb.dao.setting.UpdateVersionDao;
import com.csb.support.error.WeiboException;
import com.csb.ui.setting.AboutActivity;

public class VersionUpdateManager {

	private static final String TAG = "VersionUpdateManager";
	public static String ACTION_UPDATE_APK = "android.intent.action.AUTO_UPDATE";
	private static Context mContext;
	private DownloadManager mDownloadManager;
	private String mDownloadDir = "tonghang/download";

	private File mFile;


	private final static int UPDATE_NOTIFY = 0;
	private final static int UPDATE_ERROR = 1;
	private final static int DOWNLOAD_ERROR = 2;
	private final static int INSTALL_ERROR = 3;
	private final static int UPDATE_IS_NEWEAST = 4;
	private final static int UPDATE_NOTIFY_MANUAL = 5;
	private final static int UPDATE_NOTIFY_AUTO = 6;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATE_NOTIFY_AUTO:
				sendNotification();
				break;
			case UPDATE_NOTIFY_MANUAL:
				if(msg.obj != null && msg.obj instanceof UpdateVersionBean){
					showUpdateDialog(((UpdateVersionBean) msg.obj).getUrl());
				}
				break;
			case UPDATE_ERROR:
				Toast.makeText(mContext, R.string.update_failure,
						Toast.LENGTH_SHORT).show();
				break;
			case DOWNLOAD_ERROR:
				Toast.makeText(mContext, R.string.update_download_failure,
						Toast.LENGTH_SHORT).show();
				break;
			case INSTALL_ERROR:
				Toast.makeText(mContext, R.string.update_install_failure,
						Toast.LENGTH_SHORT).show();
				break;
			case UPDATE_IS_NEWEAST:
				Toast.makeText(mContext, R.string.update_is_neweast,
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	private static VersionUpdateManager versionUpdateManager;
	private static DownloadCompleteReceiver mReceiver;

	public final static int UPDATE_MODE_AUTO = 0;
	public final static int UPDATE_MODE_MANUAL = 1;
	private static int curUpdateMode = UPDATE_MODE_AUTO;

	private ProgressDialog mRefreshDialog;

	public static synchronized VersionUpdateManager getInstance() {
		return versionUpdateManager;
	}

	public static synchronized void createInstance(Context context) {
		if (context != null) {
			versionUpdateManager = new VersionUpdateManager(context);
		}
	}

	public static synchronized void releaseInstance() {
		if (versionUpdateManager != null) {
			versionUpdateManager = null;
		}
	}

	public static synchronized int getUpdateMode() {
		return curUpdateMode;
	}

	public static synchronized void setUpdateMode(int mode) {
		VersionUpdateManager.curUpdateMode = mode;
	}

	public VersionUpdateManager(Context context) {
		mContext = context;
		// mReceiver = new DownloadCompleteReceiver();
		// mContext.registerReceiver(mReceiver, new
		// IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

		mRefreshDialog = new ProgressDialog(mContext);
		mRefreshDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mRefreshDialog.setMessage(mContext.getString(R.string.update_waiting));
		mRefreshDialog.setIndeterminate(false);
		mRefreshDialog.setCancelable(true);

		update(context);
	}

	private void update(Context context) {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			File dir = Environment
					.getExternalStoragePublicDirectory(mDownloadDir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			if (dir.exists()) {
				String version = "android_" + getAppVersionName();
				
				DownloadTask downloadTask = new DownloadTask(version);
				downloadTask.execute();
			}
		} else {
			// show message
			Toast.makeText(context, R.string.sdcardisnotavailable,
					Toast.LENGTH_SHORT).show();
		}
	}

	private final int INIT_ID = 0x9812213;
	private static NotificationManager manager = null;
	private static Notification notification = null;

	private void sendNotification() {
		if (manager == null) {
			manager = (NotificationManager) mContext
					.getSystemService(Service.NOTIFICATION_SERVICE);
		}
		if (notification == null) {
			notification = new Notification();
		}
		PendingIntent pIntent = PendingIntent.getActivity(mContext, 0,
				new Intent(mContext, VersionUpdateManager.class), 0);

		notification.icon = R.drawable.icon;
		notification.tickerText = mContext.getString(R.string.app_name) + ":"
				+ mContext.getString(R.string.version_update);

		notification.contentView = new RemoteViews(mContext.getPackageName(),
				R.layout.update_apk_update);
		notification.contentIntent = pIntent;
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.defaults = Notification.DEFAULT_VIBRATE;
		notification.flags = Notification.FLAG_AUTO_CANCEL;

		Intent setIntent = new Intent(mContext, AboutActivity.class);
		setIntent.putExtra("UPDATE_MODE_AUTO", UPDATE_MODE_AUTO);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
				setIntent, 0);

		notification.contentIntent = contentIntent;
		manager.cancel(INIT_ID);
		manager.notify(INIT_ID, notification);
	}

	private class DownloadTask extends
			AsyncTask<String, Integer, UpdateVersionBean> {
		private String version = "";

		public DownloadTask(String version) {
			this.version = version;
		}

		@Override
		protected UpdateVersionBean doInBackground(String... params) {
			UpdateVersionDao dao = new UpdateVersionDao(version);
//			UpdateVersionBean value = new UpdateVersionBean();
//			value.setRespCode("0000");
//			value.setUrl("http://dl.client.baidu.com/download.php?source=/ime/wubi/BaiduWubiSetup.exe");
//			value.setVersion("2.0");
			UpdateVersionBean value = null;
			try {
				value = dao.get();
			} catch (WeiboException e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = UPDATE_ERROR;
				mHandler.sendMessage(msg);
			}
			return value;
		}

		@Override
		protected void onPostExecute(UpdateVersionBean result) {
			if (result != null && result.getRespCode().equals("0000")) {
				if (!version.equalsIgnoreCase(result.getVersion())) {
					if (curUpdateMode == UPDATE_MODE_AUTO) {
						Message msg = new Message();
						msg.what = UPDATE_NOTIFY_AUTO;
						mHandler.sendMessage(msg);
					} else if (curUpdateMode == UPDATE_MODE_MANUAL) {
						Message msg = new Message();
						msg.what = UPDATE_NOTIFY_MANUAL;
						msg.obj  = result;
						mHandler.sendMessage(msg);
					}
				} else {
					Message msg = new Message();
					msg.what = UPDATE_IS_NEWEAST;
					mHandler.sendMessage(msg);
				}
			} else {
				Message msg = new Message();
				msg.what = UPDATE_IS_NEWEAST;
				mHandler.sendMessage(msg);
			}
			super.onPostExecute(result);
		}
	}

	public String getAppVersionName() {
		String versionName = null;
		PackageManager packageManager = mContext.getPackageManager();
		PackageInfo packageInfo = null;
		try {
			String packageName = mContext.getPackageName();
			packageInfo = packageManager.getPackageInfo(packageName, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		versionName = packageInfo.versionName;
		return versionName;
	}

	private synchronized String pullVersionFromServer(String address) {
		try {

			URL url = new URL(address);

			HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
			urlc.setConnectTimeout(10000); // set timeout for connecting
			urlc.setReadTimeout(30000); // set timeout for reading from the file
			urlc.connect();

			int responseCode = urlc.getResponseCode(); // check the response
														// code
			android.util.Log.i(TAG, "version fetch response code: "
					+ responseCode);

			String line;
			InputStream is = urlc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();

			try {
				// Read all the text returned by the server
				while ((line = br.readLine()) != null) {
					// one line of text; readLine() strips the newline
					// character(s)
					sb.append(line);
				}
			} finally {
				br.close();
				isr.close();
				is.close();
			}
			System.out.println(sb.toString());
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private void showUpdateDialog(final String mApkUrl) {
		String message = "";
		String descript = mContext.getResources().getString(R.string.update_description);
		String modified = mContext.getResources().getString(R.string.update_modified);
		message = descript;
		message = message + '\n' + '\n' + modified + "\n" +  mContext.getString(R.string.update_content);
		
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.update_version);
		builder.setMessage(message);
		builder.setPositiveButton(R.string.update_upgrade,
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// if (VERSION.SDK_INT >= 9) {
						// DownloadManagerTool();
						// }else{
						DownloadApk downloadApk = new DownloadApk(mApkUrl);
						downloadApk.execute();
						// }
					}
				});
		builder.setNegativeButton(R.string.cancel, new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// water if the user cancel the download we should remove the
				// newest information. Also we can update next time.
				return;
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	// Receiver
	private class DownloadCompleteReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
				installApk(mFile);
			} else {
				// water if the user cancel the download we should remove the
				// newest information. Also we can update next time.
			}
		}
	};

	private void installApk(File file) {
		if (file != null && file.toString().endsWith(".apk")) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
			mContext.startActivity(intent);
		} else {
			Message msg = new Message();
			msg.what = INSTALL_ERROR;
			mHandler.sendMessage(msg);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	// for sdk < 9

	private ProgressDialog mProgressDialog;

	private class DownloadApk extends AsyncTask<String, Integer, File> {
		private String mApkUrl = "";
		public DownloadApk(String mApkUrl) {
			this.mApkUrl = mApkUrl;
		}
		
		@Override
		protected void onPreExecute() {
			String title = mContext.getString(R.string.app_name);
			String message = mContext.getResources().getString(
					R.string.update_downloading_version);

			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setTitle(title);
			mProgressDialog.setMessage(message);
			mProgressDialog.setIcon(R.drawable.icon);
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setButton(mContext.getString(R.string.cancel),
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							mProgressDialog.dismiss();
						}
					});
			mProgressDialog.show();

			super.onPreExecute();
		}

		@Override
		protected File doInBackground(String... params) {
			try {
				mFile = getFileFromServer(mApkUrl, mProgressDialog);
			} catch (Exception e) {
				Message msg = new Message();
				msg.what = DOWNLOAD_ERROR;
				mHandler.sendMessage(msg);
			}
			return mFile;
		}

		@Override
		protected void onPostExecute(File result) {

			if (mProgressDialog.isShowing()) {
				installApk(result);
				mProgressDialog.dismiss();
			}

			super.onPostExecute(result);
		}
	}

	public File getFileFromServer(String path, ProgressDialog pd) {
		try {
			URL url = new URL(path);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			int Max = conn.getContentLength();
			pd.setMax(Max);

			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			String fileName = "";
			if (path != null && path.contains("/")) {
				fileName = path.substring(path.lastIndexOf("/"), path.length());
			}
			File file = new File(
					Environment.getExternalStoragePublicDirectory(mDownloadDir),
					fileName);
			FileOutputStream fos = new FileOutputStream(file);

			byte[] buffer = new byte[2048];
			int len;
			int total = 0;
			while ((len = bis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				total += len;
				pd.setProgress(total);
			}

			fos.close();
			bis.close();
			is.close();

			conn.disconnect();

			return file;
		} catch (Exception e) {
			Message msg = new Message();
			msg.what = DOWNLOAD_ERROR;
			mHandler.sendMessage(msg);
			return null;
		}
	}
}
