package com.mobisquid.mobicash.utils;

import com.google.gson.Gson;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.mobisquid.mobicash.R;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


public class Vars {

	// JSON OBJECT TO HOLD REPLY
	
	// APP VARS
	public String username;
	public boolean isfinanance;
	public String firtstime_number;
   // public String server_sa,server_india;
	public String profile_url;
   public String server_mb;
	public String qrcode,token;
	public String electricity_number;
	public String smartcard_gotv,smartcard_dstv,smartcard_startime,smartcard_canal;
	public String pin;
	public boolean chaton;
	public String financial_server;
	public static final String glassser = "http://52.26.63.185:10080/RestApi/webresources/";
	public String profile_number;
	public String onof;
	public int regno;
	public Long msgid;
	public String recep;
	public String cyclose;
	public String active;
	public String social;
	public String chat_account;
	public String chk;
	public String mobile;
	public String country_code;
	public String font;
	public String recepMobile;
	public String location;
	public boolean review;
	public String language;
	public String sender;
	public String wallpaper;
	public SharedPreferences prefs ,shprefs;
	public Editor edit;
	public String downloadReference;
	public String cyclos_number;
   public String mylife;
	public ProgressDialog pd;
	public String imei;
	public Gson gson;
	public String updatecontacts;
	public String fullname,email;
	public String accesscode;
	public Context context;
	private Boolean logg= true;
	
	public Vars(Context context) {
		// this.alerter = new Alerter(context);
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		financial_server = prefs.getString("financial_server", null);
		email= prefs.getString("email", null);
		qrcode= prefs.getString("qrcode", null);
		token = prefs.getString("token", null);
		server_mb = prefs.getString("server", "http://52.26.63.185/sugaapp/mobisquid/");
		chaton = prefs.getBoolean("chaton",false);
		isfinanance =prefs.getBoolean("isfinanance",false);
      font = prefs.getString("font", null);

		wallpaper = prefs.getString("wallpaper", null);
		cyclos_number = prefs.getString("cyclos_number",null);
		electricity_number = prefs.getString("electricity_number", null);
		profile_url= prefs.getString("profile_url", null);
		smartcard_canal = prefs.getString("smartcard_canal", null);
		smartcard_dstv = prefs.getString("smartcard_dstv", null);
		smartcard_startime = prefs.getString("smartcard_startime", null);
		smartcard_gotv = prefs.getString("smartcard_gotv", null);
		firtstime_number = prefs.getString("firtstime_number",null);
		accesscode = prefs.getString("accesscode",null);
		downloadReference = prefs.getString("downloadReference", null);
		review = prefs.getBoolean("review",false);

      mylife = prefs.getString("mylife", null);
		active = prefs.getString("active",null);
		social= prefs.getString("social",null);
		chat_account = prefs.getString("chat_account",null);
		location = prefs.getString("location", null);
		cyclose = prefs.getString("cyclose", null);
		fullname = prefs.getString("fullname",null);
		regno = prefs.getInt("regno",0);
		updatecontacts=prefs.getString("updatecontacts",null);
		sender = null;
		msgid = null;
		onof= null;
		// TEMP REMOVE LATER
	  //userName = "andyTest";
		this.context=context;

		// TEMP REMOVE LATER
		// userName = "mworoziIDEOS";
		//username = "andrewConn";
		chk = prefs.getString("userId", null);
		recep = prefs.getString("recep",null);
		pin = prefs.getString("pin",null);
		username = prefs.getString("username", null);
		language = prefs.getString("language", null);
		profile_number= prefs.getString("profile_number", null);
		mobile = prefs.getString("mobile", null);
		country_code = prefs.getString("country_code", null);
		imei = prefs.getString("imei", null);
		recepMobile = prefs.getString("recepNumber", null);
		//username = "andrew";
		edit = prefs.edit();
		// ALERTER
		
		
		gson = new Gson();
		
	}
	public void log(String string){
		if (logg){
		System.out.println(string);	
		}else{
			
		}
	}
	public void hideKeyboard(View view,Context context)
	{
		InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	public File saveImage(Bitmap thePic, String fileName, Context context) {
		OutputStream fOut = null;


		File m = new File(Environment.getExternalStorageDirectory(), "/Mobisquid/media");
		if (!m.exists()) {
			m.mkdirs();
		}


		if (!m.exists()) {
			m.mkdirs();
		}

		File f = new File(m, fileName);

		try {
			fOut = new FileOutputStream(f);

			/** Compress image **/
			thePic.compress(Bitmap.CompressFormat.PNG, 85, fOut);
			fOut.flush();
			fOut.close();

			/** Update image to gallery **/
			MediaStore.Images.Media.insertImage(context.getContentResolver(),
					f.getAbsolutePath(), f.getName(), f.getName());
			log("IMAGE SAVED........");

			//SAVE FACE IMAGE LOCATION
			log("ABSOLUTE PATH:" + f.getAbsolutePath());
			log("FILE NAME:" + f.getName());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}
	public boolean Checknumber(Context context,String code,String number){

		String CountryID="";

		String[] rl=context.getResources().getStringArray(R.array.CountryCodes);
		for(int i=0;i<rl.length;i++){
			String[] g=rl[i].split(",");
			if(g[0].trim().equals(code.trim())){
				CountryID=g[1];
				break;
			}
		}

		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		try {
			Phonenumber.PhoneNumber phone_number = phoneUtil.parse(number, CountryID);

			return phoneUtil.isValidNumber(phone_number);
		}catch (NumberParseException e){
			e.printStackTrace();
			return false;
		}



	}


}
