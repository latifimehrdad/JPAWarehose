package com.hoomanholding.jpamanager.view.app

import android.app.Application
import android.os.Environment
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.Printer
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator
import dagger.hilt.android.HiltAndroidApp
import java.io.File

/**
 * Create by Mehrdad on 1/16/2023
 */

@HiltAndroidApp
class App : Application() {

    //---------------------------------------------------------------------------------------------- init
    init {
        val downloadFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val destinationDir = File(downloadFolder.absolutePath, "Jpa")
        if (!destinationDir.exists())
            destinationDir.mkdir()

        val destinationFile = File(destinationDir.absolutePath, "JpaLog")

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
    //---------------------------------------------------------------------------------------------- init
}