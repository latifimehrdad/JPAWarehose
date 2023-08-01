package com.hoomanholding.applibrary.view.fragment.download

import android.os.Environment
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.di.ApplicationInfoProvider
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.viewmodel.JpaDownloadViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AppDownloadViewModel @Inject constructor(
    applicationInfoProvider: ApplicationInfoProvider
) : JpaDownloadViewModel() {

    private var destinationFile: File

    //---------------------------------------------------------------------------------------------- init
    init {
        val downloadFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val destinationDir = File(downloadFolder.absolutePath, CompanionValues.APP_ID)
        if (!destinationDir.exists())
            destinationDir.mkdir()
        val destinationApk = File(destinationDir.absolutePath, "apk")
        if (!destinationApk.exists())
            destinationApk.mkdir()
        destinationFile = File(destinationApk.absolutePath,
            "${applicationInfoProvider.getAppLabel()}_${getNewFileName()}.apk")

    }
    //---------------------------------------------------------------------------------------------- init


    //______________________________________________________________________________________________ getNewFileName
    private fun getNewFileName(): String? {
        return SimpleDateFormat("yyyy_MM_dd__HH_mm_ss", Locale.getDefault()).format(Date())
    }
    //______________________________________________________________________________________________ getNewFileName



    //---------------------------------------------------------------------------------------------- downloadLastVersion
    fun downloadLastVersion(fileName: String, appName: String) {
        viewModelScope.launch(IO + exceptionHandler()) {
            delay(1000)
            if (destinationFile.exists())
                destinationFile.delete()
            val response = downloadFileRepository.downloadApkFile(
                appName,
                fileName
            )
            response.body()?.saveFile(destinationFile)?.collect { downloadState ->
                when (downloadState) {
                    is DownloadState.Downloading -> {
                        downloadProgress.postValue(downloadState.progress)
                    }
                    is DownloadState.Failed -> {
                        setMessage(downloadState.error?.message?:"Failed Download!")
                    }
                    DownloadState.Finished -> {
                        downloadSuccessLiveData.postValue(destinationFile)
                    }
                }
            } ?: run {
                setMessage(response.errorBody()?.string() ?: "")
            }
        }
    }
    //---------------------------------------------------------------------------------------------- downloadLastVersion

}