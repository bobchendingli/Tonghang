package com.csb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 返回结果 { “respCode”:”0000”, “respMsg“:“注册成功“ “userid”:”DE56756TYGH” }
 * 
 * 
 * @author bobo
 */
public class MeetingDetailBean implements Parcelable {
	private String meeting_id;
	private String meeting_name;//会议名称
	private String meeting_time;//会议时间
	private String meeting_status;//会议状
	private String meeting_address; //会议地址
	private String meeting_speaker; //会议演讲者
	private String meeting_arrangement;//会议安排表
	private MeetingQuestionBean questionBean;//提问
	private String respMsg;
	private String respCode;
	
	public void setQuestionBean(MeetingQuestionBean questionBean) {
		this.questionBean = questionBean;
	}
	public MeetingQuestionBean getQuestionBean() {
		return questionBean;
	}
	
	public String getMeeting_address() {
		return meeting_address;
	}

	public void setMeeting_address(String meeting_address) {
		this.meeting_address = meeting_address;
	}

	public String getMeeting_speaker() {
		return meeting_speaker;
	}

	public void setMeeting_speaker(String meeting_speaker) {
		this.meeting_speaker = meeting_speaker;
	}

	public String getMeeting_arrangement() {
		return meeting_arrangement;
	}

	public void setMeeting_arrangement(String meeting_arrangement) {
		this.meeting_arrangement = meeting_arrangement;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getMeeting_id() {
		return meeting_id;
	}

	public void setMeeting_id(String meeting_id) {
		this.meeting_id = meeting_id;
	}

	public String getMeeting_name() {
		return meeting_name;
	}

	public void setMeeting_name(String meeting_name) {
		this.meeting_name = meeting_name;
	}

	public String getMeeting_time() {
		return meeting_time;
	}

	public void setMeeting_time(String meeting_time) {
		this.meeting_time = meeting_time;
	}

	public String getMeeting_status() {
		return meeting_status;
	}

	public void setMeeting_status(String meeting_status) {
		this.meeting_status = meeting_status;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(meeting_id);
		dest.writeString(meeting_name);
		dest.writeString(meeting_time);
		dest.writeString(meeting_status);
		dest.writeString(meeting_address);
		dest.writeString(meeting_speaker);
		dest.writeString(meeting_arrangement);
		dest.writeString(respMsg);
		dest.writeString(respCode);
	}

	public static final Parcelable.Creator<MeetingDetailBean> CREATOR = new Parcelable.Creator<MeetingDetailBean>() {
		public MeetingDetailBean createFromParcel(Parcel in) {
			MeetingDetailBean geoBean = new MeetingDetailBean();
			geoBean.meeting_id = in.readString();
			geoBean.meeting_name = in.readString();
			geoBean.meeting_time = in.readString();
			geoBean.meeting_status = in.readString();
			geoBean.meeting_address = in.readString();
			geoBean.meeting_speaker = in.readString();
			geoBean.meeting_arrangement = in.readString();
			geoBean.respMsg = in.readString();
			geoBean.respCode = in.readString();
			return geoBean;
		}

		public MeetingDetailBean[] newArray(int size) {
			return new MeetingDetailBean[size];
		}
	};
}
