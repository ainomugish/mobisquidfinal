package com.mobisquid.mobicash.mqtt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.mobisquid.mobicash.dbstuff.ContactDetailsDB;
import com.mobisquid.mobicash.dbstuff.MessageDb;
import com.mobisquid.mobicash.dbstuff.Socialdb;
import com.mobisquid.mobicash.dbstuff.TempStore;
import com.mobisquid.mobicash.dbstuff.Transactiondb;
import com.mobisquid.mobicash.model.Transactions;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.MainUtils;
import com.mobisquid.mobicash.utils.Utils;
import com.mobisquid.mobicash.utils.Vars;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONException;

import java.sql.Timestamp;
import java.util.List;


public class SampleAsyncCallBack implements MqttCallback {
	
	static final String TAG = SampleAsyncCallBack.class.getSimpleName();
	static final int BEGIN = 0;
	static int state = BEGIN;
	static final int CONNECTED = 1;
	static final int PUBLISHED = 2;
	static final int SUBSCRIBED = 3;
	static final int DISCONNECTED = 4;
	static final int FINISH = 5;
	static final int ERROR = 6;
	static final int DISCONNECT = 7;

	// PREFS
	SharedPreferences prefs;

	static MqttAsyncClient client;
	String brokerUrl;
	private boolean quietMode;
	private MqttConnectOptions conOpt;
	private boolean clean;
	static Throwable ex = null;
	static Object waiter = new Object();
	static boolean donext = false;
	private String password;
	// private String userNameMqtt;
	// String clientId = "testerhope";
	String clientId;

	// String userName = "";
	String userName;
	Vars vars;

	SampleAsyncCallBack sampleClient;

	// ANDROID INSERTS
	Context context;

	// BROADCAST CONSTANTS
	// PINGER
	public static final String MQTT_PING_ACTION = "com.dalelane.mqtt.PING";
	// DELIVERY COMPLETE
	public static final String MQTT_DEL_COMPLETE = "MQTT_DEL_COMPLETE";
	// CONNECTED
	public static final String MQTT_CONNECTED = "MQTT_CONNECTED";
	// DIS - CONNECTED
	public static final String MQTT_DISCONNECTED = "MQTT_DISCONNECTED";

	// public static final String ACK_RESP = "eride.MESSAGE_REC";

	Gson gson;

	//CONSTRUCTOR THAT CREATS CLIENTS WITH OPTIIONS BUT HASN'T CONNECTED YET..
	public SampleAsyncCallBack(String userNameL, Context contextL)
			throws MqttException {
		System.out
				.println("-----------: MQTT - CREATING SampleAsyncCallBack :---------");
		
		this.brokerUrl = "tcp://52.26.63.185:1883";

		this.quietMode = false;
		this.clean = true;
		this.password = null;
		// this.userNameMqtt = null;
		vars = new Vars(contextL);
		userName = userNameL;
		// log("mqtt new username is:"+userName);

		gson = new Gson();
		
		// this.userName = userNameL;
		this.context = contextL;
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		// This sample stores in a temporary directory... where messages
		// temporarily
		// stored until the message has been delivered to the server.
		// ..a real application ought to store them somewhere
		// where they are not likely to get deleted or tampered with
		// String tmpDir = System.getProperty("java.io.tmpdir");
		MemoryPersistence persistence = new MemoryPersistence();

		try {

			conOpt = new MqttConnectOptions();

			conOpt.setWill("death", userName.getBytes(), 2, false);
			//conOpt.setCleanSession(true);
			
			conOpt.setCleanSession(false);
			if (password != null) {
				conOpt.setPassword(this.password.toCharArray());
			}
			if (userName != null) {
				conOpt.setUserName(this.userName);
			
			}

			// Construct the MqttClient instance
			// client = new MqttAsyncClient(this.brokerUrl, clientId,
			// persistence);
			// log("MQTT: MqttAsyncClient username is:" + this.userName);
			client = new MqttAsyncClient(this.brokerUrl, this.userName,
					persistence);

			// Set this wrapper as the callback handler
			client.setCallback(this);

		} catch (MqttException e) {
			e.printStackTrace();
			log("Unable to set up client: " + e.toString());
			// System.exit(1);
		}
	}

	/**
	 * Publish / send a message to an MQTT server
	 * 
	 * @param topicName
	 *            the name of the topic to publish to
	 * @param qos
	 *            the quality of service to delivery the message at (0,1,2)
	 * @param payload
	 *            the set of bytes to send to the MQTT server
	 * @throws MqttException
	 */

