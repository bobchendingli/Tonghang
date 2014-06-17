package com.csb.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

public class SurveyQuestion implements Parcelable, Cloneable {
	private int id;
	private String name;
	private String text;
	
	private List<Integer> answers;
	
	private String label;
	private String key;
	private int continueId = -1;
	private HashMap<String, Integer> nextQuestions;
	
	private boolean isSelectedCommentsItem = false;
	private int selectedAnswerId = -1;
	private String enteredComment = "";
	
	public SurveyQuestion() {}
	
	@SuppressWarnings("unchecked")
	private SurveyQuestion(Parcel source) {
		id = source.readInt();
		name = source.readString();
		text = source.readString();
		answers = source.readArrayList(Integer.class.getClassLoader());
		label = source.readString();
		key = source.readString();
		continueId = source.readInt();
		nextQuestions = (HashMap<String, Integer>) source.readSerializable();
		selectedAnswerId = source.readInt();
	}
	
	public int getContinueId() {
		return continueId;
	}
	
	public int getNextQuestionId(int answerId){
		
		return getNextQuestions().get(String.valueOf(answerId)) != null ? getNextQuestions().get(String.valueOf(answerId)) : 0;
	}

	public boolean hasLabel(){
		return label != null && label.length() > 0;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Integer> getAnswerIds() {
		if (answers == null) {
			answers = new ArrayList<Integer>();
		}
		return answers;
	}

	public void setAnswerIds(List<Integer> answerIds) {
		this.answers = answerIds;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setContinueId(int continueId) {
		this.continueId = continueId;
	}

	public String getKey() {
		if (key == null) {
			key = String.valueOf(getId());
		}
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Map<String, Integer> getNextQuestions() {
		if(nextQuestions == null){
			 nextQuestions = new HashMap<String, Integer>();
		}
    	return nextQuestions;
    }

	public void setNextQuestions(HashMap<String, Integer> nextQuestions) {
    	this.nextQuestions = nextQuestions;
    }

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(text);
		dest.writeList(answers);
		dest.writeString(label);
		dest.writeString(key);
		dest.writeInt(continueId);
		dest.writeSerializable(nextQuestions);
		dest.writeInt(selectedAnswerId);
	}
	
	public int getSelectedAnswerId() {
		return selectedAnswerId;
	}

	public void setSelectedAnswerId(int selectedAnswerId) {
		this.selectedAnswerId = selectedAnswerId;
	}

	public boolean isSelectedCommentsItem() {
		return isSelectedCommentsItem;
	}

	public void setSelectedCommentsItem(boolean isSelectedCommentsItem) {
		this.isSelectedCommentsItem = isSelectedCommentsItem;
	}

	public String getEnteredComment() {
		return enteredComment;
	}

	public void setEnteredComment(String enteredComment) {
		this.enteredComment = enteredComment;
	}

	public static final Parcelable.Creator<SurveyQuestion> CREATOR = new Parcelable.Creator<SurveyQuestion>() {

		@Override
		public SurveyQuestion createFromParcel(Parcel source) {
			return new SurveyQuestion(source);
		}

		@Override
		public SurveyQuestion[] newArray(int size) {
			return new SurveyQuestion[size];
		}

	};

}
