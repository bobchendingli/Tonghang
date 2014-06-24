package com.csb.ui.task;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.csb.R;
import com.csb.bean.ResultRegisterBean;
import com.csb.bean.UserBean;
import com.csb.dao.user.RegisterDao;
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
public class RegisterAsyncTask extends
		MyAsyncTask<Void, ResultRegisterBean, ResultRegisterBean> {

	private UserBean userBean;
	private WeiboException e;
	private Handler h;

	public RegisterAsyncTask(UserBean userBean, Handler h) {
		this.userBean = userBean;
		this.h = h;
	}
	
	
	@Override
	protected ResultRegisterBean doInBackground(Void... params) {
		h.sendEmptyMessage(BundleArgsConstants.REGISTER_START);
		RegisterDao dao = new RegisterDao(userBean);
		try {
			return dao.register();
		} catch (WeiboException e) {
			this.e = e;
			cancel(true);
			return null;
		}
	}

	@Override
	protected void onCancelled(ResultRegisterBean bean) {
		super.onCancelled(bean);
		if (bean == null && this.e != null)
			Toast.makeText(GlobalContext.getInstance(), e.getError(),
					Toast.LENGTH_SHORT).show();
		h.sendEmptyMessage(BundleArgsConstants.REQUEST_FAIL);
	}

	@Override
	protected void onPostExecute(ResultRegisterBean bean) {
		super.onPostExecute(bean);
		if (bean != null
				&& BundleArgsConstants.RESTURN_CODE_SUCC.equals(bean
						.getRespCode())) {
			ToastUtils.show(GlobalContext.getInstance(),
					R.string.register_successfully);
			userBean.setUserid(bean.getUserid());
			Message msg = new Message();
			msg.what = BundleArgsConstants.REQUEST_SUCC;
			msg.obj = userBean;
			h.sendMessage(msg);
		} else {
			ToastUtils
					.show(GlobalContext.getInstance(), bean.getRespMsg() + "");
			h.sendEmptyMessage(BundleArgsConstants.REQUEST_FAIL);
		}
	}
}