	public void publish(String topicName, int qos, byte[] payload,
			String userContext) throws Throwable {
		log("MQTT CLIENT...PUBLSIHING FUNCTION....:");
		printState();
		
		// Use a state machine to decide which step to do next. State change
		// occurs
		// when a notification is received that an MQTT action has completed
		while (state != FINISH) {
			switch (state) {
			case BEGIN:
				log("PUBLSIHING FUNCTION: CASE BEGIN");
				// Connect using a non blocking connect

				MqttConnector con = new MqttConnector();
				con.doConnect();
				break;
			case CONNECTED:
				Publisher pub = new Publisher();
				pub.doPublish(topicName, qos, payload, userContext);
				// log("finished publish");

				// log("state IS:" + state);

				// log("starting Subscriber");
				Subscriber sub = new Subscriber();
				//sub.doSubscribe(vars.mobile, 2);
				//sub.doSubscribe("online", 2);
				//sub.doSubscribe("webservices_", 2);
				if(vars.chk!=null){
					sub.doSubscribe(vars.chk, 2);
					sub.doSubscribe(Utils.getDeviceImei(vars.context).trim(),2);
					log("sub to========="+Utils.getDeviceImei(vars.context).trim());
				}
				if(vars.mobile!=null){
					log("sub======......"+vars.mobile);
					String number =null;

					if(String.valueOf(vars.mobile.charAt(0)).equalsIgnoreCase("+")){
						number = vars.mobile.substring(1);
						sub.doSubscribe(number, 2);

					}else {
						sub.doSubscribe(vars.mobile, 2);
					}


				}

				sub.doSubscribe(Globals.appName, 2);
				//sub.doSubscribe(Globals.appName + "/" + userName + "/#", 2);
				//sub.doSubscribe(Globals.appName + "/everyone/#", 2);

				state = FINISH;
				// log("state IS:" + state);
				donext = true;
				
				break;
			case PUBLISHED:
				// state = DISCONNECT;
				state = FINISH;
				donext = true;
				break;
			case DISCONNECT:
				Disconnector disc = new Disconnector();
				disc.doDisconnect();
				break;
			case ERROR:
				throw ex;
			case DISCONNECTED:
				state = FINISH;
				donext = true;
				break;
			}

			waitForStateChange(3000);
			// }
		}
	}

	/**
	 * Wait for a maximum amount of time for a state change event to occur
	 * 
	 * @param maxTTW
	 *            maximum time to wait in milliseconds
	 * @throws MqttException
	 */
	private void waitForStateChange(int maxTTW) throws MqttException {
		synchronized (waiter) {
			if (!donext) {
				try {
					waiter.wait(maxTTW);
				} catch (InterruptedException e) {
					log("timed out");
					e.printStackTrace();
				}

				if (ex != null) {
					throw (MqttException) ex;
				}
			}
			donext = false;
		}
	}
	
	/**
	 * Subscribe to a topic on an MQTT server Once subscribed this method waits
	 * for the messages to arrive from the server that match the subscription.
	 * It continues listening for messages until the enter key is pressed.
	 * 
	 * @param topicName
	 *            to subscribe to (can be wild carded)
	 * @param qos
	 *            the maximum quality of service to receive messages at for this
	 *            subscription
	 * @throws MqttException
	 */
	public void subscribe(String topicName, int qos) throws Throwable {
		//utils.Logg.logFunc("MQTT: - SUBSCRIBE FUNCTION");
		log("-- topic:" + topicName);
		printState();
		// Use a state machine to decide which step to do next. State change
		// occurs
		// when a notification is received that an MQTT action has completed
		while (state != FINISH) {
			switch (state) {
			case BEGIN:
				// Connect using a non blocking connect
				// MqttConnector con = new MqttConnector();
				// con.doConnect();
				break;
			case CONNECTED:
				// Subscribe using a non blocking subscribe
				Subscriber sub = new Subscriber();
				sub.doSubscribe(topicName, qos);
				break;
			case SUBSCRIBED:
				log("MQTT: INSIDE SUBSCRIBE FUNCTION");
				// Block until Enter is pressed allowing messages to arrive
				// log("Press <Enter> to exit");
				// try {
				// System.in.read();
				// } catch (IOException e) {
				// // If we can't read we'll just exit
				// }
				log("SHIT! ABOUT TO DISconnected, state changing");
				// state = DISCONNECT;
				// donext = true;
				// break;

				state = FINISH;
				donext = true;
				break;
			case DISCONNECT:
				log("MQTT: INSIDE SUBSCRIBE FUNCTION");
				log("SATE IS DISCONNECT");
				Disconnector disc = new Disconnector();
				disc.doDisconnect();
				break;
			case ERROR:
				throw ex;
			case DISCONNECTED:
				state = FINISH;
				donext = true;
				break;
			}

			// if (state != FINISH && state != DISCONNECT) {
			waitForStateChange(10000);
		}
		// }
	}

