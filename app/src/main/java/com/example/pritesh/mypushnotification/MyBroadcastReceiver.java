package com.example.pritesh.mypushnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver
{

    private static final String TAG = MyBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        if(bundle!=null)
        {

            if(bundle.containsKey("values"))
            {
                Toast.makeText(context,""+bundle.getString("values"),Toast.LENGTH_LONG).show();
                Log.d(TAG,""+bundle.getString("values"));
            }
            else
            {
                Log.d(TAG,"value key is null");
            }

            if(bundle.containsKey("snooz"))
            {
                Toast.makeText(context,""+bundle.getString("snooz"),Toast.LENGTH_LONG).show();
                Log.d(TAG,""+bundle.getString("snooz"));
            }
            else
            {
                Log.d(TAG,"snooz key is null");
            }

        }
        else
        {
            Log.d(TAG,"bundle is null");
        }
    }
}