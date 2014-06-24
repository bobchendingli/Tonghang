package com.csb.ui.login;

import java.util.Arrays;
import java.util.List;

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
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.GlobalContext;

public class XueshuActivity extends Activity implements OnClickListener {
	private static final String TAG = XueshuActivity.class.getSimpleName();
	private Button btn_get_verificationno, btn_title_left, btn_title_right;
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

	private CheckBox[] cb_xs;
	private String[] valueArr = new String[] { "专业理论知识", "临床思维与实践技能", "临床研究方法",
			"转化性研究技能", "疾病管理方法", "患者教育技能", "患者心理支持/治疗技能", "病患沟通技能", "演讲技能",
			"领导力培养与科室管理技能", "医事相关法律知识" };
	private EditText et_other;

	private UserBean userBean;
	private String xueshu;
	private Context context = GlobalContext.getInstance();

	public static Intent newIntent() {
		return new Intent(GlobalContext.getInstance(), XueshuActivity.class);
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
		outState.putString(BundleArgsConstants.XUESHU_EXTRA, xueshu);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			userBean = savedInstanceState
					.getParcelable(BundleArgsConstants.USERBEAN_EXTRA);
			xueshu = savedInstanceState
					.getString(BundleArgsConstants.XUESHU_EXTRA);
		} else {
			Intent intent = getIntent();
			userBean = intent
					.getParcelableExtra(BundleArgsConstants.USERBEAN_EXTRA);
			xueshu = intent.getStringExtra(BundleArgsConstants.XUESHU_EXTRA);
		}
		setContentView(R.layout.xueshu);
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
		cb_xs = new CheckBox[] { cb_xs1, cb_xs2, cb_xs3, cb_xs4, cb_xs5,
				cb_xs6, cb_xs7, cb_xs8, cb_xs9, cb_xs10, cb_xs11 };
		if (!TextUtils.isEmpty(xueshu)) {
			String[] xueshuArr = xueshu.split("\n");
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