	/**
	 * Utility method to handle logging. If 'quietMode' is set, this method does
	 * nothing
	 * 
	 * @param message
	 *            the message to log
	 */
	void log(String message) {
		// if (!quietMode) {
		// System.out.println(message);
		// }
		System.out.println(message);
	}

	// PRINT STATE
	void printState() {
		log("STATE:" + getState(state));
	}

	// GET THE NOTIFIER STATE
	String getState(int state) {
		String stateF = "NO STATE FOUND";
		switch (state) {
		case 0:
			stateF = "BEGIN";
		case 1:
			stateF = "CONNECTED";
		case 2:
			stateF = "PUBLISHED";
		case 3:
			stateF = "SUBSCRIBED";
		case 4:
			stateF = "DISCONNECTED";
		case 5:
			stateF = "FINISH";
		case 6:
			stateF = "ERROR";
		case 7:
			stateF = "DISCONNECT";
		}
		return stateF;
	}

	/**
	 * *************************************************************
	 */
	/* Methods to implement the MqttCallback interface */
	/**
	 * *************************************************************
	 */
	/**
	 * @see MqttCallback#connectionLost(Throwable)
	 */
	public void connectionLost(Throwable cause) {
		// Called when the connection to the server has been lost.
		// An application may choose to implement reconnection
		// logic at this point. This sample simply exits.
		log("!!!!!!!!!!!!!!!!!!!!!Connection to " + brokerUrl + " lost!!!!!!!!!!!!!!!");
		log("attempty to REEONNNECT......");
		
		// SEND connectionLost BROADCAST
				Intent broadcastIntent = new Intent();
				broadcastIntent.setAction(Globals.DISCONNECTED);
				broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
				System.out
						.println("sending DISCONNECTED broadcast......................");
				context.sendBroadcast(broadcastIntent);
		
		// reConnect();
		// System.exit(1);
	}

	// BY ME
	public void reConnect() {
		// log("recoennect funcCIOTN");
	//	Logg.logFunc("MQTT: reConnect");
		// Connect using a non blocking connect
		MqttConnector con = new MqttConnector();
		con.doConnect();
		// state = CONNECTED;
		Publisher pub = new Publisher();
		// log("RECONNECT: reconneted publishing to:hearbeat");
		// sampleClient.publish("hearbeat", 0, userName.getBytes());
		// added by moi
		// Subscriber sub = new Subscriber();
		// sub.doSubscribe(userName, 2);
		// log("RECONNECT: reconneted Subscriber to:"+userName);
		// log("RECONNECT: reconneted Subscriber to:");
	}

	/**
	 * @see MqttCallback#deliveryComplete(IMqttDeliveryToken)
	 */
	public void deliveryComplete(IMqttDeliveryToken token) {
		log("MQTT: deliveryComplete CALL BACK");
		log("IMqttToken USER CONTEXT:===============+++" + (String) token.getUserContext());
		Vars vars = new Vars(context);
		vars.log("===Mobile=======+++++++"+vars.mobile);
		
		String messageContext = (String) token.getUserContext();
		// SEND MESSAGE RECEIVED BROADCAST
		log("messageContext======"+messageContext);
		if(messageContext.contains("paymentApproval")){
			
		}else if(messageContext.contains("sendMessagehelp")){
			Intent broadcastIntent = new Intent();
			//broadcastIntent.setAction(MQTT_DEL_COMPLETE);
			broadcastIntent.setAction(Globals.MESS_DEL_SERV_HELP);
			broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
			broadcastIntent.putExtra("messageID", messageContext);
			System.out.println("sending sent to server broadcast........help..............");
			context.sendBroadcast(broadcastIntent);
		}else{
		
		if (messageContext.contains("hearbeat")|| messageContext.contains("messageAck")){
			log ("messageContext has+++heartbeat+++++++++"+(String) token.getUserContext());
		}else if(messageContext.contains("6008143L")){
            vars.log("=====================sending resquest===delivered======="+messageContext);
        }
		else{
			
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(Globals.MESS_DEL_SERV);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.putExtra("messageID", messageContext);
		System.out
		.println("sending sent to server broadcast......................");
		context.sendBroadcast(broadcastIntent);

		}
	}}

