package com.csb.ui.main;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.csb.R;
import com.csb.bean.UserBean;
import com.csb.broadcastreceiver.TongHangReciever;
import com.csb.support.http.VersionUpdateManager;
import com.csb.support.settinghelper.SettingUtility;
import com.csb.ui.login.LoginActivity;
import com.csb.ui.login.RegisterActivity;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.GlobalContext;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {
	
	protected SlidingMenu leftRightSlidingMenu;
	private TextView tv_top_title;
	private Button btn_title_left;
	private Button btn_title_right;
	private Fragment mContent;

	private FragmentTabHost mTabHost = null;;
	private View indicator = null;
	private Dialog dialog = null;

	public static Intent newIntent() {
		return new Intent(GlobalContext.getInstance(),
				RegisterActivity.class);
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addCategory(Intent.CATEGORY_HOME);
		startActivity(i);
		// logout();
	}

	public void logout() {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("确实要注销吗?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						SettingUtility.setDefaultUserBean(new UserBean());
						jumpLoginActivity();
					}

				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}

				}).create();
		dialog.show();
	}

	private void jumpLoginActivity() {
		Intent i = new Intent(this, LoginActivity.class);
		startActivity(i);
		this.finish();
		return;
	}

	protected void exit() {
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.addCategory(Intent.CATEGORY_HOME);
		startActivity(i);
	}

	public static Intent newIntent(UserBean bean) {
		Intent intent = newIntent();
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(BundleArgsConstants.USERBEAN_EXTRA, bean);
		return intent;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLeftRightSlidingMenu();
		setContentView(R.layout.activity_main);
		initTabs();
		initView();
		
		sendUpdateBroadcastRepeat(this);
//		VersionUpdateManager.createInstance(this);
//		VersionUpdateManager.setUpdateMode(VersionUpdateManager.UPDATE_MODE_AUTO);
	}
	
	public void sendUpdateBroadcastRepeat(Context ctx){
	    Intent intent = new Intent(ctx, TongHangReciever.class);
	    intent.setAction(VersionUpdateManager.ACTION_UPDATE_APK);
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);
	    AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
	    am.cancel(pendingIntent);

	    long startTime = System.currentTimeMillis();
		long schedule = 12 * 60 * 60 * 1000;
	    am.setRepeating(AlarmManager.RTC_WAKEUP, startTime, schedule, pendingIntent);
	}
			
	public void cancelUpdateBroadcast(Context ctx){
	    AlarmManager am =  (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
	    Intent i = new Intent(ctx, TongHangReciever.class);  
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, i, 0);
	    am.cancel(pendingIntent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cancelUpdateBroadcast(this);
		mTabHost = null;
	}

	private void initTabs() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		// 添加tab名称和图标
		indicator = getIndicatorView("订阅号", R.layout.tab_item);
		mTabHost.addTab(mTabHost.newTabSpec("orderNo").setIndicator(indicator),
				FragmentOrderNum.class, null);

		indicator = getIndicatorView("我的课件", R.layout.tab_item);
		mTabHost.addTab(mTabHost.newTabSpec("myClass").setIndicator(indicator),
				FragmentMyClass.class, null);
		indicator = getIndicatorView("同行圈", R.layout.tab_item);
		mTabHost.addTab(
				mTabHost.newTabSpec("sameOccupation").setIndicator(indicator),
				FragmentSameOccupation.class, null);

		indicator = getIndicatorView("会议管理", R.layout.tab_item);
		mTabHost.addTab(
				mTabHost.newTabSpec("meetingManager").setIndicator(indicator),
				FragmentMeetingManager.class, null);
		mTabHost.setCurrentTab(0);
	}

	private View getIndicatorView(String name, int layoutId) {
		View v = getLayoutInflater().inflate(layoutId, null);
		TextView tv = (TextView) v.findViewById(R.id.tabText);
		tv.setText(name);
		return v;
	}

	private void initView() {
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("心血管同行");

		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		btn_title_right.setText("");
		btn_title_right.setBackgroundResource(R.drawable.bmp_menu);
//		btn_title_right.setBackgroundDrawable(getResources().getDrawable(
//				R.drawable.icon_gerenzhongxin));
		btn_title_right.setOnClickListener(this);

		btn_title_left = (Button) findViewById(R.id.btn_title_left);
		btn_title_left.setOnClickListener(this);
		btn_title_left.setText("");
		btn_title_left.setVisibility(View.INVISIBLE);
		btn_title_left.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.showleft_selector));

	}

	private void initLeftRightSlidingMenu() {
		mContent = new FragmentOrderNum();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.realtabcontent, mContent).commitAllowingStateLoss();
		setBehindContentView(R.layout.main_left_layout);
		FragmentTransaction leftFragementTransaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment leftFrag = new LeftSlidingMenuFragment();
		leftFragementTransaction.replace(R.id.main_left_fragment, leftFrag);
		leftFragementTransaction.commitAllowingStateLoss();
		// customize the SlidingMenu
		leftRightSlidingMenu = getSlidingMenu();
		leftRightSlidingMenu.setMode(SlidingMenu.RIGHT);// 设置是左滑还是右滑，还是左右都可以滑，我这里只做了左滑
		leftRightSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 设置菜单宽度
		leftRightSlidingMenu.setFadeDegree(0.35f);// 设置淡入淡出的比例
		leftRightSlidingMenu
				.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置手势模式
		leftRightSlidingMenu.setShadowDrawable(R.drawable.shadow);// 设置左菜单阴影图片
		leftRightSlidingMenu.setFadeEnabled(true);// 设置滑动时菜单的是否淡入淡出
		leftRightSlidingMenu.setBehindScrollScale(0.333f);// 设置滑动时拖拽效果

		leftRightSlidingMenu.setSecondaryMenu(R.layout.main_right_layout);
//		leftRightSlidingMenu.setRightMenuOffsetRes();
		FragmentTransaction rightFragementTransaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment rightFrag = new RightSlidingMenuFragment();
		leftFragementTransaction.replace(R.id.main_right_fragment, rightFrag);
		rightFragementTransaction.commitAllowingStateLoss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			leftRightSlidingMenu.showMenu();
			break;
		case R.id.btn_title_right:
			leftRightSlidingMenu.showSecondaryMenu(true);
			break;
		default:
			break;
		}

	}

	/**
	 * 左侧菜单点击切换首页的内容
	 */

	public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.realtabcontent, fragment).commitAllowingStateLoss();
		getSlidingMenu().showContent();
	}

	public void showContent() {
		getSlidingMenu().showContent();
	}

}
