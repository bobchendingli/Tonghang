package com.csb.ui.task;

import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.csb.R;
import com.csb.bean.UserBean;
import com.csb.dao.user.LoginDao;
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
public class LoginAsyncTask extends
		MyAsyncTask<Void, UserBean, UserBean> {

	private UserBean userBean;
	private WeiboException e;
	private Handler h;

	public LoginAsyncTask(UserBean userBean, Handler h) {
		this.userBean = userBean;
		this.h = h;
	}
	
	
	@Override
	protected UserBean doInBackground(Void... params) {
		h.sendEmptyMessage(BundleArgsConstants.REQUEST_START);
		LoginDao dao = new LoginDao(userBean);
		try {
			return dao.login();
		} catch (WeiboException e) {
			this.e = e;
			cancel(true);
			return null;
		}
	}

	@Override
	protected void onCancelled(UserBean bean) {
		super.onCancelled(bean);
		if (bean == null && this.e != null)
			Toast.makeText(GlobalContext.getInstance(), e.getError(),
					Toast.LENGTH_SHORT).show();
		h.sendEmptyMessage(BundleArgsConstants.REQUEST_FAIL);
	}

	@Override
	protected void onPostExecute(UserBean bean) {
		super.onPostExecute(bean);
		if (bean != null
				&& BundleArgsConstants.RESTURN_CODE_SUCC.equals(bean
						.getRespCode())) {
			ToastUtils.show(GlobalContext.getInstance(),
					R.string.login_successfully);
			bean.setPassword(userBean.getPassword());
			GlobalContext.getInstance().setUserBean(bean);
			h.sendEmptyMessage(BundleArgsConstants.REQUEST_SUCC);
		} else {
			String failMessage = (bean == null || TextUtils.isEmpty(bean.getRespMsg())) ? "登录失败!" : bean.getRespMsg();
			ToastUtils
					.show(GlobalContext.getInstance(), failMessage);
			h.sendEmptyMessage(BundleArgsConstants.REQUEST_FAIL);
		}
	}
}