	/**
	 * @throws JSONException
	 * @see MqttCallback#messageArrived(String, MqttMessage)
	 */
	public void messageArrived(String topic, MqttMessage message)
			throws MqttException,NullPointerException ,JSONException {
		// Called when a message arrives from the server that matches any
		// subscription made by the client
		String time = new Timestamp(System.currentTimeMillis()).toString();
		System.out.println("MESSAGE RECEIVED: Time:\t" + time + "  Topic:\t"
				+ topic + "  Message:\t" + new String(message.getPayload())
				+ "  QoS:\t" + message.getQos());
		String checkMessage = new String(message.getPayload());

		if (topic.equalsIgnoreCase("online")){

			vars.log("begin====="+checkMessage+"+++mydevice is==="+userName);

			
		}
		else if(topic.equalsIgnoreCase(Utils.getDeviceImei(context).trim())){

			vars.log("====I THINK CLEAR=====");
			MessageDb incomemsg = new Gson().fromJson(checkMessage,
					MessageDb.class);
			vars.log("===Utils.getDeviceImei(context)====="+Utils.getDeviceImei(context).trim());
			vars.log("====incomemsg.getMessage()====="+incomemsg.getMessage());
			if(incomemsg.getMessageType().equalsIgnoreCase("clear") &&
					incomemsg.getMessage().equalsIgnoreCase(Utils.getDeviceImei(context).trim())){
				vars.log("====CLEAR=====");
				TempStore.deleteAll(TempStore.class);
				MessageDb.deleteAll(MessageDb.class);
				Socialdb.deleteAll(Socialdb.class);
				ContactDetailsDB.deleteAll(ContactDetailsDB.class);
				vars.edit.clear().commit();
				System.exit(0);

			}
		}
		else if(topic.equalsIgnoreCase(Globals.EVERYONE)){

			vars.log("begin=====Globals.EVERYONE+++ is==="+Globals.EVERYONE);
		}

		else{

		if (topic.equalsIgnoreCase(userName)) {
			Transactions gsonMessage = gson.fromJson(checkMessage,
					Transactions.class);

			log("...........: message to username topic is:" + topic);
			
			log("SENT JSON STRING:"+checkMessage);


			if (gsonMessage.getType().equalsIgnoreCase("init")) {
				log("...............init");
				if(Transactiondb.listAll(Transactiondb.class).isEmpty()){
					Transactiondb transdb = gson.fromJson(checkMessage, Transactiondb.class);
					transdb.save();
					MainUtils.Initialiazation(gsonMessage, context);
				}else{
					Log.e(TAG,"==========GOT ONE ALREDAY=====");
				}


			}

		}
	}
	}
	/**
	 * Connect in a non blocking way and then sit back and wait to be notified
	 * that the action has completed.
	 */
	public class MqttConnector {

		public MqttConnector() {
		}

		public void doConnect() {
			// Connect to the server
			// Get a token and setup an asynchronous listener on the token which
			// will be notified once the connect completes
			log("MQTT: MqttConnector: Connecting to " + brokerUrl
					+ " with client ID " + client.getClientId());

			IMqttActionListener conListener = new IMqttActionListener() {
				public void onSuccess(IMqttToken asyncActionToken) {
					log("MQTT: CONNECTOR IMqttActionListener -- CONNECT SUCCESSFULL!");
					log("MQTT: CONNECTOR IMqttActionListener -- context:"
							+ asyncActionToken.getUserContext());
					state = CONNECTED;

					// CHECK IF ITS A MESSAGE ID
					String messageContext = (String) asyncActionToken
							.getUserContext();
					// SEND MESSAGE RECEIVED BROADCAST
					Intent broadcastIntent = new Intent();
					broadcastIntent.setAction(MQTT_CONNECTED);
					broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
					broadcastIntent.putExtra("message", messageContext);
					System.out
							.println("sending sent to server broadcast......................");
					context.sendBroadcast(broadcastIntent);

					carryOn();
				}

				public void onFailure(IMqttToken asyncActionToken,
						Throwable exception) {
					ex = exception;
					state = ERROR;
					log("connect failed" + exception);
					carryOn();
				}

				public void carryOn() {
					synchronized (waiter) {
						donext = true;
						waiter.notifyAll();
					}
				}
			};

			try {
				// Connect using a non blocking connect
				client.connect(conOpt, "Connect context", conListener);
				// log("INSIDE DO CONNECT MQTT FUNCTION");
			} catch (MqttException e) {

				state = ERROR;
				donext = true;
				ex = e;
				e.printStackTrace();
			}
		}
	}

