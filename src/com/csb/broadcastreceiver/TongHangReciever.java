package com.csb.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.csb.support.http.VersionUpdateManager;

public class TongHangReciever extends BroadcastReceiver {
	public static TongHangReciever instance = null;
	private static String TAG = "TongHangReciever";

	@Override
	public synchronized void onReceive(Context context, Intent intent) {
		Log.i(TAG, "Start TongHangReciever.onReceive()");
		if ((context != null) && (intent != null)) {
			String action = intent.getAction();
			if (action.equals(VersionUpdateManager.ACTION_UPDATE_APK)) {
				VersionUpdateManager.createInstance(context);
				VersionUpdateManager
						.setUpdateMode(VersionUpdateManager.UPDATE_MODE_AUTO);
			} else {
				Log.e(TAG, "Received broadcast for " + action);
			}
		} // end of outer if
		Log.i(TAG, "End TongHangReciever.onReceive()");
	}// end of onReciever

}
