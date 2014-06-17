package com.csb.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.csb.R;

/**
 * 朋友圈页面
 * 
 * @author Administrator
 * 
 */
public class FragmentMyClass extends Fragment implements OnClickListener{
	private TextView tv_tips;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_myclass, container, false);
		tv_tips = (TextView) view.findViewById(R.id.tv_tips);
		tv_tips.setOnClickListener(this);
		return view;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}
