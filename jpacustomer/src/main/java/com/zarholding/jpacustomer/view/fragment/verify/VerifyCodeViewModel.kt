package com.zarholding.jpacustomer.view.fragment.verify

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.enums.EnumSystemType
import com.hoomanholding.applibrary.model.data.response.user.VerifyCodeModel
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
    val verifyCodeLiveData: MutableLiveData<VerifyCodeModel> by lazy {
        MutableLiveData<VerifyCodeModel>()
    }


    //---------------------------------------------------------------------------------------------- viewModelScope
    fun startTimer() {
        viewModelScope.launch(IO + exceptionHandler()) {
            repeat(120) {
                delay(1000)
                timerLiveData.postValue(119 - it)
            }
            timerLiveData.postValue(-1)
        }
    }
    //---------------------------------------------------------------------------------------------- viewModelScope


    //---------------------------------------------------------------------------------------------- requestResendVerifyCode
    fun requestResendVerifyCode() {
        viewModelScope.launch(IO + exceptionHandler()) {
            delay(2000)
            resendLiveData.postValue(true)
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
            val response =
                checkResponse(userRepository.requestVerifyCode(verificationCode, token!!))
            response?.let {
                verifyCodeLiveData.postValue(it)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestVerifyCode

}