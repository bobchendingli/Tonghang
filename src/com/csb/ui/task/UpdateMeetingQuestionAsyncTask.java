package com.csb.ui.task;

import android.os.Handler;
import android.widget.Toast;

import com.csb.R;
import com.csb.bean.ResultBean;
import com.csb.dao.meeting.UpdateMeetingQuestionDao;
import com.csb.support.error.WeiboException;
import com.csb.support.lib.MyAsyncTask;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.GlobalContext;
import com.csb.utils.ToastUtils;

/**
 * 更新会议提问 异步处理
 * 
 * @author bobo
 * 
 */
public class UpdateMeetingQuestionAsyncTask extends
		MyAsyncTask<Void, ResultBean, ResultBean> {
	private String userid;
	private String meeting_id;
	private String question_content;
	private WeiboException e;
	private Handler h;

	public UpdateMeetingQuestionAsyncTask(String userid, String meeting_id,
			String question_content, Handler h) {
		this.userid = userid;
		this.meeting_id = meeting_id;
		this.question_content = question_content;
		this.h = h;
	}
	
	
	@Override
	protected ResultBean doInBackground(Void... params) {
		h.sendEmptyMessage(BundleArgsConstants.REQUEST_START);
		UpdateMeetingQuestionDao dao = new UpdateMeetingQuestionDao(userid, meeting_id, question_content);
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
					R.string.updatequestion_successfully);
			h.sendEmptyMessage(BundleArgsConstants.REQUEST_SUCC);
		} else {
			ToastUtils
					.show(GlobalContext.getInstance(), bean.getRespMsg() + "");
			h.sendEmptyMessage(BundleArgsConstants.REQUEST_FAIL);
		}
	}
}
