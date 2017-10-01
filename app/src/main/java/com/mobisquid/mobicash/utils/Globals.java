package com.mobisquid.mobicash.utils;

import android.app.Fragment;

public class Globals {
	//ACCOUNT
	public static final String ACCOUNT_TYPE = "com.mobisquid.mobicash";
	public static final String ACCOUNT_NAME = "Mobisquid";
	public static final String ACCOUNT_TOKEN = "123451223342";
	public static final String ESHOPPING_SERVER = "http://52.38.75.235:8080/api/Category/";

//    public static final String AUTHTOKEN_TYPE_READ_ONLY = "Read only";

	public static final String AUTHTOKEN_TYPE_FULL_ACCESS = "Full access";
	//ACCOUNT

	//IPHONE
	public static final String EVERYONE ="everyone";
	public static final String SOCILA_SERV = "https://api.mobisquid.com/webresources/";
	public static final String USERIMAGE="https://api.mobisquid.com/images/profile/";
	public static String SEEN = "" ;
	public static int NUMUSERS = 0 ;
	public static String whichfag = null;
	//	financial_server = prefs.getString("financial_server", null);
	public static String WHICHUSER ="" ;
	public static final String PUSH_NOTIFICATION = "pushNotification";
	public static final String PUSH_STATUS = "pushmessagestatus";
	public static final String appName="webserviceshhhh";
	public static final String SA_SERVER = "http://test.mobicash.co.za/bio-api/mobiSquid/";
	public static final String INDIA_SERVER = "http://app.proxicash.in/bio-api/mobiSquid/";
	public static final String RW_SERVER = "http://test.mcash.rw/bio-api/mobiSquid/";
	public static final String MALAWI_SERVER = "http://malawi.mobicashonline.com/bio-api/mobiSquid/";
	//public static final String RW_SERVER = "http://app.mcash.rw/bio-api/mobiSquid/";
	public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
	public static final String REGISTRATION_COMPLETE = "registrationComplete";
	public static final int ONE=1;
	public static final int TWO=2;
	public static final int THREE=3;
	public static final int FOUR=4;
	public static final int FIVE =5 ;
	public static final int PUSH_TYPE_CHATROOM = 1;
	public static final int PUSH_TYPE_USER = 2;

	// id to handle the notification in the notification try
	public static final int NOTIFICATION_ID = 100;
	public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
	public static final String MESS_REPLY ="reply" ;
	public static final String MESS = "messages";
	public static final String SA_URL_AIRTIME = "http://54.149.233.142:9999/freePaid-1.0-SNAPSHOT/webresources/airtime/";
	public static boolean appendNotificationMessages = true;
	// BROADCAST FINALS
	public static final String MESS_REC = "suga.messageReceived";
	public static final String UPDATE_rec = "number.of.msgs";
	public static final String DONE_UPLOAD = "done.upload";
	public static final String MESS_DEL_SERV = "suga.messageReceivedServer";
	public static final String MESS_DEL_RECEP = "suga.messageReceivedRecep";

	public static boolean SingleTon = false;
	public static boolean CLOSE = false;
	public static String whichuser = "";
	public static String ONLINE_STATE = "";
	public static final String SMSPROVE = "mobisquid";

	// FLAG FOR CONNECTED
	public final static String CONNECTED = "connected";
	// FLAG FOR CONNECTED
	public final static String DISCONNECTED = "disconnected";
	public static String NO_CONNECTION = "";
	public static final String MESS_DEL_SERV_HELP ="help.messageReceivedServer";

    public static final String PUSH_INCOMINGMSG ="Received.message";
}