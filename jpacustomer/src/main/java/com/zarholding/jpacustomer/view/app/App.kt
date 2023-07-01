package com.zarholding.jpacustomer.view.app

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import com.hoomanholding.applibrary.tools.CompanionValues
import dagger.hilt.android.HiltAndroidApp


/**
 * create by m-latifi on 5/2/2023
 */

@HiltAndroidApp
class App: Application() {


    //---------------------------------------------------------------------------------------------- onCreate
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
//        initXLog()
    }
    //---------------------------------------------------------------------------------------------- onCreate



    //---------------------------------------------------------------------------------------------- createNotificationChannel
    private fun createNotificationChannel() {
        val vibrate: LongArray = longArrayOf(1000L, 1000L, 1000L, 1000L, 1000L)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            CompanionValues.APP_ID,
            CompanionValues.CHANNEL_Name,
            importance
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        channel.enableLights(true)
        channel.lightColor = Color.BLUE
        channel.enableVibration(true)
        channel.vibrationPattern = vibrate
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
    //---------------------------------------------------------------------------------------------- createNotificationChannel




/*    //---------------------------------------------------------------------------------------------- initXLog
    private fun initXLog() {
        val downloadFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val destinationDir = File(downloadFolder.absolutePath, CompanionValues.APP_ID)
        if (!destinationDir.exists())
            destinationDir.mkdir()

        val destinationFile = File(destinationDir.absolutePath, "log")
        val config: LogConfiguration = LogConfiguration.Builder()
            .logLevel(LogLevel.ALL)
            .tag("X-LOG")
            .enableStackTrace(1)
            .build()

        val androidPrinter: Printer = AndroidPrinter(true)

//        val consolePrinter: Printer = ConsolePrinter()

        val filePrinter: Printer =
            FilePrinter.Builder(destinationFile.absolutePath)
                .fileNameGenerator(DateFileNameGenerator())
                .backupStrategy(NeverBackupStrategy())
                .build()

        XLog.init(
            config,
            androidPrinter,
            filePrinter
        )
    }
    //---------------------------------------------------------------------------------------------- initXLog
    */
}