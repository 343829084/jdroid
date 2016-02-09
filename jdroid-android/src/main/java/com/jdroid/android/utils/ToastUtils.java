package com.jdroid.android.utils;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.R;

import java.lang.ref.WeakReference;

public final class ToastUtils {
	
	private static final int DEFAULT_DURATION = Toast.LENGTH_LONG;
	
	private static WeakReference<Toast> currentToast;
	
	private static final Handler HANDLER = new Handler() {
		
		@Override
		public void handleMessage(android.os.Message msg) {
			
			String message = (String)msg.obj;
			switch (msg.arg1) {
				case 1:
					showToast(message);
					break;
				case 2:
					showInfoToast(message);
					break;
				case 3:
					showWarningToast(message);
					break;
				case 4:
					showErrorToast(message);
					break;
				default:
					break;
			}
		}
	};
	
	public static void init() {
		// nothing...
	}
	
	private static void showToastOnUIThread(String message, int type) {
		HANDLER.removeMessages(1);
		HANDLER.sendMessage(HANDLER.obtainMessage(1, type, 0, message));
	}
	
	/**
	 * Show the {@link Toast} on the UI Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 */
	public static void showToastOnUIThread(String message) {
		showToastOnUIThread(message, 1);
	}
	
	/**
	 * Show the {@link Toast} on the UI Thread.
	 * 
	 * @param messageId The id of the text to show.
	 */
	public static void showToastOnUIThread(int messageId) {
		showToastOnUIThread(LocalizationUtils.getString(messageId));
	}
	
	/**
	 * Show the {@link Toast} on the current Thread.
	 * 
	 * @param messageId The id of the text to show.
	 */
	public static void showToast(int messageId) {
		showToast(LocalizationUtils.getString(messageId), DEFAULT_DURATION);
	}
	
	/**
	 * Show the {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 */
	public static void showToast(String message) {
		showToast(message, DEFAULT_DURATION);
	}
	
	/**
	 * Show the {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 */
	public static void showToast(String message, int duration) {
		showToast(message, duration, null, null, null);
	}
	
	/**
	 * Show the {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 * @param gravity The location at which the notification should appear on the screen.
	 * @param xOffset The X offset in pixels to apply to the gravity's location.
	 * @param yOffset The Y offset in pixels to apply to the gravity's location.
	 */
	public static void showToast(String message, int duration, Integer gravity, Integer xOffset, Integer yOffset) {
		showCustomToast(null, message, duration, gravity, xOffset, yOffset);
	}
	
	/**
	 * Show an info {@link Toast} on the current Thread.
	 * 
	 * @param messageId The id of the text to show.
	 */
	public static void showInfoToast(int messageId) {
		showInfoToast(LocalizationUtils.getString(messageId), DEFAULT_DURATION);
	}
	
	/**
	 * Show an info {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 */
	public static void showInfoToast(String message) {
		showInfoToast(message, DEFAULT_DURATION);
	}
	
	/**
	 * Show an info {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 */
	public static void showInfoToast(String message, int duration) {
		showInfoToast(message, duration, null, null, null);
	}
	
	/**
	 * Show an info {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 * @param gravity The location at which the notification should appear on the screen.
	 * @param xOffset The X offset in pixels to apply to the gravity's location.
	 * @param yOffset The Y offset in pixels to apply to the gravity's location.
	 */
	public static void showInfoToast(String message, int duration, Integer gravity, Integer xOffset, Integer yOffset) {
		showCustomToast(R.drawable.ic_info_white_24dp, message, duration, gravity, xOffset, yOffset);
	}
	
	/**
	 * Show a warning {@link Toast} on the current Thread.
	 * 
	 * @param messageId The id of the text to show.
	 */
	public static void showWarningToast(int messageId) {
		showWarningToast(LocalizationUtils.getString(messageId), DEFAULT_DURATION);
	}
	
	/**
	 * Show a warning {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 */
	public static void showWarningToast(String message) {
		showWarningToast(message, DEFAULT_DURATION);
	}
	
	/**
	 * Show a warning {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 */
	public static void showWarningToast(String message, int duration) {
		showWarningToast(message, duration, null, null, null);
	}
	
	/**
	 * Show a warning {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 * @param gravity The location at which the notification should appear on the screen.
	 * @param xOffset The X offset in pixels to apply to the gravity's location.
	 * @param yOffset The Y offset in pixels to apply to the gravity's location.
	 */
	public static void showWarningToast(String message, int duration, Integer gravity, Integer xOffset, Integer yOffset) {
		showCustomToast(R.drawable.ic_warning_white_24dp, message, duration, gravity, xOffset, yOffset);
	}
	
	/**
	 * Show the error {@link Toast} on the UI Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 */
	public static void showErrorToastOnUIThread(String message) {
		showToastOnUIThread(message, 4);
	}
	
	/**
	 * Show an error {@link Toast} on the current Thread.
	 * 
	 * @param messageId The id of the text to show.
	 */
	public static void showErrorToast(int messageId) {
		showErrorToast(LocalizationUtils.getString(messageId), DEFAULT_DURATION);
	}
	
	/**
	 * Show an error {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 */
	public static void showErrorToast(String message) {
		showErrorToast(message, DEFAULT_DURATION);
	}
	
	/**
	 * Show an error {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 */
	public static void showErrorToast(String message, int duration) {
		showErrorToast(message, duration, null, null, null);
	}
	
	/**
	 * Show an error {@link Toast} on the current Thread.
	 * 
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 * @param gravity The location at which the notification should appear on the screen.
	 * @param xOffset The X offset in pixels to apply to the gravity's location.
	 * @param yOffset The Y offset in pixels to apply to the gravity's location.
	 */
	public static void showErrorToast(String message, int duration, Integer gravity, Integer xOffset, Integer yOffset) {
		showCustomToast(R.drawable.ic_error_white_24dp, message, duration, gravity, xOffset, yOffset);
	}
	
	/**
	 * Show the {@link Toast} on the current Thread.
	 * 
	 * @param imageId The id of the image to display
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 * @param gravity The location at which the notification should appear on the screen.
	 * @param xOffset The X offset in pixels to apply to the gravity's location.
	 * @param yOffset The Y offset in pixels to apply to the gravity's location.
	 */
	public static void showCustomToast(Integer imageId, String message, int duration, Integer gravity, Integer xOffset,
			Integer yOffset) {
		AbstractApplication androidApplication = AbstractApplication.get();
		if (!androidApplication.isInBackground()) {
			
			LayoutInflater inflater = LayoutInflater.from(androidApplication);
			View view = inflater.inflate(R.layout.toast_custom, null);
			
			if (imageId != null) {
				ImageView imageView = (ImageView)view.findViewById(R.id.toastImage);
				imageView.setImageResource(imageId);
				imageView.setVisibility(View.VISIBLE);
			}
			
			TextView textView = (TextView)view.findViewById(R.id.toastMessage);
			textView.setText(message);
			
			cancelCurrentToast();
			
			Toast toast = new Toast(androidApplication);
			if ((gravity != null) && (xOffset != null) && (yOffset != null)) {
				toast.setGravity(gravity, xOffset, yOffset);
			}
			toast.setDuration(duration);
			toast.setView(view);
			toast.show();
			
			currentToast = new WeakReference<>(toast);
		}
	}
	
	public static void cancelCurrentToast() {
		Toast toast = currentToast != null ? currentToast.get() : null;
		if (toast != null) {
			toast.cancel();
		}
	}
}
