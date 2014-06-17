package com.csb.dao.setting;

import java.util.HashMap;
import java.util.Map;

import com.csb.bean.UpdateVersionBean;
import com.csb.dao.URLHelper;
import com.csb.support.debug.AppLogger;
import com.csb.support.error.WeiboException;
import com.csb.support.http.HttpMethod;
import com.csb.support.http.HttpUtility;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 
 * 在线更新接口
 * 
 * @author bobo
 * 
 */
public class UpdateVersionDao {
	private String version;

	public UpdateVersionDao(String version) {
		this.version = version;
	}

	public UpdateVersionBean get() throws WeiboException {

		String apiUrl = URLHelper.URL_UPDATEVERSION;

		Map<String, String> map = new HashMap<String, String>();
		map.put("version", version);

		String jsonData = HttpUtility.getInstance().executeNormalTask(
				HttpMethod.Post, apiUrl, map);

		Gson gson = new Gson();

		UpdateVersionBean value = null;
		try {
			value = gson.fromJson(jsonData, UpdateVersionBean.class);
		} catch (JsonSyntaxException e) {
			AppLogger.e(e.getMessage());
		}

		return value;
	}

}
