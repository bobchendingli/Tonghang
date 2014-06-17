package com.csb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 返回结果 { “respCode”:”0000”, “respMsg“:“成功“, “upgrade”:”true”, “version”:”2.0”,
 * “url”:”http:xxxxxx/xxx” }
 * 
 * 
 * @author bobo
 */
public class UpdateVersionBean implements Parcelable {
	private String respCode;
	private String respMsg;
	private String upgrade;
	private String version;
	private String url;

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

	public String getUpgrade() {
		return upgrade;
	}

	public void setUpgrade(String upgrade) {
		this.upgrade = upgrade;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(respCode);
		dest.writeString(respMsg);
		dest.writeString(upgrade);
		dest.writeString(version);
		dest.writeString(url);
	}

	public static final Parcelable.Creator<UpdateVersionBean> CREATOR = new Parcelable.Creator<UpdateVersionBean>() {
		public UpdateVersionBean createFromParcel(Parcel in) {
			UpdateVersionBean geoBean = new UpdateVersionBean();
			geoBean.respCode = in.readString();
			geoBean.respMsg = in.readString();
			geoBean.upgrade = in.readString();
			geoBean.version = in.readString();
			geoBean.url = in.readString();
			return geoBean;
		}

		public UpdateVersionBean[] newArray(int size) {
			return new UpdateVersionBean[size];
		}
	};
}
