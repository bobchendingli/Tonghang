package com.csb.dao.meeting;

import java.util.HashMap;
import java.util.Map;

import com.csb.bean.ResultBean;
import com.csb.dao.URLHelper;
import com.csb.support.debug.AppLogger;
import com.csb.support.error.WeiboException;
import com.csb.support.http.HttpMethod;
import com.csb.support.http.HttpUtility;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 更新会议提问处理
 * 
 * @author bobo
 * 
 */
public class UpdateMeetingQuestionDao {
	private String userid;
	private String meeting_id;
	private String question_content;

	public UpdateMeetingQuestionDao(String userid, String meeting_id,
			String question_content) {
		this.userid = userid;
		this.meeting_id = meeting_id;
		this.question_content = question_content;
	}
	
	public void setQuestion_content(String question_content) {
		this.question_content = question_content;
	}
	
	public String getQuestion_content() {
		return question_content;
	}
	
	public void setMeeting_id(String meeting_id) {
		this.meeting_id = meeting_id;
	}

	public String getMeeting_id() {
		return meeting_id;
	}

	public ResultBean update() throws WeiboException {
		String apiUrl = URLHelper.URL_UPDATEMEETINGQUESTION;

		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("meeting_id", meeting_id);
		map.put("question_content", question_content);

		String jsonData = HttpUtility.getInstance().executeNormalTask(
				HttpMethod.Post, apiUrl, map);

		Gson gson = new Gson();

		ResultBean value = null;
		try {
			value = gson.fromJson(jsonData, ResultBean.class);
		} catch (JsonSyntaxException e) {
			AppLogger.e(e.getMessage());
		}

		return value;
	}
}
