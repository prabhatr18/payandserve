package com.digital.payandserve.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.digital.payandserve.BuildConfig;
import com.digital.payandserve.R;
import com.digital.payandserve.network.SessionExpired;
import com.digital.payandserve.utill.Print;

import timber.log.Timber;


public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();
    public static AppController myAutoLogoutApp;
    private static AppController mInstance;
    private static Handler handler;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Runnable runnable;
    private String state;
    long startTime = 0;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    private void timerTime() { }

    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
        mInstance = this;
        handler = new Handler();
        runnable = () -> {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            Print.P("Time : " + minutes + " sec : " + seconds);
            if (!getApplicationContext().getClass().getName().equals(BuildConfig.APPLICATION_ID + ".activity.Login")) {
                Intent i = new Intent(getApplicationContext(), SessionExpired.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
        };

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                try {
                    state = "Created";
                    Print.P("->> " + state);
                    handler.removeCallbacks(runnable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                state = "Started";
                Print.P("->> " + state);
                handler.removeCallbacks(runnable);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                state = "Resumed";
                Print.P("->> " + state);
                handler.removeCallbacks(runnable);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                state = "PAUSED";
                Print.P("->> " + state);
                startTrackingPauseTime();
            }

            @Override
            public void onActivityStopped(Activity activity) {
                state = "Stop";
                Print.P("->> " + state);
                startTrackingPauseTime();
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                Print.P("->> " + state);
                state = "SaveInstanceState";
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                try {
                    if (!activity.getClass().getName().equals(BuildConfig.APPLICATION_ID + ".activity.SplashScreen") ||
                            !activity.getClass().getName().equals(BuildConfig.APPLICATION_ID + ".activity.Login"))
                    if (!state.equals("Stop"))
                        handler.removeCallbacks(runnable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void startTrackingPauseTime() {
        handler.postDelayed(runnable, 1000 * 60 * Constants.AUTO_LOGOUT_TIME);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
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

    public void applyThemeToDrawable(Context context, boolean isTrans, Drawable image) {
        if (image != null) {
            PorterDuffColorFilter porterDuffColorFilter;
            if (isTrans) {
                porterDuffColorFilter = new PorterDuffColorFilter(context
                        .getResources().getColor(R.color.colorPrimaryTrans), PorterDuff.Mode.SRC_ATOP);
            } else {
                porterDuffColorFilter = new PorterDuffColorFilter(context
                        .getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
            image.setColorFilter(porterDuffColorFilter);
        }
    }


}