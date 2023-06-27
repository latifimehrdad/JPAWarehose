package com.zarholding.jpacustomer.tools

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hoomanholding.applibrary.tools.CompanionValues
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.view.activity.MainActivity
import kotlin.random.Random

/**
 * Created by m-latifi on 6/19/2023.
 */

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "meri"
    }

    private fun needsToBeScheduled() = true


    //---------------------------------------------------------------------------------------------- [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")
        if (remoteMessage.data.isNotEmpty()) {
            if (needsToBeScheduled()) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }
        }
        remoteMessage.notification?.let {
            showNotification(it.title, it.body)
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }
    }
    //---------------------------------------------------------------------------------------------- [END receive_message]


    //---------------------------------------------------------------------------------------------- [START on_new_token]
    override fun onNewToken(token: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(packageName)
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }
    //---------------------------------------------------------------------------------------------- [END on_new_token]


    //---------------------------------------------------------------------------------------------- scheduleJob
    private fun scheduleJob() {
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .build()
        WorkManager.getInstance(this)
            .beginWith(work)
            .enqueue()
    }
    //---------------------------------------------------------------------------------------------- scheduleJob


    //---------------------------------------------------------------------------------------------- scheduleJob
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }
    //---------------------------------------------------------------------------------------------- scheduleJob


    //---------------------------------------------------------------------------------------------- scheduleJob
    private fun sendRegistrationToServer(token: String?) {
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }
    //---------------------------------------------------------------------------------------------- scheduleJob


    //---------------------------------------------------------------------------------------------- showNotification
    private fun showNotification(title: String?, message: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, System.currentTimeMillis().toInt(), intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val icon = BitmapFactory.decodeResource(
            this.resources,
            R.drawable.ic_launcher
        )
        val vibrate: LongArray = longArrayOf(1000L, 1000L, 1000L, 1000L, 1000L)
        val notifyManager = NotificationManagerCompat.from(this)
        val notificationBuilder = NotificationCompat
            .Builder(this, CompanionValues.APP_ID)
        val notification = notificationBuilder
            .setSmallIcon(R.drawable.ic_launcher)
            .setLargeIcon(icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentTitle(title)
            .setContentText(message)
            .setVibrate(vibrate)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_SOUND)
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val id = Random.nextInt(1, 7126)
        notifyManager.notify(id, notification.build())
    }
    //---------------------------------------------------------------------------------------------- showNotification


    //---------------------------------------------------------------------------------------------- MyWorker
    internal class MyWorker(appContext: Context, workerParams: WorkerParameters) :
        Worker(appContext, workerParams) {
        override fun doWork(): Result {
            // TODO(developer): add long running task here.
            return Result.success()
        }
    }
    //---------------------------------------------------------------------------------------------- MyWorker

}