package com.hoomanholding.applibrary.view.fragment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.hoomanholding.applibrary.di.ResourcesProvider
import com.hoomanholding.applibrary.model.data.response.GeneralResponse
import com.hoomanholding.applibrary.tools.SingleLiveEvent
import com.zar.core.enums.EnumApiError
import com.zar.core.models.ErrorApiModel
import com.zar.core.tools.api.checkResponseError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
open class JpaViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var resourcesProvider: ResourcesProvider
    val errorLiveDate = SingleLiveEvent<ErrorApiModel>()

    //---------------------------------------------------------------------------------------------- checkResponse
    suspend fun <T: Any> checkResponse(response: Response<GeneralResponse<T?>>?): T?{
        if (response?.isSuccessful == true) {
            val body = response.body()
            body?.let {generalResponse ->
                if (generalResponse.hasError || generalResponse.data == null)
                    setMessage(generalResponse.message)
                else
                    return generalResponse.data
            } ?: run {
                setMessage(
                    resourcesProvider.getString(
                        com.hoomanholding.applibrary.R.string.dataReceivedIsEmpty
                    )
                )
            }
        } else setMessage(response)
        return null
    }
    //---------------------------------------------------------------------------------------------- checkResponse


    //---------------------------------------------------------------------------------------------- setError
    suspend fun setMessage(response: Response<*>?) {
        withContext(Dispatchers.Main) {
            checkResponseError(response, errorLiveDate)
        }
    }
    //---------------------------------------------------------------------------------------------- setError


    //---------------------------------------------------------------------------------------------- setMessage
    suspend fun setMessage(message : String) {
        withContext(Dispatchers.Main) {
            errorLiveDate.value = ErrorApiModel(EnumApiError.Error, message)
        }
    }
    //---------------------------------------------------------------------------------------------- setMessage


    //---------------------------------------------------------------------------------------------- exceptionHandler
    fun exceptionHandler() = CoroutineExceptionHandler { _, throwable ->
        CoroutineScope(Dispatchers.Main).launch {
            throwable.localizedMessage?.let {
            Log.e("meri", it)
                setMessage(it)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- exceptionHandler

}