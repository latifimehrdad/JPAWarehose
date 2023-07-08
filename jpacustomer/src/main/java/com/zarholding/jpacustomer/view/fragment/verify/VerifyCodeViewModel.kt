package com.zarholding.jpacustomer.view.fragment.verify

import android.content.SharedPreferences
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.user.VerifyCodeModel
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.tools.SingleLiveEvent
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * create by m-latifi on 5/3/2023
 */

@HiltViewModel
class VerifyCodeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences
) : JpaViewModel() {

    var token: String? = null
    val resendLiveData: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>() }
    val forceChanePasswordLiveData: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>() }

    //---------------------------------------------------------------------------------------------- requestResendVerifyCode
    fun requestResendVerifyCode() {
        viewModelScope.launch(IO + exceptionHandler()) {
            if (token == null) {
                setMessage("Token is empty")
                return@launch
            }
            val response = checkResponse(userRepository.requestResendVerificationCode(token!!))
            response?.let { resendLiveData.postValue(it) }
        }
    }
    //---------------------------------------------------------------------------------------------- requestResendVerifyCode


    //---------------------------------------------------------------------------------------------- requestVerifyCode
    fun requestVerifyCode(verificationCode: String) {
        viewModelScope.launch(IO + exceptionHandler()) {
            if (token == null) {
                setMessage("Token is empty")
                return@launch
            }
            delay(2000)
            val response =
                checkResponse(userRepository.requestVerifyCode(verificationCode, token!!))
            response?.let {
                saveToken(it)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestVerifyCode


    //---------------------------------------------------------------------------------------------- deleteToken
    fun deleteToken() {
        sharedPreferences
            .edit()
            .putString(CompanionValues.TOKEN, null)
            .apply()
    }
    //---------------------------------------------------------------------------------------------- deleteToken



    //---------------------------------------------------------------------------------------------- saveToken
    private fun saveToken(item: VerifyCodeModel) {
        sharedPreferences
            .edit()
            .putString(CompanionValues.TOKEN, token)
            .apply()
        if (item.isVerificationCorrect)
            forceChanePasswordLiveData.postValue(item.isforceChangePassword)
    }
    //---------------------------------------------------------------------------------------------- saveToken

}