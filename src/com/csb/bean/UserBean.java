package com.csb.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户{
	“respCode”:”0000”,
	“respMsg“:“登陆成功“,
	“userid”:”DE56756TYGH”,
	“username”:”xxx”,
       “gender”:”xxx”,
       “birthday”:”xxx”,
       “code”:””,
       “hospital”:”xxx”,
       “departments”:”xxx”
       “post”:”xxx”
       “region”:”xxx”
       “phone”:”xxx”
       “mail”:”xxx”
	“imageurl”:”27_100000.png”
}

 * @author bobo
 *
 */
public class UserBean implements Parcelable {
	private String respCode;
	private String respMsg;
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
	private String imageurl;
	private String licenseimgurl;
	private String password;
	private String verificationno;
	private String degree;
	private String planning;
	private String interest;
	private String passwordCofirm;
	
	public void setPasswordCofirm(String passwordCofirm) {
		this.passwordCofirm = passwordCofirm;
	}
	
	public String getPasswordCofirm() {
		return passwordCofirm;
	}
	
	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getPlanning() {
		return planning;
	}

	public void setPlanning(String planning) {
		this.planning = planning;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public void setLicenseimgurl(String licenseimgurl) {
		this.licenseimgurl = licenseimgurl;
	}
	
	public String getLicenseimgurl() {
		return licenseimgurl;
	}
	
	public void setVerificationno(String verificationno) {
		this.verificationno = verificationno;
	}
	
	public String getVerificationno() {
		return verificationno;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
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

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(respCode);
		dest.writeString(respMsg);
		dest.writeString(userid);
		dest.writeString(username);
		dest.writeString(gender);
		dest.writeString(age);
		dest.writeString(code);
		dest.writeString(hospital);
		dest.writeString(departments);
		dest.writeString(post);
		dest.writeString(region);
		dest.writeString(phone);
		dest.writeString(mail);
		dest.writeString(imageurl);
		dest.writeString(password);
		dest.writeString(verificationno);
		dest.writeString(licenseimgurl);
		dest.writeString(degree);
		dest.writeString(planning);
		dest.writeString(interest);
		dest.writeString(passwordCofirm);
	}

	public static final Parcelable.Creator<UserBean> CREATOR = new Parcelable.Creator<UserBean>() {
		public UserBean createFromParcel(Parcel in) {
			UserBean bean = new UserBean();
			bean.respCode = in.readString();
			bean.respMsg = in.readString();
			bean.userid = in.readString();
			bean.username = in.readString();
			bean.gender = in.readString();
			bean.age = in.readString();
			bean.code = in.readString();
			bean.hospital = in.readString();
			bean.departments = in.readString();
			bean.post = in.readString();
			bean.region = in.readString();
			bean.phone = in.readString();
			bean.mail = in.readString();
			bean.imageurl = in.readString();
			bean.password = in.readString();
			bean.verificationno = in.readString();
			bean.licenseimgurl = in.readString();
			bean.degree = in.readString();
			bean.planning = in.readString();
			bean.interest = in.readString();
			bean.passwordCofirm = in.readString();
			return bean;
		}

		public UserBean[] newArray(int size) {
			return new UserBean[size];
		}
	};
	@Override
	public boolean equals(Object o) {
		return o instanceof UserBean && userid.equals(((UserBean) o).getUserid());
	}

	@Override
	public int hashCode() {
		return getUserid().hashCode();
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	
	

}
