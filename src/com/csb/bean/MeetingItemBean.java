package com.csb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 返回结果 { “respCode”:”0000”, “respMsg“:“注册成功“ “userid”:”DE56756TYGH” }
 * 
 * 
 * @author bobo
 */
public class MeetingItemBean implements Parcelable {
	private String meeting_id;
	private String meeting_name;
	private String meeting_time;
	private String meeting_status;
	private String user_status;
	
	public void setUser_status(String user_status) {
		this.user_status = user_status;
	}
	
	public String getUser_status() {
		return user_status;
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
		dest.writeString(user_status);
	}

	public static final Parcelable.Creator<MeetingItemBean> CREATOR = new Parcelable.Creator<MeetingItemBean>() {
		public MeetingItemBean createFromParcel(Parcel in) {
			MeetingItemBean geoBean = new MeetingItemBean();
			geoBean.meeting_id = in.readString();
			geoBean.meeting_name = in.readString();
			geoBean.meeting_time = in.readString();
			geoBean.meeting_status = in.readString();
			geoBean.user_status = in.readString();
			return geoBean;
		}

		public MeetingItemBean[] newArray(int size) {
			return new MeetingItemBean[size];
		}
	};
}
