package com.csb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 返回结果 { “respCode”:”0000”, “respMsg“:“上传医师执照成功“
 * “imageurl”:”27_100000_license.png” }
 * 
 * @author bobo
 */
public class ResultUploadPicBean implements Parcelable {
	private String respCode;
	private String respMsg;
	private String imageurl;

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
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
		return "ResultUploadPicBean [respCode=" + respCode + ", respMsg="
				+ respMsg + ", imageurl=" + imageurl + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(respCode);
		dest.writeString(respMsg);
		dest.writeString(imageurl);
	}

	public static final Parcelable.Creator<ResultUploadPicBean> CREATOR = new Parcelable.Creator<ResultUploadPicBean>() {
		public ResultUploadPicBean createFromParcel(Parcel in) {
			ResultUploadPicBean geoBean = new ResultUploadPicBean();
			geoBean.respCode = in.readString();
			geoBean.respMsg = in.readString();
			geoBean.imageurl = in.readString();
			return geoBean;
		}

		public ResultUploadPicBean[] newArray(int size) {
			return new ResultUploadPicBean[size];
		}
	};
}
