package com.mobisquid.mobicash.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.isseiaoki.simplecropview.CropImageView;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.MainActivity;
import com.mobisquid.mobicash.activities.SupportActivity;
import com.mobisquid.mobicash.dbstuff.MessageDb;
import com.mobisquid.mobicash.model.Transactions;
import com.mobisquid.mobicash.mqtt.ServiceDemo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;


/**
 * Created by mobicash on 12/21/15.
 */
public class Utils {
    private static final String DL_ID = "downloadId";
    private static SharedPreferences prefs;
    private static DownloadManager dm;
    static AlertDialog alertDialog_crop;
    static final String ABNUM = "0123456789";
    public static String datenow;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static Random rnd = new Random();
    static String online_status;
    static JSONArray detailsArray;
    static Date resultdatetwo, date;
    static SimpleDateFormat formatter, formatter_two, today;
    private Context context;
    private SharedPreferences sharedPref;
    static Vars vars;
    private static final String KEY_SHARED_PREF = "ANDROID_WEB_CHAT";
    private static final int KEY_MODE_PRIVATE = 0;
    private static final String KEY_SESSION_ID = "sessionId",
            FLAG_MESSAGE = "message";

    public Utils(Context context) {
        this.context = context;
        sharedPref = this.context.getSharedPreferences(KEY_SHARED_PREF,
                KEY_MODE_PRIVATE);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeUri(Uri selectedImage, Context context) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 150;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                context.getContentResolver().openInputStream(selectedImage), null, o2);
    }

    public static void cropimage(final Context context, final String imagename, final Bitmap bitmap,
                                 final ImageView imageView, final StringCallback callback) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView;
        promptsView = li.inflate(R.layout.cropping, null);

        final CropImageView cropImageView = (CropImageView) promptsView.findViewById(R.id.cropImageView);
        cropImageView.setCropMode(CropImageView.CropMode.RATIO_FREE);
        cropImageView.setImageBitmap(bitmap);

        FloatingActionButton fab_done = (FloatingActionButton) promptsView.findViewById(R.id.fab_tick);
        FloatingActionButton fab_delete = (FloatingActionButton) promptsView.findViewById(R.id.fab_delte);
        FloatingActionButton fab_rotate = (FloatingActionButton) promptsView.findViewById(R.id.fab_rotate);
        fab_rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
            }
        });
        fab_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageView.setImageBitmap(cropImageView.getCroppedBitmap());
                callback.onSuccess(saveImage(cropImageView.getCroppedBitmap(), imagename, context));

                alertDialog_crop.dismiss();
            }
        });
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog_crop.dismiss();
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        alertDialog_crop = alertDialogBuilder.create();
        alertDialog_crop.show();

    }

    public static void cropimageString(final Context context, final String imagename, final Bitmap bitmap,
                                       final ImageView imageView, final StringCallback callback) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView;
        promptsView = li.inflate(R.layout.cropping, null);

        final CropImageView cropImageView = (CropImageView) promptsView.findViewById(R.id.cropImageView);
        cropImageView.setCropMode(CropImageView.CropMode.RATIO_FREE);
        cropImageView.setImageBitmap(bitmap);

        FloatingActionButton fab_done = (FloatingActionButton) promptsView.findViewById(R.id.fab_tick);
        FloatingActionButton fab_delete = (FloatingActionButton) promptsView.findViewById(R.id.fab_delte);
        FloatingActionButton fab_rotate = (FloatingActionButton) promptsView.findViewById(R.id.fab_rotate);
        fab_rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
            }
        });
        fab_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageView.setImageBitmap(cropImageView.getCroppedBitmap());
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                cropImageView.getCroppedBitmap().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                callback.onSuccess(Base64.encodeToString(byteArray, Base64.DEFAULT));

                alertDialog_crop.dismiss();
            }
        });
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog_crop.dismiss();
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        alertDialog_crop = alertDialogBuilder.create();
        alertDialog_crop.show();

    }

    public static boolean checklogin(Context context) {
        Vars vars = new Vars(context);
        if (vars.chk == null) {
            return false;
        } else {
            return true;
        }

    }

    /*   public static void notify_user(Context context,String message){
           Vars vars = new Vars(context);
           Intent notificationIntent = new Intent(context, NewActivity.class);
           PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
           Notification notification = new NotificationCompat.Builder(context)
                   .setCategory(Notification.CATEGORY_PROMO)
                   .setContentTitle(context.getResources().getString(R.string.app_name))
                   .setContentIntent(contentIntent)
                   .setContentText(message)
                   .setSmallIcon(R.mipmap.ic_laucher_appp)
                   .setAutoCancel(true)
                   .setVisibility(View.VISIBLE)
                   .setPriority(Notification.PRIORITY_HIGH)
                   .setVibrate(new long[]{500,500,500,500,500}).build();
           NotificationManager notificationManager =
                   (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
           notificationManager.notify(2, notification);

           vars.edit.remove("firtstime_number");
           vars.edit.commit();

       }
   */
    public static void download_notfy(Context mContext, String name) {
        Vars vars = new Vars(mContext);
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        dm = (DownloadManager) mContext.getSystemService(mContext.DOWNLOAD_SERVICE);
        if (!prefs.contains(DL_ID)) {
            Uri resource = Uri.parse(vars.server_mb + "MobiSquid.apk");
            DownloadManager.Request request = new DownloadManager.Request(resource);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, "MobiSquid" + name + vars.downloadReference + ".apk");
            request.setTitle("Updating MobiSquid");
            long id = dm.enqueue(request);
            prefs.edit().putLong(DL_ID, id).commit();
        } else {
            queryDownloadStatus(mContext);
        }
        mContext.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }


    public static void queryDownloadStatus(Context mContext) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(prefs.getLong(DL_ID, 0));
        Cursor c = dm.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            Log.d("DM Sample", "Status Check: " + status);
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                case DownloadManager.STATUS_PENDING:
                case DownloadManager.STATUS_RUNNING:
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    try {
                        // ParcelFileDescriptor file = dm.openDownloadedFile(prefs.getLong(DL_ID, 0));
                        //   FileInputStream fis = new ParcelFileDescriptor.AutoCloseInputStream(file);
                        // imageView.setImageBitmap(BitmapFactory.decodeStream(fis));
                        Intent intent = new Intent(Intent.ACTION_VIEW);

                        intent.setDataAndType(dm.getUriForDownloadedFile(prefs.getLong(DL_ID, 0)),
                                "application/vnd.android.package-archive");

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);

                        mContext.unregisterReceiver(receiver);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case DownloadManager.STATUS_FAILED:
                    dm.remove(prefs.getLong(DL_ID, 0));
                    prefs.edit().apply();
                    break;
            }
        }
    }

    public static BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            queryDownloadStatus(context);
        }
    };


    /**
     * Uses static final constants to detect if the device's platform version is Gingerbread or
     * later.
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * Uses static final constants to detect if the device's platform version is Honeycomb or
     * later.
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * Uses static final constants to detect if the device's platform version is Honeycomb MR1 or
     * later.
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * Uses static final constants to detect if the device's platform version is ICS or
     * later.
     */
    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }


    public void storeSessionId(String sessionId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY_SESSION_ID, sessionId);
        editor.commit();
    }

    public String getSessionId() {
        return sharedPref.getString(KEY_SESSION_ID, null);
    }

    public String getSendMessageJSON(String message) {
        String json = null;

        try {
            JSONObject jObj = new JSONObject();
            jObj.put("flag", FLAG_MESSAGE);
            jObj.put("sessionId", getSessionId());
            jObj.put("message", message);

            json = jObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    public static String radomchr() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid.substring(4);
    }

    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public static void showdialog(final Activity activity, String message, String title) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(activity);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.finish();
                System.exit(0);

            }
        });
        downloadDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        downloadDialog.show();
    }

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static void getDate(Context context, final StringCallback callback) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        datenow = year + "-" + month + "-" + dayOfMonth;
                        callback.onSuccess(datenow);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    public interface StringCallback {
        void onSuccess(String result);
    }

    public static String saveImage(Bitmap thePic, String fileName, Context context) {
        OutputStream fOut = null;

        File m = new File(Environment.getExternalStoragePublicDirectory("profile"), "mobisquid");
        //	File m = new File(Environment.getExternalStorageDirectory(), "/SugaWorld/media");
        if (!m.exists()) {
            m.mkdirs();
        }


        if (!m.exists()) {
            m.mkdirs();
        }
        String strDirectory = m.toString();
        File f = new File(m, fileName);
        try {
            fOut = new FileOutputStream(f);

            /** Compress image **/
            thePic.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();

            /** Update image to gallery **/
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    f.getAbsolutePath(), f.getName(), f.getName());

            System.out.println("ABSOLUTE PATH:==saved=====" + f.getAbsolutePath());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return f.getAbsolutePath();
    }

    public static String FincialServer(String code) {
        if (code.equalsIgnoreCase("27")) {
            return Globals.SA_SERVER;
        } else if (code.equalsIgnoreCase("91")) {
            return Globals.INDIA_SERVER;
        } else if (code.equalsIgnoreCase("265")) {
            return Globals.MALAWI_SERVER;
        } else {
            return Globals.RW_SERVER;
        }
    }

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

    public static String randomNumber(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(ABNUM.charAt(rnd.nextInt(ABNUM.length())));
        return sb.toString();
    }

    public static String sendMessage(Context context, Transactions message, int msgid) {
        message.setExtra("second");
        message.setTransid(msgid);
        Vars vars = new Vars(context);
        vars.log("===============Utils SEND MSG=================");

        // CONVERT MESSAGE TO JSGON
        String jsonMessage = new Gson().toJson(message);

        vars.log("MESSAGE MqUtil======ID IS " + msgid);

        System.out.println("recep:" + message.getReceiverMobile());
        vars.log("MESSAGE CONVERTED TO JSON:" + jsonMessage);

        final Intent intentS = new Intent();
        intentS.setClass(context, ServiceDemo.class);
        intentS.putExtra("command", "sendMessage");
        intentS.putExtra("topic", message.getReceiverMobile());
        intentS.putExtra("userName", message.getSenderMobile());

        vars.log("AM IN MQ sender IS " + message.getSenderMobile());
        vars.log("AM IN MQ RECEP IS " + message.getReceiverMobile());
        vars.log("AM IN MQ TOPIC IS " + message.getReceiverMobile());
        intentS.putExtra("message", jsonMessage);

        intentS.putExtra("messageId", msgid);
        System.out.println("Utils.sendM starting...ServiceDemo)");
        context.startService(intentS);
        return "ok";

    }
    public static String sendMessage(Context context, MessageDb message, int msgid) {
        Vars vars = new Vars(context);
        vars.log("===============Utils SEND MSG================="+message.getMessage());

        // CONVERT MESSAGE TO JSGON
        String jsonMessage = new Gson().toJson(message);
        vars.log("MESSAGE CONVERTED TO JSON:" + jsonMessage);

        final Intent intentS = new Intent();
        intentS.setClass(context, ServiceDemo.class);
        intentS.putExtra("command", "sendMessage");
        intentS.putExtra("topic", message.getMessage());
       // intentS.putExtra("userName", vars.chk);

        vars.log("AM IN MQ RECEP IS " + message.getMessage());
        intentS.putExtra("message", jsonMessage);

        intentS.putExtra("messageId", msgid);
        context.startService(intentS);
        return "ok";

    }

    public static void hidekBoard(View view, final Context context) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });

    }

    public static void home(Activity context) {
        Intent home = new Intent(context, MainActivity.class);
        home.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(home);
        context.finish();
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
    public static String getDeviceImei(Context cxt) {

        TelephonyManager telephonyManager = (TelephonyManager) cxt.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();

    }
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }
    public static void PlaySound(Context context) {
        MediaPlayer mp = new MediaPlayer();
        mp.reset();

        try {
            Uri path = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.receivemessage);
            mp.setDataSource(context, path);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void Vibarate(Context context) {
        MediaPlayer mp = new MediaPlayer();
        mp.reset();

        try {
            Uri path = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.receivemessage);
            mp.setDataSource(context, path);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(2000);
    }
    public static String getDateformart(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    // GET MESSAGE DATE
    public static String getDate(long milliSeconds) {
        if(getDateformart(milliSeconds,"yyyy-MM-dd").equalsIgnoreCase(getDateformart(System.currentTimeMillis(),"yyyy-MM-dd"))){
            date = new Date(milliSeconds);
            Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
            calendar.setTime(date);
            return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        }else{
            return getDateformart(milliSeconds,"dd-MM-yyyy HH:mm");
        }

    }
    public static void isFinance(Context cm,boolean istrue){
        vars = new Vars(cm);
        vars.edit.putBoolean("isfinanance",istrue);
        vars.edit.apply();

    }
    public static void changeFragment(Context context, Fragment firstfragment,
                                      FragmentTransaction ft, Bundle extras,
                                      int container,boolean replace,boolean addbackstck){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slideTransition = new Slide(Gravity.LEFT);
            slideTransition.setDuration(context.getResources().getInteger(R.integer.anim_duration_long));
            if(extras!=null){
                firstfragment.setArguments(extras);
            }

            firstfragment.setReenterTransition(slideTransition);
            firstfragment.setExitTransition(slideTransition);
            firstfragment.setSharedElementEnterTransition(new ChangeBounds());
            if(replace) {
                if(addbackstck){
                    ft.replace(container, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                }else {
                    ft.replace(container, firstfragment, "MAIN")
                            .commit();
                }
            }else{
                ft.add(container, firstfragment, "MAIN")
                        .commit();
            }
        } else {
            System.out.println("===========t============");
            if(extras!=null){
                firstfragment.setArguments(extras);
            }
           // FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if(replace) {
                if(addbackstck){
                    ft.replace(container, firstfragment, "MAIN")
                            .addToBackStack(null)
                            .commit();
                }else {
                    ft.replace(container, firstfragment, "MAIN")
                            .commit();
                }
            }else{
                ft.add(container, firstfragment, "MAIN")
                        .commit();
            }
        }
    }
    public static void Support(Activity context,Bundle extras,View view){
        Intent notice = new Intent(context, SupportActivity.class);
        if(extras!=null){
          notice.putExtras(extras);
        }
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, view, "help");
        ActivityCompat.startActivity(context, notice, options.toBundle());
    }

}
