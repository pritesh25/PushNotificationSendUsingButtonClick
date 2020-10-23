package com.example.pritesh.mypushnotification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey("values")) {
                Toast.makeText(context, "" + bundle.getString("values"), Toast.LENGTH_LONG).show()
                Log.d(TAG, "" + bundle.getString("values"))
            } else {
                Log.d(TAG, "value key is null")
            }
            if (bundle.containsKey("snooz")) {
                Toast.makeText(context, "" + bundle.getString("snooz"), Toast.LENGTH_LONG).show()
                Log.d(TAG, "" + bundle.getString("snooz"))
            } else {
                Log.d(TAG, "snooz key is null")
            }
        } else {
            Log.d(TAG, "bundle is null")
        }
    }

    companion object {
        private val TAG = MyBroadcastReceiver::class.java.simpleName
    }
}