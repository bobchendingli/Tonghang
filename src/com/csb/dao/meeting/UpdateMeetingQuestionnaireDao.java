package com.csb.dao.meeting;

import java.util.HashMap;
import java.util.List;
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
public class UpdateMeetingQuestionnaireDao {
	private String userid;
	private String meeting_id;
	private List<Map<String, String>> questionnaire_answer;

	public UpdateMeetingQuestionnaireDao(String userid, String meeting_id,
			List<Map<String, String>> questionnaire_answer) {
		this.userid = userid;
		this.meeting_id = meeting_id;
		this.questionnaire_answer = questionnaire_answer;
	}
	
	public void setQuestionnaire_answer(
			List<Map<String, String>> questionnaire_answer) {
		this.questionnaire_answer = questionnaire_answer;
	}
	
	public List<Map<String, String>> getQuestionnaire_answer() {
		return questionnaire_answer;
	}
	public void setMeeting_id(String meeting_id) {
		this.meeting_id = meeting_id;
	}

	public String getMeeting_id() {
		return meeting_id;
	}

	public ResultBean update() throws WeiboException {
		String apiUrl = URLHelper.URL_UPDATEMEETINGQUESTIONNAIREANSWER;
		
		String qaJson = null;
		Gson gson = new Gson();
		try {
			Map<String, Object> answer = new HashMap<String, Object>();
			answer.put("userid", userid);
			answer.put("meeting_id", meeting_id);
			answer.put("questionnaire_answer", questionnaire_answer);
//			Map<String, Object> answer = new HashMap<String, Object>();
//			answer.put("answers", m);

			qaJson = gson.toJson(answer);
		} catch (JsonSyntaxException e) {
			AppLogger.e(e.getMessage());
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("answers", qaJson);
//		map.put("meeting_id", meeting_id);
//		map.put("questionnaire_answer", qaJson);

		String jsonData = HttpUtility.getInstance().executeNormalTask(
				HttpMethod.Post, apiUrl, map);


		ResultBean value = null;
		try {
			value = gson.fromJson(jsonData, ResultBean.class);
		} catch (JsonSyntaxException e) {
			AppLogger.e(e.getMessage());
		}

		return value;
	}
}
