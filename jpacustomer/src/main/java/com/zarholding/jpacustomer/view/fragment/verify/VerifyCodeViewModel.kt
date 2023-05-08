package com.zarholding.jpacustomer.view.fragment.verify

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.tools.CompanionValues
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
    val timerLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val resendLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val forceChanePasswordLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }


    //---------------------------------------------------------------------------------------------- viewModelScope
    fun startTimer() {
        viewModelScope.launch(IO + exceptionHandler()) {
            repeat(30) {
                delay(1000)
                timerLiveData.postValue(29 - it)
            }
            timerLiveData.postValue(-1)
        }
    }
    //---------------------------------------------------------------------------------------------- viewModelScope


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
                if (it.isforceChangePassword)
                    forceChanePasswordLiveData.postValue(true)
                else
                    saveUserNameAndPassword()
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestVerifyCode


    //---------------------------------------------------------------------------------------------- saveToken
    private fun saveUserNameAndPassword() {
        sharedPreferences
            .edit()
            .putString(CompanionValues.TOKEN, token)
            .apply()
        forceChanePasswordLiveData.postValue(false)
    }
    //---------------------------------------------------------------------------------------------- saveToken


}