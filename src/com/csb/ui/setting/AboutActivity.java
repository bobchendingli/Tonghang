package com.csb.ui.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.csb.R;
import com.csb.support.http.VersionUpdateManager;
import com.csb.support.lib.MyAsyncTask;
import com.csb.ui.task.FeedbackAsyncTask;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.GlobalContext;
import com.csb.utils.ToastUtils;
import com.csb.utils.Utility;

/**
 * 关于
 * 
 * @author bobo
 * 
 */
public class AboutActivity extends Activity implements OnClickListener {
	private Button btn_title_left, btn_title_right, btn_response, btn_update;
	private TextView tv_top_title;
	private Context context = null;
	private FeedbackAsyncTask feedbackAsyncTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		context = this;
		initView();

		int autoUpdate = getIntent().getIntExtra("UPDATE_MODE_AUTO", 1);
		if (autoUpdate == VersionUpdateManager.UPDATE_MODE_AUTO) {
			VersionUpdateManager
					.setUpdateMode(VersionUpdateManager.UPDATE_MODE_MANUAL);
			VersionUpdateManager.createInstance(context);
		}
	}

	private void initView() {
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("关于");
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setBackgroundDrawable(context.getResources()
				.getDrawable(R.drawable.back_selector));
		btn_title_left.setText("");
		btn_title_left.setOnClickListener(this);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		btn_title_right.setVisibility(View.INVISIBLE);

		btn_update = (Button) findViewById(R.id.btn_update);
		btn_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				VersionUpdateManager
						.setUpdateMode(VersionUpdateManager.UPDATE_MODE_MANUAL);
				VersionUpdateManager.createInstance(context);
			}
		});
		btn_response = (Button) findViewById(R.id.btn_response);
		btn_response.setOnClickListener(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Utility.cancelTasks(feedbackAsyncTask);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_update:
			doUpdate();
			break;
		case R.id.btn_response:
			doResponse();
			break;
		case R.id.btn_title_left:
			AboutActivity.this.finish();
			break;
		}
	}

	private void doResponse() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(
				R.layout.alert_dialog_text_question, null);
		AlertDialog dialog = new AlertDialog.Builder(AboutActivity.this)
				// .setIconAttribute(android.R.attr.alertDialogIcon)
				// .setTitle("问题")
				.setView(textEntryView)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								EditText et_question = (EditText) textEntryView
										.findViewById(R.id.et_question_content);
								final String question = et_question.getText()
										.toString();
								if (TextUtils.isEmpty(question)) {
									ToastUtils.show(context, "内容不能为空!");
									return;
								} else {
									if (Utility
											.isTaskStopped(feedbackAsyncTask)) {
										feedbackAsyncTask = new FeedbackAsyncTask(
												GlobalContext.getInstance()
														.getUserBean()
														.getUserid(),
												question, myHandler);
										feedbackAsyncTask
												.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
									}
								}
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

								dialog.dismiss();
							}
						}).create();
		dialog.show();
	}

	private void doUpdate() {
		ToastUtils.show(context, "已经是最新版本");
	}
	
	
	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case BundleArgsConstants.REQUEST_START:
				showDialog();
				break;
			case BundleArgsConstants.REQUEST_SUCC:
				dismissDialog();
				break;
			case BundleArgsConstants.REQUEST_FAIL:
				dismissDialog();
			default:
				break;
			}
		}
	};
	
	private ProgressDialog dialog = null;

	private void showDialog() {
		dialog = new ProgressDialog(context);
		dialog.setMessage("数据处理中,请稍候...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.show();
	}

	private void dismissDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

}
