package com.example.fstestapp

import android.app.Application
import android.util.Log


class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("soltest", "onCreate")
        startForegroundService(MyForegroundService.createIntent(this))
    }
}