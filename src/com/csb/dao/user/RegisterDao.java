package com.csb.dao.user;

import java.util.HashMap;
import java.util.Map;

import com.csb.bean.ResultBean;
import com.csb.bean.ResultRegisterBean;
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
public class RegisterDao {
	private String access_token;
	private String user_name;
	private String hospital;
	private String departments;
	private String post;
	private String region;
	private String phone;
	private String mail;
	private String password;
	private String gender;
	private String birthday;
	private String code = "";
	private String verificationNo = "";

	public RegisterDao() {

	}
	
	public RegisterDao(UserBean bean) {
		this.user_name = bean.getUsername();
		this.hospital = bean.getHospital();
		this.departments = bean.getDepartments();
		this.post = bean.getPost();
		this.region = bean.getRegion();
		this.phone = bean.getPhone();
		this.mail = bean.getMail();
		this.password = bean.getPassword();
		this.gender = bean.getGender();
		this.birthday = bean.getBirthday();
		this.code = bean.getCode();
		this.verificationNo = bean.getVerificationno();
	}

	public RegisterDao(String token, String user_name, String hospital,
			String departments, String post, String region, String phone,
			String mail, String password, String gender, String birthday, String code, String verificationNo) {
		this.access_token = token;
		this.user_name = user_name;
		this.hospital = hospital;
		this.departments = departments;
		this.post = post;
		this.region = region;
		this.phone = phone;
		this.mail = mail;
		this.password = password;
		this.gender = gender;
		this.birthday = birthday;
		this.code = code;
		this.verificationNo = verificationNo;
	}

	public ResultBean requestSmsVerification() throws WeiboException {

		String apiUrl = URLHelper.URL_REQUEST_SMS_VERIFICATION;

		Map<String, String> map = new HashMap<String, String>();
		map.put("phone", phone);

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

	public ResultRegisterBean register() throws WeiboException {

		String apiUrl = URLHelper.URL_REGISTER;
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", user_name);
		map.put("hospital", hospital);
		map.put("departments", departments);
		map.put("post", post);
		map.put("region", region);
		map.put("phone", phone);
		map.put("mail", mail);
		map.put("password", password);
		map.put("gender", gender);
		map.put("birthday", birthday);
		map.put("code", code);
		map.put("verificationNo", verificationNo);

		String jsonData = HttpUtility.getInstance().executeNormalTask(
				HttpMethod.Post, apiUrl, map);

		Gson gson = new Gson();

		ResultRegisterBean value = null;
		try {
			value = gson.fromJson(jsonData, ResultRegisterBean.class);
		} catch (JsonSyntaxException e) {
			AppLogger.e(e.getMessage());
		}

		return value;
	}


	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getDepartments() {
		return departments;
	}

	public void setDepartments(String departments) {
		this.departments = departments;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getVerificationNo() {
		return verificationNo;
	}

	public void setVerificationNo(String verificationNo) {
		this.verificationNo = verificationNo;
	}
}
