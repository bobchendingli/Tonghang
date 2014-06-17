package com.csb.ui.task;

import android.widget.Toast;

import com.csb.R;
import com.csb.bean.ResultBean;
import com.csb.dao.user.RegisterDao;
import com.csb.support.error.WeiboException;
import com.csb.support.lib.MyAsyncTask;
import com.csb.utils.GlobalContext;
import com.csb.utils.ToastUtils;

/**
 * 注册异步处理
 * 
 * @author bobo
 * 
 */
public class RequestSmsAsyncTask extends
		MyAsyncTask<Void, ResultBean, ResultBean> {

	private String phone;
	private WeiboException e;

	public RequestSmsAsyncTask(String phone) {
		this.phone = phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Override
	protected ResultBean doInBackground(Void... params) {
		RegisterDao dao = new RegisterDao();
		dao.setPhone(phone);
		try {
			return dao.requestSmsVerification();
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
			ToastUtils.show(GlobalContext.getInstance(),  e.getError());
	}

	@Override
	protected void onPostExecute(ResultBean bean) {
		super.onPostExecute(bean);
		if (bean != null)
			ToastUtils.show(GlobalContext.getInstance(),
					R.string.request_sms_successfully);
	}
}
