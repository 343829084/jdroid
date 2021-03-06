package com.jdroid.android.google.analytics;

import android.app.Activity;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.HitBuilders.AppViewBuilder;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.google.android.gms.analytics.HitBuilders.ItemBuilder;
import com.google.android.gms.analytics.HitBuilders.SocialBuilder;
import com.google.android.gms.analytics.HitBuilders.TransactionBuilder;
import com.google.android.gms.analytics.Logger.LogLevel;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.jdroid.android.analytics.AbstractAnalyticsTracker;
import com.jdroid.android.analytics.AppLoadingSource;
import com.jdroid.android.analytics.ExperimentHelper;
import com.jdroid.android.analytics.ExperimentHelper.Experiment;
import com.jdroid.android.analytics.ExperimentHelper.ExperimentVariant;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.UsageStats;
import com.jdroid.android.google.inappbilling.Product;
import com.jdroid.android.social.AccountType;
import com.jdroid.android.social.SocialAction;
import com.jdroid.android.utils.DeviceUtils;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class GoogleAnalyticsTracker extends AbstractAnalyticsTracker {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(GoogleAnalyticsTracker.class);
	
	public static final String NOTIFICATION_CATEGORY = "notification";
	private static final String ABOUT_CATEGORY = "about";
	private static final String FEEDBACK_CATEGORY = "feedback";
	private static final String ADS_CATEGORY = "ads";
	private static final String CLICK_ACTION = "click";
	
	// 30 minutes
	private static final int SESSION_TIMEOUT = 1800;
	public static final String SOCIAL = "social";
	
	private Map<String, Integer> customDimensionsMap = Maps.newHashMap();
	private Map<String, Integer> customMetricsMap = Maps.newHashMap();
	private Tracker tracker;
	private Boolean firstTrackingSent = false;

	private Map<String, String> commonCustomDimensionsValues = Maps.newHashMap();
	
	public enum CustomDimension {
		LOGIN_SOURCE,
		IS_LOGGED,
		INSTALLATION_SOURCE,
		APP_LOADING_SOURCE,
		DEVICE_TYPE,
		DEVICE_YEAR_CLASS;
	}
	
	public GoogleAnalyticsTracker() {
		if (isEnabled()) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(AbstractApplication.get());
			if (AbstractApplication.get().getAppContext().isGoogleAnalyticsDebugEnabled()) {
				analytics.getLogger().setLogLevel(LogLevel.VERBOSE);
			} else {
				analytics.getLogger().setLogLevel(LogLevel.ERROR);
			}
			tracker = analytics.newTracker(AbstractApplication.get().getAppContext().getGoogleAnalyticsTrackingId());
			tracker.setSessionTimeout(SESSION_TIMEOUT);
			tracker.enableAdvertisingIdCollection(isAdvertisingIdCollectionEnabled());
			
			init(customDimensionsMap, customMetricsMap);
		}
	}
	
	protected Boolean isAdvertisingIdCollectionEnabled() {
		return true;
	}
	
	protected void init(Map<String, Integer> customDimensionsMap, Map<String, Integer> customMetricsMap) {
		// Do nothing
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#isEnabled()
	 */
	@Override
	public Boolean isEnabled() {
		return AbstractApplication.get().getAppContext().isGoogleAnalyticsEnabled();
	}
	
	/**
	 * @see com.jdroid.android.analytics.AbstractAnalyticsTracker#onActivityStart(java.lang.Class,
	 *      com.jdroid.android.analytics.AppLoadingSource, java.lang.Object)
	 */
	@Override
	public void onActivityStart(Class<? extends Activity> activityClass, AppLoadingSource appLoadingSource, Object data) {
		
		synchronized (GoogleAnalyticsTracker.class) {
			
			AppViewBuilder appViewBuilder = new HitBuilders.AppViewBuilder();
			if (appLoadingSource != null) {
				if (!hasCommonCustomDimension(CustomDimension.APP_LOADING_SOURCE.name()) || isSessionExpired()) {
					addCommonCustomDimension(CustomDimension.APP_LOADING_SOURCE.name(), appLoadingSource.getName());
				}
			} else if (!hasCommonCustomDimension(CustomDimension.APP_LOADING_SOURCE.name())) {
				addCommonCustomDimension(CustomDimension.APP_LOADING_SOURCE.name(), AppLoadingSource.NORMAL.getName());
			}

			addCustomDimension(appViewBuilder, CustomDimension.DEVICE_YEAR_CLASS, DeviceUtils.getDeviceYearClass().toString());

			if (!firstTrackingSent) {
				addCustomDimension(appViewBuilder, CustomDimension.DEVICE_TYPE, DeviceUtils.getDeviceType());
				
				for (Entry<Experiment, ExperimentVariant> entry : ExperimentHelper.getExperimentsMap().entrySet()) {
					Experiment experiment = entry.getKey();
					ExperimentVariant experimentVariant = entry.getValue();
					sendEvent("abTesting", "load", experiment.getId() + "-" + experimentVariant.getId());
				}
				
				String installationSource = SharedPreferencesHelper.get().loadPreference(
					AbstractApplication.INSTALLATION_SOURCE);
				if (installationSource != null) {
					addCustomDimension(appViewBuilder, CustomDimension.INSTALLATION_SOURCE, installationSource);
					
					onAppLoadTrack(appViewBuilder, data);
					firstTrackingSent = true;
				}
			}
			onActivityStartTrack(appViewBuilder, data);
			sendScreenView(appViewBuilder, activityClass.getSimpleName());
		}
	}
	
	protected void onAppLoadTrack(AppViewBuilder appViewBuilder, Object data) {
		// Do nothing
	}
	
	protected void onActivityStartTrack(AppViewBuilder appViewBuilder, Object data) {
		// Do nothing
	}
	
	/**
	 * @see com.jdroid.android.analytics.AbstractAnalyticsTracker#onFragmentStart(java.lang.String)
	 */
	@Override
	public void onFragmentStart(String screenViewName) {
		synchronized (GoogleAnalyticsTracker.class) {
			AppViewBuilder appViewBuilder = new HitBuilders.AppViewBuilder();
			sendScreenView(appViewBuilder, screenViewName);
		}
	}
	
	/**
	 * @see com.jdroid.android.analytics.AbstractAnalyticsTracker#trackInAppBillingPurchaseTry(com.jdroid.android.google.inappbilling.Product)
	 */
	@Override
	public void trackInAppBillingPurchaseTry(Product product) {
		sendEvent("inAppBilling", "purchaseTry", product.getProductType().getProductId());
	}
	
	/**
	 * @see com.jdroid.android.analytics.AbstractAnalyticsTracker#trackInAppBillingPurchase(com.jdroid.android.google.inappbilling.Product)
	 */
	@Override
	public void trackInAppBillingPurchase(Product product) {
		HitBuilders.TransactionBuilder transactionBuilder = new HitBuilders.TransactionBuilder();
		transactionBuilder.setTransactionId(product.getPurchase().getOrderId());
		transactionBuilder.setAffiliation("Google Play");
		transactionBuilder.setRevenue(product.getPrice());
		transactionBuilder.setCurrencyCode(product.getCurrencyCode());
		sendTransaction(transactionBuilder);
		
		HitBuilders.ItemBuilder itemBuilder = new HitBuilders.ItemBuilder();
		itemBuilder.setTransactionId(product.getPurchase().getOrderId());
		itemBuilder.setName(product.getProductType().getProductId());
		itemBuilder.setCategory(product.getProductType().isConsumable() ? "consumable" : "notConsumable");
		itemBuilder.setSku(product.getProductType().getProductId());
		itemBuilder.setQuantity(1);
		itemBuilder.setPrice(product.getPrice());
		itemBuilder.setCurrencyCode(product.getCurrencyCode());
		sendTransactionItem(itemBuilder);
	}
	
	/**
	 * @see com.jdroid.android.analytics.AbstractAnalyticsTracker#trackNotificationDisplayed(java.lang.String)
	 */
	@Override
	public void trackNotificationDisplayed(String notificationName) {
		sendEvent(NOTIFICATION_CATEGORY, "display", notificationName);
	}
	
	/**
	 * @see com.jdroid.android.analytics.AnalyticsTracker#trackNotificationOpened(java.lang.String)
	 */
	@Override
	public void trackNotificationOpened(String notificationName) {
		sendEvent(NOTIFICATION_CATEGORY, "open", notificationName);
	}

	@Override
	public void trackEnjoyingApp(Boolean enjoying) {
		sendEvent(FEEDBACK_CATEGORY, "enjoying", enjoying.toString());
	}

	@Override
	public void trackRateOnGooglePlay(Boolean rate) {
		sendEvent(FEEDBACK_CATEGORY, "rate", rate.toString());
	}

	@Override
	public void trackGiveFeedback(Boolean feedback) {
		sendEvent(FEEDBACK_CATEGORY, "giveFeedback", feedback.toString());
	}

	@Override
	public void trackSendAppInvitation(String invitationId) {
		sendEvent(SOCIAL, "sendAppInvitation", invitationId);
	}

	@Override
	public void trackTiming(String category, String variable, String label, long value) {
		// Avoid timing trackings when the app is in background to avoid session times data corruption.
		// TODO Improve this to track timings 30 seconds after of the last onResume,
		// so we don´t lose the tracking when an use case finish in background
 		if (!(ignoreBackgroundTimingTrackings() && AbstractApplication.get().isInBackground())) {
			HitBuilders.TimingBuilder timingBuilder = new HitBuilders.TimingBuilder();
			timingBuilder.setCategory(category);
			timingBuilder.setVariable(variable);
			timingBuilder.setLabel(label);
			timingBuilder.setValue(value);
			tracker.send(timingBuilder.build());
			LOGGER.debug("Timing sent. Category [" + category + "] Variable [" + variable + "] Label [" + label
					+ "] Value [" + value + "]");
		}
	}

	protected Boolean ignoreBackgroundTimingTrackings() {
		return true;
	}

	
	/**
	 * @see com.jdroid.android.analytics.AbstractAnalyticsTracker#trackRemoveAdsBannerClicked()
	 */
	@Override
	public void trackRemoveAdsBannerClicked() {
		sendEvent(ADS_CATEGORY, CLICK_ACTION, "removeAds");
	}
	
	/**
	 * @see com.jdroid.android.analytics.AbstractAnalyticsTracker#trackUriOpened(java.lang.String, java.lang.String)
	 */
	@Override
	public void trackUriOpened(String uriType, String screenName) {
		sendEvent(uriType, "open", screenName);
	}
	
	/**
	 * @see com.jdroid.android.analytics.AbstractAnalyticsTracker#trackSocialInteraction(com.jdroid.android.social.AccountType,
	 *      com.jdroid.android.social.SocialAction, java.lang.String)
	 */
	@Override
	public void trackSocialInteraction(AccountType accountType, SocialAction socialAction, String socialTarget) {
		
		String category = SOCIAL;
		String network = "Undefined";
		if (accountType != null) {
			category += accountType.getFriendlyName();
			network = accountType.getFriendlyName();
		}
		sendEvent(category, socialAction.getName(), socialTarget);
		
		SocialBuilder socialBuilder = new SocialBuilder();
		socialBuilder.setNetwork(network);
		socialBuilder.setAction(socialAction.getName());
		socialBuilder.setTarget(socialTarget);
		
		tracker.send(socialBuilder.build());
		LOGGER.debug("Social interaction sent. Network [" + network + "] Action [" + socialAction.getName()
				+ "] Target [" + socialTarget + "]");
	}
	
	/**
	 * @see com.jdroid.android.analytics.AbstractAnalyticsTracker#trackContactUs()
	 */
	@Override
	public void trackContactUs() {
		sendEvent(ABOUT_CATEGORY, "contactUs", "contactUs");
	}
	
	/**
	 * @see com.jdroid.android.analytics.AbstractAnalyticsTracker#trackAboutLibraryOpen(java.lang.String)
	 */
	@Override
	public void trackAboutLibraryOpen(String libraryKey) {
		sendEvent(ABOUT_CATEGORY, "openLibrary", libraryKey);
	}

	@Override
	public void trackFatalException(Throwable throwable, List<String> tags) {
		HitBuilders.ExceptionBuilder builder = new HitBuilders.ExceptionBuilder();
		String description = new StandardExceptionParser(AbstractApplication.get(), null).getDescription(Thread.currentThread().getName(), throwable);
		builder.setDescription(description);
		builder.setFatal(true);
		tracker.send(builder.build());
		dispatchLocalHits();
		LOGGER.debug("Fatal exception sent. Description [" + description + "]");
	}

	@Override
	public void trackHandledException(Throwable throwable, List<String> tags) {
		HitBuilders.ExceptionBuilder builder = new HitBuilders.ExceptionBuilder();
		String description = new StandardExceptionParser(AbstractApplication.get(), null).getDescription(Thread.currentThread().getName(), throwable);
		builder.setDescription(description);
		builder.setFatal(false);
		tracker.send(builder.build());
		LOGGER.debug("Non fatal exception sent. Description [" + description + "]");
	}

	protected void addCustomDimension(AppViewBuilder appViewBuilder, CustomDimension customDimension, String dimension) {
		addCustomDimension(appViewBuilder, customDimension.name(), dimension);
	}
	
	protected void addCustomDimension(AppViewBuilder appViewBuilder, String customDimension, String dimension) {
		Integer index = customDimensionsMap.get(customDimension);
		addCustomDimension(appViewBuilder, index, dimension);
	}
	
	protected void addCustomDimension(AppViewBuilder appViewBuilder, Integer index, String dimension) {
		if (index != null) {
			LOGGER.debug("Added custom dimension: " + index + " - " + dimension);
			appViewBuilder.setCustomDimension(index, dimension);
		}
	}
	
	protected void addCustomDimension(AppViewBuilder appViewBuilder, Map<String, String> customDimensions) {
		if (customDimensions != null) {
			for (Entry<String, String> entry : customDimensions.entrySet()) {
				addCustomDimension(appViewBuilder, entry.getKey(), entry.getValue());
			}
		}
	}
	
	protected void addCustomDimension(EventBuilder eventBuilder, Map<String, String> customDimensions) {
		if (customDimensions != null) {
			for (Entry<String, String> entry : customDimensions.entrySet()) {
				addCustomDimension(eventBuilder, entry.getKey(), entry.getValue());
			}
		}
	}
	
	protected void addCustomDimension(EventBuilder eventBuilder, CustomDimension customDimension, String dimension) {
		addCustomDimension(eventBuilder, customDimension.name(), dimension);
	}
	
	protected void addCustomDimension(EventBuilder eventBuilder, String customDimension, String dimension) {
		Integer index = customDimensionsMap.get(customDimension);
		addCustomDimension(eventBuilder, index, dimension);
	}
	
	protected void addCustomDimension(EventBuilder eventBuilder, Integer index, String dimension) {
		if (index != null) {
			LOGGER.debug("Added custom dimension: " + index + " - " + dimension);
			eventBuilder.setCustomDimension(index, dimension);
		}
	}
	
	protected void addCustomDimension(TransactionBuilder transactionBuilder, Map<String, String> customDimensions) {
		if (customDimensions != null) {
			for (Entry<String, String> entry : customDimensions.entrySet()) {
				addCustomDimension(transactionBuilder, entry.getKey(), entry.getValue());
			}
		}
	}
	
	protected void addCustomDimension(TransactionBuilder transactionBuilder, String customDimension, String dimension) {
		Integer index = customDimensionsMap.get(customDimension);
		addCustomDimension(transactionBuilder, index, dimension);
	}
	
	protected void addCustomDimension(TransactionBuilder transactionBuilder, Integer index, String dimension) {
		if (index != null) {
			LOGGER.debug("Added custom dimension: " + index + " - " + dimension);
			transactionBuilder.setCustomDimension(index, dimension);
		}
	}
	
	protected void addCustomMetric(AppViewBuilder appViewBuilder, String customDimensionKey, Float metric) {
		Integer index = customMetricsMap.get(customDimensionKey);
		addCustomMetric(appViewBuilder, index, metric);
	}
	
	protected void addCustomMetric(AppViewBuilder appViewBuilder, Integer index, Float metric) {
		if (index != null) {
			LOGGER.debug("Added custom metric: " + index + " - " + metric);
			appViewBuilder.setCustomMetric(index, metric);
		}
	}
	
	public void sendScreenView(String screenName) {
		sendScreenView(new AppViewBuilder(), screenName);
	}
	
	public void sendScreenView(AppViewBuilder appViewBuilder, String screenName) {
		
		addCustomDimension(appViewBuilder, commonCustomDimensionsValues);
		
		tracker.setScreenName(screenName);
		tracker.send(appViewBuilder.build());
		LOGGER.debug("Screen view sent. Screen name [" + screenName + "]");
	}
	
	public void sendEvent(String category, String action, String label) {
		sendEvent(category, action, label, null, null);
	}
	
	public void sendEvent(String category, String action, String label, Map<String, String> customDimensions) {
		sendEvent(category, action, label, null, customDimensions);
	}
	
	public void sendEvent(String category, String action, String label, Long value, Map<String, String> customDimensions) {
		HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder();
		
		addCustomDimension(eventBuilder, commonCustomDimensionsValues);
		addCustomDimension(eventBuilder, customDimensions);
		
		eventBuilder.setCategory(category);
		eventBuilder.setAction(action);
		eventBuilder.setLabel(label);
		if (value != null) {
			eventBuilder.setValue(value);
		}
		
		tracker.send(eventBuilder.build());
		LOGGER.debug("Event sent. Category [" + category + "] Action [" + action + "] Label [" + label + "]"
				+ (value != null ? " Value" + value + "]" : ""));
	}
	
	public void sendTransaction(TransactionBuilder transactionBuilder) {
		sendTransaction(transactionBuilder, null);
	}
	
	public void sendTransaction(TransactionBuilder transactionBuilder, Map<String, String> customDimensions) {
		
		addCustomDimension(transactionBuilder, commonCustomDimensionsValues);
		addCustomDimension(transactionBuilder, customDimensions);
		
		tracker.send(transactionBuilder.build());
		LOGGER.debug("Transaction sent. " + transactionBuilder.build());
	}
	
	public void sendTransactionItem(ItemBuilder itemBuilder) {
		tracker.send(itemBuilder.build());
		LOGGER.debug("Transaction item sent. " + itemBuilder.build());
	}
	
	public void sendSocialInteraction(AccountType accountType, SocialAction socialAction, String socialTarget) {
		
		SocialBuilder socialBuilder = new SocialBuilder();
		socialBuilder.setNetwork(accountType.getFriendlyName());
		socialBuilder.setAction(socialAction.getName());
		socialBuilder.setTarget(socialTarget);
		
		tracker.send(socialBuilder.build());
		LOGGER.debug("Social interaction sent. Network [" + accountType.getFriendlyName() + "] Action ["
				+ socialAction.getName() + "] Target [" + socialTarget + "]");
	}
	
	public void dispatchLocalHits() {
		GoogleAnalytics.getInstance(AbstractApplication.get()).dispatchLocalHits();
	}
	
	public void addCommonCustomDimension(String key, String value) {
		commonCustomDimensionsValues.put(key, value);
	}
	
	public Boolean hasCommonCustomDimension(String key) {
		return commonCustomDimensionsValues.containsKey(key);
	}
	
	public Boolean isSessionExpired() {
		return (DateUtils.nowMillis() - UsageStats.getLastStopTime()) > (4 * SESSION_TIMEOUT * DateUtils.MILLIS_PER_SECOND);
	}
}
