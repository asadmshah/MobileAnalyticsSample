package com.asadmshah.mobileanalyticssample;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.TagManager;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by asadmshah on 7/3/15.
 */
public class Application extends android.app.Application implements android.app.Application.ActivityLifecycleCallbacks {

    private static Phone[] sProductsList;
    private static ShoppingCart sShoppingCart;
    private static AnalyticsTracker sAnalyticsTracker;
    private static TagManager sTagManager;
    private static ContainerHolder sContainerHolder;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public Phone[] getProductsList() {
        if (sProductsList == null) {
            InputStream inputStream = getResources().openRawResource(R.raw.products);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            sProductsList = new Gson().fromJson(reader, Phone[].class);
        }
        return sProductsList;
    }

    public ShoppingCart getShoppingCart() {
        if (sShoppingCart == null) {
            sShoppingCart = new ShoppingCart();
        }
        return sShoppingCart;
    }

    public AnalyticsTracker getTracker() {
        if (sAnalyticsTracker == null) {
            Tracker tracker = GoogleAnalytics.getInstance(this).newTracker(getString(R.string.GOOGLE_ANALYTICS_API_KEY));
            tracker.enableAutoActivityTracking(false);
            tracker.enableExceptionReporting(true);
            sAnalyticsTracker = new AnalyticsTracker(tracker);
        }
        return sAnalyticsTracker;
    }

    public TagManager getTagManager() {
        if (sTagManager == null) {
            sTagManager = TagManager.getInstance(this);
        }
        return sTagManager;
    }

    public void setContainerHolder(ContainerHolder containerHolder) {
        sContainerHolder = containerHolder;
    }

    public ContainerHolder getContainerHolder() {
        return sContainerHolder;
    }

}
