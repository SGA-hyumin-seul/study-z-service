package com.example.fstestapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.time.Duration
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    companion object {
        private const val SCHEDULER_JOB_ID = 33
    }

    private lateinit var scheduler: JobScheduler
    private lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val pending = createPendingIntent()

        findViewById<Button>(R.id.btn_start_alarm_manager).setOnClickListener {
            startAlarmManager(pending)
        }

        findViewById<Button>(R.id.btn_stop_alarm_manager).setOnClickListener {
            alarmManager.cancel(pending)
        }

        findViewById<Button>(R.id.btn_start_work_manager).setOnClickListener {
            startWorkManager()
        }

        findViewById<Button>(R.id.btn_stop_work_manager).setOnClickListener {
            WorkManager.getInstance(applicationContext).cancelAllWork()
        }

        findViewById<Button>(R.id.btn_start_job_scheduler).setOnClickListener {
            startJobScheduler(SCHEDULER_JOB_ID)
        }

        findViewById<Button>(R.id.btn_stop_job_scheduler).setOnClickListener {
            scheduler.cancel(SCHEDULER_JOB_ID)
        }

        findViewById<Button>(R.id.btn_kill_process).setOnClickListener {
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(
            applicationContext,
            AlarmReceiver::class.java
        )

        return PendingIntent.getBroadcast(
            applicationContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun startAlarmManager(pending: PendingIntent) {
        Log.v("soltest", "startAlarmManager")

//        alarmManager.setRepeating(
//            AlarmManager.ELAPSED_REALTIME_WAKEUP,
//            SystemClock.elapsedRealtime() + 1 * 1000,
//            60 * 1000,
//            pending
//        )

        val timeInMillis = Calendar.getInstance().apply {
            add(Calendar.SECOND, 30)
        }.timeInMillis

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pending
        )
    }

    private fun startWorkManager() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(false)
            .setRequiresStorageNotLow(false)
            .build()

        val request: WorkRequest =
            PeriodicWorkRequestBuilder<ReviveWorker>(15, TimeUnit.MINUTES)
                .setInitialDelay(0, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()

        WorkManager.getInstance(applicationContext).enqueue(request)
    }

    private fun startJobScheduler(jobId: Int) {
        val componentName = ComponentName(
            this,
            CasarealJobService::class.java
        )

        val jobInfo = JobInfo.Builder(jobId, componentName)
            .apply {
                setBackoffCriteria(10000, JobInfo.BACKOFF_POLICY_LINEAR)
                setPersisted(true)
                setPeriodic(Duration.ofMinutes(15).toMillis())
                setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                setRequiresCharging(false)
            }.build()

        scheduler.schedule(jobInfo)
    }
}