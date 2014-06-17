package com.csb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 返回结果 { “respCode”:”0000”, “respMsg“:“注册成功“ “userid”:”DE56756TYGH” }
 * 
 * 
 * @author bobo
 */
public class MeetingQuestionBean implements Parcelable {
	private String respCode;
	private String respMsg;
	private String[] question_content;

	public void setQuestion_content(String[] question_content) {
		this.question_content = question_content;
	}

	public String[] getQuestion_content() {
		return question_content;
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
		dest.writeString(respCode);
		dest.writeString(respMsg);
		dest.writeStringArray(question_content);
	}

	public static final Parcelable.Creator<MeetingQuestionBean> CREATOR = new Parcelable.Creator<MeetingQuestionBean>() {
		public MeetingQuestionBean createFromParcel(Parcel in) {
			MeetingQuestionBean geoBean = new MeetingQuestionBean();
			geoBean.respCode = in.readString();
			geoBean.respMsg = in.readString();
//			geoBean.question_content = in.readStringArray(question_content);
			return geoBean;
		}

		public MeetingQuestionBean[] newArray(int size) {
			return new MeetingQuestionBean[size];
		}
	};
}
