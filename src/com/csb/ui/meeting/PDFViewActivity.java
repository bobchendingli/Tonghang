package com.csb.ui.meeting;

import static java.lang.String.format;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.csb.R;
import com.csb.utils.GlobalContext;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

public class PDFViewActivity extends SherlockActivity implements
		OnPageChangeListener, OnClickListener {

	public static final String SAMPLE_FILE = "sample.pdf";

	public static final String ABOUT_FILE = "about.pdf";

	private Button btn_title_left, btn_title_right;
	private TextView tv_top_title;

	PDFView pdfView;

	String pdfName = SAMPLE_FILE;

	Integer pageNumber = 1;
	
	private String title;
	private String path;
	
	public static Intent newIntent() {
		return new Intent(GlobalContext.getInstance(),
				PDFViewActivity.class);
	}

	public static Intent newIntent(String title, String path) {
		Intent intent = newIntent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.putExtra("title", title);
		intent.putExtra("path", path);
		return intent;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("title", title);
		outState.putString("path", path);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			title = savedInstanceState.getString("title");
			path = savedInstanceState.getString("path");
		} else {
			Intent intent = getIntent();
			title = intent.getStringExtra("title");
			path = intent.getStringExtra("path");
		}
		setContentView(R.layout.pdf_view);
		pdfView = (PDFView) findViewById(R.id.pdfView);
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("我的信息");
		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setText("返回");
		btn_title_left.setOnClickListener(this);
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		btn_title_right.setVisibility(View.GONE);

		display(pdfName, false);
	}

	private void display(String fileName, boolean jumpToFirstPage) {
		if (jumpToFirstPage)
			pageNumber = 1;
		File file = new File(fileName);
		if (file.exists()) {
			pdfView.fromFile(file).defaultPage(pageNumber).onPageChange(this)
					.load();
		}
	}

	@Override
	public void onPageChanged(int page, int pageCount) {
		pageNumber = page;
		setTitle(format("%s / %s", page, pageCount));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			this.finish();
			break;
		}
	}
}
