package com.zarholding.jpacustomer.view.fragment.report.pdf

import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.enums.EnumReportType
import com.hoomanholding.applibrary.model.repository.DownloadFileRepository
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Created by m-latifi on 6/18/2023.
 */

@HiltViewModel
class ReportPDFViewModel @Inject constructor(
    private val downloadFileRepository: DownloadFileRepository
): JpaViewModel() {

    val downloadProgress: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val downloadSuccessLiveData: MutableLiveData<File> by lazy { MutableLiveData<File>() }


    //---------------------------------------------------------------------------------------------- downloadCustomerBalancePDF
    fun downloadCustomerBalancePDF() {
        viewModelScope.launch(IO + exceptionHandler()) {
            val file = initFile(EnumReportType.Balance)
            delay(1000)
            val response = downloadFileRepository.downloadCustomerBalancePDF()
            response.body()?.saveFile(file)?.collect { downloadState ->
                when (downloadState) {
                    is DownloadState.Downloading -> {
                        downloadProgress.postValue(downloadState.progress)
                    }
                    is DownloadState.Failed -> {
                        setMessage(downloadState.error?.message?:"Failed Download!")
                    }
                    DownloadState.Finished -> {
                        downloadSuccessLiveData.postValue(file)
                    }
                }
            } ?: run {
                setMessage(response.errorBody()?.string() ?: "")
            }
        }
    }
    //---------------------------------------------------------------------------------------------- downloadCustomerBalancePDF



    //---------------------------------------------------------------------------------------------- initFile
    private fun initFile(type: EnumReportType): File{
        val time = SimpleDateFormat("yyyy_MM_dd__HH_mm_ss", Locale.getDefault()).format(Date())
        val downloadFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val destinationDir = File(downloadFolder.absolutePath, CompanionValues.APP_ID)
        if (!destinationDir.exists())
            destinationDir.mkdir()
        val destinationApk = File(destinationDir.absolutePath, "pdf")
        if (!destinationApk.exists())
            destinationApk.mkdir()
        return File(destinationApk.absolutePath, "${type.name}_${time}.pdf")
    }
    //---------------------------------------------------------------------------------------------- initFile



    //---------------------------------------------------------------------------------------------- DownloadState
    private sealed class DownloadState {
        data class Downloading(val progress: Int) : DownloadState()
        object Finished : DownloadState()
        data class Failed(val error: Throwable? = null) : DownloadState()
    }
    //---------------------------------------------------------------------------------------------- DownloadState



    //---------------------------------------------------------------------------------------------- saveFile
    private fun ResponseBody.saveFile(destinationFile: File): Flow<DownloadState> {
        return flow {
            emit(DownloadState.Downloading(0))
            try {
                byteStream().use { inputStream ->
                    destinationFile.outputStream().use { outputStream ->
                        val totalBytes = contentLength()
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var progressBytes = 0L

                        var bytes = inputStream.read(buffer)
                        while (bytes >= 0) {
                            outputStream.write(buffer, 0, bytes)
                            progressBytes += bytes
                            bytes = inputStream.read(buffer)
                            emit(DownloadState.Downloading(((progressBytes * 100) / totalBytes).toInt()))
                        }
                    }
                }
                emit(DownloadState.Finished)
            } catch (e: Exception) {
                emit(DownloadState.Failed(e))
            }
        }.flowOn(IO).distinctUntilChanged()
    }
    //---------------------------------------------------------------------------------------------- saveFile

}