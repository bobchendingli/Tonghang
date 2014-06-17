package com.csb.bean;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 返回结果 {
	“respCode”:”0000”,
	“respMsg“:“资料列表获得成功“
       “fileList”:[
               {
                     file_id:”1”,
                     file_name:”xxx”,
                     file_url:”http://aaa/tt.doc”
               }
        ]
}

 * 
 * @author bobo
 */
public class ResultFileDownloadBean implements Parcelable {
	private String respCode;
	private String respMsg;
	private List<FileBean> files;
	
	public List<FileBean> getFiles() {
		return files;
	}
	
	public void setFiles(List<FileBean> files) {
		this.files = files;
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
//		dest.writeString(respCode);
//		dest.writeString(respMsg);
	}

//	public static final Parcelable.Creator<ResultFileDownloadBean> CREATOR = new Parcelable.Creator<ResultFileDownloadBean>() {
//		public ResultFileDownloadBean createFromParcel(Parcel in) {
//			ResultFileDownloadBean geoBean = new ResultFileDownloadBean();
//			geoBean.respCode = in.readString();
//			geoBean.respMsg = in.readString();
//			return geoBean;
//		}
//
//		public ResultFileDownloadBean[] newArray(int size) {
//			return new ResultFileDownloadBean[size];
//		}
//	};
}
