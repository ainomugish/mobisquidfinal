package com.mobisquid.mobicash.dbstuff;

import android.content.Context;
import com.orm.SugarRecord;

public class ContactDetailsDB extends SugarRecord {
	
	public String username;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String userid;
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


	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public ContactDetailsDB() {
		
	}
	
	public ContactDetailsDB (String username,String mobile,String userid,String url,String online,String dateonline,String language){

		this.username = username;
		this.mobile =mobile;
		this.userid = userid;
		this.url= url;
		this.online= online;
		this.dateonline=dateonline;
		this.language = language;
		
	}
	

	    }
	

