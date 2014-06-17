package com.csb.ui.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.csb.R;
import com.csb.utils.BundleArgsConstants;

public class XueYuanDetailActivity extends Activity implements
		OnClickListener {
	private Button btn_title_left, btn_title_right;
	private TextView tv_top_title, tv_content;
	private Context context = null;
	private Bundle bundle;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bundle = getIntent().getExtras();
		setContentView(R.layout.xueyuan_detail);
		context = this;
		initView();
	}

	private void initView() {
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_content = (TextView) findViewById(R.id.tv_content);
		tv_top_title.setText(bundle.getString("title"));
		tv_content.setText(bundle.getString("context"));
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setBackgroundDrawable(context.getResources()
				.getDrawable(R.drawable.back_selector));
		btn_title_left.setText("");
		btn_title_left.setOnClickListener(this);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		btn_title_right.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_title_left:
			XueYuanDetailActivity.this.finish();
			break;
		}
	}

}
