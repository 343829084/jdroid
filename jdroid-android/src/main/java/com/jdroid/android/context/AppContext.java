package com.jdroid.android.context;

import android.preference.PreferenceManager;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.http.Server;
import com.jdroid.java.utils.ReflectionUtils;

import java.util.Locale;

public abstract class AppContext {
	
	// Environment
	private Server server;

	public AppContext() {

		server = findServerByName(getServerName());
	}

	public Class<?> getBuildConfigClass() {
		return ReflectionUtils.getClass(AndroidUtils.getApplicationId() + ".BuildConfig");
	}

	@SuppressWarnings("unchecked")
	public <T> T getBuildConfigValue(String property) {
		return (T)ReflectionUtils.getStaticFieldValue(getBuildConfigClass(), property);
	}

	@SuppressWarnings("unchecked")
	public <T> T getBuildConfigValue(String property, Object defaultValue) {
		return (T)ReflectionUtils.getStaticFieldValue(getBuildConfigClass(), property, defaultValue);
	}
	
	protected Server findServerByName(String name) {
		return null;
	}
	
	public Server getServer() {
		return getServer(server);
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends Server> T getServer(Server defaultServer) {
		if (isProductionEnvironment() || !displayDebugSettings()) {
			return (T)defaultServer;
		} else {
			Class<?> clazz = defaultServer.getClass().getEnclosingClass() != null ? defaultServer.getClass().getEnclosingClass()
					: defaultServer.getClass();
			return (T)defaultServer.instance(PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getString(
				clazz.getSimpleName(), defaultServer.getName()).toUpperCase(Locale.US));
		}
	}

	protected String getServerName() {
		return getBuildConfigValue("SERVER_NAME", null);
	}
	
	/**
	 * @return Whether the application should display the debug settings
	 */
	public Boolean displayDebugSettings() {
		return !isProductionEnvironment();
	}

	public Boolean isLoggingEnabled() {
		return !isProductionEnvironment();
	}

	public String getBuildType() {
		return getBuildConfigValue("BUILD_TYPE");
	}

	public String getBuildTime() {
		return getBuildConfigValue("BUILD_TIME", null);
	}

	/**
	 * @return Whether the application is running on a production environment
	 */
	public Boolean isProductionEnvironment() {
		return getBuildType().equals("release");
	}

	/**
	 * @return Whether the application has Google Analytics enabled or not
	 */
	public Boolean isGoogleAnalyticsEnabled() {
		return getBuildConfigValue("GOOGLE_ANALYTICS_ENABLED", false);
	}
	
	/**
	 * @return The Google Analytics Tracking ID
	 */
	public String getGoogleAnalyticsTrackingId() {
		return getBuildConfigValue("GOOGLE_ANALYTICS_TRACKING_ID", null);
	}
	
	public  Boolean isGoogleAnalyticsDebugEnabled() {
		return getBuildConfigValue("GOOGLE_ANALYTICS_DEBUG_ENABLED", false);
	}
	
	public String getLocalIp() {
		return getBuildConfigValue("LOCAL_IP", null);
	}

	public String getInstallationSource() {
		return getBuildConfigValue("INSTALLATION_SOURCE", "GooglePlay");
	}

	public Boolean isChromeInstallationSource() {
		return getInstallationSource().equals("Chrome");
	}
	
	public String getServerApiVersion() {
		return null;
	}
	
	public String getWebsite() {
		return null;
	}

	public String getContactUsEmail() {
		return null;
	}

	public String getTwitterAccount() {
		return null;
	}
	
	public String getFacebookPageId() {
		return null;
	}
	
	public String getGooglePlusCommunityId() {
		return null;
	}
}
