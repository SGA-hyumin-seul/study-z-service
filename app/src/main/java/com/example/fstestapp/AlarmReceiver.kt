package com.example.fstestapp

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, p1: Intent) {
        Log.d("soltest", "AlarmReceiver onReceive")
        Toast.makeText(context, "Received ", Toast.LENGTH_LONG).show()

        val activityManager: ActivityManager =
            context.getSystemService(ActivityManager::class.java)

        CoroutineScope(Dispatchers.Default).launch {
            repeat(30) {
                activityManager.runningAppProcesses.forEach {
                    Log.v("soltest", "[alarmManager] running process: " + it.processName)
                }
                delay(2000)
            }
        }
    }
}