package com.csb.dao.meeting;

import java.util.HashMap;
import java.util.Map;

import com.csb.R;
import com.csb.bean.QuestionnaireListBean;
import com.csb.dao.URLHelper;
import com.csb.support.debug.AppLogger;
import com.csb.support.error.WeiboException;
import com.csb.support.http.HttpMethod;
import com.csb.support.http.HttpUtility;
import com.csb.utils.GlobalContext;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 注册处理
 * 
 * @author bobo
 * 
 */
public class GetMeetingQuestionnaireDao {
	private String userid;
	private String meeting_id;
	private String questionnaire_type;

	public GetMeetingQuestionnaireDao(String userid, String meeting_id, String questionnaire_type) {
		this.userid = userid;
		this.meeting_id = meeting_id;
		this.questionnaire_type = questionnaire_type;
	}
	
	public void setMeeting_id(String meeting_id) {
		this.meeting_id = meeting_id;
	}
	
	public String getMeeting_id() {
		return meeting_id;
	}
	

	public QuestionnaireListBean get() throws WeiboException {
		String apiUrl = URLHelper.URL_GETMEETINGQUESTIONNAIRE;

		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("meeting_id", meeting_id);
		map.put("questionnaire_type", questionnaire_type);

		String jsonData = HttpUtility.getInstance().executeNormalTask(
				HttpMethod.Post, apiUrl, map);
//		jsonData = GlobalContext.getInstance().getString(R.string.wj);
		Gson gson = new Gson();

		QuestionnaireListBean value = null;
		try {
			value = gson.fromJson(jsonData, QuestionnaireListBean.class);
		} catch (JsonSyntaxException e) {
			AppLogger.e(e.getMessage());
		}

		return value;
	}
}
