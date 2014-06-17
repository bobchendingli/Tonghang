package com.csb.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.csb.R;

/**
 * 主要控制左边按钮点击事件
 * 
 * @author Administrator
 * 
 */
public class LeftSlidingMenuFragment extends Fragment implements
		OnClickListener {
	private View homeBtnLayout;

	// private View circleBtnLayout;
	// private View settingBtnLayout;
	// private View groupBtnLayout;
	// private View listBtnLayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_left_fragment, container,
				false);
		homeBtnLayout = view.findViewById(R.id.homeBtnLayout);
		homeBtnLayout.setOnClickListener(this);
		// circleBtnLayout = view.findViewById(R.id.myPublishBtnLayout);
		// circleBtnLayout.setOnClickListener(this);
		// groupBtnLayout = view.findViewById(R.id.myCalendarBtnLayout);
		// groupBtnLayout.setOnClickListener(this);
		// settingBtnLayout = view.findViewById(R.id.myFavBtnLayout);
		// settingBtnLayout.setOnClickListener(this);
		// listBtnLayout = view.findViewById(R.id.listBtnLayout);
		// listBtnLayout.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		switch (v.getId()) {
		case R.id.homeBtnLayout:
			newContent = new FragmentOrderNum();
			homeBtnLayout.setSelected(true);
			// circleBtnLayout.setSelected(false);
			// settingBtnLayout.setSelected(false);
			// groupBtnLayout.setSelected(false);
			// listBtnLayout.setSelected(false);
			break;

		default:
			break;
		}

		if (newContent != null)
			switchFragment(newContent);

	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		MainActivity ra = (MainActivity) getActivity();
		ra.switchContent(fragment);

	}
}
