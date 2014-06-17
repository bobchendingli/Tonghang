package com.csb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 返回结果 { “respCode”:”0000”, “respMsg“:“注册成功“ “userid”:”DE56756TYGH” }
 * 
 * 
 * @author bobo
 */
public class ResultRegisterBean implements Parcelable {
	private String respCode;
	private String respMsg;
	private String userid;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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
	public String toString() {
		return "ResultRegisterBean [respCode=" + respCode + ", respMsg=" + respMsg
				+ ", userid=" + userid + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(respCode);
		dest.writeString(respMsg);
		dest.writeString(userid);
	}

	public static final Parcelable.Creator<ResultRegisterBean> CREATOR = new Parcelable.Creator<ResultRegisterBean>() {
		public ResultRegisterBean createFromParcel(Parcel in) {
			ResultRegisterBean geoBean = new ResultRegisterBean();
			geoBean.respCode = in.readString();
			geoBean.respMsg = in.readString();
			geoBean.userid = in.readString();
			return geoBean;
		}

		public ResultRegisterBean[] newArray(int size) {
			return new ResultRegisterBean[size];
		}
	};
}
