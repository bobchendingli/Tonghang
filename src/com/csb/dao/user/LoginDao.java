package com.csb.dao.user;

import java.util.HashMap;
import java.util.Map;

import com.csb.bean.UserBean;
import com.csb.dao.URLHelper;
import com.csb.support.debug.AppLogger;
import com.csb.support.error.WeiboException;
import com.csb.support.http.HttpMethod;
import com.csb.support.http.HttpUtility;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 注册处理
 * 
 * @author bobo
 * 
 */
public class LoginDao {
	private String mail;
	private String password;

	public LoginDao() {

	}

	public LoginDao(UserBean userBean) {
		this.mail = userBean.getMail();
		this.password = userBean.getPassword();
	}

	public LoginDao(String mail, String password) {
		this.mail = mail;
		this.password = password;
	}

	public UserBean login() throws WeiboException {

		String apiUrl = URLHelper.URL_LOGIN;

		Map<String, String> map = new HashMap<String, String>();
		map.put("mail", mail);
		map.put("password", password);

		String jsonData = HttpUtility.getInstance().executeNormalTask(
				HttpMethod.Post, apiUrl, map);

		Gson gson = new Gson();

		UserBean value = null;
		try {
			value = gson.fromJson(jsonData, UserBean.class);
		} catch (JsonSyntaxException e) {
			AppLogger.e(e.getMessage());
		}

		return value;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
