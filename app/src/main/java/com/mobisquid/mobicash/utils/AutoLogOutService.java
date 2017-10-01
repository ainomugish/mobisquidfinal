package com.mobisquid.mobicash.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by mobicash on 3/1/17.
 */

public class AutoLogOutService extends Service {
    Vars vars;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //long updatePeriod =1000;
    long updatePeriod =10 * 60 * 1000;
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("=====Service oncret====");
        setAlarm(0, updatePeriod);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         vars = new Vars(this);
        vars.log("service=========onStart==");
        if (intent == null) {
            return START_STICKY;
        }

        final String intentAction = intent.getAction();
        if ("action_log_out".equals(intentAction)) {
            vars.log("=======service=====logout===chk");
            if(Utils.isAppIsInBackground(this) && vars.active!=null){
               vars.edit.remove("active");
                vars.edit.remove("qrcode");
                vars.edit.apply();
                vars.log("=======service=====loged out+++++");
                stopService(new Intent(this, AutoLogOutService.class));
            }
            //Toast.makeText(getApplicationContext(),"logout="+Utils.isAppIsInBackground(this),Toast.LENGTH_LONG).show();
        }

        return START_STICKY;
    }
    public void setAlarm(long startAt, long ulogOutPeriod) {
        System.out.println("=====setAlarm====");
        final Context context = getBaseContext();
        Intent intent = new Intent(context, AutoLogOutService.class);
        intent.setAction("action_log_out");
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(
                AlarmManager.RTC,
                startAt, updatePeriod, pendingIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("=====Service down====");
    }
}
