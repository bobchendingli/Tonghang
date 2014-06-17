package com.csb.ui.meeting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import uk.co.senab.photoview.PhotoView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.csb.R;
import com.csb.bean.FileBean;
import com.csb.dao.URLHelper;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.Constants;
import com.csb.utils.ToastUtils;
import com.csb.utils.Utility;

public class MeetingDownloadDetailActivity extends Activity implements
		OnClickListener {
	private Button btn_title_left, btn_title_right;
	private TextView tv_top_title;
	private WebView webView;
	private Context context = null;
	private FileBean fileBean = null;
	private PhotoView picIv = null;
	private TextView textTv = null;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(BundleArgsConstants.FILEBEAN_EXTRA, fileBean);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			fileBean = savedInstanceState
					.getParcelable(BundleArgsConstants.FILEBEAN_EXTRA);
		} else {
			Intent intent = getIntent();
			fileBean = intent
					.getParcelableExtra(BundleArgsConstants.FILEBEAN_EXTRA);
		}

		setContentView(R.layout.meetiing_download_detail);
		context = this;
		initView();

		// 显示不同类型的文件
		if (fileBean == null || TextUtils.isEmpty(fileBean.getPicurl())) {
			ToastUtils.show(this, "资料内容为空,请重新上传文件！");
		} else {
			String fileName = fileBean.getPicurl();
			if (fileName != null && fileName.contains("/")) {
				fileName = fileName.substring(fileName.lastIndexOf("/") + 1,
						fileName.length());
			}
			final String extName = fileName.substring(
					fileName.lastIndexOf("."), fileName.length());
			final String path = Environment.getExternalStorageDirectory()
					.getPath()
					+ Constants.SD_FILE_MEETING
					+ File.separator
					+ fileName;
			if (fileName != null) {
				if (Constants.TXT.equalsIgnoreCase(extName)) {
					textTv.setVisibility(View.VISIBLE);
					textTv.setText(getString(path));
					picIv.setVisibility(View.GONE);
				} else if (Constants.PIC_LIST.contains(extName)) {
					textTv.setVisibility(View.GONE);
					picIv.setZoomable(true);
					picIv.setVisibility(View.VISIBLE);
					picIv.setImageURI(Uri.parse(path));
				}
			}
		}
	}

	public static String getString(String path) {
		InputStreamReader inputStreamReader = null;
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(path);
			inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		} catch (Exception e1) {
			e1.printStackTrace();
			if(inputStreamReader != null) {
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
		BufferedReader reader = new BufferedReader(inputStreamReader);
		StringBuffer sb = new StringBuffer("");
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(inputStreamReader!=null) {
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	private void initView() {
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("资料详情");
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setText("返回");
		btn_title_left.setOnClickListener(this);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		btn_title_right.setVisibility(View.INVISIBLE);

		textTv = (TextView) findViewById(R.id.textTv);
		picIv = (PhotoView) findViewById(R.id.picIv);

		// webView = (WebView) findViewById(R.id.webView);
		// webView.setWebViewClient(new WeiboWebViewClient());

		// WebSettings settings = webView.getSettings();
		// settings.setJavaScriptEnabled(true);
		// settings.setSaveFormData(false);
		// settings.setSavePassword(false);
		// settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		// settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
		// refresh();
	}

	public void refresh() {
		webView.clearView();
		webView.loadUrl("about:blank");
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ImageView iv = (ImageView) inflater.inflate(
				R.layout.refresh_action_view, null);

		Animation rotation = AnimationUtils.loadAnimation(this, R.anim.refresh);
		iv.startAnimation(rotation);

		webView.loadUrl(getWeiboOAuthUrl());
	}

	private String getWeiboOAuthUrl() {
		return URLHelper.URL_SERVER + fileBean.getPicurl();
	}

	private class WeiboWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// if (url.startsWith(URLHelper.DIRECT_URL)) {
			//
			// handleRedirectUrl(view, url);
			// view.stopLoading();
			// return;
			// }
			super.onPageStarted(view, url, favicon);

		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			// new SinaWeiboErrorDialog().show(getSupportFragmentManager(), "");
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			if (!url.equals("about:blank")) {
				// completeRefresh();
			}
		}
	}

	private void handleRedirectUrl(WebView view, String url) {
		Bundle values = Utility.parseUrl(url);

		String error = values.getString("error");
		String error_code = values.getString("error_code");

		Intent intent = new Intent();
		intent.putExtras(values);

		if (error == null && error_code == null) {

			String access_token = values.getString("access_token");
			String expires_time = values.getString("expires_in");
			setResult(RESULT_OK, intent);
			// new OAuthTask(this).execute(access_token, expires_time);
		} else {
			// Toast.makeText(OAuthActivity.this,
			// getString(R.string.you_cancel_login),
			// Toast.LENGTH_SHORT).show();
			finish();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_title_left:
			MeetingDownloadDetailActivity.this.finish();
			break;
		}
	}

}
