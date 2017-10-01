package com.mobisquid.mobicash.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mobisquid.mobicash.mqtt.GcmIntentService;
import com.mobisquid.mobicash.mqtt.ServiceDemo;

/**
 * Created by mobicash on 3/1/17.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Vars vars = new Vars(context);

        if(vars.mobile!=null && vars.active!=null){

            Intent startServiceIntent = new Intent(context, AutoLogOutService.class);
            context.startService(startServiceIntent);


            vars.log("==============Serices started========="+vars.mobile);

        }else if(vars.social!=null){
            Intent startService = new Intent(context, ServiceDemo.class);
            startService.putExtra("userName",vars.mobile);
            context.startService(startService);

            Intent intents = new Intent(context, GcmIntentService.class);
            context.startService(intents);
        }

    }
}
