package com.example.jquiz.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.jquiz.MainActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit



const val CHANNEL_ID = "jquiz_reminder"
const val WORK_NAME = "reminder_work"


enum class Frequency {
    EVERY_MINUTE,
    HOURLY,
    DAILY,
    WEEKLY,
    NEVER
}


class NotificationsManager (private val notifications: DataStore<Preferences>) {

    private val frequencyKey = stringPreferencesKey("frequency")
    private val permissionsGrantedKey = booleanPreferencesKey("permissionsGranted")

    fun getFrequencyFlow (): Flow<Frequency> {
        return notifications.data.map { Frequency.valueOf(it[frequencyKey] ?: Frequency.NEVER.name) }
    }

    fun getPermissionsFlow (): Flow<Boolean> {
        return notifications.data.map { it[permissionsGrantedKey] ?: false }
    }

    suspend fun setFrequency (frequency: Frequency) {
        notifications.edit { it[frequencyKey] = frequency.name }
    }

    suspend fun setPermissions (granted: Boolean) {
        notifications.edit { it[permissionsGrantedKey] = granted }
    }

    fun schedule (frequency: Frequency, context: Context) {
        val workManager = WorkManager.getInstance(context)

        if (frequency == Frequency.NEVER) {
            workManager.cancelUniqueWork(WORK_NAME)
        } else {
            if (frequency == Frequency.EVERY_MINUTE) {
                val request = OneTimeWorkRequestBuilder<ReminderWorker>()
                    .setInitialDelay(1, TimeUnit.MINUTES)
                    .build()

                workManager.enqueueUniqueWork(
                    WORK_NAME,
                    ExistingWorkPolicy.REPLACE,
                    request
                )
            } else {
                val repeatInterval = when (frequency) {
                    Frequency.HOURLY -> 1L to TimeUnit.HOURS
                    Frequency.DAILY  -> 1L to TimeUnit.DAYS
                    Frequency.WEEKLY -> 7L to TimeUnit.DAYS
                    Frequency.EVERY_MINUTE -> TODO()
                    Frequency.NEVER -> TODO()
                }

                val request = PeriodicWorkRequestBuilder<ReminderWorker>(
                    repeatInterval.first,
                    repeatInterval.second
                ).setInitialDelay(repeatInterval.first, repeatInterval.second).build()

                workManager.enqueueUniquePeriodicWork(
                    WORK_NAME,
                    ExistingPeriodicWorkPolicy.UPDATE,
                    request
                )
            }
        }

    }

}


class ReminderWorker (val context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        createNotification(context)
        return Result.success()
    }

}


fun createNotification (context: Context) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_popup_reminder)
        .setContentTitle("hey !")
        .setContentText("mind to jquiz a lil bit ?")
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()
    notificationManager.notify(1, notification)
}


fun createChannel(context: Context) {
    val channel = NotificationChannel(CHANNEL_ID, "JQuiz Reminder", NotificationManager.IMPORTANCE_DEFAULT)
    channel.description = "Reminding user to jquiz"
    val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}
