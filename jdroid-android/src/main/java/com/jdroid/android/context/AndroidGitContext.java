package com.jdroid.android.context;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.context.GitContext;

public class AndroidGitContext implements GitContext {

	@Override
	public String getBranch() {
		return AbstractApplication.get().getAppContext().getBuildConfigValue("GIT_BRANCH");
	}

	@Override
	public String getSha() {
		return AbstractApplication.get().getAppContext().getBuildConfigValue("GIT_SHA");
	}
}
