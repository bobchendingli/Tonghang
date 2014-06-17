package com.csb.ui.meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.csb.R;
import com.csb.bean.MeetingDetailBean;
import com.csb.bean.MeetingItemBean;
import com.csb.bean.QuestionnaireBean;
import com.csb.bean.QuestionnaireListBean;
import com.csb.bean.Survey;
import com.csb.bean.SurveyAnswer;
import com.csb.bean.SurveyQuestion;
import com.csb.support.lib.MyAsyncTask;
import com.csb.ui.task.GetMeetingDetailAsyncTask;
import com.csb.ui.task.GetMeetingQuestionnaireAsyncTask;
import com.csb.ui.task.UpdateMeetingQuestionnaireAsyncTask;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.GlobalContext;
import com.csb.utils.ToastUtils;
import com.csb.utils.Utility;

/**
 * 
 */
public class SurveyActivity extends Activity implements OnItemClickListener, OnClickListener, TextWatcher {
	private static final String TAG = "SurveyActivity";
	public static final String ACTION = "com.amanzitel.geoptima.SURVEY";
	public static final String SURVEYID = "com.bold.geoptima.SURVEYID";
	public static final String SURVEYSOURCE = "com.bold.geoptima.SURVEYSOURCE";
	public static final String SURVEY_PAGE_INDEX = "com.bold.geoptima.SURVEYPAGEINDEX";
    public static final String IS_NOTIFICATION_SURVEY="is_notification_survey";
	public static final int MSG_ID_NEXT_PAGE = 100;
	public static final int MSG_ID_SURVEY_END = 101;
	public static final int MSG_ID_SURVEY_CANCEL = 102;

	public static final int REQUEST_CODE_SURVEY_COMMENTS = 1000;
	public static final int RESULT_FINISH = 1001;

	private TextView surveyTitle, surveyQuestion, surveyLabel;
	private ListView listView;
	private Survey survey;
	private SurveyAnwserAdapter adapter;
	private ImageView ivBack;
	private ImageView ivCancel;
	private ProgressBar progress;
	private View footerView;
	private PopupWindow popup;
	private long clickIntervalTime = 0;
	
	private Button skipButton;
	private SurveyAnswer selectedSurveyAnswer;
	private ConnectivityManager connectivityManager;
	private boolean isNotificationSurvey;
	
	private Context context;
	private int  surveyType = 1;
	private GetMeetingQuestionnaireAsyncTask getMeetingQuestionnaireAsyncTask;
	private UpdateMeetingQuestionnaireAsyncTask updateMeetingQuestionnaireAsyncTask;
	private MeetingItemBean meetingItemBean;
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(BundleArgsConstants.SURVEY_TYPE,
				surveyType);
		outState.putParcelable(BundleArgsConstants.MEETING_EXTRA,
				meetingItemBean);
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		if (savedInstanceState != null) {
			surveyType = savedInstanceState
					.getInt(BundleArgsConstants.SURVEY_TYPE);
		} else {
			Intent intent = getIntent();
			surveyType = intent.getIntExtra(BundleArgsConstants.SURVEY_TYPE, 1);
		}
		
		if (savedInstanceState != null) {
			meetingItemBean = savedInstanceState
					.getParcelable(BundleArgsConstants.MEETING_EXTRA);
		} else {
			Intent intent = getIntent();
			meetingItemBean = intent
					.getParcelableExtra(BundleArgsConstants.MEETING_EXTRA);
		}
		
		setContentView(R.layout.survey);
		surveyTitle = (TextView) findViewById(R.id.survey_title);
		surveyQuestion = (TextView) findViewById(R.id.survey_question);
		surveyLabel = (TextView) findViewById(R.id.survey_label);
		listView = (ListView) findViewById(R.id.radio_answers);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		ivCancel = (ImageView) findViewById(R.id.ivCancel);
		progress = (ProgressBar) findViewById(R.id.pbProgress);

		skipButton = (Button) findViewById(R.id.skip);
		
