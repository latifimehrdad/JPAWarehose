package com.hoomanholding.applibrary.view.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.R
import com.hoomanholding.applibrary.model.data.enums.EnumSystemType
import com.hoomanholding.applibrary.model.data.enums.EnumVerifyType
import com.hoomanholding.applibrary.model.data.request.ForgetPassModel
import com.hoomanholding.applibrary.model.data.request.LoginRequestModel
import com.hoomanholding.applibrary.tools.SingleLiveEvent
import com.hoomanholding.applibrary.model.repository.LoginRepository
import com.hoomanholding.applibrary.tools.SharedPreferencesManager
import com.zar.core.tools.extensions.persianNumberToEnglishNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import javax.inject.Inject

/**
 * Created by m-latifi on 11/9/2022.
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    private var repository: LoginRepository,
    private val sharedPreferencesManager: SharedPreferencesManager
) : JpaViewModel() {

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
        if (fromFingerPrint){
            userName.value = sharedPreferencesManager.getUserName()
            password.value = sharedPreferencesManager.getPassword()
        }
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
                val user = userName.value.persianNumberToEnglishNumber()
                val pass = password.value.persianNumberToEnglishNumber()
                userName.postValue(user)
                password.postValue(pass)
                val request = LoginRequestModel(
                    user,
                    pass,
                    systemType,
                    androidId
                )
                callApi(
                    request = repository.requestLogin(request),
                    onReceiveData = {
                        sharedPreferencesManager.saveUserLogin(
                            token = it,
                            userName = user,
                            password = pass
                        )
                        loginLiveDate.postValue(it)
                    }
                )
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
                callApi(
                    request = repository.requestForgetPassword(request),
                    onReceiveData = { loginLiveDate.postValue(it) }
                )
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestForgetPassword


    //---------------------------------------------------------------------------------------------- isBiometricEnable
    fun isBiometricEnable() = sharedPreferencesManager.isBiometricEnable()
    //---------------------------------------------------------------------------------------------- isBiometricEnable



    //---------------------------------------------------------------------------------------------- saveNewIp
    fun saveNewIp(ip: String?) {
        sharedPreferencesManager.saveNewIp(ip)
    }
    //---------------------------------------------------------------------------------------------- saveNewIp


    //---------------------------------------------------------------------------------------------- changeBiometricEnable
    fun changeBiometricEnable(biometric: Boolean) {
        sharedPreferencesManager.changeBiometricEnable(biometric)
    }
    //---------------------------------------------------------------------------------------------- changeBiometricEnable

}