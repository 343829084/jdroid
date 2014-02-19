package com.jdroid.android.social;

import com.jdroid.android.R;
import com.jdroid.android.domain.Entity;

/**
 * 
 * @author Maxi Rosson
 */
public class SocialUser extends Entity {
	
	public enum SocialNetwork {
		FACEBOOK(R.drawable.facebook_icon),
		GOOGLE_PLUS(R.drawable.common_signin_btn_icon_normal_light);
		
		private int iconResId;
		
		private SocialNetwork(int iconResId) {
			this.iconResId = iconResId;
		}
		
		public int getIconResId() {
			return iconResId;
		}
	}
	
	private String socialId;
	private SocialNetwork socialNetwork;
	private String firstName;
	private String lastName;
	private String imageURL;
	
	public SocialUser(Long id, String socialId, SocialNetwork socialNetwork, String firstName, String lastName,
			String imageURL) {
		super(id);
		this.socialId = socialId;
		this.socialNetwork = socialNetwork;
		this.firstName = firstName;
		this.lastName = lastName;
		this.imageURL = imageURL;
	}
	
	public String getFullname() {
		return firstName + " " + lastName;
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	
	public String getImageUrl() {
		return imageURL;
	}
	
	/**
	 * @return the socialNetwork
	 */
	public SocialNetwork getSocialNetwork() {
		return socialNetwork;
	}
	
	/**
	 * @return the socialId
	 */
	public String getSocialId() {
		return socialId;
	}
}