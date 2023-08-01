package com.hoomanholding.applibrary.viewmodel

import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.R
import com.hoomanholding.applibrary.di.Providers
import com.hoomanholding.applibrary.di.ResourcesProvider
import com.hoomanholding.applibrary.model.data.response.GeneralResponse
import com.hoomanholding.applibrary.model.repository.DownloadFileRepository
import com.hoomanholding.applibrary.model.repository.LogRepository
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.tools.SingleLiveEvent
import com.zar.core.enums.EnumApiError
import com.zar.core.models.ErrorApiModel
import com.zar.core.tools.api.checkResponseError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * Created by m-latifi on 8/1/2023.
 */

@HiltViewModel
open class JpaDownloadViewModel @Inject constructor(): ViewModel(){

    @Inject
    lateinit var downloadFileRepository: DownloadFileRepository

    val downloadProgress: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val downloadSuccessLiveData: MutableLiveData<File> by lazy { MutableLiveData<File>() }

    @Inject
    lateinit var resourcesProvider: ResourcesProvider

    @Inject
    lateinit var logRepository: LogRepository

    val errorLiveDate = SingleLiveEvent<ErrorApiModel>()


    //---------------------------------------------------------------------------------------------- callApi
    suspend fun <T : Any> callApi(
        request: Response<GeneralResponse<T?>>?,
        showMessageAfterSuccessResponse: Boolean = false,
        onReceiveData: (T) -> Unit
    ) {
        if (request?.isSuccessful == true) {
            val body = request.body()
            body?.let { generalResponse ->
                if (generalResponse.hasError || generalResponse.data == null)
                    setMessage(generalResponse.message)
                else {
                    if (showMessageAfterSuccessResponse)
                        setMessage(generalResponse.message)
                    onReceiveData(generalResponse.data)
                }
            } ?: run {
                setMessage(
                    resourcesProvider.getString(
                        R.string.dataReceivedIsEmpty
                    )
                )
            }
        } else setMessage(request)
    }
    //---------------------------------------------------------------------------------------------- callApi


    //---------------------------------------------------------------------------------------------- setError
    private suspend fun setMessage(response: Response<*>?) {
        withContext(Dispatchers.Main) {
            checkResponseError(response, errorLiveDate)
        }
    }
    //---------------------------------------------------------------------------------------------- setError


    //---------------------------------------------------------------------------------------------- setMessage
    suspend fun setMessage(message: String) {
        withContext(Dispatchers.Main) {
            errorLiveDate.value = ErrorApiModel(EnumApiError.Error, message)
        }
    }
    //---------------------------------------------------------------------------------------------- setMessage


    //---------------------------------------------------------------------------------------------- exceptionHandler
    fun exceptionHandler() = CoroutineExceptionHandler { _, throwable ->
        CoroutineScope(Dispatchers.Main).launch {
            throwable.localizedMessage?.let {
//                XLog.e(it)
                val url = Providers.url
                    .replace("http://", "")
                    .replace("https://", "")
                if (it.contains(url))
                    setMessage(resourcesProvider.getString(R.string.pleaseCheckYouConnection))
                else {
                    setMessage(it)
                    sendLogToServer(it)
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------- exceptionHandler


    //---------------------------------------------------------------------------------------------- sendLogToServer
    private fun sendLogToServer(log: String) {
        viewModelScope.launch(Dispatchers.IO) {
            callApi(
                request = logRepository.requestSaveError(log),
                onReceiveData = {}
            )
        }
    }
    //---------------------------------------------------------------------------------------------- sendLogToServer


    //---------------------------------------------------------------------------------------------- initFile
    fun initFile(type: String, extension: String): File {
        val time = SimpleDateFormat("yyyy_MM_dd__HH_mm_ss", Locale.getDefault()).format(Date())
        val downloadFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val destinationDir = File(downloadFolder.absolutePath, CompanionValues.APP_ID)
        if (!destinationDir.exists())
            destinationDir.mkdir()
        val destinationApk = File(destinationDir.absolutePath, extension)
        if (!destinationApk.exists())
            destinationApk.mkdir()
        return File(destinationApk.absolutePath, "${type}_${time}.${extension}")
    }
    //---------------------------------------------------------------------------------------------- initFile


    //---------------------------------------------------------------------------------------------- DownloadState
    sealed class DownloadState {
        data class Downloading(val progress: Int) : DownloadState()
        object Finished : DownloadState()
        data class Failed(val error: Throwable? = null) : DownloadState()
    }
    //---------------------------------------------------------------------------------------------- DownloadState



    //---------------------------------------------------------------------------------------------- saveFile
    fun ResponseBody.saveFile(destinationFile: File): Flow<DownloadState> {
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
        }.flowOn(Dispatchers.IO).distinctUntilChanged()
    }
    //---------------------------------------------------------------------------------------------- saveFile


}