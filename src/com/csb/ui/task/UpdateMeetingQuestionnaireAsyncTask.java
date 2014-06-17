package com.csb.ui.task;

import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.widget.Toast;

import com.csb.R;
import com.csb.bean.ResultBean;
import com.csb.dao.meeting.UpdateMeetingQuestionnaireDao;
import com.csb.support.error.WeiboException;
import com.csb.support.lib.MyAsyncTask;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.GlobalContext;
import com.csb.utils.ToastUtils;

/**
 * 更新会议问卷 异步处理
 * 
 * @author bobo
 * 
 */
public class UpdateMeetingQuestionnaireAsyncTask extends
		MyAsyncTask<Void, ResultBean, ResultBean> {
	private String userid;
	private String meeting_id;
	private List<Map<String, String>> questionnaire_answer;
	private WeiboException e;
	private Handler h;

	public void setQuestionnaire_answer(
			List<Map<String, String>> questionnaire_answer) {
		this.questionnaire_answer = questionnaire_answer;
	}
	
	public List<Map<String, String>> getQuestionnaire_answer() {
		return questionnaire_answer;
	}
	public void setMeeting_id(String meeting_id) {
		this.meeting_id = meeting_id;
	}

	public String getMeeting_id() {
		return meeting_id;
	}


	public UpdateMeetingQuestionnaireAsyncTask(String userid, String meeting_id,
			List<Map<String, String>> questionnaire_answer, Handler h) {
		this.userid = userid;
		this.meeting_id = meeting_id;
		this.questionnaire_answer = questionnaire_answer;
		this.h = h;
	}
	
	
	@Override
	protected ResultBean doInBackground(Void... params) {
		h.sendEmptyMessage(BundleArgsConstants.REQUEST_START);
		UpdateMeetingQuestionnaireDao dao = new UpdateMeetingQuestionnaireDao(userid, meeting_id, questionnaire_answer);
		try {
			return dao.update();
		} catch (WeiboException e) {
			this.e = e;
			cancel(true);
			return null;
		}
	}

	@Override
	protected void onCancelled(ResultBean bean) {
		super.onCancelled(bean);
		if (bean == null && this.e != null)
			Toast.makeText(GlobalContext.getInstance(), e.getError(),
					Toast.LENGTH_SHORT).show();
		h.sendEmptyMessage(BundleArgsConstants.REQUEST_FAIL);
	}

	@Override
	protected void onPostExecute(ResultBean bean) {
		super.onPostExecute(bean);
		if (bean != null
				&& BundleArgsConstants.RESTURN_CODE_SUCC.equals(bean
						.getRespCode())) {
			ToastUtils.show(GlobalContext.getInstance(),
					R.string.updatequestionnaire_successfully);
			h.sendEmptyMessage(BundleArgsConstants.UPDATEQUESTIONNAIRE_SUCC);
		} else {
			String msg = bean == null ? "问卷提交失败!" : bean.getRespMsg();
			ToastUtils
					.show(GlobalContext.getInstance(), msg + "");
			h.sendEmptyMessage(BundleArgsConstants.REQUEST_FAIL);
		}
	}
}
