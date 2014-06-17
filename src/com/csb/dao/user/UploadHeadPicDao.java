package com.csb.dao.user;

import java.util.HashMap;
import java.util.Map;

import com.csb.bean.ResultUploadPicBean;
import com.csb.dao.URLHelper;
import com.csb.support.debug.AppLogger;
import com.csb.support.error.WeiboException;
import com.csb.support.http.HttpUtility;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 上传头像
 * 
 * @author bobo
 * 
 */
public class UploadHeadPicDao {
	private static final String FORMAT_IMG_NAME = "%s_%d.png";

	public ResultUploadPicBean upload() throws WeiboException {
		String url = URLHelper.URL_UPLOAD_HEAD_PIC;
		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		String jsonData = HttpUtility.getInstance()
				.executeUploadTaskWithResult(
						url,
						map,
						image,
						"file",
						String.format(FORMAT_IMG_NAME, userid,
								System.currentTimeMillis()), null);
		Gson gson = new Gson();

		ResultUploadPicBean value = null;
		try {
			value = gson.fromJson(jsonData, ResultUploadPicBean.class);
		} catch (JsonSyntaxException e) {
			AppLogger.e(e.getMessage());
		}

		return value;
	}

	private String userid;
	private String image;

	public UploadHeadPicDao(String userid, String image) {
		this.userid = userid;
		this.image = image;
	}
}
