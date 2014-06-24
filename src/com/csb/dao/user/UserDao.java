package com.csb.dao.user;

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
 * 用户信息处理
 * 
 * @author bobo
 * 
 */
public class UserDao {
	private String userid;
	private String username;
	private String gender;
	private String age;
	private String code;
	private String hospital;
	private String departments;
	private String post;
	private String region;
	private String phone;
	private String mail;
	private String password;
	private String degree;
	private String planning;
	private String interest;
	
	
	

	public UserDao(UserBean userBean) {
		this.userid = userBean.getUserid();
		this.username = userBean.getUsername();
		this.gender = userBean.getGender();
		this.age = userBean.getAge();
		this.code = userBean.getCode();
		this.hospital = userBean.getHospital();
		this.departments = userBean.getDepartments();
		this.post = userBean.getPost();
		this.region = userBean.getRegion();
		this.phone = userBean.getPhone();
		this.mail = userBean.getMail();
		this.password = userBean.getPassword();
		this.degree = userBean.getDegree();
		this.planning = userBean.getPlanning();
		this.interest = userBean.getInterest();
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setAge(String age) {
		this.age = age;
	}
	
	public String getAge() {
		return age;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public ResultBean update() throws WeiboException {

		String apiUrl = URLHelper.URL_UPDATE_USER;

		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("username", username);
		map.put("gender", "男".equals(gender) ? "1" : "0");
		map.put("age", age);
		map.put("code", code);
		map.put("hospital", hospital);
		map.put("departments", departments);
		map.put("post", post);
		map.put("region", region);
		map.put("phone", phone);
		map.put("mail", mail);
		map.put("password", password);
		map.put("degree", degree);
		map.put("planning", planning);
		map.put("interest", interest);
		
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
