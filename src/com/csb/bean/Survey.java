package com.csb.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
 

public class Survey implements Parcelable {
	private int id;
	private String name;
	private String key;
	
	private String label;
	private String skipValue;
	
	private List<String> showAfter;
	private List<SurveyAnswer> answers;
	private List<SurveyQuestion> questions;

	private static final String TAG = "Survey";
	
	public Survey(){
		id = -1;
		name = "";
		key = "";
		label = "";
		skipValue = "";

		showAfter = new ArrayList<String>();
		answers = new ArrayList<SurveyAnswer>();
		questions = new ArrayList<SurveyQuestion>();
	}
	
	@SuppressWarnings("unchecked")
	private Survey(Parcel source) {
		id = source.readInt();
		name = source.readString();
		key = source.readString();
		label = source.readString();
		skipValue = source.readString();
		
		showAfter = source.readArrayList(String.class.getClassLoader());
		answers = source.readArrayList(SurveyAnswer.class.getClassLoader());
		questions = source.readArrayList(SurveyQuestion.class.getClassLoader());
	}

	/*public static Status parse(JSONArray jsonArray, List<Survey> surveys) throws JSONException {
		Status status = Status.OK;
		for (int index = 0; index < jsonArray.length(); index++) {
			Survey survey = new Survey();
			JSONObject json = jsonArray.getJSONObject(index);
			if (json.has("id")) {
				int id = json.getInt("id");
				survey.setId(id);
			}
			if (json.has("name")) {
				String name = json.getString("name");
				survey.setName(name);
			}
			if (json.has("key")) {
				String key = json.getString("key");
				survey.setKey(key);
			}
			if (json.has("label")) {
				String label = json.getString("label");
				survey.setLabel(label);
			}
			if(json.has("skip")){
				String skipValue = json.getString("skip");
				survey.setSkipValue(skipValue);
			}
			
			// parse the show afters
			if (json.has("show_after")) {
				JSONArray showAfter = new JSONArray();
				try {
					showAfter = json.getJSONArray("show_after");
				}catch(Exception e) {
					String strShowAfter = json.getString("show_after");
					showAfter.put(strShowAfter);
				}
				survey.setShowAfter(parseShowAfter(showAfter));
			}
			
			// parse the answers
			if (json.has("answers")) {
				JSONArray answers = json.getJSONArray("answers");
				survey.setAnswers(parseSurveyAnswer(answers));
			}
			if (json.has("answers-from")) {
				int surveyId = json.getInt("answers-from");
				List<SurveyAnswer> fromAnswers = parseSurveyAnswerFrom(surveys, surveyId);
				if (fromAnswers != null) {
					survey.getAnswers().addAll(fromAnswers);
				}
			} 
			if (json.has("questions")) {
				JSONArray questions = json.getJSONArray("questions");
				survey.setQuestions(parseSurveyQuestion(questions, survey, surveys));
			}
			addSurvey(surveys, survey); 
		} 
		return status;
	}*/

	public static void printSurveysDetail(List<Survey> surveys) {
		for (Survey s : surveys) {
			printSurveyDetail(s);
		}
	}

	public static void printSurveyDetail(Survey survey) {
		Log.d(TAG, survey.getId() + survey.getName() + survey.getShowAfter());
		for (SurveyAnswer a : survey.getAnswers()) {
			Log.d(TAG, "--->ANSWER:" + a.getValue() + ":" + a.getId());
		}
		for (SurveyQuestion q : survey.getQuestions()) {
			Log.d(TAG, "--->QUEST:" + q.getText() +"[nextQuestion:" + q.getNextQuestions() +"]:" + Arrays.toString(q.getAnswerIds().toArray()));
		}
	}

	// parse the entries from the tag 'show.aftr'
	private static List<String> parseShowAfter(JSONArray array) throws JSONException {
		List<String> showAfter = new ArrayList<String>(array.length());
		for (int i = 0; i < array.length(); i++) {
			String after = array.getString(i);
			showAfter.add(after);
		}
		return showAfter;
	}
	
	// parse the answers from the tag 'answers-from'
	private static List<SurveyAnswer> parseSurveyAnswerFrom(List<Survey> surveys, int surveyId) {
		List<SurveyAnswer> answers = null;
		for (Survey s : surveys) {
			if (s.getId() == surveyId) {
				answers = s.getAnswers();
				break;
			}
		}
		return answers;
	}

	// parse the answers from the tag 'answers'
	private static List<SurveyAnswer> parseSurveyAnswer(JSONArray array) throws JSONException {
		List<SurveyAnswer> answers = new ArrayList<SurveyAnswer>(array.length());
		for (int i = 0; i < array.length(); i++) {
			SurveyAnswer sa = new SurveyAnswer();
			JSONObject jsonAnswer = array.getJSONObject(i);
			if (jsonAnswer.has("id")) {
				sa.setId(jsonAnswer.getInt("id"));
			}
			if (jsonAnswer.has("value")) {
				sa.setValue(jsonAnswer.getString("value"));
			}
			if (jsonAnswer.has("comments")) {
				sa.setComments(jsonAnswer.getBoolean("comments"));
			} else {
				sa.setComments(false);
			}
			if (jsonAnswer.has("key")) {
				sa.setKey(jsonAnswer.getString("key"));
			}

			answers.add(sa);
		}
		return answers;
	}

