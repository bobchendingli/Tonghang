package com.csb.ui.login;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.csb.R;
import com.csb.support.settinghelper.SettingUtility;

public class WelcomeActivity extends Activity implements OnPageChangeListener {
	/** 是不是启动activity */
	public static Boolean entryStart = true;
	/** 滚动布局 */
	private ViewPager viewPager;
	private List<View> viewList;

	/** 点图片 */
	private ImageView point0;
	private ImageView point1;

	private ImageView lastPoint;
	private View view1, view2;
	private Button okBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initViews();
	}

	private void jumpLoginActivity() {
		Intent i = new Intent(WelcomeActivity.this, LoginActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		i.putExtra("login", true);
		startActivity(i);
		this.finish();
		return;
	}

	public void initViews() {
		setContentView(R.layout.welcome);
		viewPager = (ViewPager) findViewById(R.id.viewpager);

		LayoutInflater lf = LayoutInflater.from(this);
		view1 = lf.inflate(R.layout.welcome_p1, null);
		view2 = lf.inflate(R.layout.welcome_p2, null);

		viewList = new ArrayList<View>();
		viewList.add(view1);
		viewList.add(view2);
		okBtn = (Button) view2.findViewById(R.id.okbutton);

		okBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// SettingUtility.setFirstStartWelcome(false);
				SettingUtility.setFirstStart(false);
				jumpLoginActivity();
			}
		});

		point0 = (ImageView) findViewById(R.id.point0);
		point1 = (ImageView) findViewById(R.id.point1);

		point0.setImageDrawable(this.getResources().getDrawable(
				R.drawable.userguide_point2));
		lastPoint = point0;

		PagerAdapter pagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {

				return arg0 == arg1;
			}

			@Override
			public int getCount() {

				return viewList.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(viewList.get(position));

			}

			@Override
			public int getItemPosition(Object object) {

				return super.getItemPosition(object);
			}

			@Override
			public CharSequence getPageTitle(int position) {

				return "";
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(viewList.get(position));
				return viewList.get(position);
			}

		};

		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		lastPoint.setImageDrawable(this.getResources().getDrawable(
				R.drawable.userguide_point1));
		switch (arg0) {
		case 0:
			point0.setImageDrawable(this.getResources().getDrawable(
					R.drawable.userguide_point2));
			lastPoint = point0;
			break;

		case 1:
			point1.setImageDrawable(this.getResources().getDrawable(
					R.drawable.userguide_point2));
			lastPoint = point1;
			break;
		default:
			break;
		}
	}

}
