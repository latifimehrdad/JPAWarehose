package com.hoomanholding.applibrary.view.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.R
import com.hoomanholding.applibrary.di.Providers
import com.hoomanholding.applibrary.di.ResourcesProvider
import com.hoomanholding.applibrary.model.data.response.GeneralResponse
import com.hoomanholding.applibrary.model.repository.LogRepository
import com.hoomanholding.applibrary.tools.SingleLiveEvent
import com.zar.core.enums.EnumApiError
import com.zar.core.models.ErrorApiModel
import com.zar.core.tools.api.checkResponseError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
open class JpaViewModel @Inject constructor() : ViewModel() {

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
        viewModelScope.launch(IO) {
            callApi(
                request = logRepository.requestSaveError(log),
                onReceiveData = {}
            )
        }
    }
    //---------------------------------------------------------------------------------------------- sendLogToServer

}