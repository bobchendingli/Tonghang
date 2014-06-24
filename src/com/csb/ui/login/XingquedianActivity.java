package com.csb.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.csb.R;
import com.csb.bean.UserBean;
import com.csb.ui.main.MainActivity;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.GlobalContext;

public class XingquedianActivity extends Activity implements OnClickListener {
	private static final String TAG = XingquedianActivity.class.getSimpleName();
	private Button btn_title_left, btn_title_right;
	private TextView tv_top_title;
	private CheckBox cb_xs1;
	private CheckBox cb_xs2;
	private CheckBox cb_xs3;
	private CheckBox cb_xs4;
	private CheckBox cb_xs5;
	private CheckBox cb_xs6;
	private CheckBox cb_xs7;
	private CheckBox cb_xs8;
	private CheckBox cb_xs9;
	private CheckBox cb_xs10;
	private CheckBox cb_xs11;
	private CheckBox cb_xs12;
	private CheckBox cb_xs13;
	private CheckBox cb_xs14;
	private CheckBox cb_xs15;
	private CheckBox cb_xs16;
	private CheckBox cb_xs17;

	private CheckBox[] cb_xs;
	private String[] valueArr = new String[] { "基础医学", "心脏药学", "高学压", "心律失常",
			"心衰/左室功能障碍", "瓣膜病", "肺血管病", "心肌/心包疾病", "先天性心脏病与儿童心脏病",
			"缺血性心脏病(含心脏急症)", "冠脉介入", "外周血管病", "卒中", "心脏手术", "预防、康复、运动和护理",
			"心脏影像", "数码电子技术" };
	private EditText et_other;

	private String xingquedian;
	private UserBean userBean;
	private Context context = GlobalContext.getInstance();

	public static Intent newIntent() {
		return new Intent(GlobalContext.getInstance(),
				XingquedianActivity.class);
	}

	public static Intent newIntent(UserBean bean) {
		Intent intent = newIntent();
		// intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.putExtra(BundleArgsConstants.USERBEAN_EXTRA, bean);
		return intent;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(BundleArgsConstants.USERBEAN_EXTRA, userBean);
		outState.putString(BundleArgsConstants.XINGQUEDIAN_EXTRA, xingquedian);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			userBean = savedInstanceState
					.getParcelable(BundleArgsConstants.USERBEAN_EXTRA);
			xingquedian = savedInstanceState
					.getString(BundleArgsConstants.XINGQUEDIAN_EXTRA);
		} else {
			Intent intent = getIntent();
			userBean = intent
					.getParcelableExtra(BundleArgsConstants.USERBEAN_EXTRA);
			xingquedian = intent
					.getStringExtra(BundleArgsConstants.XINGQUEDIAN_EXTRA);
		}
		setContentView(R.layout.xingquedian);
		initView();
	}

	private void initView() {
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("学术与职业生涯规划需求");
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setOnClickListener(this);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		et_other = (EditText) findViewById(R.id.et_other);
		btn_title_right.setText("确定");
		btn_title_right.setOnClickListener(this);

		cb_xs1 = (CheckBox) findViewById(R.id.cb_xs1);
		cb_xs2 = (CheckBox) findViewById(R.id.cb_xs2);
		cb_xs3 = (CheckBox) findViewById(R.id.cb_xs3);
		cb_xs4 = (CheckBox) findViewById(R.id.cb_xs4);
		cb_xs5 = (CheckBox) findViewById(R.id.cb_xs5);
		cb_xs6 = (CheckBox) findViewById(R.id.cb_xs6);
		cb_xs7 = (CheckBox) findViewById(R.id.cb_xs7);
		cb_xs8 = (CheckBox) findViewById(R.id.cb_xs8);
		cb_xs9 = (CheckBox) findViewById(R.id.cb_xs9);
		cb_xs10 = (CheckBox) findViewById(R.id.cb_xs10);
		cb_xs11 = (CheckBox) findViewById(R.id.cb_xs11);
		cb_xs12 = (CheckBox) findViewById(R.id.cb_xs12);
		cb_xs13 = (CheckBox) findViewById(R.id.cb_xs13);
		cb_xs14 = (CheckBox) findViewById(R.id.cb_xs14);
		cb_xs15 = (CheckBox) findViewById(R.id.cb_xs15);
		cb_xs16 = (CheckBox) findViewById(R.id.cb_xs16);
		cb_xs17 = (CheckBox) findViewById(R.id.cb_xs17);
		cb_xs = new CheckBox[] { cb_xs1, cb_xs2, cb_xs3, cb_xs4, cb_xs5,
				cb_xs6, cb_xs7, cb_xs8, cb_xs9, cb_xs10, cb_xs11, cb_xs12,
				cb_xs13, cb_xs14, cb_xs15, cb_xs16, cb_xs17 };

		if (!TextUtils.isEmpty(xingquedian)) {
			String[] xueshuArr = xingquedian.split("\n");
			for (int i = 0; i < xueshuArr.length; i++) {
				int index = search(valueArr, xueshuArr[i]);
				if (index >= 0 && index < valueArr.length) {
					cb_xs[index].setChecked(true);
				} else {
					et_other.setText(et_other.getText().toString()
							+ xueshuArr[i]);
				}
			}
		}
	}

	private int search(String[] arr, String value) {
		int r = -1;
		for (int i = 0; i < arr.length; i++) {
			if (value.equals(arr[i])) {
				r = i;
				break;
			}

		}
		return r;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_right:
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < cb_xs.length; i++) {
				if (cb_xs[i].isChecked())
					sb.append(valueArr[i]).append("\n");
			}
			if (!TextUtils.isEmpty(et_other.getText())) {
				sb.append(et_other.getText());
			}
			this.setResult(RESULT_OK, new Intent().setAction(sb.toString()));
			this.finish();
			break;
		case R.id.btn_title_left:
			finish();
			break;
		}
	}

}