		ivBack.setOnClickListener(this);
		ivCancel.setOnClickListener(this);
		skipButton.setOnClickListener(this);
		ivBack.setOnTouchListener(otl_viewPressed);
		ivCancel.setOnTouchListener(otl_viewPressed);

		ivBack.setVisibility(View.GONE);
		connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		
		
		Intent intent = getIntent();
		int surveyId = intent.getIntExtra(SURVEYID, -1);
		isNotificationSurvey =intent.getBooleanExtra(IS_NOTIFICATION_SURVEY, false);
		String surveySource = intent.getStringExtra(SURVEYSOURCE); 
		surveySource = surveySource == null ? "" : surveySource;// prevent null
//		survey = lookupSurvey(surveyId);
//
//		surveyTitle.setText(survey.getName());
//		Log.d(TAG, String.format(Locale.ENGLISH, "startup the survey of the [%s]", surveyId));
//		adapter = new SurveyAnwserAdapter(this, survey, surveySource);
//		restAdapater();
		listView.setOnItemClickListener(this);
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		if (Utility.isTaskStopped(getMeetingQuestionnaireAsyncTask)) {
			getMeetingQuestionnaireAsyncTask = new GetMeetingQuestionnaireAsyncTask(
					GlobalContext.getInstance().getUserBean().getUserid(),
					meetingItemBean.getMeeting_id(),
					String.valueOf(surveyType), 
					myHandler);
			getMeetingQuestionnaireAsyncTask
					.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Utility.cancelTasks(getMeetingQuestionnaireAsyncTask, updateMeetingQuestionnaireAsyncTask);
	}
	
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
//				if (msg.obj != null) {
//					try {
//						int meeting_status = Integer.valueOf(msg.obj.toString());
//						updateButtonStatus(meeting_status);
//						meetingItemBean.setUser_status(msg.obj.toString());
//						getIntent().putExtra(BundleArgsConstants.MEETING_EXTRA, meetingItemBean);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}

				break;
			case BundleArgsConstants.REQUEST_SUCC:
				dismissDialog();
				if (msg.obj != null) {
					QuestionnaireListBean bean = (QuestionnaireListBean) msg.obj;
					survey = lookupSurveyByBean(bean);
					if(survey == null) {
						ToastUtils.show(context, "没有调查问卷!");
						SurveyActivity.this.setResult(RESULT_OK, new Intent().setAction(""));
						SurveyActivity.this.finish();
						break;
					}
					surveyTitle.setText(survey.getName());
					adapter = new SurveyAnwserAdapter(context, survey, "");
					restAdapater();
//					onSuccess(bean);
				}

