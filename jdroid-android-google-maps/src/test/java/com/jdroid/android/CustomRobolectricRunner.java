package com.jdroid.android;

import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.SdkConfig;
import org.robolectric.annotation.Config;

import java.io.File;
import java.util.Properties;

public class CustomRobolectricRunner extends RobolectricTestRunner {

	public CustomRobolectricRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
	}

	@Override
	protected AndroidManifest getAppManifest(Config config) {
		String manifestPath = "src/test/AndroidManifest.xml";

		// android studio has a different execution root for tests than pure gradle
		// so we avoid here manual effort to get them running inside android studio
		if (!new File(manifestPath).exists()) {
			manifestPath = "jdroid-android/" + manifestPath;
		}

		config = overwriteConfig(config, "manifest", manifestPath);
		config = overwriteConfig(config, "resourceDir", "../main/res");
		return super.getAppManifest(config);
	}

	@Override
	protected SdkConfig pickSdkVersion(AndroidManifest appManifest, Config config) {
		// current Robolectric supports not the latest android SDK version
		// so we must downgrade to simulate the latest supported version.
		config = overwriteConfig(config, "emulateSdk", "18");
		return super.pickSdkVersion(appManifest, config);
	}

	protected Config.Implementation overwriteConfig(Config config, String key, String value) {
		Properties properties = new Properties();
		properties.setProperty(key, value);
		return new Config.Implementation(config, Config.Implementation.fromProperties(properties));
	}
}