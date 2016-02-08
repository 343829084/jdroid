package com.jdroid.android.google.inappbilling;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.StringUtils;

import java.util.List;

public class InAppBillingContext {
	
	public static final String MOCK_ENABLED = "inAppBillingMockEnabled";
	public static final String TEST_PRODUCT_IDS = "inAppBillingTestProductIds";
	public static final String PURCHASED_PRODUCT_TYPES = "inAppBillingPurchasedProductTypes";
	
	private String googlePlayPublicKey;
	private List<ProductType> purchasedProductTypes;

	public InAppBillingContext() {
		
		// TODO Instead of just storing the entire literal string here embedded in the program, construct the key at
		// runtime from pieces or use bit manipulation (for example, XOR with some other string) to hide the actual key.
		// The key itself is not secret information, but we don't want to make it easy for an attacker to replace the
		// public key with one of their own and then fake messages from the server.
		googlePlayPublicKey = AbstractApplication.get().getAppContext().getBuildConfigValue("GOOGLE_PLAY_PUBLIC_KEY");
		
	}
	
	/**
	 * @return Your application's public key, encoded in base64. This is used for verification of purchase signatures.
	 *         You can find your app's base64-encoded public key in your application's page on Google Play Developer
	 *         Console. Note that this is NOT your "developer public key".
	 */
	public String getGooglePlayPublicKey() {
		return googlePlayPublicKey;
	}
	
	public Boolean isInAppBillingMockEnabled() {
		return !AbstractApplication.get().getAppContext().isProductionEnvironment()
				&& PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getBoolean(MOCK_ENABLED,
					false);
	}
	
	public TestProductType getTestProductType() {
		return TestProductType.valueOf(PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getString(
			TEST_PRODUCT_IDS, TestProductType.PURCHASED.name()));
	}
	
	public synchronized void setPurchasedProductTypes(Inventory inventory) {
		purchasedProductTypes = Lists.newArrayList();
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get());
		Editor editor = sharedPreferences.edit();
		List<String> productIds = Lists.newArrayList();
		for (Product each : inventory.getProducts()) {
			if (each.isPurchaseVerified()) {
				productIds.add(each.getId());
				purchasedProductTypes.add(each.getProductType());
			}
		}
		editor.putString(PURCHASED_PRODUCT_TYPES, StringUtils.join(productIds));
		editor.apply();
	}
	
	public synchronized void addPurchasedProductType(ProductType productType) {
		if (purchasedProductTypes != null) {
			purchasedProductTypes.add(productType);
		}
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get());
		String value = sharedPreferences.getString(PURCHASED_PRODUCT_TYPES, null);
		Editor editor = sharedPreferences.edit();
		if (StringUtils.isNotEmpty(value)) {
			value = value + "," + productType.getProductId();
		} else {
			value = productType.getProductId();
		}
		editor.putString(PURCHASED_PRODUCT_TYPES, value);
		editor.apply();
	}

	public synchronized List<ProductType> getPurchasedProductTypes() {
		if (purchasedProductTypes == null) {
			String purchasedProductTypesPref = PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getString(
				PURCHASED_PRODUCT_TYPES, null);
			purchasedProductTypes = Lists.newArrayList();
			for (String each : StringUtils.splitToCollectionWithCommaSeparator(purchasedProductTypesPref)) {
				List<ProductType> supportedProductTypes = Lists.newArrayList();
				supportedProductTypes.addAll(getManagedProductTypes());
				supportedProductTypes.addAll(getSubscriptionsProductTypes());
				for (ProductType supportedProductType : supportedProductTypes) {
					if (supportedProductType.getProductId().equals(each)) {
						purchasedProductTypes.add(supportedProductType);
						break;
					}
				}
			}
		}
		return purchasedProductTypes;
	}

	public List<ProductType> getManagedProductTypes() {
		return Lists.newArrayList();
	}

	public List<ProductType> getSubscriptionsProductTypes() {
		return Lists.newArrayList();
	}
}
