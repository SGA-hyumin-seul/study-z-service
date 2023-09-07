package com.example.fstestapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import kotlinx.coroutines.delay


class MyForegroundService : LifecycleService() {

    companion object {
        const val CHANNEL_ID = "1111"

        fun createIntent(context: Context): Intent =
            Intent(context, MyForegroundService::class.java)
    }

    private val receiver = TestReceiver()
    private var isRegistered = false

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        Log.d("soltest", "onBind")

        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d("soltest", "onStartCommand")

        val chan = NotificationChannel(
            "MyChannelId",
            "My Foreground Service",
            NotificationManager.IMPORTANCE_LOW
        )

        //2．通知チャネル登録
        val channelId = CHANNEL_ID
        val channelName = "TestService Channel"
        val channel = NotificationChannel(
            channelId, channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)


        //4．通知の作成（ここでPendingIntentを通知領域に渡す）
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Foreground test")
            .setContentText("test")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        startForeground(2222, notification)

//        lifecycleScope.launch(Dispatchers.IO) {
//            while (true) {
//                printLog()
//            }
//        }

        registerReceiver(receiver, IntentFilter("ACTION_TEST"))
        isRegistered = true

        return START_STICKY
    }

    override fun onDestroy() {
        Log.v("soltest", "onDestroy")

        if (isRegistered) {
            try {
                unregisterReceiver(receiver)
            } catch (e: IllegalStateException) {
                Log.e("TAG", e.toString())
            } finally {
                isRegistered = false
            }
        }
        super.onDestroy()
    }

    private suspend fun printLog() {
        Log.v("soltest", "Service is running... ${Thread.currentThread().name}")
        delay(2000L)
    }
}