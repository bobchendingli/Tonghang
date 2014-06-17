package com.csb.ui.task;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.csb.bean.MeetingDetailBean;
import com.csb.bean.MeetingQuestionBean;
import com.csb.dao.meeting.GetMeetingDetailDao;
import com.csb.dao.meeting.GetMeetingQuestionDao;
import com.csb.dao.meeting.GetMeetingQuestionnaireDao;
import com.csb.support.error.WeiboException;
import com.csb.support.lib.MyAsyncTask;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.GlobalContext;
import com.csb.utils.ToastUtils;

/**
 * 获取会议详情异步处理
 * 
 * @author bobo
 * 
 */
public class GetMeetingDetailAsyncTask extends
		MyAsyncTask<Void, MeetingDetailBean, MeetingDetailBean> {
	private String userid;
	private String meeting_id;
	private WeiboException e;
	private Handler h;

	public GetMeetingDetailAsyncTask(String userid, String meeting_id, Handler h) {
		this.userid = userid;
		this.meeting_id = meeting_id;
		this.h = h;
	}
	
	
	@Override
	protected MeetingDetailBean doInBackground(Void... params) {
		h.sendEmptyMessage(BundleArgsConstants.REQUEST_START);
		GetMeetingDetailDao dao = new GetMeetingDetailDao(userid, meeting_id);
		GetMeetingQuestionDao qDao = new GetMeetingQuestionDao(userid, meeting_id);
		MeetingDetailBean bean = null;
		MeetingQuestionBean qBean = null;
		try {
			bean = dao.get();
			qBean = qDao.get();
			if(bean != null) {
				bean.setQuestionBean(qBean);
			}
			return bean; 
		} catch (WeiboException e) {
			this.e = e;
			cancel(true);
			return null;
		}
	}

	@Override
	protected void onCancelled(MeetingDetailBean bean) {
		super.onCancelled(bean);
		if (bean == null && this.e != null)
			Toast.makeText(GlobalContext.getInstance(), e.getError(),
					Toast.LENGTH_SHORT).show();
		h.sendEmptyMessage(BundleArgsConstants.REQUEST_FAIL);
	}

	@Override
	protected void onPostExecute(MeetingDetailBean bean) {
		super.onPostExecute(bean);
		if (bean != null
				&& BundleArgsConstants.RESTURN_CODE_SUCC.equals(bean
						.getRespCode())) {
			Message msg = new Message();
			msg.what = BundleArgsConstants.REQUEST_SUCC;
			msg.obj = bean;
			h.sendMessage(msg);
		} else {
			String failMessage = (bean == null || TextUtils.isEmpty(bean.getRespMsg())) ? "获取会议信息失败!" : bean.getRespMsg();
			ToastUtils
					.show(GlobalContext.getInstance(), failMessage);
			h.sendEmptyMessage(BundleArgsConstants.REQUEST_FAIL);
		}
	}
}
