package com.csb.bean;

import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;


public class SurveyAnswer implements Parcelable {
	private int id;
	private String value;
	private String name;
	private String key;
	private String label;
	private boolean comments = false;
    private String commnetValue = "";
    private boolean isEditing = false;
	
	public SurveyAnswer(){}
	
	public SurveyAnswer(Parcel source) {
		id = source.readInt();
		value = source.readString();
		name = source.readString();
		key = source.readString();
		label = source.readString();
		comments = (Boolean) source.readSerializable();
        commnetValue = source.readString();
        isEditing = (Boolean) source.readSerializable();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		if (null == value) { 
			value = String.format(Locale.getDefault(), "%.0f", (float)getId());
		}
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isComments() {
		return comments;
	}

	public void setComments(boolean comments) {
		this.comments = comments;
	}

	public String getName() {
		if (name == null) {
			name = getKey();
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		if (key == null) { 
			key = String.format(Locale.getDefault(), "%.0f", (float)getId());
		}
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(value);
		dest.writeString(name);
		dest.writeString(key);
		dest.writeString(label);
		dest.writeSerializable(Boolean.valueOf(comments));
        dest.writeString(commnetValue);
        dest.writeSerializable(Boolean.valueOf(isEditing));
	}
	
	public static final Parcelable.Creator<SurveyAnswer> CREATOR = new Parcelable.Creator<SurveyAnswer>() {

		@Override
		public SurveyAnswer createFromParcel(Parcel source) {
			return new SurveyAnswer(source);
		}

		@Override
		public SurveyAnswer[] newArray(int size) {
			return new SurveyAnswer[size];
		}
		
	};

    public String getCommnetValue() {
        return commnetValue;
    }

    public void setCommnetValue(String commnetValue) {
        this.commnetValue = commnetValue;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditting(boolean isEditing) {
        this.isEditing = isEditing;
    }
}
