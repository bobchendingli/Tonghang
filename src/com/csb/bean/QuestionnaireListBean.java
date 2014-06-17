package com.csb.bean;

import java.util.List;

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
public class QuestionnaireListBean implements Parcelable {
	private String respCode;
	private String respMsg;
	private String questionnaire_type;
	private List<QuestionnaireBean> question_content;

	public void setQuestion_content(List<QuestionnaireBean> question_content) {
		this.question_content = question_content;
	}
	
	public List<QuestionnaireBean> getQuestion_content() {
		return question_content;
	}
	
	public void setQuestionnaire_type(String questionnaire_type) {
		this.questionnaire_type = questionnaire_type;
	}
	
	public String getQuestionnaire_type() {
		return questionnaire_type;
	}
	
	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
	}



}
