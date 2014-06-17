package com.csb.ui.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.csb.R;
import com.csb.bean.ResultUploadPicBean;
import com.csb.dao.user.UploadDoctorLicensePicDao;
import com.csb.dao.user.UploadHeadPicDao;
import com.csb.support.error.WeiboException;
import com.csb.support.lib.MyAsyncTask;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.GlobalContext;
import com.csb.utils.ToastUtils;

/**
 * 注册异步处理
 * 
 * @author bobo
 * 
 */
public class UploadPicAsyncTask extends
		MyAsyncTask<Void, ResultUploadPicBean, ResultUploadPicBean> {
	public final static int UPLOAD_HEAD_PIC = 0;
	public final static int UPLOAD_DOCTOR_CODE_PIC = 1;
	
	private int updateType;
	private String userid;
	private String image;
	private WeiboException e;
	private Handler h;

	public void setImage(String image) {
		this.image = image;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setUpdateType(int updateType) {
		this.updateType = updateType;
	}
	
	public int getUpdateType() {
		return updateType;
	}
	
	public UploadPicAsyncTask(String userid, String image, int updateType, Handler h) {
		this.userid = userid;
		this.image = image;
		this.updateType = updateType;
		this.h = h;
	}
	
	
	@Override
	protected ResultUploadPicBean doInBackground(Void... params) {
		h.sendEmptyMessage(BundleArgsConstants.REQUEST_START);
		ResultUploadPicBean bean = null;
		try {
			switch(this.updateType){
			case UPLOAD_HEAD_PIC:
				UploadHeadPicDao uploadHeadPicDao = new UploadHeadPicDao(userid, image);
				bean = uploadHeadPicDao.upload();
				break;
			case UPLOAD_DOCTOR_CODE_PIC:
				UploadDoctorLicensePicDao uploadDoctorLicensePicDao = new UploadDoctorLicensePicDao(userid, image);
				bean = uploadDoctorLicensePicDao.upload();
				break;
			}
			return bean;
		} catch (WeiboException e) {
			this.e = e;
			cancel(true);
			return null;
		}
	}

	@Override
	protected void onCancelled(ResultUploadPicBean bean) {
		super.onCancelled(bean);
		if (bean == null && this.e != null)
			Toast.makeText(GlobalContext.getInstance(), e.getError(),
					Toast.LENGTH_SHORT).show();
		h.sendEmptyMessage(BundleArgsConstants.REQUEST_FAIL);
	}

	@Override
	protected void onPostExecute(ResultUploadPicBean bean) {
		super.onPostExecute(bean);
		if (bean != null
				&& BundleArgsConstants.RESTURN_CODE_SUCC.equals(bean
						.getRespCode())) {
			Message msg = new Message();
			msg.what = updateType == UPLOAD_HEAD_PIC ? BundleArgsConstants.UPLOAD_HEAD_PIC_SUCC : BundleArgsConstants.UPLOAD_CODE_PIC_SUCC;
			msg.obj  = bean;
			h.sendMessage(msg);
		} else {
			String failMessage = (bean == null || TextUtils.isEmpty(bean.getRespMsg())) ? "上传图片失败!" : bean.getRespMsg();
			ToastUtils
					.show(GlobalContext.getInstance(), failMessage);
			h.sendEmptyMessage(BundleArgsConstants.REQUEST_FAIL);
		}
	}
}