	/**
	 * Publish in a non blocking way and then sit back and wait to be notified
	 * that the action has completed.
	 */
	public class Publisher {

		// public void doPublish(String topicName, int qos, byte[] payload) {
		public void doPublish(String topicName, int qos, byte[] payload,
				String userContext) {
			// Send / publish a message to the server
			// Get a token and setup an asynchronous listener on the token which
			// will be notified once the message has been delivered
			MqttMessage message = new MqttMessage(payload);
			message.setQos(qos);

			String time = new Timestamp(System.currentTimeMillis()).toString();
			log("Publishing at: " + time + " to topic \"" + topicName
					+ "\" qos " + qos);

			// Setup a listener object to be notified when the publish
			// completes.
			// ASD
			IMqttActionListener pubListener = new IMqttActionListener() {
				public void onSuccess(IMqttToken asyncActionToken) {
					log("Publish Completed");

					String test = "test";
					asyncActionToken.setUserContext(test);

					state = PUBLISHED;
					
					//SEND CONNECTED BROADCAST
					// SEND MESSAGE RECEIVED BROADCAST
					Intent broadcastIntent = new Intent();
					broadcastIntent.setAction(Globals.CONNECTED);
					broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
					broadcastIntent.putExtra("message", "CONNECTED");
					System.out
							.println("sending connected to broadcast......................");
					context.sendBroadcast(broadcastIntent);
					
					carryOn();
				}

				public void onFailure(IMqttToken asyncActionToken,
						Throwable exception) {
					ex = exception;
					state = ERROR;
					log("Publish failed" + exception);
					carryOn();
				}

				public void carryOn() {
					synchronized (waiter) {
						donext = true;
						waiter.notifyAll();
					}
				}
			};

			try {
				// Publish the message
				// client.publish(topicName, message, "Pub sample context",
				// pubListener);
				// NEW WAY ADDING CONTEXT VAR
				client.publish(topicName, message, userContext, pubListener);

			} catch (MqttException e) {
				state = ERROR;
				donext = true;
				ex = e;
			}
		}
	}

	/**
	 * Subscribe in a non blocking way and then sit back and wait to be notified
	 * that the action has completed.
	 */
	public static class Subscriber {

		public void doSubscribe(String topicName, int qos) {
			// Make a subscription
			// Get a token and setup an asynchronous listener on the token which
			// will be notified once the subscription is in place.

			
			// log("Subscribing to topic \"" + topicName + "\" qos " + qos);

			IMqttActionListener subListener = new IMqttActionListener() {
				public void onSuccess(IMqttToken asyncActionToken) {

					// log("Subscribe Completed");
					state = SUBSCRIBED;
					carryOn();
				}

				public void onFailure(IMqttToken asyncActionToken,
						Throwable exception) {

					state = ERROR;

					carryOn();
				}

				public void carryOn() {
					synchronized (waiter) {
						donext = true;
						waiter.notifyAll();
					}
				}
			};

			try {
				client.subscribe(topicName, qos, "Subscribe context - "
						+ topicName, subListener);
			} catch (MqttException e) {
				state = ERROR;
				donext = true;
				ex = e;
			}
		}

		public void unSubscribe(String topicName) {

			try {
				client.unsubscribe(topicName);
			} catch (MqttException e) {
				state = ERROR;
				donext = true;
				ex = e;
			}
		}
	}

	/**
	 * Disconnect in a non blocking way and then sit back and wait to be
	 * notified that the action has completed.
	 */
	public class Disconnector {

		public void doDisconnect() {
			// Disconnect the client
			log("Disconnecting");

			IMqttActionListener discListener = new IMqttActionListener() {
				public void onSuccess(IMqttToken asyncActionToken) {
					log("Disconnect Completed");
					state = DISCONNECTED;
					carryOn();
				}

				public void onFailure(IMqttToken asyncActionToken,
						Throwable exception) {
					
					ex = exception;
					state = ERROR;
					log("Disconnect failed" + exception);
					carryOn();
				}

				public void carryOn() {
					synchronized (waiter) {
						donext = true;
						waiter.notifyAll();
					}
				}
			};

			try {
				client.disconnect("Disconnect sample context", discListener);
			} catch (MqttException e) {
				state = ERROR;
				donext = true;
				ex = e;
			}
		}
	}

	// @Override
	// public IBinder onBind(Intent arg0) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	class Jsono {
		String name;

		public List<String> getOnLine() {
			return onLine;
		}

		public void setOnLine(List<String> onLine) {
			this.onLine = onLine;
		}

		List<String> onLine;


		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}


	}
}