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
	
	ADMOB(R.string.adMob, R.drawable.ic_admob, AdsActivity.class),
	CARD_VIEW(R.string.cardView, R.drawable.ic_cardview, CardViewActivity.class),
	DATE_TIME(R.string.dateTime, R.drawable.ic_date_time, DateTimeActivity.class),
	EXCEPTION_HANDLING(R.string.exceptionHandling, R.drawable.ic_exception_handling, ExceptionHandlingActivity.class),
	FIREBASE(R.string.firebase, R.drawable.ic_firebase, FirebaseActivity.class),
	GOOGLE_ANALYTCS(R.string.googleAnalytics, R.drawable.ic_analytics, AnalyticsActivity.class),
	GCM(R.string.gcm, R.drawable.ic_gcm, GcmActivity.class),
	GOOGLE_MAPS(R.string.googleMaps, R.drawable.ic_maps, MapActivity.class),
	GOOGLE_PLUS(R.string.googlePlus, R.drawable.google_plus, GooglePlusActivity.class),
	HERO(R.string.hero, R.drawable.ic_photo, HeroActivity.class),
	HTTP(R.string.http, R.drawable.ic_http, HttpActivity.class),
	IN_APP_BILLING(R.string.inAppBilling, R.drawable.ic_inapp_billing, ImageLoaderActivity.class),
	LOADING(R.string.loading, R.drawable.ic_loading, LoadingActivity.class),
	NAVDRAWER(R.string.navDrawer, R.drawable.ic_nav_drawer, NavDrawerActivity.class),
	NOTIFICATIONS(R.string.notifications, R.drawable.ic_notifications, NotificationsActivity.class),
	RATE_APP(R.string.rateApp, R.drawable.ic_rate, RateAppActivity.class),
	RECYCLER_VIEW(R.string.recyclerView, R.drawable.ic_recycler_view, RecyclerViewActivity.class),
	SERVICE(R.string.service, R.drawable.ic_service, ServiceActivity.class),
	SQLITE(R.string.sqlite, R.drawable.ic_sqlite, SQLiteActivity.class),
	TABLETS(R.string.tablets, R.drawable.ic_tablets, ScreenUtils.is10Inches() ? TabletActivity.class : LeftTabletActivity.class),
	TOASTS(R.string.toasts, R.drawable.ic_toasts, ToastsActivity.class),
	UNVERSAL_IMAGE_LOADER(R.string.universalImageLoader, R.drawable.ic_photo, ImageLoaderActivity.class);

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