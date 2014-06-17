package com.csb.dao.setting;

import java.util.HashMap;
import java.util.Map;

import com.csb.bean.ResultBean;
import com.csb.bean.UserBean;
import com.csb.dao.URLHelper;
import com.csb.support.debug.AppLogger;
import com.csb.support.error.WeiboException;
import com.csb.support.http.HttpMethod;
import com.csb.support.http.HttpUtility;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 *
 * 意见反馈接口
 * @author bobo
 * 
 */
public class FeedbackDao {
	private String userid;
	private String content;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public FeedbackDao() {

	}

	public FeedbackDao(String userid, String content) {
		this.userid = userid;
		this.content = content;
	}

	public ResultBean update() throws WeiboException {

		String apiUrl = URLHelper.URL_FEEDBACK;

		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("content", content);

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
