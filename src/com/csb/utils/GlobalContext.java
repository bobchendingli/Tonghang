package com.csb.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;
import cn.trinea.android.common.entity.FailedReason;
import cn.trinea.android.common.service.impl.ImageCache;
import cn.trinea.android.common.service.impl.RemoveTypeLastUsedTimeFirst;
import cn.trinea.android.common.service.impl.ImageMemoryCache.OnImageCallbackListener;

import com.csb.R;
//import com.csb.bean.AccountBean;
import com.csb.bean.UserBean;
import com.csb.support.settinghelper.SettingUtility;

/**
 * User: Jiang Qi Date: 12-7-27
 */
public final class GlobalContext extends Application {

	// singleton
	private static GlobalContext globalContext = null;

	// image size
	private Activity activity = null;

	private Activity currentRunningActivity = null;

	private DisplayMetrics displayMetrics = null;


	// current account info
	private UserBean userBean = null;

	private Handler handler = new Handler();

	public boolean tokenExpiredDialogIsShowing = false;
	
	public static final String TAG_CACHE                = "image_cache";
	public static final String DEFAULT_CACHE_FOLDER     = new StringBuilder()
																.append(Environment.getExternalStorageDirectory()
																        .getAbsolutePath()).append(File.separator)
																.append("Tonghang").append(File.separator)
																.append("ImageCache").toString();
	

	@Override
	public void onCreate() {
		super.onCreate();
		globalContext = this;
//		buildCache();
		
		IMAGE_CACHE.initData(this, TAG_CACHE);
        IMAGE_CACHE.setContext(this);
        IMAGE_CACHE.setCacheFolder(DEFAULT_CACHE_FOLDER);
	}
	
	

	public static GlobalContext getInstance() {
		return globalContext;
	}

	public Handler getUIHandler() {
		return handler;
	}

	public DisplayMetrics getDisplayMetrics() {
		if (displayMetrics != null) {
			return displayMetrics;
		} else {
			Activity a = getActivity();
			if (a != null) {
				Display display = getActivity().getWindowManager()
						.getDefaultDisplay();
				DisplayMetrics metrics = new DisplayMetrics();
				display.getMetrics(metrics);
				this.displayMetrics = metrics;
				return metrics;
			} else {
				// default screen is 800x480
				DisplayMetrics metrics = new DisplayMetrics();
				metrics.widthPixels = 480;
				metrics.heightPixels = 800;
				return metrics;
			}
		}
	}
	
	public void setUserBean(final UserBean userBean) {
		this.userBean = userBean;
		SettingUtility.setDefaultUserBean(userBean);
	}
	
	public UserBean getUserBean() {
		if (userBean == null) {
			/*String id = SettingUtility.getDefaultAccountId();
			if (!TextUtils.isEmpty(id)) {
				accountBean = AccountDBTask.getAccount(id);
			} else {
				List<AccountBean> accountList = AccountDBTask.getAccountList();
				if (accountList != null && accountList.size() > 0) {
					accountBean = accountList.get(0);
				}
			}*/
		}

		return userBean;
	}
	/*public void setAccountBean(final AccountBean accountBean) {
		this.accountBean = accountBean;
	}

	public void updateUserInfo(final UserBean userBean) {
		this.accountBean.setInfo(userBean);
		handler.post(new Runnable() {
			@Override
			public void run() {
				for (MyProfileInfoChangeListener listener : profileListenerSet) {
					listener.onChange(userBean);
				}
			}
		});
	}*/


	private Set<MyProfileInfoChangeListener> profileListenerSet = new HashSet<MyProfileInfoChangeListener>();

	public void registerForAccountChangeListener(
			MyProfileInfoChangeListener listener) {
		if (listener != null) {
			profileListenerSet.add(listener);
		}
	}

	public void unRegisterForAccountChangeListener(
			MyProfileInfoChangeListener listener) {
		profileListenerSet.remove(listener);
	}

	public static interface MyProfileInfoChangeListener {

		public void onChange(UserBean newUserBean);
	}

	/*public String getCurrentAccountId() {
		return getAccountBean().getUid();
	}

	public String getCurrentAccountName() {

		return getAccountBean().getUsernick();
	}*/


	/*public String getSpecialToken() {
		if (getAccountBean() != null) {
			return getAccountBean().getAccess_token();
		} else {
			return "";
		}
	}*/

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Activity getCurrentRunningActivity() {
		return currentRunningActivity;
	}

	public void setCurrentRunningActivity(Activity currentRunningActivity) {
		this.currentRunningActivity = currentRunningActivity;
	}


	/** icon cache **/
    public static final ImageCache IMAGE_CACHE = new ImageCache(128, 512);

    static {
        /** init icon cache **/
        OnImageCallbackListener imageCallBack = new OnImageCallbackListener() {

            /**
             * callback function after get image successfully, run on ui thread
             * 
             * @param imageUrl imageUrl
             * @param loadedImage bitmap
             * @param view view need the image
             * @param isInCache whether already in cache or got realtime
             */
            @Override
            public void onGetSuccess(String imageUrl, Bitmap loadedImage, View view, boolean isInCache) {
                if (view != null && loadedImage != null) {
                    ImageView imageView = (ImageView)view;
                    imageView.setImageBitmap(loadedImage);
                    // first time show with animation
                    if (!isInCache) {
                        imageView.startAnimation(getInAlphaAnimation(2000));
                    }

                    // auto set height accroding to rate between height and weight
//                    LayoutParams imageParams = (LayoutParams)imageView.getLayoutParams();
//                    imageParams.height = imageParams.width * loadedImage.getHeight() / loadedImage.getWidth();
//                    imageView.setScaleType(ScaleType.FIT_XY);
                }
            }

            /**
             * callback function before get image, run on ui thread
             * 
             * @param imageUrl imageUrl
             * @param view view need the image
             */
            @Override
            public void onPreGet(String imageUrl, View view) {
                // Log.e(TAG_CACHE, "pre get image");
            }

            /**
             * callback function after get image failed, run on ui thread
             * 
             * @param imageUrl imageUrl
             * @param loadedImage bitmap
             * @param view view need the image
             * @param failedReason failed reason for get image
             */
            @Override
            public void onGetFailed(String imageUrl, Bitmap loadedImage, View view, FailedReason failedReason) {
                Log.e(TAG_CACHE,
                        new StringBuilder(128).append("get image ").append(imageUrl).append(" error, failed type is: ")
                                .append(failedReason.getFailedType()).append(", failed reason is: ")
                                .append(failedReason.getCause().getMessage()).toString());
            }

            @Override
            public void onGetNotInCache(String imageUrl, View view) {
                if (view != null && view instanceof ImageView) {
//                    ((ImageView)view).setImageResource(R.drawable.img_head);
                }
            }
        };
        IMAGE_CACHE.setOnImageCallbackListener(imageCallBack);
        IMAGE_CACHE.setCacheFullRemoveType(new RemoveTypeLastUsedTimeFirst<Bitmap>());

        IMAGE_CACHE.setHttpReadTimeOut(10000);
        IMAGE_CACHE.setOpenWaitingQueue(true);
        IMAGE_CACHE.setValidTime(-1);
        /**
         * close connection, default is connect keep-alive to reuse connection. if image is from different server, you
         * can set this
         */
        // IMAGE_CACHE.setRequestProperty("Connection", "false");
    }

    public static AlphaAnimation getInAlphaAnimation(long durationMillis) {
        AlphaAnimation inAlphaAnimation = new AlphaAnimation(0, 1);
        inAlphaAnimation.setDuration(durationMillis);
        return inAlphaAnimation;
    }
}
