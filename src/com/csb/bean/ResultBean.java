package com.csb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 返回结果 { “respCode”:”0000”, “respMsg“:“请求成功“ }
 * 
 * @author bobo
 */
public class ResultBean implements Parcelable {
	private String respCode;
	private String respMsg;

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
		return "ResultBean [respCode=" + respCode + ", respMsg=" + respMsg
				+ "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(respCode);
		dest.writeString(respMsg);
	}

	public static final Parcelable.Creator<ResultBean> CREATOR = new Parcelable.Creator<ResultBean>() {
		public ResultBean createFromParcel(Parcel in) {
			ResultBean geoBean = new ResultBean();
			geoBean.respCode = in.readString();
			geoBean.respMsg = in.readString();
			return geoBean;
		}

		public ResultBean[] newArray(int size) {
			return new ResultBean[size];
		}
	};
}