	// parse the questions from the tag 'questions'
	private static List<SurveyQuestion> parseSurveyQuestion(JSONArray array, Survey survey, List<Survey> surveys)
	        throws JSONException {
		List<SurveyQuestion> questions = new ArrayList<SurveyQuestion>();
		for (int i = 0; i < array.length(); i++) {
			SurveyQuestion ques = new SurveyQuestion();
			JSONObject json = array.getJSONObject(i);
			if (json.has("id")) {
				ques.setId(json.getInt("id"));
			}
			if (json.has("name")) {
				ques.setName(json.getString("name"));
			}
			if (json.has("text")) {
				ques.setText(json.getString("text"));
			}
			if (json.has("label")) {
				ques.setLabel(json.getString("label"));
			}
			if (json.has("key")) {
				ques.setKey(json.getString("key"));
			}
			if (json.has("answers")) {
				JSONArray answers = json.getJSONArray("answers");
				ques.setAnswerIds(parseSurveyAnswerIds(answers, survey));
			}
			if (json.has("nextQuestion")) {
				parseNextQuestions(json.getJSONObject("nextQuestion"), ques);
			}

			if (json.has("question-from")) {
				JSONObject questionFrom = json.getJSONObject("question-from");
				int surveyId = questionFrom.getInt("survey");
				int questionId = questionFrom.getInt("question");
				boolean isOK = fillQuestionContent(surveys, surveyId, questionId, ques);
				if (!isOK) {
					ques = null;
				}
			}

			if (json.has("continue")) {
				ques.setContinueId(json.getInt("continue"));
			}

			if (ques != null) {
				questions.add(ques);
			}
		}
		return questions;
	}
	
	private static void parseNextQuestions(JSONObject json, SurveyQuestion question) {
		Iterator<?> answerIDs = json.keys();
		while (answerIDs.hasNext()) {
			String answerID = answerIDs.next().toString();
			try {
				int questionID = json.getInt(answerID);
				question.getNextQuestions().put(answerID, questionID);
			} catch (JSONException e) {
				Log.e(TAG, "The next question error please check your remote config file.");
			}
		}
	}

	//
	private static boolean fillQuestionContent(List<Survey> surveys, int surveyId, int questionId, SurveyQuestion hold) {
		Survey survey = null;
		for (Survey s : surveys) {
			if (s.getId() == surveyId) {
				survey = s;
				break;
			}
		}

		if (survey != null) {
			for (SurveyQuestion q : survey.getQuestions()) {
				if (q.getId() == questionId) {
					hold.setAnswerIds(q.getAnswerIds());
					hold.setLabel(q.getLabel());
					hold.setName(q.getName());
					hold.setText(q.getText());
				}
			}
			return true;
		} else {
			return false;
		}
	}

	// parse the answersId from the tag 'answers'
	private static List<Integer> parseSurveyAnswerIds(JSONArray array, Survey survey) throws JSONException {

		List<Integer> ids = new ArrayList<Integer>(array.length());
		for (int i = 0; i < array.length(); i++) {
			int id = array.getInt(i);
			ids.add(id);
			// fill the new Answer if the id not contained in the Survey.Answers
			fillSpecificAnswers(survey, id);
		}
		return ids;
	}

	// when the answer.id == answer.value we don't declare it to the answer
	// specification,but we'll automation create it
	private static void fillSpecificAnswers(Survey survey, int id) {
		for (SurveyAnswer sa : survey.getAnswers()) {
			if (sa.getId() == id) {
				return; // found
			}
		}

		//not found go create it
		SurveyAnswer sa = new SurveyAnswer();
		sa.setId(id);
//		sa.setValue(String.format(Locale.getDefault(), "%.0f", (float)id));
		survey.getAnswers().add(sa);
	}
	
	private static void addSurvey(List<Survey> surveys, Survey survey){
		for (int i = 0; i < surveys.size(); i++) {
			if(survey.getId() == surveys.get(i).getId()){
				surveys.set(i, survey);
				return;
			}
		}
		surveys.add(survey);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		if(name == null){
			name = getKey();
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getShowAfter() {
		return showAfter;
	}

	public void setShowAfter(List<String> showAfter) {
		this.showAfter = showAfter;
	}

	public List<SurveyQuestion> getQuestions() {
		if (questions == null) {
			questions = new ArrayList<SurveyQuestion>();
		}
		return questions;
	}

	public void setQuestions(List<SurveyQuestion> questions) {
		this.questions = questions;
	}

	public List<SurveyAnswer> getAnswers() {
		if (answers == null) {
			answers = new ArrayList<SurveyAnswer>();
		}
		return answers;
	}

	public void setAnswers(List<SurveyAnswer> answers) {
		this.answers = answers;
	}

	public String getKey() {
		if(key == null){
			key = String.valueOf(getId());
		}
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSkipValue() {
    	return skipValue;
    }

	public void setSkipValue(String skipValue) {
    	this.skipValue = skipValue;
    }

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(key);
		dest.writeString(label);
		dest.writeString(skipValue);
		dest.writeList(showAfter);
		dest.writeList(answers);
		dest.writeList(questions);
	}
	
	public static final Parcelable.Creator<Survey> CREATOR = new Parcelable.Creator<Survey>() {

		@Override
		public Survey createFromParcel(Parcel source) {
			return new Survey(source);
		}

		@Override
		public Survey[] newArray(int size) {
			return new Survey[size];
		}
		
	};
}
