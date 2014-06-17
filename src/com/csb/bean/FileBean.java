package com.csb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * 
 * @author bobo
 */
public class FileBean implements Parcelable {
	private String id;
	private String aid;
	private String mid;
	private String title;
	private String picurl;
	private String info_type;
	private String create_time;

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getCreate_time() {
		return create_time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicurl() {
		return picurl;
	}

	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

	public String getInfo_type() {
		return info_type;
	}

	public void setInfo_type(String info_type) {
		this.info_type = info_type;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(aid);
		dest.writeString(mid);
		dest.writeString(title);
		dest.writeString(picurl);
		dest.writeString(info_type);
		dest.writeString(create_time);
	}

	public static final Parcelable.Creator<FileBean> CREATOR = new Parcelable.Creator<FileBean>() {
		public FileBean createFromParcel(Parcel in) {
			FileBean geoBean = new FileBean();
			geoBean.id = in.readString();
			geoBean.aid = in.readString();
			geoBean.mid = in.readString();
			geoBean.title = in.readString();
			geoBean.picurl = in.readString();
			geoBean.info_type = in.readString();
			geoBean.create_time = in.readString();
			return geoBean;
		}

		public FileBean[] newArray(int size) {
			return new FileBean[size];
		}
	};

}
