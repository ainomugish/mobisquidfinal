package com.mobisquid.mobicash.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;



public class AppController extends com.orm.SugarApp {
	public AppController(){

	}
	public static volatile Handler applicationHandler = null;
	private static AppController mInstance;
	private RequestQueue mRequestQueue;
	private MyPreferenceManager pref;
	private ImageLoader mImageLoader;
	private static Context mCtx;

	public Gson gson;

	public boolean wsConnected = false;

	//TO STOP CHAT NOFITICAITON
	public boolean inChat = false;

	@Override
	public void onCreate() {
		super.onCreate();
		// SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));
		mInstance = this;
		applicationHandler = new Handler(getInstance().getMainLooper());

		mCtx = mInstance;
		mRequestQueue = getRequestQueue();

		gson = new Gson();


		mImageLoader = new ImageLoader(mRequestQueue,
				new ImageLoader.ImageCache() {
					private final LruCache<String, Bitmap>
							cache = new LruCache<String, Bitmap>(20);

					@Override
					public Bitmap getBitmap(String url) {
						return cache.get(url);
					}

					@Override
					public void putBitmap(String url, Bitmap bitmap) {
						cache.put(url, bitmap);
					}
				});
	}

	public static synchronized AppController getInstance() {

		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {

			mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req) {
		getRequestQueue().add(req);
	}
	public <T> void addRequest(Request<T> request, String tag) {
		request.setTag(tag);
		getRequestQueue().add(request);
	}

	public <T> void addRequest(Request<T> request) {
		addRequest(request);
	}
	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new LruBitmapCache());
		}
		return this.mImageLoader;
	}
	public MyPreferenceManager getPrefManager(Context context) {
		if (pref == null) {
			pref = new MyPreferenceManager(context);
		}
		return pref;
	}
}
