package com.mobisquid.mobicash.model;

public class ContactsDb {

	public String username;

	public String getContactID() {
		return contactID;
	}

	public void setContactID(String contactID) {
		this.contactID = contactID;
	}

	public String contactID;
	public String userID;
	public String mobile;
	public String url;
	public String dateonline;
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}




	public String language;
	public String online;
	public String getDateonline() {
		return dateonline;
	}

	public void setDateonline(String dateonline) {
		this.dateonline = dateonline;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public ContactsDb (String usernameo,String mobileo,String userido,String urlo,String onlineo,String dateonlineo,String languageo,String contactIDs){

		username = usernameo;
		mobile =mobileo;
		userID = userido;
		contactID =contactIDs;
		url= urlo;
		online= onlineo;
		dateonline=dateonlineo;
		language = languageo;

	}


}


