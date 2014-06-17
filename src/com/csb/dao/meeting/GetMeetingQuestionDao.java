package com.csb.dao.meeting;

import java.util.HashMap;
import java.util.Map;

import com.csb.bean.MeetingQuestionBean;
import com.csb.dao.URLHelper;
import com.csb.support.debug.AppLogger;
import com.csb.support.error.WeiboException;
import com.csb.support.http.HttpMethod;
import com.csb.support.http.HttpUtility;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 获取会议提问内容处理
 * 
 * @author bobo
 * 
 */
public class GetMeetingQuestionDao {
	private String userid;
	private String meeting_id;

	public GetMeetingQuestionDao(String userid, String meeting_id) {
		this.userid = userid;
		this.meeting_id = meeting_id;
	}

	public void setMeeting_id(String meeting_id) {
		this.meeting_id = meeting_id;
	}

	public String getMeeting_id() {
		return meeting_id;
	}

	public MeetingQuestionBean get() throws WeiboException {
		String apiUrl = URLHelper.URL_GETMEETINGQUESTION;

		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("meeting_id", meeting_id);

		String jsonData = HttpUtility.getInstance().executeNormalTask(
				HttpMethod.Post, apiUrl, map);

		Gson gson = new Gson();

		MeetingQuestionBean value = null;
		try {
			value = gson.fromJson(jsonData, MeetingQuestionBean.class);
		} catch (JsonSyntaxException e) {
			AppLogger.e(e.getMessage());
		}

		return value;
	}
}
