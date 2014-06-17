package com.csb.ui.meeting;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.csb.R;
import com.csb.utils.ToastUtils;
/**
 * 关于
 * @author bobo
 *
 */
public class AboutActivity extends Activity implements OnClickListener {
	private Button btn_title_left, btn_title_right, btn_response, btn_update;
	private TextView tv_top_title;
	private Context context = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		context = this;
		initView();
	}

	private void initView() {
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("关于");
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setText("返回");
		btn_title_left.setOnClickListener(this);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		btn_title_right.setVisibility(View.INVISIBLE);
		
		btn_update = (Button) findViewById(R.id.btn_update);
		btn_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ToastUtils.show(context, "已经是最新版本");
			}
		});
//		btn_response = (Button) findViewById(R.id.btn_response);
//		btn_response.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_update:
			doUpdate();
			break;
//		case R.id.btn_response:
//			doResponse();
//			break;
		case R.id.btn_title_left:
			AboutActivity.this.finish();
			break;
		}
	}

	private void doResponse() {
		
	}

	private void doUpdate() {
		ToastUtils.show(context, "已经是最新版本");
	}

}
