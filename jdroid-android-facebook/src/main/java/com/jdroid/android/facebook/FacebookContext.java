package com.jdroid.android.facebook;

import com.jdroid.android.application.AbstractApplication;

public class FacebookContext {

	/**
	 * @return The registered Facebook app ID that is used to identify this application for Facebook.
	 */
	public String getFacebookAppId() {
		return AbstractApplication.get().getAppContext().getBuildConfigValue("FACEBOOK_APP_ID");
	}

}
