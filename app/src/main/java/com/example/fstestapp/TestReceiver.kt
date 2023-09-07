package com.example.fstestapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class TestReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val intentValue = intent.extras?.getString("key") ?: "null"
        Log.i("soltest", intentValue)

        if (intentValue == "kill-service") {
            context.stopService(MyForegroundService.createIntent(context.applicationContext))
        }
    }

}