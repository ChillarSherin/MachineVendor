package com.chillarcards.machinevendor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by test on 05-08-2016.
 */
public class BootReciever extends BroadcastReceiver
{

    // THIS METHOD IS USED FOR THE APP WILL OPEN WHEN THE DEVICE IS ON
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Intent myIntent = new Intent(context, SplashScreen.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);
    }

}
