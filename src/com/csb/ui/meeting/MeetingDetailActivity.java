package com.csb.ui.meeting;

import java.util.Arrays;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csb.R;
import com.csb.bean.MeetingDetailBean;
import com.csb.bean.MeetingItemBean;
import com.csb.qrcode.CaptureActivity;
import com.csb.support.lib.MyAsyncTask;
import com.csb.ui.task.GetMeetingDetailAsyncTask;
import com.csb.ui.task.UpdateMeetingQuestionAsyncTask;
import com.csb.ui.task.UpdateMeetingStatusAsyncTask;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.GlobalContext;
import com.csb.utils.ToastUtils;
import com.csb.utils.Utility;

/**
 * 会议管理详细页面
 * 
 * @author bobo
 * 
 */
public class MeetingDetailActivity extends ListActivity implements
		android.view.View.OnClickListener {
	public static final int REGION_SELECT = 1;
	private TextView tv_top_title;
	private TextView tv_meeting_name;
	private TextView tv_meeting_time;
	private TextView tv_meeting_address;
	private TextView tv_meeting_spaker;
	private TextView tv_meeting_arrangement;
	// private TextView tv_meeting_question;
	private Button btn_title_left, btn_title_right;
	private Context context;
	private Button btn_baoming;
	private Button btn_qiandao;
	private Button btn_question;
	private LinearLayout ll_question;

	private MeetingDetailBean detailBean;
	private GetMeetingDetailAsyncTask getMeetingDetailAsyncTask = null;
	private UpdateMeetingQuestionAsyncTask updateMeetingQuestionAsyncTask = null;
	private UpdateMeetingStatusAsyncTask updateMeetingStatusAsyncTask = null;
	private MeetingItemBean meetingItemBean;

	private String[] questionContent = {};
	private EfficientAdapter adapter;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(BundleArgsConstants.MEETING_EXTRA,
				meetingItemBean);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			meetingItemBean = savedInstanceState
					.getParcelable(BundleArgsConstants.MEETING_EXTRA);
		} else {
			Intent intent = getIntent();
			meetingItemBean = intent
					.getParcelableExtra(BundleArgsConstants.MEETING_EXTRA);
		}
		setContentView(R.layout.meetiing_detail);
		context = this;
		initView();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (Utility.isTaskStopped(getMeetingDetailAsyncTask)) {
			getMeetingDetailAsyncTask = new GetMeetingDetailAsyncTask(
					GlobalContext.getInstance().getUserBean().getUserid(),
					meetingItemBean.getMeeting_id(), myHandler);
			getMeetingDetailAsyncTask
					.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		Utility.cancelTasks(getMeetingDetailAsyncTask,
				updateMeetingQuestionAsyncTask, updateMeetingStatusAsyncTask);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	private void initView() {
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("会议信息");

		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		btn_title_right.setText("资料下载");
		btn_title_right.setOnClickListener(this);

		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setText("");
		btn_title_left.setBackgroundDrawable(context.getResources()
				.getDrawable(R.drawable.back_selector));
		btn_title_left.setOnClickListener(this);

		btn_baoming = (Button) findViewById(R.id.btn_baoming);
		btn_baoming.setOnClickListener(this);
		btn_qiandao = (Button) findViewById(R.id.btn_qiandao);
		btn_qiandao.setOnClickListener(this);
		btn_question = (Button) findViewById(R.id.btn_question);
		btn_question.setOnClickListener(this);

		tv_meeting_name = (TextView) findViewById(R.id.tv_meeting_name);
		tv_meeting_time = (TextView) findViewById(R.id.tv_meeting_time);
		tv_meeting_address = (TextView) findViewById(R.id.tv_meeting_address);
		tv_meeting_spaker = (TextView) findViewById(R.id.tv_meeting_spaker);
		tv_meeting_arrangement = (TextView) findViewById(R.id.tv_meeting_arrangement);
		
//		if(meetingItemBean != null && "0".equals(meetingItemBean.getUser_status())){
//			btn_title_right.setVisibility(View.INVISIBLE);
//			btn_question.setVisibility(View.GONE);
//			getListView().setVisibility(View.GONE);
//		} else {
//			btn_title_right.setVisibility(View.VISIBLE);
//			btn_question.setVisibility(View.VISIBLE);
//			getListView().setVisibility(View.VISIBLE);
//		}
		// tv_meeting_question = (TextView)
		// findViewById(R.id.tv_meeting_question);

	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_title_left:
			MeetingDetailActivity.this.finish();
			break;
		case R.id.btn_title_right:
			intent = new Intent(context, MeetingDownloadListActivity.class);
			intent.putExtra(BundleArgsConstants.MEETING_EXTRA, meetingItemBean);
			startActivity(intent);
			break;
		case R.id.btn_baoming:
			confirmBaoming();
			break;
		case R.id.btn_qiandao:
			jumpToCapturActivity();
			break;
		case R.id.btn_question:
			doQuestion();
			break;
		}
	}

	private ProgressDialog dialog = null;
	private Dialog alertDialog = null;

	private void showDialog() {
		if(dialog != null  && dialog.isShowing()){
			dialog.dismiss();
		}
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

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case BundleArgsConstants.REQUEST_START:
				showDialog();
				break;
			case BundleArgsConstants.UPDATESTATUS_SUCC:
				dismissDialog();
				if (msg.obj != null) {
					try {
						int meeting_status = Integer
								.valueOf(msg.obj.toString());
						updateButtonStatus(meeting_status);
						meetingItemBean.setUser_status(msg.obj.toString());
						getIntent().putExtra(BundleArgsConstants.MEETING_EXTRA,
								meetingItemBean);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				break;
			case BundleArgsConstants.REQUEST_SUCC:
				dismissDialog();
				if (msg.obj != null) {
					MeetingDetailBean bean = (MeetingDetailBean) msg.obj;
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

	protected void onSuccess(MeetingDetailBean bean) {
		if (bean == null)
			return;
		tv_meeting_name.setText(bean.getMeeting_name());

		tv_meeting_time.setText(bean.getMeeting_time());
		tv_meeting_address.setText(bean.getMeeting_address());
		tv_meeting_spaker.setText(bean.getMeeting_speaker());
		String arrangement = bean.getMeeting_arrangement();
		if (arrangement != null) {
			arrangement = arrangement.replace(",", "\n");
		}
		tv_meeting_arrangement.setText(arrangement);

		if (bean.getQuestionBean() != null
				&& bean.getQuestionBean().getQuestion_content() != null) {
			questionContent = bean.getQuestionBean().getQuestion_content();
			adapter = new EfficientAdapter(context);
			setListAdapter(adapter);
		}

		String status = meetingItemBean.getUser_status();
		int userStatus = -1;
		int meetingStatus = -1;
		try {
			userStatus = Integer.valueOf(status);
			meetingStatus = Integer
					.valueOf(meetingItemBean.getMeeting_status());
		} catch (Exception e) {
			e.printStackTrace();
		}
		updateButtonStatus(userStatus);

		if (userStatus == 4) {
			// 会后调查问卷提醒
			if(alertDialog!=null && alertDialog.isShowing()) {
				alertDialog.dismiss();
			}
			alertDialog = new AlertDialog.Builder(this)
					.setMessage("您还有一个会后问卷需要填写.")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									jumpToSurveyActivity(SURVEY_CODE_END);
								}
							}).create();
			alertDialog.show();
		}
	}

	private class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);

		}

		/**
		 * The number of items in the list is determined by the number of
		 * speeches in our array.
		 * 
		 * @see android.widget.ListAdapter#getCount()
		 */
		public int getCount() {
			return questionContent.length;
		}

		/**
		 * Since the data comes from an array, just returning the index is
		 * sufficent to get at the data. If we were using a more complex data
		 * structure, we would return whatever object represents one row in the
		 * list.
		 * 
		 * @see android.widget.ListAdapter#getItem(int)
		 */
		public Object getItem(int position) {
			return position;
		}

		/**
		 * Use the array index as a unique id.
		 * 
		 * @see android.widget.ListAdapter#getItemId(int)
		 */
		public long getItemId(int position) {
			return position;
		}

		/**
		 * Make a view to hold each row.
		 * 
		 * @see android.widget.ListAdapter#getView(int, android.view.View,
		 *      android.view.ViewGroup)
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			// A ViewHolder keeps references to children views to avoid
			// unneccessary calls
			// to findViewById() on each row.
			ViewHolder holder;

			// When convertView is not null, we can reuse it directly, there is
			// no need
			// to reinflate it. We only inflate a new View when the convertView
			// supplied
			// by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.listview_item_questions, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.item_title_tv);
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			// Bind the data efficiently with the holder.
			holder.title.setText(questionContent[position]);

			return convertView;
		}

		class ViewHolder {
			TextView title;
		}
	}

	/**
	 * meeting_status 会议状态(5种 0:未接受邀请 1:已报名 2:已签到 3:会议结束，未填写会后问卷 4:已填写会后问卷5:会议结束)
	 * 
	 * @param userStatus
	 */
	private void updateButtonStatus(int userStatus) {
		Drawable drBaoming, drYinBaoming, drQiandao, drYinQiandao;
		
		btn_title_right.setVisibility(View.VISIBLE);
		btn_question.setVisibility(View.VISIBLE);
		getListView().setVisibility(View.VISIBLE);
		switch (userStatus) {
		case 0: //未接受邀请
			// btn_question.setEnabled(true);
			btn_baoming.setText("报名");
			btn_qiandao.setText("签到");

			btn_baoming.setTextColor(getResources().getColor(R.color.white));
			btn_qiandao.setTextColor(getResources().getColor(R.color.white));

			drBaoming = getResources().getDrawable(R.drawable.icon_baoming);
			drBaoming.setBounds(0, 0, drBaoming.getMinimumWidth(),
					drBaoming.getMinimumHeight());
			btn_baoming.setCompoundDrawables(drBaoming, null, null, null);

			drQiandao = getResources().getDrawable(R.drawable.icon_qiandao);
			drQiandao.setBounds(0, 0, drQiandao.getMinimumWidth(),
					drQiandao.getMinimumHeight());
			btn_qiandao.setCompoundDrawables(drQiandao, null, null, null);
			
			btn_title_right.setVisibility(View.INVISIBLE);
			btn_question.setVisibility(View.GONE);
			getListView().setVisibility(View.GONE);

			btn_baoming.setEnabled(true);
			btn_qiandao.setEnabled(false);
			break;
		case 1: //已报名
			// btn_question.setEnabled(true);

			btn_baoming.setText("已报名");
			btn_qiandao.setText("签到");

			btn_baoming.setTextColor(getResources().getColor(
					R.color.orange_drak));
			btn_qiandao.setTextColor(getResources().getColor(R.color.white));

			drYinBaoming = getResources()
					.getDrawable(R.drawable.icon_yibaoming);
			drYinBaoming.setBounds(0, 0, drYinBaoming.getMinimumWidth(),
					drYinBaoming.getMinimumHeight());
			btn_baoming.setCompoundDrawables(drYinBaoming, null, null, null);

			drQiandao = getResources().getDrawable(R.drawable.icon_qiandao);
			drQiandao.setBounds(0, 0, drQiandao.getMinimumWidth(),
					drQiandao.getMinimumHeight());
			btn_qiandao.setCompoundDrawables(drQiandao, null, null, null);
			
			btn_title_right.setVisibility(View.INVISIBLE);
			btn_question.setVisibility(View.GONE);
			getListView().setVisibility(View.GONE);

			btn_baoming.setEnabled(false);
			btn_qiandao.setEnabled(true);
			break;
		case 2: //已签到但未填写会前问卷
			// btn_question.setEnabled(true);
			btn_baoming.setText("已报名");
			btn_qiandao.setText("已签到");

			btn_baoming.setTextColor(getResources().getColor(
					R.color.orange_drak));
			btn_qiandao.setTextColor(getResources().getColor(
					R.color.orange_drak));

			drYinBaoming = getResources()
					.getDrawable(R.drawable.icon_yibaoming);
			drYinBaoming.setBounds(0, 0, drYinBaoming.getMinimumWidth(),
					drYinBaoming.getMinimumHeight());
			btn_baoming.setCompoundDrawables(drYinBaoming, null, null, null);

			drYinQiandao = getResources()
					.getDrawable(R.drawable.icon_yiqiandao);
			drYinQiandao.setBounds(0, 0, drYinQiandao.getMinimumWidth(),
					drYinQiandao.getMinimumHeight());
			btn_qiandao.setCompoundDrawables(drYinQiandao, null, null, null);

			btn_baoming.setEnabled(false);
			btn_qiandao.setEnabled(false);
			
			if(alertDialog!=null && alertDialog.isShowing()) {
				alertDialog.dismiss();
			}
			alertDialog = new AlertDialog.Builder(this)
			.setMessage("您有一个会前问卷需要填写.")
			.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss();
							jumpToSurveyActivity(SURVEY_CODE_START);
						}

					}).create();
			alertDialog.show();
			break;
		case 3://已签到已填写会前问卷
		case 4://会议结束但未填写会后问卷
		case 5://会议结束已填写会后问卷
			btn_baoming.setText("已报名");
			btn_qiandao.setText("已签到");

			btn_baoming.setTextColor(getResources().getColor(
					R.color.orange_drak));
			btn_qiandao.setTextColor(getResources().getColor(
					R.color.orange_drak));

			drYinBaoming = getResources()
					.getDrawable(R.drawable.icon_yibaoming);
			drYinBaoming.setBounds(0, 0, drYinBaoming.getMinimumWidth(),
					drYinBaoming.getMinimumHeight());
			btn_baoming.setCompoundDrawables(drYinBaoming, null, null, null);

			drYinQiandao = getResources()
					.getDrawable(R.drawable.icon_yiqiandao);
			drYinQiandao.setBounds(0, 0, drYinQiandao.getMinimumWidth(),
					drYinQiandao.getMinimumHeight());
			btn_qiandao.setCompoundDrawables(drYinQiandao, null, null, null);

			btn_qiandao.setEnabled(false);
			btn_baoming.setEnabled(false);
			break;
		default:
			btn_baoming.setText("报名");
			btn_qiandao.setText("签到");

			btn_baoming.setTextColor(getResources().getColor(R.color.white));
			btn_qiandao.setTextColor(getResources().getColor(R.color.white));

			drYinBaoming = getResources()
					.getDrawable(R.drawable.icon_yibaoming);
			drYinBaoming.setBounds(0, 0, drYinBaoming.getMinimumWidth(),
					drYinBaoming.getMinimumHeight());
			btn_baoming.setCompoundDrawables(drYinBaoming, null, null, null);

			drYinQiandao = getResources()
					.getDrawable(R.drawable.icon_yiqiandao);
			drYinQiandao.setBounds(0, 0, drYinQiandao.getMinimumWidth(),
					drYinQiandao.getMinimumHeight());
			btn_qiandao.setCompoundDrawables(drYinQiandao, null, null, null);

			btn_qiandao.setEnabled(false);
			btn_baoming.setEnabled(false);
			ToastUtils.show(this, "会议状态异常！");
		}
	}

	private void confirmBaoming() {
		if(alertDialog!=null && alertDialog.isShowing()) {
			alertDialog.dismiss();
		}
		alertDialog = new AlertDialog.Builder(this).setMessage("您确定要报名吗?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						doBaoming();
					}

				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}

				}).create();
		alertDialog.show();
	}


	private void jumpToCapturActivity() {
		Intent intent = new Intent(context, CaptureActivity.class);
		intent.putExtra(BundleArgsConstants.MEETING_EXTRA, meetingItemBean);
		startActivityForResult(intent, QRCODE_CODE);
	}

	private void jumpToSurveyActivity(int requestCode) {
		Intent intent = new Intent(context, SurveyActivity.class);
		intent.putExtra(BundleArgsConstants.SURVEY_TYPE, requestCode - 1);
		intent.putExtra(BundleArgsConstants.MEETING_EXTRA, meetingItemBean);
		startActivityForResult(intent, requestCode);
	}

	private void doBaoming() {
		updateStatus("1");
	}

	private void doQiandaoWithoutAnswer() {
		updateStatus("2");
	}

	private void doQiandaoWithAnswer() {
		updateStatus("3");
	}

	private void doFinishWithoutAnswer() {
		updateStatus("4");
	}
	
	private void doFinishWithAnswer() {
		updateStatus("5");
	}

	private void updateStatus(String status) {
		if (Utility.isTaskStopped(updateMeetingStatusAsyncTask)) {
			updateMeetingStatusAsyncTask = new UpdateMeetingStatusAsyncTask(
					GlobalContext.getInstance().getUserBean().getUserid(),
					meetingItemBean.getMeeting_id(), status, myHandler);
			updateMeetingStatusAsyncTask
					.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
		}
	}

	private static String[] insertStr(String[] arr, String str) {
		int size = arr.length;
		String[] tmp = new String[size + 1];
		System.arraycopy(arr, 0, tmp, 1, size);
		tmp[0] = str;
		return tmp;
	}

	private void doQuestion() {
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(
				R.layout.alert_dialog_text_question, null);
		if(alertDialog!=null && alertDialog.isShowing()) {
			alertDialog.dismiss();
		}
		alertDialog = new AlertDialog.Builder(MeetingDetailActivity.this)
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
									questionContent = insertStr(questionContent, question);
									adapter.notifyDataSetChanged();
									if (Utility
											.isTaskStopped(updateMeetingQuestionAsyncTask)) {
										updateMeetingQuestionAsyncTask = new UpdateMeetingQuestionAsyncTask(
												GlobalContext.getInstance()
														.getUserBean()
														.getUserid(),
												meetingItemBean.getMeeting_id(),
												question, myHandler);
										updateMeetingQuestionAsyncTask
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
		alertDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// You can use the requestCode to select between multiple child
		// activities you may have started. Here there is only one thing
		// we launch.
		if (requestCode == QRCODE_CODE) {
			// This is a standard resultCode that is sent back if the
			// activity doesn't supply an explicit result. It will also
			// be returned if the activity failed to launch.
			if (resultCode == RESULT_CANCELED) {
				ToastUtils.show(context, "签到已经取消!");
				// Our protocol with the sending activity is that it will send
				// text in 'data' as its result.
			} else {
				doQiandaoWithoutAnswer();
			}
		} else if (requestCode == SURVEY_CODE_START) {
			if (resultCode == RESULT_CANCELED) {
				ToastUtils.show(context, "会前问卷已经取消!");
				// Our protocol with the sending activity is that it will send
				// text in 'data' as its result.
			} else {
				doQiandaoWithAnswer();
			}
		} else if (requestCode == SURVEY_CODE_END) {
			if (resultCode == RESULT_CANCELED) {
				ToastUtils.show(context, "会后问卷已经取消!");
				// Our protocol with the sending activity is that it will send
				// text in 'data' as its result.
			} else {
				// Dialog dialog = new
				// AlertDialog.Builder(this).setMessage("恭喜您,会后问卷提交成功.")
				// .setPositiveButton("确定", new
				// DialogInterface.OnClickListener() {
				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// dialog.dismiss();
				// }
				//
				// })
				// .create();
				// dialog.show();

				doFinishWithAnswer();
				ToastUtils.show(context, "恭喜您,会后问卷提交成功.");
			}
		}
	}

	// Definition of the one requestCode we use for receiving resuls.
	static final private int QRCODE_CODE = 0;
	static final private int SURVEY_CODE_START = 1;
	static final private int SURVEY_CODE_END = 2;

}
