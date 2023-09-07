package com.example.fstestapp

import android.app.ActivityManager
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ReviveWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private val activityManager: ActivityManager =
        context.getSystemService(ActivityManager::class.java)

    override fun doWork(): Result {
        Log.v("soltest", "Worker doWork")

        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                activityManager.runningAppProcesses.forEach {
                    Log.i("soltest", "[workManager] running process: " + it.processName)
                }
                delay(1000)
            }
        }

        return Result.success()
    }
}