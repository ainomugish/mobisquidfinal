package com.mobisquid.mobicash.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.mobisquid.mobicash.activities.ChatActivity;
import com.mobisquid.mobicash.activities.PaymentDetails;
import com.mobisquid.mobicash.dbstuff.ContactDetailsDB;
import com.mobisquid.mobicash.dbstuff.MessageDb;
import com.mobisquid.mobicash.model.Transactions;
import com.mobisquid.mobicash.mqtt.MyGcmPushReceiver;
import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by mobicash on 3/20/17.
 */
public class MainUtils {
    static final String TAG = MainUtils.class.getSimpleName();
    static NotificationUtils notificationUtils;

    public static void Initialiazation(Transactions gsonMessage, Context context) {
        Vars vars = new Vars(context);
        vars.log("MainUtils=======Initialiazation=======");
       // MessageDetails newMessage = new Gson().fromJson(gsonMessage.getMessage(),MessageDetails.class);

        if (Globals.WHICHUSER != null && Globals.WHICHUSER.equalsIgnoreCase("pay")) {
            System.out.println("User on payment page.............");
            Intent pushNotification = new Intent(Globals.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", new Gson().toJson(gsonMessage));
            LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotification);

            Intent notificationIntent = new Intent(context, PaymentDetails.class);
            showNotificationMessageWithBigImage(context, "Mobiseller", "Payment", gsonMessage.getType(), notificationIntent);
        }else {
            System.out.println("User not on paypage.............");
            Intent notificationIntent = new Intent(context, PaymentDetails.class);
            notificationIntent.putExtra("message", new Gson().toJson(gsonMessage));
            showNotificationMessageWithBigImage(context, "Mobiseller", "Payment", gsonMessage.getType(), notificationIntent);
        }

    }
    public static void processChat(MessageDb gsonMessage, Context context) {
        Vars vars = new Vars(context);
        //send acknowledgment
        MessageDb ACK = gsonMessage;
        messageReceived(new Gson().toJson(ACK),context);
        Log.e(TAG,"WORK 0M__++__"+new Gson().toJson(gsonMessage));
        int msgid = MessageDb.listAll(MessageDb.class).size()+1;
        Log.e(TAG,"BEFORE==="+gsonMessage.getThread()+"====after==="+vars.chk+gsonMessage.getSender());
        List<ContactDetailsDB> member = Select.from(ContactDetailsDB.class).where(Condition.prop("userid").eq(gsonMessage.getSender()))
                .and(Condition.prop("mobile").eq(gsonMessage.getMobileNumber())).list();
        if(!member.isEmpty()){
            for(ContactDetailsDB con :member){
                gsonMessage.setUserName(con.getUsername());
            }
        }else {
            gsonMessage.setUserName(gsonMessage.getMobileNumber());
        }
        gsonMessage.setThread(vars.chk+gsonMessage.getSender());
        gsonMessage.setMessageId(String.valueOf(msgid));
        gsonMessage.setIsself(false);
        gsonMessage.save();


        Log.e(TAG,"STORED==="+gsonMessage.getThread());
        if (Globals.WHICHUSER != null && Globals.WHICHUSER.equalsIgnoreCase(gsonMessage.getSender())) {
            System.out.println("User on payment page.............");
            Intent pushNotification = new Intent(Globals.PUSH_INCOMINGMSG);
            pushNotification.putExtra("message", new Gson().toJson(gsonMessage));
            LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotification);

        }else {
            downloadimages(context, gsonMessage.getSender(), gsonMessage,gsonMessage.getMessageType());
        }

    }
    public static void downloadimages(final Context context, final String name, final MessageDb newMessage,String messagType ){

        Log.i(TAG,"========Down loading image========"+name);
        String message= null;
        if(messagType.equalsIgnoreCase("chat")){
            message = newMessage.getMessage();
        }else if(messagType.equalsIgnoreCase("audio")){
            message = "Audio";
        }else if(messagType.equalsIgnoreCase("image")){
            message = "Image";
        }else if(messagType.equalsIgnoreCase("video")){
            message = "Video";
        }else{
            message = "File";
        }

        Log.i(TAG,"MESS=======SENDING BROADCAST===");

        Intent notificationIntent = new Intent(context, ChatActivity.class);
        notificationIntent.putExtra("message", new Gson().toJson(newMessage));
        showNotificationMessageWithBigImage(context, newMessage.getUserName(), newMessage,message ,
                notificationIntent,Globals.USERIMAGE+name+".png");


    }
    public static void Ackreceiver(MessageDb chatmessage, Context context) {
        Log.i(TAG,"============processing ackreceiver======ID====="+chatmessage.getMessageId());
        MessageDb update = MessageDb.findById(MessageDb.class, Long.valueOf(chatmessage.getMessageId()));
        update.setDelRecep("yes");
        update.save();
        Log.e(TAG,"I updated==fffffffff" +new Gson().toJson(update));
        Intent pushNotification = new Intent(Globals.PUSH_INCOMINGMSG);
        pushNotification.putExtra("message", new Gson().toJson(update));
        LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotification);

    }
    private static void messageReceived(String newsg, Context mcontext) {
        Vars vars = new Vars(mcontext);
        Log.i(TAG,"vars to==="+vars.chk);

        MessageDb ackmsg = new Gson().fromJson(newsg, MessageDb.class);
        Log.d(TAG,"sender==="+ackmsg.getSender());
        Log.d(TAG,"recep==="+ackmsg.getRecep());
        String reccep = ackmsg.getSender();
        ackmsg.setMessageType("ackreceiver");
        ackmsg.setSender(vars.chk);
        ackmsg.setMessage("I received the message");
        ackmsg.setRecep(reccep);

        Log.d(TAG,"recep===afetr=="+ackmsg.getRecep());

        Log.e(TAG,"ACK===SEND===="+new Gson().toJson(ackmsg));
        String url = Globals.SOCILA_SERV+"entities.chat/sendMessage";
        try {
            JSONObject json = new JSONObject(new Gson().toJson(ackmsg));
            ConnectionClass.JsonString(Request.Method.POST, mcontext, url, json, "MESSAGE", new ConnectionClass.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i(TAG,"Sent recep===========" + result);
                    try {
                        JSONObject reader = new JSONObject(result);
                        if (reader.getString("error").equalsIgnoreCase("success")) {


                        }else {
                            Log.i(TAG,"NOT SENT  ======   " + result);

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    private static void showNotificationMessageWithBigImage(Context context, String title, MessageDb message, String messagetype, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, messagetype, intent, imageUrl);
    }
    private static void showNotificationMessageWithBigImage(Context context, String title, String message, String messagetype, Intent intent) {
        System.out.println("Sending noti.............");
        NotificationUtils notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, messagetype, intent);
    }


}
