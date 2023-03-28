package com.hoomanholding.jpamanager.view.fragment.login

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.hoomanholding.jpamanager.JpaViewModel
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.model.data.request.LoginRequestModel
import com.hoomanholding.jpamanager.model.repository.LoginRepository
import com.hoomanholding.jpamanager.tools.CompanionValues
import com.hoomanholding.jpamanager.tools.SingleLiveEvent
import com.hoomanholding.jpawarehose.di.ResourcesProvider
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
    private val resourcesProvider: ResourcesProvider
) : JpaViewModel() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    val loginLiveDate: SingleLiveEvent<String?> by lazy { SingleLiveEvent<String?>() }
    val userNameError: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }
    val passwordError: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }
    val userName: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val password: MutableLiveData<String> by lazy { MutableLiveData<String>() }


    //---------------------------------------------------------------------------------------------- login
    fun login(fromFingerPrint: Boolean, androidId: String) {
        if (fromFingerPrint)
            setUserNamePasswordFromSharePreferences()
        var valueIsEmpty = false
        if (userName.value.isNullOrEmpty()) {
            userNameError.postValue(resourcesProvider.getString(R.string.userNameIsEmpty))
            valueIsEmpty = true
        }
        if (password.value.isNullOrEmpty()) {
            passwordError.postValue(resourcesProvider.getString(R.string.passcodeIsEmpty))
            valueIsEmpty = true
        }
        if (!valueIsEmpty)
            requestLogin(androidId)
    }
    //---------------------------------------------------------------------------------------------- login


    //---------------------------------------------------------------------------------------------- requestLogin
    private fun requestLogin(androidId: String) {
        job = CoroutineScope(IO + exceptionHandler()).launch {
            if (userName.value.isNullOrEmpty() || password.value.isNullOrEmpty())
                setMessage(resourcesProvider.getString(R.string.dataSendingIsEmpty))
            else {
                val response = repository.requestLogin(
                    LoginRequestModel(
                        userName.value!!, password.value!!, "managerapp", androidId
                    )
                )
                if (response?.isSuccessful == true) {
                    val loginResponse = response.body()
                    loginResponse?.let {
                        if (!it.hasError)
                            saveUserNameAndPassword(it.data)
                        setMessage(it.message)
                    } ?: run {
                        setMessage(resourcesProvider.getString(R.string.dataReceivedIsEmpty))
                    }
                } else
                    setMessage(response)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestLogin


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

}