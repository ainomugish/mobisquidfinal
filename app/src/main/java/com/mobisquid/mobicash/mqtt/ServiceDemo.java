package com.mobisquid.mobicash.mqtt;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.Vars;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import java.util.Calendar;


public class ServiceDemo extends Service {
	static final String TAG = "async test";
	Vars vars;
	private static final Object sSyncAdapterLock = new Object();

	SampleAsyncCallBack sampleClient;	

	//sync staff===================
	 
	//Username for mqtt client
	String userName;
	
	// -------------------keep alive
	// PINGER
	public static final String MQTT_PING_ACTION = "com.dalelane.mqtt.PING";
	// receiver that wakes the Service up when it's time to ping the server
	private PingSender pingSender;
	// receiver that notifies the Service when the phone gets data connection
	private NetworkConnectionIntentReceiver netConnReceiver;
	// KEEPALIVE
	int keepAlive = 50;

	@Override
	public IBinder onBind(Intent arg0) {

		return null;
	}

	@Override
	public void onCreate() {
		System.out
				.println("-----------: CREATING SERVICE DEMO CLASS :---------service created");

		synchronized (sSyncAdapterLock) {

		}
		
	
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("-----------: service demo - ON START :---------");
		vars = new Vars(this);

		//SET USERNAME FOR MQTT CLIENT
		if (intent != null) {
			Bundle extrasU = intent.getExtras();
			if (extrasU != null) {
				userName = extrasU.getString("userName");
				log("userName set to:" + userName);
			} else if (extrasU == null) {
				log("Username here===" + vars.chk);

				userName = vars.chk;
			}
		}else{
			log("Username here===" + vars.chk);
			userName = vars.chk;
		}
		try {
			// log("creating client");

			if (sampleClient == null) {

				Thread thread = new Thread() {
					@Override
					public void run() {
						// log("NEW THREAD");

						scheduleNextPing();
						// create pinger
						if (pingSender == null) {
							// log("ping sender is NULL creating");
							pingSender = new PingSender();
							registerReceiver(pingSender, new IntentFilter(
									MQTT_PING_ACTION));
						}
						// STATE CHANGE RECEIVER
						if (netConnReceiver == null) {
							netConnReceiver = new NetworkConnectionIntentReceiver();
							registerReceiver(netConnReceiver, new IntentFilter(
									ConnectivityManager.CONNECTIVITY_ACTION));
						}

						if (isOnline()) {
							try {
								sampleClient = new SampleAsyncCallBack(
										userName,
										getApplicationContext());

								sampleClient.publish("deathReceiver", 2,
										userName.getBytes(), "heartbeat");
							} catch (MqttException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							// sampleClient = new SampleAsyncCallBack(userName);
							catch (Throwable e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							log("inside service demo init: NOT ONLINE");
						}

					}
				};

				thread.start();

				// sampleClient.publish("hearbeat", 0, userName.getBytes());
			} else if (sampleClient != null) {
				// log("CALLED SERVICE DEMO AGAIN, CLIENT IS NOT NULL PUBLISHING...GOOD NEWS");
				String test = "threads win!";
				// sampleClient.publish("hearbeat", 0, userName.getBytes());
				if (sampleClient.client.isConnected()) {
					// log("client is connecteed");
					// log("sample client current state:" + sampleClient.state);
					sampleClient.state = 1;

					if (intent != null) {
						Bundle extras = intent.getExtras();
						if (extras != null) {
							// GET COMMAND
							if (extras.getString("command") != null) {
								System.out.println("command request received");
								String command = extras.getString("command");
								String topic = extras.getString("topic");
								//String sendernow = extras.getString("userName");
								long messId = extras.getLong("messageId");
								String messageIdString = String.valueOf(messId);
								log ("Here is msg id "+messId);
								log ("+++++++++++++++comand is ++++=============="+command);
								
								// CHECK COMMAND
								// SEND MESSAGE
								if (command.equalsIgnoreCase("sendMessage")) {
									String message = extras.getString("message");
									log("SENT==========="+message);
									sampleClient.publish(topic, 2, message.getBytes(), messageIdString);

								}else if(command.equalsIgnoreCase("sendPaymentApproval")){
									String message = extras.getString("message");
									 log("cccccccccccccccccccccccccc: COMMAND IS:"+command);
									
										//sampleClient.publish("rtip/paymentReceipt", 1,message.getBytes(),"paymentApproval");
										sampleClient.publish("2", 2,message.getBytes(),"paymentApproval");
										//--------------------SEND MESSAGE
								 }else if(command.equalsIgnoreCase("sendMessagehelp")){
									 
									 String message = extras.getString("message");
									 vars.log("===========sending help msg==========");
									 vars.log("help topic===="+topic);
									 sampleClient.publish(topic, 2,message.getBytes(),"sendMessagehelp");
									 
								 }
							}
						}
					}
					
					// sampleClient.publish("desktop2", 0, test.getBytes());
				} else if (!sampleClient.client.isConnected()) {

					reconnectClient();
				}

			}

			vars.log("SERVICE DEMO - JUST FINISHED -sampleClient.publish");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return ServiceDemo.START_STICKY;
	//	return START_STICKY;
	}
	// -----------------------------------------KEEP ALIVE ADS.
	/*
	 * Schedule the next time that you want the phone to wake up and ping the
	 * message broker server
	 */
	private void scheduleNextPing() {
		System.out.println("scheduleNextPing....called");
		PendingIntent pendingIntent = PendingIntent
				.getBroadcast(this, 0, new Intent(MQTT_PING_ACTION),
						PendingIntent.FLAG_UPDATE_CURRENT);

		Calendar wakeUpTime = Calendar.getInstance();
		// Calendar.m
		wakeUpTime.add(Calendar.SECOND, keepAlive);

		AlarmManager aMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
		aMgr.set(AlarmManager.RTC_WAKEUP, wakeUpTime.getTimeInMillis(),
				pendingIntent);
	}

	public class PingSender extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			vars = new Vars(context);
			System.out
					.println("ppppppppppppppp   Ping Sender broadcast received...");

			superReconnect();

			// start the next keep alive period
			scheduleNextPing();
		}
	}

	public void superReconnect() {
		// log("super reconnect");

		if (isOnline()) {
			System.out
					.println(" we are online attempting to reconnect Attempting to reconnect...");
			// mqttClient.connect();
			// if (mqttClient.isConnected()) {

			if (sampleClient == null) {
				// log("sample client is NULL");
				try {
					sampleClient = new SampleAsyncCallBack(userName,getApplicationContext());
					
					// log("username is: " + userName);
					sampleClient.publish("hearbeat", 2, userName.getBytes(),
							"reconnect");

				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				// log("sample client is NOT NULL");

				if (sampleClient.client.isConnected()) {
					Globals.NO_CONNECTION="connetion_found";
					
					Intent noInternet = new Intent();
					 noInternet.setAction(Globals.NO_CONNECTION);
					 noInternet.addCategory(Intent.CATEGORY_DEFAULT);
					 this.sendBroadcast(noInternet);
					
					
					System.out
							.println("Service is ALREADY CONNECTED sending heartbeat");
					log("SERVICE DEMO: STATE FROM CLIENT" + sampleClient.state);

					// log("cccccccccccccccccccc: CHECK FOR UNSENT MESSAGES");
					// MainUtils.processUnsentMessages(getApplicationContext());

					try {
						
						
						
						sampleClient.state = 1;
						sampleClient.publish("hearbeat", 2,
								userName.getBytes(), "hearbeat");
						//MainUtils.unsentMessages(vars.context);

						//MainUtils unsent = new MainUtils();
						
						// mqttClient.publish("hearbeat",
						// "cats".getBytes(),0,false);
					} catch (MqttPersistenceException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MqttException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (!sampleClient.client.isConnected()) {
					Globals.NO_CONNECTION="Connection lost";
					System.out
							.println("SERVICE DEMO: ping function CLIENT NOT CONNECTED.");
					try {
						sampleClient.state = SampleAsyncCallBack.BEGIN;
						log("SERVICE DEMO: state set to begin");
						sampleClient.publish("hearbeat", 2,
								userName.getBytes(), "reconnect");
						System.out
								.println("Service is NOT connected..now reconnected");
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						log("ERROR unable to reconnect CLIENT");
					}
					// connectBroker();
					// mqttClient.setCallback(new PushCallback(cwrap));
					// mqttClient.connect();

				}
			}

		} else {
			Globals.NO_CONNECTION="Connection lost";
			System.out.println("no internet connection found");
			 Intent noInternet = new Intent();
			 noInternet.setAction(Globals.NO_CONNECTION);
			 noInternet.addCategory(Intent.CATEGORY_DEFAULT);
			 this.sendBroadcast(noInternet);		 
			 
		
		}		
	}
	
	public void reconnectClient() {
		log("RECONNECT CLIENT FUNCTION");
		System.out.println("inside ping function CLIENT NOT CONNECTED.");
		try {
			sampleClient.state = SampleAsyncCallBack.BEGIN;
			log("cleint state set to begin");
			sampleClient.publish("hearbeat", 2, userName.getBytes(),
					"reconnectClient");
			System.out.println("Service is NOT connected..now reconnected");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log("ERROR unable to reconnect CLIENT");
		}
	}

	private boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		try {
			if (cm.getActiveNetworkInfo() != null
					&& cm.getActiveNetworkInfo().isAvailable()
					&& cm.getActiveNetworkInfo().isConnected()) {
				return true;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public void onDestroy() {
		 super.onDestroy();
		Log.d(TAG, "Servicedemo destroyed=========");
	}

	public void log(String message) {
		System.out.println("LOG:" + message);
		System.out.println(message);
	}

	public class NetworkConnectionIntentReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context ctx, Intent intent) {
			System.out
					.println("nnnnnnnnnnnnnnnnnnnnnnnnnnnnnn    ---------NetworkConnectionIntentReceiver");
			// we protect against the phone switching off while we're doing this
			// by requesting a wake lock - we request the minimum possible wake
			// lock - just enough to keep the CPU running until we've finished
			PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
			WakeLock wl = pm
					.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MQTT");
			wl.acquire();

			if (sampleClient == null) {
				log("SERVICE DEMO: INSIDE NETWORK RECEIVER: SAMPLE CLIENT IS NULLLLLLLLLLLLLLLLLLL!");
			} else if (sampleClient != null) {
				log("SERVICE DEMO: INSIDE NETWORK RECEIVER: SAMPLE CLIENT IS NOT NOT NOT NOT NULLLLLL!");
				superReconnect();
			}
			// superReconnect();
			// }

			// we're finished - if the phone is switched off, it's okay for the
			// CPU
			// to sleep now
			wl.release();
		}
	}

}