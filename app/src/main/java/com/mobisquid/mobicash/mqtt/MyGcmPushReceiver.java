package com.mobisquid.mobicash.mqtt;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.mobisquid.mobicash.dbstuff.MessageDb;
import com.mobisquid.mobicash.dbstuff.Transactiondb;
import com.mobisquid.mobicash.model.Transactions;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.MainUtils;
import com.mobisquid.mobicash.utils.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;




public class MyGcmPushReceiver extends FirebaseMessagingService {

    private static final String TAG = MyGcmPushReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageSent(String s) {
        System.out.println("============Message sent=========="+s);
        super.onMessageSent(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage message){

        String from = message.getFrom();
        Map data = message.getData();

        System.out.println("============Message arrived=========="+data.toString());
        try {
            //JSONObject datObj = new JSONObject( data.get("data").toString());
            String mymessage = data.get("message").toString();
            Log.e(TAG,"=====mymessage====="+mymessage);
            Transactions gsonMessage = new Gson().fromJson(mymessage,
                    Transactions.class);
            MessageDb chatmessage = new Gson().fromJson(mymessage,
                    MessageDb.class);
            if (gsonMessage.getType()!=null && gsonMessage.getType().equalsIgnoreCase("init")) {
                Log.e(TAG,"...............init");
                if(Transactiondb.listAll(Transactiondb.class).isEmpty()){
                    Transactiondb transdb = new Gson().fromJson(mymessage, Transactiondb.class);
                    transdb.save();
                    MainUtils.Initialiazation(gsonMessage, this);
                }else{
                    Log.e(TAG,"==========GOT ONE ALREDAY=====");
                }


            }else if(chatmessage.getMessageType()!=null){
                if(chatmessage.getMessageType().equalsIgnoreCase("chat")){
                    try {
                        String  decomes =  URLDecoder.decode(chatmessage.getMessage(), "UTF-8");
                        chatmessage.setMessage(decomes);
                        MainUtils.processChat(chatmessage,this);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                }else if(chatmessage.getMessageType().equalsIgnoreCase("ackreceiver")){
                    MainUtils.Ackreceiver(chatmessage,this);
                }

            }
           // boolean isBackground = datObj.getBoolean("is_background");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void processChatRoomPush(boolean isBackground, JSONObject datObj) {
        if (!isBackground) {
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            // the push notification is silent, may be other operations needed
            // like inserting it in to SQLite
        }
    }


    /**
     * Showing notification with text only
     * */
    private void showNotificationMessage(Context context, String title, String message, Intent intent) {
        System.out.println("=====showNotificationMessage=========");
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, "2016-09-30 9:01:20", intent);
    }


   /* private String saveMessage(Messages message){
        Messages mymsg = new Messages(message.getName(),message.getMessage(),message.getRecep(),
                message.getSender(),message.getTimesent(),message.getType(),false,1,message.getFeeditem(),message.getChatid(),
                message.getExtra(),message.getOthername(),message.getChatthread());
        mymsg.save();
        return new Gson().toJson(mymsg);
    }*/

   /* public void sendackreceive(Messages messages){
        messages.setType("ackreceive");
        String gsonMsg = new Gson().toJson(messages);
        String url="/message";
        String[] params ={"user_id","message","msgid"};
        String[] values ={};
        ConnectionClass.stringRequest(Request.Method.POST,url, params, values, new ConnectionClass.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                System.out.println("======result========"+result);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("error") == false) {


                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

    }*/
    private void showBigNotification(String title,String message,String type) {


    }
}