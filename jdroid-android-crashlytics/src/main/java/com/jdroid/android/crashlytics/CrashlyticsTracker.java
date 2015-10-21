package com.jdroid.android.crashlytics;

import android.app.Activity;

import com.crashlytics.android.Crashlytics;
import com.jdroid.android.analytics.AbstractAnalyticsTracker;
import com.jdroid.android.analytics.AppLoadingSource;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.java.utils.ReflectionUtils;

import java.util.Map;
import java.util.Map.Entry;

public class CrashlyticsTracker extends AbstractAnalyticsTracker {
	
	private static final CrashlyticsTracker INSTANCE = new CrashlyticsTracker();
	
	public static CrashlyticsTracker get() {
		return INSTANCE;
	}
	
	@Override
	public Boolean isEnabled() {
		return CrashlyticsAppModule.get().getCrashlyticsAppContext().isCrashlyticsEnabled();
	}
	
	@Override
	public void onInitExceptionHandler(Map<String, String> metadata) {
		if (metadata != null) {
			for (Entry<String, String> entry : metadata.entrySet()) {
				if (entry.getValue() != null) {
					Crashlytics.getInstance().core.setString(entry.getKey(), entry.getValue());
				}
			}
		}
	}
	
	@Override
	public void trackHandledException(Throwable throwable, int priority) {
		if (isPriorityLevelEnabled()) {
			assignPriorityLevel(throwable, priority);
		}
		Crashlytics.getInstance().core.logException(throwable);
	}

	protected Boolean isPriorityLevelEnabled() {
		return false;
	}

	protected Throwable assignPriorityLevel(Throwable throwable, int priorityLevel) {
		StringBuilder builder = new StringBuilder();
		builder.append("level");
		builder.append(String.format("%02d", priorityLevel));
		if (throwable.getMessage() != null) {
			builder.append(" ");
			builder.append(throwable.getMessage());
		}
		try {
			ReflectionUtils.set(throwable, "detailMessage", builder.toString());
		} catch (Exception e) {
			// do nothing
		}
		return throwable;
	}

	@Override
	public void trackErrorBreadcrumb(String message) {
		Crashlytics.getInstance().core.log(message);
	}

	@Override
	public void onActivityStart(Class<? extends Activity> activityClass, AppLoadingSource appLoadingSource, Object data) {
		if (appLoadingSource != null) {
			Crashlytics.getInstance().core.setString(AppLoadingSource.class.getSimpleName(), appLoadingSource.getName());
		}
		
		Crashlytics.getInstance().core.setString("UserId",
				SecurityContext.get().isAuthenticated() ? SecurityContext.get().getUser().getId().toString() : null);

		Crashlytics.getInstance().core.log("Started " + activityClass.getSimpleName());
	}
}