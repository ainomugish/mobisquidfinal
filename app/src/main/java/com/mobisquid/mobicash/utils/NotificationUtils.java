package com.mobisquid.mobicash.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.dbstuff.MessageDb;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;



public class NotificationUtils {

    private static String TAG = NotificationUtils.class.getSimpleName();

    private Context mContext;

    public NotificationUtils() {
    }

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void showNotificationMessage(final String title, final MessageDb newmessage,
                                        final String messagetype, Intent intent, String imageUrl) {
        // Check for empty push message
        if (TextUtils.isEmpty(newmessage.getMessage()))
            return;


        // notification icon
        final int icon = R.mipmap.ic_launcher;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext);

      //  final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
      //          + "://" + mContext.getPackageName() + "/raw/notification");

        if (!TextUtils.isEmpty(imageUrl)) {

            if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {

                Bitmap bitmap = getBitmapFromURL(imageUrl);

                if (bitmap != null) {
                    showBigNotification(mContext,bitmap, mBuilder, icon, title, newmessage, messagetype, resultPendingIntent);
                } else {
                    showSmallNotification(mContext,mBuilder, icon, title, newmessage, messagetype, resultPendingIntent);
                }
            }
        } else {
            showSmallNotification(mContext,mBuilder, icon, title, newmessage, messagetype, resultPendingIntent);
            // playNotificationSound();
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showBigNotification(Context context, Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, MessageDb newmessage, String messagetype, PendingIntent resultPendingIntent) {
        Vars vars = new Vars(context);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        if (Globals.appendNotificationMessages) {
            // store the notification in shared pref first
            AppController.getInstance().getPrefManager(context).addNotification(newmessage.getThreadid(),newmessage.getMessage());

            // get the notifications from shared preferences
            String oldNotification = AppController.getInstance().getPrefManager(context).getNotifications(newmessage.getThreadid());

            List<String> messages = Arrays.asList(oldNotification.split("\\|"));
            vars.log("============message size============="+messages.size());
            if(messages.size()>1){
                title ="MobiSquid";
                for (int i = messages.size() - 1; i >= 0; i--) {

                    vars.log("============message size============="+messages.size());
                    inboxStyle.addLine(newmessage.getUserName()+"@"+" "+messages.get(i));

                }
            }else{
                for (int i = messages.size() - 1; i >= 0; i--) {
                    inboxStyle.addLine(messages.get(i));
                }
            }
        } else {
            inboxStyle.addLine(newmessage.getMessage());
        }

        Uri path = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.receivemessage);
        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setCategory(Notification.CATEGORY_PROMO)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVisibility(View.VISIBLE)
                .setVibrate(new long[]{500,500,500,500,500})
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(path)
                .setStyle(inboxStyle)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(Utils.getCircleBitmap(bitmap))
                .setContentText(messagetype)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Globals.NOTIFICATION_ID, notification);
    }
    public void showNotificationMessage(final String title, final String message, final String timeStamp, Intent intent) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;


        // notification icon
        final int icon = R.mipmap.ic_launcher;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext);
        showBigNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent);

    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showSmallNotification(Context context, NotificationCompat.Builder mBuilder, int icon, String title, MessageDb newmessage, String timeStamp, PendingIntent resultPendingIntent) {
        Vars vars = new Vars(context);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        if (Globals.appendNotificationMessages) {
            // store the notification in shared pref first
            AppController.getInstance().getPrefManager(context).addNotification(newmessage.getThreadid(),newmessage.getMessage());

            // get the notifications from shared preferences
            String oldNotification = AppController.getInstance().getPrefManager(context).getNotifications(newmessage.getThreadid());

            List<String> messages = Arrays.asList(oldNotification.split("\\|"));
            vars.log("============message size============="+messages.size());
            if(messages.size()>1){
                title ="MobiSquid";
                for (int i = messages.size() - 1; i >= 0; i--) {
                    inboxStyle.addLine(newmessage.getUserName()+" "+messages.get(i));
                }

            }else {
                for (int i = messages.size() - 1; i >= 0; i--) {
                    inboxStyle.addLine(messages.get(i));
                }
            }

        } else {
            inboxStyle.addLine(newmessage.getMessage());
        }
        Uri path = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.receivemessage);

        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setCategory(Notification.CATEGORY_PROMO)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVisibility(View.VISIBLE)
                .setVibrate(new long[]{500,500,500,500,500})
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(path)
                .setStyle(inboxStyle)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(newmessage.getMessage())
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Globals.NOTIFICATION_ID, notification);
    }

    private void showBigNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent) {
       System.out.println("=====notification with bitmap=========");
        //NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        //bigPictureStyle.setBigContentTitle(title);
       // bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setCategory(Notification.CATEGORY_PROMO)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{500,500,500,500,500})
                //.setStyle(bigPictureStyle)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Globals.NOTIFICATION_ID_BIG_IMAGE, notification);
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     * */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound
    public void playNotificationSound() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications() {
        Log.e(TAG,"======Clearing notification=====");
        NotificationManager notificationManager = (NotificationManager) AppController.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }


}
