package com.hoomanholding.applibrary.view.fragment

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.R
import com.hoomanholding.applibrary.model.data.enums.EnumSystemType
import com.hoomanholding.applibrary.model.data.enums.EnumVerifyType
import com.hoomanholding.applibrary.model.data.request.ForgetPassModel
import com.hoomanholding.applibrary.model.data.request.LoginRequestModel
import com.hoomanholding.applibrary.tools.SingleLiveEvent
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.model.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import javax.inject.Inject

/**
 * Created by m-latifi on 11/9/2022.
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private var repository: LoginRepository
) : JpaViewModel() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    val loginLiveDate: SingleLiveEvent<String?> by lazy { SingleLiveEvent<String?>() }
    val userNameError: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }
    val passwordError: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }
    val userName: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val password: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    var verifyType: EnumVerifyType = EnumVerifyType.Login


    //---------------------------------------------------------------------------------------------- login
    fun login(
        fromFingerPrint: Boolean,
        androidId: String,
        systemType: EnumSystemType,
        verifyType: EnumVerifyType = EnumVerifyType.Login
    ) {
        this.verifyType = verifyType
        if (fromFingerPrint)
            setUserNamePasswordFromSharePreferences()
        var valueIsEmpty = false
        if (userName.value.isNullOrEmpty()) {
            userNameError.postValue(resourcesProvider.getString(R.string.userNameIsEmpty))
            valueIsEmpty = true
        }
        if (verifyType == EnumVerifyType.Login)
            if (password.value.isNullOrEmpty()) {
                passwordError.postValue(resourcesProvider.getString(R.string.passcodeIsEmpty))
                valueIsEmpty = true
            }

        if (!valueIsEmpty)
            when (verifyType) {
                EnumVerifyType.Login -> requestLogin(androidId, systemType)
                EnumVerifyType.ForgetPass -> requestForgetPassword(androidId, systemType)
            }

    }
    //---------------------------------------------------------------------------------------------- login


    //---------------------------------------------------------------------------------------------- requestLogin
    private fun requestLogin(androidId: String, systemType: EnumSystemType) {
        viewModelScope.launch(IO + exceptionHandler()) {
            if (userName.value.isNullOrEmpty() || password.value.isNullOrEmpty())
                setMessage(
                    resourcesProvider.getString(
                        R.string.dataSendingIsEmpty
                    )
                )
            else {
                val request = LoginRequestModel(
                    userName.value!!,
                    password.value!!,
                    systemType,
                    androidId
                )
                val response = checkResponse(repository.requestLogin(request))
                response?.let {
                    saveUserNameAndPassword(it)
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestLogin


    //---------------------------------------------------------------------------------------------- requestForgetPassword
    private fun requestForgetPassword(androidId: String, systemType: EnumSystemType) {
        viewModelScope.launch(IO + exceptionHandler()) {
            if (userName.value.isNullOrEmpty())
                setMessage(
                    resourcesProvider.getString(
                        R.string.dataSendingIsEmpty
                    )
                )
            else {
                val request = ForgetPassModel(
                    systemType,
                    userName.value!!,
                    androidId
                )
                val response = checkResponse(repository.requestForgetPassword(request))
                response?.let {
                    loginLiveDate.postValue(it)
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestForgetPassword


    //---------------------------------------------------------------------------------------------- isBiometricEnable
    fun isBiometricEnable() = sharedPreferences.getBoolean(CompanionValues.biometric, false)
    //---------------------------------------------------------------------------------------------- isBiometricEnable


    //---------------------------------------------------------------------------------------------- setUserNamePasswordFromSharePreferences
    private fun setUserNamePasswordFromSharePreferences() {
        userName.value = sharedPreferences.getString(CompanionValues.userName, "")
        password.value = sharedPreferences.getString(CompanionValues.password, "")
    }
    //---------------------------------------------------------------------------------------------- setUserNamePasswordFromSharePreferences


    //---------------------------------------------------------------------------------------------- saveUserNameAndPassword
    private fun saveUserNameAndPassword(token: String?) {
        sharedPreferences
            .edit()
            .putString(CompanionValues.TOKEN, token)
            .putString(CompanionValues.userName, userName.value)
            .putString(CompanionValues.password, password.value)
            .apply()
        loginLiveDate.postValue(token)
    }
    //---------------------------------------------------------------------------------------------- saveUserNameAndPassword


    //---------------------------------------------------------------------------------------------- saveNewIp
    fun saveNewIp(ip: String?) {
        sharedPreferences
            .edit()
            .putString(CompanionValues.URL, ip)
            .apply()
    }
    //---------------------------------------------------------------------------------------------- saveNewIp


    //---------------------------------------------------------------------------------------------- changeBiometricEnable
    fun changeBiometricEnable(enable: Boolean) {
        sharedPreferences.edit()
            .putBoolean(CompanionValues.biometric, enable)
            .apply()
    }
    //---------------------------------------------------------------------------------------------- changeBiometricEnable

}