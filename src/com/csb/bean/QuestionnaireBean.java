package com.csb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * {
	“respCode”:”0000”,
	“respMsg“:“会议问卷获得成功“，
       “questionnaire_type”:”0”,
	“questionnaire_question“:[
             {
                   question_id:”1”,
                   question_content:”aaaaa”,
                   question_selectio:[
                      “xxxx”,
                      “xxxx”,
                      “xxxx”
                  ]
             },
             {
                   question_id:”2”,
                   question_content:”aaaaa”
                   question_selectio:[
                      “xxxx”,
                      “xxxx”,
                      “xxxx”
                  ]
             }
        ]
}

 * 
 * 
 * @author bobo
 */
public class QuestionnaireBean implements Parcelable {
	private int question_id;
	private String question_content;
	private String[] question_selection;
	
	public int getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}

	public String getQuestion_content() {
		return question_content;
	}

	public void setQuestion_content(String question_content) {
		this.question_content = question_content;
	}

	public String[] getQuestion_selection() {
		return question_selection;
	}

	public void setQuestion_selection(String[] question_selection) {
		this.question_selection = question_selection;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(question_id);
		dest.writeString(question_content);
		dest.writeStringArray(question_selection);;
	}

	public final Parcelable.Creator<QuestionnaireBean> CREATOR = new Parcelable.Creator<QuestionnaireBean>() {
		public QuestionnaireBean createFromParcel(Parcel in) {
			QuestionnaireBean geoBean = new QuestionnaireBean();
			geoBean.question_id = in.readInt();
			geoBean.question_content = in.readString();
//			geoBean.question_selectio = in.readTypedArray(question_selectio, );
			return geoBean;
		}

		public QuestionnaireBean[] newArray(int size) {
			return new QuestionnaireBean[size];
		}
	};
}
