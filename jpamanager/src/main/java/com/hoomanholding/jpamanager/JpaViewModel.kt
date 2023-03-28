package com.hoomanholding.jpamanager

import android.util.Log
import androidx.lifecycle.ViewModel
import com.hoomanholding.jpamanager.tools.SingleLiveEvent
import com.zar.core.enums.EnumApiError
import com.zar.core.models.ErrorApiModel
import com.zar.core.tools.api.checkResponseError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
open class JpaViewModel @Inject constructor() : ViewModel() {

    var job: Job? = null
    val errorLiveDate = SingleLiveEvent<ErrorApiModel>()


    //---------------------------------------------------------------------------------------------- setError
    suspend fun setMessage(response: Response<*>?) {
        withContext(Dispatchers.Main) {
            checkResponseError(response, errorLiveDate)
        }
        job?.cancel()
    }
    //---------------------------------------------------------------------------------------------- setError


    //---------------------------------------------------------------------------------------------- setMessage
    suspend fun setMessage(message : String) {
        withContext(Dispatchers.Main) {
            errorLiveDate.value = ErrorApiModel(EnumApiError.Error, message)
        }
        job?.cancel()
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


    //---------------------------------------------------------------------------------------------- onCleared
    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
    //---------------------------------------------------------------------------------------------- onCleared

}