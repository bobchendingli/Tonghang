package com.csb.ui.meeting;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.csb.R;
import com.csb.bean.FileBean;
import com.csb.bean.MeetingItemBean;
import com.csb.bean.ResultFileDownloadBean;
import com.csb.dao.URLHelper;
import com.csb.dao.meeting.GetFileDownloadListDao;
import com.csb.support.error.WeiboException;
import com.csb.support.file.FileDownloaderHttpHelper;
import com.csb.support.file.FileManager;
import com.csb.support.http.HttpUtility;
import com.csb.ui.view.dropdownlist.DropDownListView;
import com.csb.ui.view.dropdownlist.DropDownListView.OnDropDownListener;
import com.csb.utils.BundleArgsConstants;
import com.csb.utils.Constants;
import com.csb.utils.GlobalContext;
import com.csb.utils.IntentUtils;
import com.csb.utils.ToastUtils;

public class MeetingDownloadListActivity extends Activity implements
		OnClickListener {
	private Button btn_title_left, btn_title_right;
	private TextView tv_top_title;

	private static LinkedList<FileBean> listItems = new LinkedList<FileBean>();
	private DropDownListView listView = null;
	private EfficientAdapter adapter;
	public static final int MORE_DATA_MAX_COUNT = 3;
	public int moreDataCount = 0;
	private Context context = null;
	private DownloadFile downloadFile = null;

	private MeetingItemBean meetingItemBean;
	private GetDataTask mGetDataTask;
	private List<String> mExistsDownloadFileList = null;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(BundleArgsConstants.MEETING_EXTRA,
				meetingItemBean);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			meetingItemBean = savedInstanceState
					.getParcelable(BundleArgsConstants.MEETING_EXTRA);
		} else {
			Intent intent = getIntent();
			meetingItemBean = intent
					.getParcelableExtra(BundleArgsConstants.MEETING_EXTRA);
		}

		setContentView(R.layout.listview_download);
		context = this;
		initView();
		mExistsDownloadFileList = getExistsDownloadFile();
		listView = (DropDownListView) findViewById(R.id.list_view_download);
		// set drop down listener
		listView.setOnDropDownListener(new OnDropDownListener() {
			@Override
			public void onDropDown() {

			}
		});

		// set on bottom listener
		listView.setOnBottomListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if (mGetDataTask == null
				// || mGetDataTask.isCancelled()
				// || mGetDataTask.getStatus().equals(
				// AsyncTask.Status.FINISHED)) {
				// mGetDataTask = new GetDataTask(false);
				// mGetDataTask.execute();
				// }
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(context,
						MeetingDownloadDetailActivity.class);
				FileBean fileBean = listItems.get(position);
				
				

				String fileName = fileBean.getPicurl();
				if (fileName != null && fileName.contains("/")) {
					fileName = fileName.substring(
							fileName.lastIndexOf("/") + 1, fileName.length());
				}
				
				if(!mExistsDownloadFileList.contains(fileName)) {
					return;
				}
				
				final String extName = fileName.substring(
						fileName.lastIndexOf("."), fileName.length());
				final String path = Environment.getExternalStorageDirectory()
						.getPath()
						+ Constants.SD_FILE_MEETING
						+ File.separator
						+ fileName;
				if (fileName != null) {
					if (Constants.TXT.equalsIgnoreCase(extName)) {
						intent.putExtra(BundleArgsConstants.FILEBEAN_EXTRA,
								fileBean);
						startActivity(intent);
					} else if (Constants.PDF.equalsIgnoreCase(extName)) {
						startActivity(IntentUtils.getPdfFileIntent(path));
					} else if (Constants.PIC_LIST.contains(extName)) {
						intent.putExtra(BundleArgsConstants.FILEBEAN_EXTRA,
								fileBean);
						startActivity(intent);
					} else if (Constants.DOC_LIST.contains(extName)) {
						startActivity(IntentUtils.getWordFileIntent(path));
					} else if (Constants.XlS_LIST.contains(extName)) {
						startActivity(IntentUtils.getExcelFileIntent(path));
					} else if (Constants.PPT_LIST.contains(extName)) {
						startActivity(IntentUtils.getPptFileIntent(path));
					} else {
						ToastUtils.show(context, "不支持显示此" + extName + "文件格式");
					}
				}
			}
		});
		listView.setShowFooterWhenNoMore(false);
		listView.setDropDownStyle(false);

		adapter = new EfficientAdapter(context);
		listView.setAdapter(adapter);
	}

	private List<String> getExistsDownloadFile() {
		List<String> result = new ArrayList<String>();
		File meetingFile = new File(Environment.getExternalStorageDirectory()
				.getPath() + Constants.SD_FILE_MEETING);
		if (meetingFile.exists() && meetingFile.isDirectory()) {
			File[] files = meetingFile.listFiles();
			if (files != null && files.length > 0) {
				for (File file : files) {
					result.add(file.getName());
				}
			}
		}
		return result;
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mGetDataTask == null || mGetDataTask.isCancelled()
				|| mGetDataTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
			mGetDataTask = new GetDataTask(false);
			mGetDataTask.execute();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mGetDataTask != null
				&& mGetDataTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
			mGetDataTask.cancel(true);
		}
	}

	private void initView() {
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_title.setText("资料下载");
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
		switch (v.getId()) {
		case R.id.btn_title_left:
			MeetingDownloadListActivity.this.finish();
			break;
		}
	}

	private class GetDataTask extends
			AsyncTask<Void, Void, LinkedList<FileBean>> {
		private boolean isDropDown;

		public GetDataTask(boolean isDropDown) {
			this.isDropDown = isDropDown;
		}

		@Override
		protected LinkedList<FileBean> doInBackground(Void... params) {
			mHandler.sendEmptyMessage(BundleArgsConstants.REQUEST_START);
			GetFileDownloadListDao dao = new GetFileDownloadListDao(
					GlobalContext.getInstance().getUserBean().getUserid(),
					meetingItemBean.getMeeting_id());
			try {
				ResultFileDownloadBean bean = dao.get();
				listItems.clear();
				if (bean != null && bean.getFiles() != null) {
					listItems.addAll(bean.getFiles());
					mHandler.sendEmptyMessage(BundleArgsConstants.REQUEST_SUCC);
				} else {
					mHandler.sendEmptyMessage(BundleArgsConstants.IS_NOT_DOWNLOADFILE);
				}

			} catch (WeiboException e) {
				Message msg = new Message();
				msg.what = BundleArgsConstants.REQUEST_FAIL;
				msg.obj = e.getError();
				mHandler.sendMessage(msg);
				e.printStackTrace();
			}
			return listItems;
		}

		@Override
		protected void onPostExecute(LinkedList<FileBean> result) {
			adapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
	}

	private class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// A ViewHolder keeps references to children views to avoid
			// unneccessary calls
			// to findViewById() on each row.
			final ViewHolder holder;
			final FileBean fileBean = listItems.get(position);
			String tempName = fileBean.getPicurl();
			if (tempName != null && tempName.contains("/")) {
				tempName = tempName.substring(tempName.lastIndexOf("/") + 1,
						tempName.length());
			}
			final String fileName = tempName;

			// When convertView is not null, we can reuse it directly, there is
			// no need
			// to reinflate it. We only inflate a new View when the convertView
			// supplied
			// by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.listview_item_download, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.item_title_tv);
				holder.status = (Button) convertView
						.findViewById(R.id.btn_item_download);
				holder.status.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!FileManager.isExternalStorageMounted()) {
							ToastUtils.show(context, "手机无SD卡,请插入SD卡后再重试！");
						} else if (downloadFile == null
								|| downloadFile.getStatus() != AsyncTask.Status.RUNNING) {

							downloadFile = new DownloadFile(
									URLHelper.URL_SERVER + fileBean.getPicurl(),
									Environment.getExternalStorageDirectory()
											.getPath()
											+ Constants.SD_FILE_MEETING
											+ File.separator + fileName,
									position);
							downloadFile.execute();
						}
					}
				});

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			// Bind the data efficiently with the holder.

			holder.title.setText(fileBean.getTitle());
			initStatus(fileName, holder.status);
			return convertView;
		}

		class ViewHolder {
			TextView title;
			Button status;
		}

		private void initStatus(String fileName, Button btn) {
			if (mExistsDownloadFileList.contains(fileName)) {
				btn.setEnabled(false);
				btn.setText("已下载");
				btn.setBackgroundDrawable(btn.getContext().getResources()
						.getDrawable(R.drawable.bt_blue_select));
			} else {
				btn.setEnabled(true);
				btn.setText("下载");
				btn.setBackgroundDrawable(btn.getContext().getResources()
						.getDrawable(R.drawable.bt_orange_select));

			}
		}
	}

	private class DownloadFile extends AsyncTask<Void, Void, Boolean> {
		private String url;
		private String path;
		private int postion;

		public DownloadFile(String url, String path, int postion) {
			this.url = url;
			this.path = path;
			this.postion = postion;
		}

		@Override
		protected void onPreExecute() {
			Message msg = new Message();
			msg.what = DOWNLOAD_START;
			mHandler.sendMessage(msg);
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				return HttpUtility.getInstance().executeDownloadTask(url, path,
						new FileDownloaderHttpHelper.DownloadListener() {
							@Override
							public void pushProgress(int progress, int max) {
								Bundle bundle = new Bundle();
								bundle.putInt("progress", progress);
								bundle.putInt("max", max);
								Message msg = new Message();
								msg.what = DOWNLOAD_PROGRESS;
								msg.obj = bundle;
								mHandler.sendMessage(msg);
							}
						});
			} catch (Exception e) {
				Message msg = new Message();
				msg.what = DOWNLOAD_ERROR;
				mHandler.sendMessage(msg);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
			if (result) {
				Message msg = new Message();
				msg.what = DOWNLOAD_SUCC;
				msg.obj = postion;
				mHandler.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = DOWNLOAD_ERROR;
				mHandler.sendMessage(msg);
			}
			super.onPostExecute(result);
		}
	}

	// 下载
	private ProgressDialog mProgressDialog;
	
	private void showProgressDialog() {
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// mProgressDialog.setTitle(title);
		mProgressDialog.setMessage("资料下载");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setButton(context.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog,
							final int which) {
						if(downloadFile != null){
							downloadFile.cancel(true);
						}
						mProgressDialog.dismiss();
					}
				});
		mProgressDialog.show();
	}

	private void showDialog() {
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setMessage("数据处理中,请稍候...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();
	}

	private void dismissDialog() {
		if(MeetingDownloadListActivity.this != null && !MeetingDownloadListActivity.this.isFinishing()){
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		}
	}

	private final static int DOWNLOAD_PROGRESS = 100;
	private final static int DOWNLOAD_ERROR = 101;
	private final static int DOWNLOAD_SUCC = 102;
	private final static int DOWNLOAD_START = 103;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case DOWNLOAD_START:
				showProgressDialog();
				break;
			case DOWNLOAD_ERROR:
				Toast.makeText(context, "文件下载失败！", Toast.LENGTH_SHORT).show();
				break;
			case DOWNLOAD_PROGRESS:
				if (msg.obj != null && msg.obj instanceof Bundle) {
					mProgressDialog.setMax(((Bundle) msg.obj).getInt("max"));
					mProgressDialog.setProgress(((Bundle) msg.obj)
							.getInt("progress"));
				}
				break;
			case DOWNLOAD_SUCC:
				Toast.makeText(context, "资料下载成功!", Toast.LENGTH_SHORT).show();
				if (mProgressDialog != null && mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
				if (msg.obj != null) {
					int postion = (Integer) msg.obj;
					View view = listView.getChildAt(postion);
					Button btn = (Button) view
							.findViewById(R.id.btn_item_download);
					btn.setEnabled(false);
					btn.setText("已下载");
					btn.setBackgroundDrawable(btn.getContext().getResources()
							.getDrawable(R.drawable.bt_blue_select));
					mExistsDownloadFileList = getExistsDownloadFile();
				}
				break;
			case BundleArgsConstants.REQUEST_START:
				showDialog();
				break;
			case BundleArgsConstants.REQUEST_SUCC:
				dismissDialog();
				break;
			case BundleArgsConstants.REQUEST_FAIL:
				dismissDialog();
				break;
			case BundleArgsConstants.IS_NOT_DOWNLOADFILE:
				Toast.makeText(context, "暂无下载资料!", Toast.LENGTH_SHORT).show();
				dismissDialog();
			}
		}
	};

}
