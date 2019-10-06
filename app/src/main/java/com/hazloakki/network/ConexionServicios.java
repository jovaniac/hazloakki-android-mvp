package com.hazloakki.network;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;


public class ConexionServicios extends Application {
	private static ConexionServicios mInstance;
	private static Context mCtx;
	private RequestQueue mRequestQueue;
	public static final String TAG = ConexionServicios.class.getSimpleName();

	private ConexionServicios(Context context) {
		mCtx = context;
		mRequestQueue = getRequestQueue();
	}

	public static synchronized ConexionServicios getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new ConexionServicios(context);
		}
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 10 * 1024 * 1024);
			Network network = new BasicNetwork(new HurlStack());
			mRequestQueue = new RequestQueue(cache, network); // Volley.newRequestQueue(getApplicationContext());
			// Don't forget to start the volley request queue
			mRequestQueue.start();
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

}
