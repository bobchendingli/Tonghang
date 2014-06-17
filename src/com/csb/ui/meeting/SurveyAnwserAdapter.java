package com.csb.ui.meeting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.csb.R;
import com.csb.bean.Survey;
import com.csb.bean.SurveyAnswer;
import com.csb.bean.SurveyQuestion;
import android.content.Context;

/**
 *
 */
class SurveyAnwserAdapter extends BaseAdapter {

	private static final String TAG = "SurveyAnwserAdapter";
	private Map<Integer, Integer> indexMapping = new HashMap<Integer, Integer>();
	private Context context;
	private int stepIndex;
	private Survey survey;
	private String source;
	private SurveyQuestion question;
	private List<SurveyAnswer> answers;
	private SparseIntArray results;
	private List<Map<String, String>> answerList = new ArrayList<Map<String, String>>();

	//
	public SurveyAnwserAdapter(Context context, Survey survey, String source) {
		this.context = context;
		results = new SparseIntArray(survey.getQuestions().size());
		stepIndex = 0;
		// end = false;
		this.survey = survey;
		this.source = source;
		question = survey.getQuestions().get(stepIndex);
		answers = listAnswers();
		indexMapping.put(stepIndex, question.getId());
	}

	public void collect(int answerId) {
		results.put(question.getId(), answerId);
	}
	
	public void collectResults(String answerValue) {
		boolean isUpdate = false;
		for (Map<String, String> item : answerList) {
			if(item.get("question_id").equals(String.valueOf(question.getId()))){
				isUpdate = true;
				item.put("answer_no", answerValue);
			} 
		}
		
		if(!isUpdate) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("question_id", String.valueOf(question.getId()));
			map.put("answer_no", answerValue);
			answerList.add(map);
		}
	}
	
	public List<Map<String, String>> getAnswerList() {
		return answerList;
	}

	public void previous() {
		if (stepIndex >= 1) {
			
			stepIndex = stepIndex - 1;
//			for (Map<String, String> map : answerList) {
//				if(map.get("question_id").equals(String.valueOf(indexMapping.get(stepIndex)))){
//					answerList.remove(map);
//				}
//			}
			// question = survey.getQuestions().get(stepIndex);
			question = getQuestionById(indexMapping.get(stepIndex));
			
			answers = listAnswers();
		}
	}

	public void next(int answerId) {
		if (stepIndex < survey.getQuestions().size()) {
			stepIndex = stepIndex + 1;
			// question = survey.getQuestions().get(stepIndex);
			question = getNextQuestion(answerId);
			answers = listAnswers();
		} else {
			Log.e(TAG, "The next fail.");
		}
	}

	public String record() {
		// "interface","surveyId","source","questions","answers","duration"
		String netType = "";
		int surveyId = survey.getId();

		String questions = "";
		for (int i = 0; i < results.size(); i++) {
			if (questions.length() > 0) {
				questions += ";";
			}
			questions += results.keyAt(i);
		}

		int answer;
		String answers = "";
		for (int i = 0; i < results.size(); i++) {
			if (answers.length() > 0) {
				answers += ";";
			}
			answer = results.valueAt(i);
			answers += answer;
		}

		String patten = "\"%s\",\"%d\",\"%s\",\"%s\",\"%s\",\"\"";
		return String.format(patten, netType, surveyId, source, questions, answers);
	}

	public void reject() {
		results.delete(question.getId());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// CrashReporter.printLog("call in the getView, pos=" + position + ":selected=" +
		// results.get(question.getId(), -1));
		SurveyAnswer answer = null;
		if (convertView == null) {
			int resId = R.layout.list_adapter_layout;
			convertView = LayoutInflater.from(context).inflate(resId, null);
			answer = answers.get(position);
			convertView.setTag(answer);
		} else {
			answer = (SurveyAnswer) convertView.getTag();
		}

		TextView text = (TextView) convertView.findViewById(R.id.textId);
		text.setText(answers.get(position).getValue());

		int answerID = results.get(question.getId(), -1);
		if (answerID == answer.getId()) {
			convertView.findViewById(R.id.iconId).setBackgroundResource(R.drawable.btn_radio_on);
		}
		return convertView;
	}

	@Override
	public int getCount() {
		return answers.size();
	}

	@Override
	public Object getItem(int position) {
		return answers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return answers.get(position).getId();
	}

	private List<SurveyAnswer> listAnswers() {
		List<SurveyAnswer> answers = new ArrayList<SurveyAnswer>(question.getAnswerIds().size());
		for (int id : question.getAnswerIds()) {
			for (SurveyAnswer answer : survey.getAnswers()) {
				if (answer.getId() == id) {
					answers.add(answer);
					break;
				}
			}
		}
		return answers;
	}

	private SurveyQuestion getNextQuestion(int answerId) {

		int nextQuestionId = question.getNextQuestionId(answerId);
		if (nextQuestionId <= 0) {
			if(stepIndex < survey.getQuestions().size()){
				question = survey.getQuestions().get(stepIndex);
				nextQuestionId = question.getId();
			}
		} else {
			question = getQuestionById(nextQuestionId);
		}

		indexMapping.put(stepIndex, nextQuestionId);

		return question;
	}

	private SurveyQuestion getQuestionById(int questionId) {
		for (SurveyQuestion sq : survey.getQuestions()) {
			if (sq.getId() == questionId) {
				return sq;
			}
		}
		return null;
	}

	public SurveyQuestion getQuestion() {
		return question;
	}

	public boolean isEnd() {
		return stepIndex >= (survey.getQuestions().size() - 1);
	}

	public int getStepIndex() {
		return stepIndex;
	}

	public Survey getSurvey() {
		return survey;
	}
}
