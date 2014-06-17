package com.csb.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回结果 { “respCode”:”0000”, “respMsg“:“注册成功“ “userid”:”DE56756TYGH” }
 * 
 * 
 * @author bobo
 */
public class MeetingListBean  {
	private String respCode;
	private String respMsg;
	private List<MeetingItemBean> meetingList = new ArrayList<MeetingItemBean>();

	public void setMeetingList(List<MeetingItemBean> meetingList) {
		this.meetingList = meetingList;
	}

	public List<MeetingItemBean> getMeetingList() {
		return meetingList;
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


}
