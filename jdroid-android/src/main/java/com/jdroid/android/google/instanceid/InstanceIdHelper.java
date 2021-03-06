package com.jdroid.android.google.instanceid;

import com.google.android.gms.iid.InstanceID;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.google.GooglePlayServicesUtils;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.UUID;

public class InstanceIdHelper {

	private final static Logger LOGGER = LoggerUtils.getLogger(InstanceIdHelper.class);

	private static final String INSTANCE_ID_PREFERENCES = "usageStats";

	private static final String INSTANCE_ID = "instanceId";
	private static final String ANONYMOUS_INSTANCE_ID = "anonymousInstanceId";

	private static SharedPreferencesHelper sharedPreferencesHelper;

	private static String instanceId;
	private static String anonymousInstanceId;

	public static synchronized String getInstanceId() {
		if (instanceId == null) {
			instanceId = getSharedPreferencesHelper().loadPreference(INSTANCE_ID);
			if (instanceId == null) {
				instanceId = InstanceID.getInstance(AbstractApplication.get()).getId();
				getSharedPreferencesHelper().savePreferenceAsync(INSTANCE_ID, instanceId);
			}
			LOGGER.debug("Instance id: " + instanceId);
		}
		if (instanceId != null) {
			return instanceId;
		} else {
			if (anonymousInstanceId == null) {
				anonymousInstanceId = getSharedPreferencesHelper().loadPreference(ANONYMOUS_INSTANCE_ID);
				if (anonymousInstanceId == null) {
					anonymousInstanceId = "anonymous" + UUID.randomUUID();
					getSharedPreferencesHelper().savePreferenceAsync(ANONYMOUS_INSTANCE_ID, anonymousInstanceId);
				}
				LOGGER.debug("Anonymous Instance id: " + anonymousInstanceId);
			}
			return anonymousInstanceId;
		}
	}

	public static void clearInstanceId() {
		instanceId = null;
		getSharedPreferencesHelper().removePreferences(INSTANCE_ID);
	}

	public static void removeInstanceId() {
		if (GooglePlayServicesUtils.isGooglePlayServicesAvailable(AbstractApplication.get())) {
			try {
				InstanceID.getInstance(AbstractApplication.get()).deleteInstanceID();
			} catch (IOException e) {
				AbstractApplication.get().getExceptionHandler().logHandledException(e);
			}
		} else {
			LOGGER.warn("Instance id not removed because Google Play Services is not available");
		}
	}

	private static SharedPreferencesHelper getSharedPreferencesHelper() {
		if (sharedPreferencesHelper == null) {
			sharedPreferencesHelper = SharedPreferencesHelper.get(INSTANCE_ID_PREFERENCES);
		}
		return sharedPreferencesHelper;
	}

}
