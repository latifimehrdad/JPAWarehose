package com.zarholding.jpacustomer.view.fragment.verify

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.enums.EnumVerifyType
import com.hoomanholding.applibrary.model.data.response.user.VerifyCodeModel
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.tools.SharedPreferencesManager
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
    private val sharedPreferencesManager: SharedPreferencesManager
) : JpaViewModel() {

    var verifyType: EnumVerifyType = EnumVerifyType.Login
    var token: String? = null
    val resendLiveData: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>() }
    val forceChanePasswordLiveData: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>() }


    //---------------------------------------------------------------------------------------------- setVerifyTypeFromBundle
    fun setVerifyTypeFromBundle(bundle: Bundle?) {
        verifyType = if (bundle == null)
            EnumVerifyType.Login
        else {
            val type = bundle.getString(CompanionValues.VERIFY_TYPE, EnumVerifyType.Login.name)
            enumValueOf(type)
        }
    }
    //---------------------------------------------------------------------------------------------- setVerifyTypeFromBundle


    //---------------------------------------------------------------------------------------------- requestResendVerifyCode
    fun requestResendVerifyCode() {
        viewModelScope.launch(IO + exceptionHandler()) {
            if (token == null) {
                setMessage("Token is empty")
                return@launch
            }
            callApi(
                request = userRepository.requestResendVerificationCode(token!!),
                onReceiveData = { resendLiveData.postValue(it) }
            )
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
            callApi(
                request = userRepository.requestVerifyCode(verificationCode, token!!),
                onReceiveData = { saveToken(it) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- requestVerifyCode


    //---------------------------------------------------------------------------------------------- deleteToken
    fun deleteToken() {
        sharedPreferencesManager.deleteUserLogin()
    }
    //---------------------------------------------------------------------------------------------- deleteToken


    //---------------------------------------------------------------------------------------------- saveToken
    private fun saveToken(item: VerifyCodeModel) {
        sharedPreferencesManager.saveToken(token)
        if (item.isVerificationCorrect)
            if (item.isforceChangePassword || verifyType == EnumVerifyType.ForgetPass)
                forceChanePasswordLiveData.postValue(true)
            else
                forceChanePasswordLiveData.postValue(false)
    }
    //---------------------------------------------------------------------------------------------- saveToken

}