package com.example.fstestapp

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootCompletedReceiver : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(ctx: Context, p1: Intent) {
        Log.d("soltest", "boot completed")
        ctx.startForegroundService(
            MyForegroundService.createIntent(ctx)
        )
    }
}