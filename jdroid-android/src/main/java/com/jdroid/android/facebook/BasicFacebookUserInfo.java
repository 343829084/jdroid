package com.jdroid.android.facebook;

public class BasicFacebookUserInfo {
	
	private String firstName;
	private String lastName;
	private String email;
	private String facebookId;
	
	public String getEmail() {
		return email;
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * @return the lasttName
	 */
	public String getLasttName() {
		return lastName;
	}
	
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFacebookId() {
		return facebookId;
	}
	
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BasicFacebookUserInfo [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}
}
