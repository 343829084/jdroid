package com.jdroid.android.crashlytics;

import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.application.AbstractApplication;

public class CrashlyticsAppContext {

	public Boolean isCrashlyticsEnabled() {
		return AbstractApplication.get().getAppContext().getBuildConfigValue("CRASHLYTICS_ENABLED", false);
	}

	public AnalyticsTracker getAnalyticsTracker() {
		return CrashlyticsTracker.get();
	}
}