				break;
			case BundleArgsConstants.UPDATEQUESTIONNAIRE_SUCC:
				dismissDialog();
				SurveyActivity.this.setResult(RESULT_OK, new Intent().setAction(""));
				SurveyActivity.this.finish();
				break;
			case BundleArgsConstants.REQUEST_FAIL:
//				if (msg.obj != null)
//					ToastUtils.show(context, msg.obj.toString());
				dismissDialog();
				ivBack.setVisibility(View.VISIBLE);
				progress.setVisibility(View.GONE);
				listView.setEnabled(true);
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivCancel:
			cancelSurvey();
			break;
		case R.id.ivBack:
			if (adapter != null) {
				adapter.previous();
				if(adapter.getStepIndex()==0){
					ivBack.setVisibility(View.GONE);
				}else{
					ivBack.setVisibility(View.VISIBLE);
				}
				restAdapater();
			}
			break;
		case R.id.skip:
			if (adapter != null) {
				adapter.collect(Integer.parseInt(survey.getSkipValue()));
				if(isEnd()){
					return;
				}
				handler.sendEmptyMessageDelayed(MSG_ID_NEXT_PAGE, 300);
			}
			finish();
			break;
		case R.id.btSaveComment:
			View view = null;
			view = getPopupWindow().getContentView();
			EditText editor = ((EditText) view.findViewById(R.id.answer_comments));
			String freeText = editor.getText().toString();
			Button btSave = (Button) getFooterView().findViewById(R.id.btSaveComment);
			btSave.setEnabled(false);
			if (getPopupWindow().isShowing()) {
				getPopupWindow().dismiss();
			}
			break;
		case R.id.btCancelInput:
			EditText et = ((EditText) getFooterView().findViewById(R.id.answer_comments));
			et.setText("");
			Button bt = (Button) getFooterView().findViewById(R.id.btSaveComment);
			bt.setEnabled(false);
			if (getPopupWindow().isShowing()) {
				getPopupWindow().dismiss();
			}
			adapter.reject();
			restAdapater();
			break;
		default:
			break;
		}
	}

	// The free text change
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		View view = getPopupWindow().getContentView();
		//view.findViewById(R.id.btSaveComment).setEnabled(s.length() > 0);
	}

	@Override
	public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
		// Log.e(TAG, "call in the onItemClick, pos=" + pos + ":id=" + id);
		listView.setEnabled(false);
		if (System.currentTimeMillis() - clickIntervalTime < 200) {
			return;
		}
		clickIntervalTime = System.currentTimeMillis();

		// get current the answer
		SurveyAnswer sa = (SurveyAnswer) view.getTag();
		selectedSurveyAnswer = sa;
		
		if (isDenied(sa)) {
			listView.setEnabled(true);
			cancelSurvey();
			return;
		}
		
		// collect survey result.
		adapter.collect(sa.getId());
		adapter.collectResults(selectedSurveyAnswer.getLabel());
		
		// reset icon res_id for list item
		resetIcons(list, view);

		if (sa.isComments()) {
			showPopupWindow(view);
			return;
		}
		
		//
		hiddenButtons();

		// check is last page
		if(isEnd()){
			return;
		}
		if (!hasNext(sa)) {
			handler.sendEmptyMessageDelayed(MSG_ID_SURVEY_END, 100);
			return;
		}
		

		// automation got next page.
		handler.sendEmptyMessageDelayed(MSG_ID_NEXT_PAGE, 300);
	}

	private boolean hasNext(SurveyAnswer sa){
		return adapter.getQuestion().getNextQuestionId(sa.getId()) == -1 ? false : true;
	}
	
	private void hiddenButtons(){
		ivBack.setVisibility(View.GONE);
		progress.setVisibility(View.VISIBLE);
	}
	
	private boolean isEnd(){
		if (adapter.isEnd()) {
			handler.sendEmptyMessageDelayed(MSG_ID_SURVEY_END, 100);
			return true;
		}
		return false;
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_ID_NEXT_PAGE:
				// automation got next page.
				ivBack.setVisibility(View.VISIBLE);
				progress.setVisibility(View.GONE);
				adapter.next(selectedSurveyAnswer.getId());
				restAdapater();
				listView.setEnabled(true);
				break;
			case MSG_ID_SURVEY_END:
				if (Utility.isTaskStopped(updateMeetingQuestionnaireAsyncTask)) {
					updateMeetingQuestionnaireAsyncTask = new UpdateMeetingQuestionnaireAsyncTask(
							GlobalContext.getInstance().getUserBean().getUserid(),
							meetingItemBean.getMeeting_id(),
							adapter.getAnswerList(), 
							myHandler);
					updateMeetingQuestionnaireAsyncTask
							.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
				}
//				finish();
				// save survey result to JSON file.
//				saveSurveyResult(Utility.EVENT_TYPE_SURVEY_ANSWERS, adapter.record(), true);
				break;
			case MSG_ID_SURVEY_CANCEL:
				progress.setVisibility(View.VISIBLE);
				ivBack.setVisibility(View.GONE);
				setResult(RESULT_CANCELED);
				finish();
				
				break;
			default:
				ivBack.setVisibility(View.VISIBLE);
				progress.setVisibility(View.GONE);
				super.handleMessage(msg);
			}
		}
	};

	

	//
	private void cancelSurvey() {
		// when no survey was found in the configuration
		// no need to close message and prevent a null pointer crash
		if (adapter != null) {
			handler.sendEmptyMessageDelayed(MSG_ID_SURVEY_CANCEL, 600);
//			String msg = getString(R.string.survey_cancel, adapter.getSurvey().getName());
			
		}
	}

	private void restAdapater() {
		listView.setAdapter(adapter);

		int total = adapter.getSurvey().getQuestions().size();
	    String title = String.format(Locale.getDefault(), "%s (%.0f/%.0f)", adapter.getSurvey().getName(), (float)(adapter.getStepIndex() + 1), (float)total);
		surveyTitle.setText(title);

		surveyQuestion.setText( adapter.getQuestion().getText());
		if (!adapter.getQuestion().hasLabel()) {
			surveyLabel.setVisibility(View.GONE);
		} else {
			surveyLabel.setVisibility(View.VISIBLE);
			surveyLabel.setText(adapter.getQuestion().getLabel());
		}
	}

	private boolean isDenied(SurveyAnswer sa) {
		// get the continue Id
		SurveyQuestion question = adapter.getQuestion();
		int continueId = question.getContinueId();
		
		// check if continueId is set
		if(continueId < 0){
			return false;
		}
		// apply continue rule
		int selectedId = sa.getId();
		return selectedId != continueId;
	}

	private OnTouchListener otl_viewPressed = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int action = event.getAction();
			if (action == KeyEvent.ACTION_DOWN) {// darken on press
				((ImageView) v).setColorFilter(0xFF888888, PorterDuff.Mode.MULTIPLY);
			} else if (action == KeyEvent.ACTION_UP) {
				// returns to normal on release
				((ImageView) v).clearColorFilter();
			}
			// does not prevents the event from being consumed by
			// other listeners
			return false;
		}
	};

	private Survey lookupSurveyByBean(QuestionnaireListBean bean) {
		if(bean.getQuestion_content() == null || bean.getQuestion_content().size() <=0) return null;
		Survey s = new Survey();
		s.setId(9);
		s.setName("调查问卷");
		s.setShowAfter(Arrays.asList(new String[] {"customer-service"}));
		s.setKey("调查问卷");
		s.setLabel("");
		s.setSkipValue("");
		
		List<QuestionnaireBean> questionList = bean.getQuestion_content();
		int len = 0;
		List<SurveyQuestion> sqList = new ArrayList<SurveyQuestion>();
		List<SurveyAnswer> answerList = new ArrayList<SurveyAnswer>();
		for (int i = 0; i < questionList.size(); i++) {
			QuestionnaireBean questionnaireBean = questionList.get(i);
			SurveyQuestion sq = new SurveyQuestion();
			sq.setId(questionnaireBean.getQuestion_id());
			sq.setName("experience");
			sq.setText(questionnaireBean.getQuestion_content());
			String[] questionAnswers = questionnaireBean.getQuestion_selection();
			Integer[] answer = new Integer[questionAnswers.length];
			int j = 0;
			for (String answerContent : questionAnswers) {
				SurveyAnswer sa = new SurveyAnswer();
				sa.setId(len);
				sa.setLabel(String.valueOf(j + 1));
				answer[j++] = len;
				sa.setValue(answerContent);
				len ++;
				answerList.add(sa);
			}
			sq.setAnswerIds(Arrays.asList(answer));
			sqList.add(sq);
		}
		s.setAnswers(answerList);
		s.setQuestions(sqList);
		
		return s;
	}

	private Survey lookupSurvey(int surveyId) {
//		DeviceConfig conf = SharedObjects.getGeoptima().getDeviceConfig();
//		for (Survey survey : conf.getSurveys()) {
//			if (survey.getId() == surveyId) {
//				// Log.e(TAG,"found survey:[" + survey.getId() + "]");
//				// Survey.printSurveyDetail(survey);
//				return survey;
//			}
//		}
//		Log.e(TAG, "not found survey by id:" + surveyId);
//		Survey.printSurveysDetail(conf.getSurveys());
		Survey s = new Survey();
		s.setId(9);
		s.setName("调查问卷");
		String[] sa = {"customer-service"};
		s.setShowAfter(Arrays.asList(sa));
		s.setKey("squeak a leak");
		s.setLabel("");
		s.setSkipValue("");
		SurveyAnswer a1 = new SurveyAnswer();
		a1.setId(1);
		a1.setValue("1 - Very dissatisfied");
		SurveyAnswer a2 = new SurveyAnswer();
		a2.setId(2);
		a2.setValue("2 - Dissatisfied");
		SurveyAnswer a3 = new SurveyAnswer();
		a3.setId(3);
		a3.setValue("3 - Slightly dissatisfied");
		SurveyAnswer a4 = new SurveyAnswer();
		a4.setId(4);
		a4.setValue("4 - Neutral");
		
		SurveyAnswer a5 = new SurveyAnswer();
		a5.setId(5);
		a5.setValue("5 - Unable to connect datas");
		
		SurveyAnswer a6 = new SurveyAnswer();
		a6.setId(6);
		a6.setValue("6 - No service");
		
		List al = new ArrayList();
		al.add(a1);
		al.add(a2);
		al.add(a3);
		al.add(a4);
		al.add(a5);
		al.add(a6);
		
		s.setAnswers(al);
		
		SurveyQuestion sq = new SurveyQuestion();
		sq.setId(2);
		sq.setName("experience");
		sq.setText("Overall, how satisfied are you with your experience using your mobile phone just now?");
		sq.setAnswerIds(Arrays.asList(new Integer[]{1,2,4,5,6}));
		
		SurveyQuestion sq1 = new SurveyQuestion();
		sq1.setId(3);
		sq1.setName("experience");
		sq1.setText("Overall, how satisfied are you with your experience using your mobile phone just now?");
		sq1.setAnswerIds(Arrays.asList(new Integer[]{2,3,4,5,6,1}));
		List ql =new ArrayList();
		ql.add(sq);
		ql.add(sq1);
		s.setQuestions(ql);
		return s;
	}

	private View getFooterView() {
		if (footerView == null) {
			footerView = LayoutInflater.from(this).inflate(R.layout.survey_comment, null);
			footerView.findViewById(R.id.btSaveComment).setOnClickListener(this);
			footerView.findViewById(R.id.btCancelInput).setOnClickListener(this);
			((EditText) footerView.findViewById(R.id.answer_comments)).addTextChangedListener(this);
		}
		
		return footerView;
	}
	
	private PopupWindow getPopupWindow() {
		if (popup == null) {
			popup = new PopupWindow(this);
			popup.setWidth(WindowManager.LayoutParams.FILL_PARENT);
			popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
			popup.setTouchable(true);
			popup.setFocusable(true);
			popup.setOutsideTouchable(true);

			View view = LayoutInflater.from(this).inflate(R.layout.survey_comments, null);
			//bind event processor
			view.findViewById(R.id.btSaveComment).setOnClickListener(this);
			view.findViewById(R.id.btCancelInput).setOnClickListener(this);
			((EditText) view.findViewById(R.id.answer_comments)).addTextChangedListener(this);
			
			popup.setContentView(view);			
		}
		return popup;
	}

	//
	private void showPopupWindow(View view) {
		// get position of the popupWindow
		int coord[] = new int[2];
		view.getLocationOnScreen(coord);

		// set offset for the x
		coord[1] += view.getHeight() + 5;

		PopupWindow popup = getPopupWindow();
		popup.showAtLocation(view, Gravity.LEFT | Gravity.TOP, coord[0], coord[1]);

		listView.setEnabled(true);
	}

	private void resetIcons(AdapterView<?> listView, View currentView) {
		for (int i = 0; i < listView.getChildCount(); i++) {
			View view = listView.getChildAt(i).findViewById(R.id.iconId);
			if (view != null) {
				view.setBackgroundResource(R.drawable.btn_radio_off);
			}
		}
		currentView.findViewById(R.id.iconId).setBackgroundResource(R.drawable.btn_radio_on);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void afterTextChanged(Editable s) {
	}
}
