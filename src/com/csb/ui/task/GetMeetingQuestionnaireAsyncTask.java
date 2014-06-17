package com.csb.ui.task;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.csb.bean.QuestionnaireListBean;
import com.csb.dao.meeting.GetMeetingQuestionnaireDao;
import com.csb.support.error.WeiboException;
import com.csb.support.lib.MyAsyncTask;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.GlobalContext;
import com.csb.utils.ToastUtils;

/**
 * 获取问卷异步处理
 * 
 * @author bobo
 * 
 */
public class GetMeetingQuestionnaireAsyncTask extends
		MyAsyncTask<Void, QuestionnaireListBean, QuestionnaireListBean> {
	private String userid;
	private String meeting_id;
	private String questionnaire_type;
	private WeiboException e;
	private Handler h;

	public GetMeetingQuestionnaireAsyncTask(String userid, String meeting_id,
			String questionnaire_type, Handler h) {
		this.userid = userid;
		this.meeting_id = meeting_id;
		this.questionnaire_type = questionnaire_type;
		this.h = h;
	}

	@Override
	protected QuestionnaireListBean doInBackground(Void... params) {
		h.sendEmptyMessage(BundleArgsConstants.REQUEST_START);
		GetMeetingQuestionnaireDao dao = new GetMeetingQuestionnaireDao(userid,
				meeting_id, questionnaire_type);
		QuestionnaireListBean bean = null;
		try {
			bean = dao.get();
			return bean;
		} catch (WeiboException e) {
			this.e = e;
			cancel(true);
			return null;
		}
	}

	@Override
	protected void onCancelled(QuestionnaireListBean bean) {
		super.onCancelled(bean);
		if (bean == null && this.e != null)
			Toast.makeText(GlobalContext.getInstance(), e.getError(),
					Toast.LENGTH_SHORT).show();
		h.sendEmptyMessage(BundleArgsConstants.REQUEST_FAIL);
	}

	@Override
	protected void onPostExecute(QuestionnaireListBean bean) {
		super.onPostExecute(bean);
		if (bean != null
				&& BundleArgsConstants.RESTURN_CODE_SUCC.equals(bean
						.getRespCode())) {
			Message msg = new Message();
			msg.what = BundleArgsConstants.REQUEST_SUCC;
			msg.obj = bean;
			h.sendMessage(msg);
		} else {
			String failMessage = (bean == null || TextUtils.isEmpty(bean
					.getRespMsg())) ? "获取会议信息失败!" : bean.getRespMsg();
			ToastUtils.show(GlobalContext.getInstance(), failMessage);
			h.sendEmptyMessage(BundleArgsConstants.REQUEST_FAIL);
		}
	}
}
