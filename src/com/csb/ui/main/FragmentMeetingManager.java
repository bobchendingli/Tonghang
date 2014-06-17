package com.csb.ui.main;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.csb.R;
import com.csb.bean.MeetingItemBean;
import com.csb.bean.MeetingListBean;
import com.csb.dao.meeting.GetMeetingListDao;
import com.csb.support.error.WeiboException;
import com.csb.ui.meeting.MeetingDetailActivity;
import com.csb.ui.view.dropdownlist.DropDownListView;
import com.csb.ui.view.dropdownlist.DropDownListView.OnDropDownListener;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.GlobalContext;
import com.csb.utils.ToastUtils;

public class FragmentMeetingManager extends Fragment {
	private static List<MeetingItemBean> listItems = new ArrayList<MeetingItemBean>();
	private DropDownListView listView = null;
	private EfficientAdapter adapter;
	public static final int MORE_DATA_MAX_COUNT = 3;
	public int moreDataCount = 0;
	private Context context = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_meetingmanager,
				container, false);
		context = view.getContext();

		listView = (DropDownListView) view.findViewById(R.id.list_view_meeting);
		// set drop down listener
		listView.setOnDropDownListener(new OnDropDownListener() {

			@Override
			public void onDropDown() {
				new GetDataTask(true).execute();
			}
		});

		// set on bottom listener
		listView.setOnBottomListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// new GetDataTask(false).execute();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MeetingItemBean bean = listItems.get(position);
				Intent intent = new Intent(context, MeetingDetailActivity.class);
				intent.putExtra(BundleArgsConstants.MEETING_EXTRA, bean);
				startActivity(intent);
			}
		});
		listView.setShowFooterWhenNoMore(false);
		listView.setDropDownStyle(false);

		/*
		 * listItems = new LinkedList<Map<String, Object>>(); Map<String,
		 * Object> m1 = new HashMap<String, Object>(); m1.put("title",
		 * "高压学院压学院高压学院高压学院高压学院高1"); m1.put("date", "2014-01-02");
		 * m1.put("status", 0); listItems.add(m1); Map<String, Object> m2 = new
		 * HashMap<String, Object>(); m2.put("title", "高压学院压学院高压学院高压学院高压学院高2");
		 * m2.put("date", "2014-01-03"); m2.put("status", 2); listItems.add(m2);
		 * Map<String, Object> m3 = new HashMap<String, Object>();
		 * m3.put("title", "高压学院压学院高压学院高压学院高压学院高3s"); m3.put("date",
		 * "2014-01-03"); m3.put("status", 3); listItems.add(m3); Map<String,
		 * Object> m4 = new HashMap<String, Object>(); m4.put("title",
		 * "高压学院压学院高压学院高压学院高压学院高1"); m4.put("date", "2014-01-02");
		 * m4.put("status", 4); listItems.add(m4); Map<String, Object> m5 = new
		 * HashMap<String, Object>(); m5.put("title", "高压学院压学院高压学院高压学院高压学院高1");
		 * m5.put("date", "2014-01-02"); m5.put("status", 1); listItems.add(m5);
		 * Map<String, Object> m6 = new HashMap<String, Object>();
		 * m6.put("title", "高压学院压学院高压学院高压学院高压学院高1"); m6.put("date",
		 * "2014-01-02"); m6.put("status", 1); listItems.add(m6);
		 */

		// listItems.addAll(Arrays.asList(mStrings));

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		adapter = new EfficientAdapter(context);
		listView.setAdapter(adapter);
		new GetDataTask(true).execute();
	}

	private class GetDataTask extends
			AsyncTask<Void, Void, List<MeetingItemBean>> {

		private boolean isDropDown;

		public GetDataTask(boolean isDropDown) {
			this.isDropDown = isDropDown;
		}

		@Override
		protected List<MeetingItemBean> doInBackground(Void... params) {
			myHandler.sendEmptyMessage(BundleArgsConstants.REQUEST_START);
			GetMeetingListDao dao = new GetMeetingListDao(GlobalContext
					.getInstance().getUserBean().getUserid());
			try {
				MeetingListBean bean = dao.get();
				if (bean != null && bean.getMeetingList() != null){
					listItems.clear();
					listItems.addAll(bean.getMeetingList());
				}
				myHandler.sendEmptyMessage(BundleArgsConstants.REQUEST_SUCC);
			} catch (WeiboException e) {
				Message msg = new Message();
				msg.what = BundleArgsConstants.REQUEST_FAIL;
				msg.obj = e.getError();
				myHandler.sendMessage(msg);
				e.printStackTrace();
			}
			return listItems;
		}

		@Override
		protected void onPostExecute(List<MeetingItemBean> result) {
			adapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
	}

	@SuppressLint("ResourceAsColor")
	private static class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		// private Bitmap mIcon1;
		// private Bitmap mIcon2;

		public EfficientAdapter(Context context) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);

		}

		/**
		 * The number of items in the list is determined by the number of
		 * speeches in our array.
		 * 
		 * @see android.widget.ListAdapter#getCount()
		 */
		public int getCount() {
			return listItems.size();
		}

		/**
		 * Since the data comes from an array, just returning the index is
		 * sufficent to get at the data. If we were using a more complex data
		 * structure, we would return whatever object represents one row in the
		 * list.
		 * 
		 * @see android.widget.ListAdapter#getItem(int)
		 */
		public Object getItem(int position) {
			return position;
		}

		/**
		 * Use the array index as a unique id.
		 * 
		 * @see android.widget.ListAdapter#getItemId(int)
		 */
		public long getItemId(int position) {
			return position;
		}

		/**
		 * Make a view to hold each row.
		 * 
		 * @see android.widget.ListAdapter#getView(int, android.view.View,
		 *      android.view.ViewGroup)
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			// A ViewHolder keeps references to children views to avoid
			// unneccessary calls
			// to findViewById() on each row.
			ViewHolder holder;

			// When convertView is not null, we can reuse it directly, there is
			// no need
			// to reinflate it. We only inflate a new View when the convertView
			// supplied
			// by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.listview_item_meetingmanager, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.item_title_tv);
				holder.date = (TextView) convertView
						.findViewById(R.id.item_date_tv);
				holder.status = (TextView) convertView
						.findViewById(R.id.item_status_tv);

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			// Bind the data efficiently with the holder.
			MeetingItemBean item = listItems.get(position);
			holder.title.setText(item.getMeeting_name());
			holder.date.setText(item.getMeeting_time());
			try {
				formatStatus(Integer.valueOf(item.getUser_status()),
						holder.status);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return convertView;
		}

		static class ViewHolder {
			TextView title;
			TextView date;
			TextView status;
		}

		private void formatStatus(int status, TextView tv) {
			String result = null;
			int orange = tv.getContext().getResources()
					.getColor(R.color.orange);
			int gray = tv.getContext().getResources()
					.getColor(R.color.black);
			tv.setTextColor(gray);
			switch (status) {
			case 0:
				result = "未接受邀请";
				tv.setTextColor(orange);
				break;
			case 1:
				result = "已报名";
				break;
			case 2:
				result = "已签到但未填写会前问卷";
				break;
			case 3:
				result = "已签到已填写会前问卷";
				break;
			case 4:
				result = "会议结束但未填写会后问卷";
				break;
			case 5:
				result = "会议结束已填写会后问卷";
				break;
			default:
				result = "";
			}
			tv.setText(result);
		}
	}

	private ProgressDialog dialog = null;

	private void showDialog() {
		dialog = new ProgressDialog(context);
		dialog.setMessage("获取数据中,请稍候...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.show();
	}

	private void dismissDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case BundleArgsConstants.REQUEST_START:
				showDialog();
				break;
			case BundleArgsConstants.REQUEST_SUCC:
				dismissDialog();
				break;
			case BundleArgsConstants.REQUEST_FAIL:
				if (msg.obj != null)
					ToastUtils.show(context, msg.obj.toString());
				dismissDialog();
			default:
				break;
			}
		}
	};
}
