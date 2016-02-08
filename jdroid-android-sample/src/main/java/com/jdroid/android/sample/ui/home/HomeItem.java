package com.jdroid.android.sample.ui.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.jdroid.android.ActionItem;
import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.ui.cardview.CardViewActivity;
import com.jdroid.android.sample.ui.firebase.FirebaseActivity;
import com.jdroid.android.sample.ui.google.gcm.GcmActivity;
import com.jdroid.android.sample.ui.google.plus.GooglePlusActivity;
import com.jdroid.android.sample.ui.rateme.RateAppActivity;
import com.jdroid.android.sample.ui.service.ServiceActivity;
import com.jdroid.android.sample.ui.sqlite.SQLiteActivity;
import com.jdroid.android.utils.ScreenUtils;
import com.jdroid.android.sample.ui.ads.AdsActivity;
import com.jdroid.android.sample.ui.analytics.AnalyticsActivity;
import com.jdroid.android.sample.ui.datetime.DateTimeActivity;
import com.jdroid.android.sample.ui.exceptions.ExceptionHandlingActivity;
import com.jdroid.android.sample.ui.hero.HeroActivity;
import com.jdroid.android.sample.ui.http.HttpActivity;
import com.jdroid.android.sample.ui.imageloader.ImageLoaderActivity;
import com.jdroid.android.sample.ui.loading.LoadingActivity;
import com.jdroid.android.sample.ui.maps.MapActivity;
import com.jdroid.android.sample.ui.navdrawer.NavDrawerActivity;
import com.jdroid.android.sample.ui.notifications.NotificationsActivity;
import com.jdroid.android.sample.ui.recyclerview.RecyclerViewActivity;
import com.jdroid.android.sample.ui.tablets.LeftTabletActivity;
import com.jdroid.android.sample.ui.tablets.TabletActivity;
import com.jdroid.android.sample.ui.toasts.ToastsActivity;

public enum HomeItem implements ActionItem {
	
	ADMOB(R.string.adMob, R.drawable.apps, AdsActivity.class),
	CARD_VIEW(R.string.cardView, R.drawable.datetime, CardViewActivity.class),
	DATE_TIME(R.string.dateTime, R.drawable.datetime, DateTimeActivity.class),
	EXCEPTION_HANDLING(R.string.exceptionHandling, R.drawable.bug, ExceptionHandlingActivity.class),
	FIREBASE(R.string.firebase, R.drawable.analytics, FirebaseActivity.class),
	GOOGLE_ANALYTCS(R.string.googleAnalytics, R.drawable.analytics, AnalyticsActivity.class),
	GCM(R.string.gcm, R.drawable.cloud, GcmActivity.class),
	GOOGLE_MAPS(R.string.googleMaps, R.drawable.map, MapActivity.class),
	GOOGLE_PLUS(R.string.googlePlus, R.drawable.google_plus, GooglePlusActivity.class),
	HERO(R.string.hero, R.drawable.photo, HeroActivity.class),
	HTTP(R.string.http, R.drawable.http, HttpActivity.class),
	IN_APP_BILLING(R.string.inAppBilling, R.drawable.shopping_cart, ImageLoaderActivity.class),
	LOADING(R.string.loading, R.drawable.refresh, LoadingActivity.class),
	NAVDRAWER(R.string.navDrawer, R.drawable.drawer, NavDrawerActivity.class),
	NOTIFICATIONS(R.string.notifications, R.drawable.notifications, NotificationsActivity.class),
	RATE_APP(R.string.rateApp, R.drawable.ic_rate_us, RateAppActivity.class),
	RECYCLER_VIEW(R.string.recyclerView, R.drawable.list, RecyclerViewActivity.class),
	SERVICE(R.string.service, R.drawable.list, ServiceActivity.class),
	SQLITE(R.string.sqlite, R.drawable.list, SQLiteActivity.class),
	TABLETS(R.string.tablets, R.drawable.tablet, ScreenUtils.is10Inches() ? TabletActivity.class : LeftTabletActivity.class),
	TOASTS(R.string.toasts, R.drawable.info, ToastsActivity.class),
	UNVERSAL_IMAGE_LOADER(R.string.universalImageLoader, R.drawable.photo, ImageLoaderActivity.class);

	private Integer resourceId;
	private Integer iconId;
	private Class<? extends FragmentActivity> activityClass;
	
	HomeItem(Integer resourceId, Integer iconId, Class<? extends FragmentActivity> activityClass) {
		this.resourceId = resourceId;
		this.iconId = iconId;
		this.activityClass = activityClass;
	}
	
	/**
	 * @see com.jdroid.android.ActionItem#getNameResource()
	 */
	@Override
	public Integer getNameResource() {
		return resourceId;
	}
	
	/**
	 * @see com.jdroid.android.ActionItem#getIconResource()
	 */
	@Override
	public Integer getIconResource() {
		return iconId;
	}
	
	/**
	 * @see com.jdroid.android.ActionItem#startActivity(android.support.v4.app.FragmentActivity)
	 */
	@Override
	public void startActivity(FragmentActivity fragmentActivity) {
		ActivityLauncher.launchActivity(activityClass);
	}
	
	/**
	 * @see com.jdroid.android.ActionItem#matchesActivity(android.support.v4.app.FragmentActivity)
	 */
	@Override
	public Boolean matchesActivity(FragmentActivity fragmentActivity) {
		return activityClass.equals(fragmentActivity.getClass());
	}
	
	/**
	 * @see com.jdroid.android.ActionItem#createFragment(java.lang.Object)
	 */
	@Override
	public Fragment createFragment(Object args) {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.ActionItem#getName()
	 */
	@Override
	public String getName() {
		return name();
	}
	
	/**
	 * @see com.jdroid.android.ActionItem#getDescriptionResource()
	 */
	@Override
	public Integer getDescriptionResource() {
		return null;
	}
}