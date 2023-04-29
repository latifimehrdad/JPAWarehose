package com.hoomanholding.applibrary.view.fragment.download

import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.repository.DownloadFileRepository
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val downloadFileRepository: DownloadFileRepository
) : JpaViewModel() {

    private var destinationFile: File

    val downloadProgress: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val downloadSuccessLiveData: MutableLiveData<File> by lazy { MutableLiveData<File>() }

    //---------------------------------------------------------------------------------------------- init
    init {
        val downloadFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        destinationFile = File(downloadFolder.absolutePath, "JpaManager.apk")

    }
    //---------------------------------------------------------------------------------------------- init


    //---------------------------------------------------------------------------------------------- downloadLastVersion
    fun downloadLastVersion(fileName: String, appName: String) {
        viewModelScope.launch(IO + exceptionHandler()) {
            delay(2000)
            if (destinationFile.exists())
                destinationFile.delete()
            val response = downloadFileRepository.downloadApkFile(
                appName,
                fileName
            )
            response.body()?.saveFile()?.collect { downloadState ->
                when (downloadState) {
                    is DownloadState.Downloading -> {
                        downloadProgress.postValue(downloadState.progress)
                    }
                    is DownloadState.Failed -> {

                    }
                    DownloadState.Finished -> {
                        downloadSuccessLiveData.postValue(destinationFile)
                    }
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------- downloadLastVersion


/*
    //---------------------------------------------------------------------------------------------- saveFile
    private fun saveFile(body: ResponseBody?, fileName: String) {
        if (body == null)
            return
        val downloadFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val destinationFile = File(downloadFolder.absolutePath, fileName)
        var input: InputStream? = null
        try {
            input = body.byteStream()
            val fos = FileOutputStream(destinationFile)
            fos.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                    Log.e("meri", "read $read")
                }
                output.flush()
            }
            downloadSuccessLiveData.postValue(destinationFile.absolutePath)
        } catch (e: Exception) {
            Log.e("saveFile", e.toString())
        } finally {
            input?.close()
        }
    }
    //---------------------------------------------------------------------------------------------- saveFile
*/


    //---------------------------------------------------------------------------------------------- DownloadState
    private sealed class DownloadState {
        data class Downloading(val progress: Int) : DownloadState()
        object Finished : DownloadState()
        data class Failed(val error: Throwable? = null) : DownloadState()
    }
    //---------------------------------------------------------------------------------------------- DownloadState


    //---------------------------------------------------------------------------------------------- saveFile
    private fun ResponseBody.saveFile(): Flow<DownloadState> {
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