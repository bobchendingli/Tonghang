package com.csb.dao.meeting;

import java.util.HashMap;
import java.util.Map;

import com.csb.bean.MeetingListBean;
import com.csb.bean.UserBean;
import com.csb.dao.URLHelper;
import com.csb.support.debug.AppLogger;
import com.csb.support.error.WeiboException;
import com.csb.support.http.HttpMethod;
import com.csb.support.http.HttpUtility;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 获取会议列表
 * 
 * @author bobo
 * 
 */
public class GetMeetingListDao {
	private String userid;

	public GetMeetingListDao() {

	}

	public GetMeetingListDao(UserBean userBean) {
		this.userid = userBean.getUserid();
	}

	public GetMeetingListDao(String userid) {
		this.userid = userid;
	}

	public MeetingListBean get() throws WeiboException {

		String apiUrl = URLHelper.URL_GET_MEETINGLIST;

		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);

		String jsonData = HttpUtility.getInstance().executeNormalTask(
				HttpMethod.Post, apiUrl, map);

		Gson gson = new Gson();

		MeetingListBean value = null;
		try {
			value = gson.fromJson(jsonData, MeetingListBean.class);
		} catch (JsonSyntaxException e) {
			AppLogger.e(e.getMessage());
		}

		return value;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public String getUserid() {
		return userid;
	}
}
