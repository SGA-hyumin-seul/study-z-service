package com.example.fstestapp

import android.app.ActivityManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import androidx.work.Configuration

class CasarealJobService : JobService() {

    init {
        val builder: Configuration.Builder = Configuration.Builder()
        builder.setJobSchedulerJobIdRange(0, 1000)
        builder.build()
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        jobFinished(params, false)
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        val activityManager: ActivityManager = getSystemService(ActivityManager::class.java)

        Log.i("soltest", "onStartJob ${Thread.currentThread().name} | params $params")
        Thread {
            while (true) {
                activityManager.runningAppProcesses.forEach {
                    Log.v("soltest", "[jobScheduler] running process: " + it.processName)
                }
                Thread.sleep(1000)
            }

        }.start()

        //jobFinished(params, true)
        return true
    }

}