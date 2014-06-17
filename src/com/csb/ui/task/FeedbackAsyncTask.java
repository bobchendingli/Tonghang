package com.csb.ui.task;

import android.os.Handler;
import android.widget.Toast;

import com.csb.R;
import com.csb.bean.ResultBean;
import com.csb.dao.setting.FeedbackDao;
import com.csb.support.error.WeiboException;
import com.csb.support.lib.MyAsyncTask;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.GlobalContext;
import com.csb.utils.ToastUtils;

/**
 * 更新意见反馈异步处理
 * 
 * @author bobo
 * 
 */
public class FeedbackAsyncTask extends
		MyAsyncTask<Void, ResultBean, ResultBean> {
	private String userid;
	private String content;
	private WeiboException e;
	private Handler h;

	public FeedbackAsyncTask(String userid, String content, Handler h) {
		this.userid = userid;
		this.content = content;
		this.h = h;
	}

	@Override
	protected ResultBean doInBackground(Void... params) {
		h.sendEmptyMessage(BundleArgsConstants.REQUEST_START);
		FeedbackDao dao = new FeedbackDao(userid, content);
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
					R.string.updatefeedback_successfully);
			h.sendEmptyMessage(BundleArgsConstants.REQUEST_SUCC);
		} else {
			ToastUtils
					.show(GlobalContext.getInstance(), bean.getRespMsg() + "");
			h.sendEmptyMessage(BundleArgsConstants.REQUEST_FAIL);
		}
	}
}
