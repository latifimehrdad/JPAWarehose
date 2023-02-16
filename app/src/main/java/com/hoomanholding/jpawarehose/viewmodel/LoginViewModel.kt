package com.hoomanholding.jpawarehose.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.model.data.request.LoginRequestModel
import com.hoomanholding.jpawarehose.model.repository.LoginRepository
import com.hoomanholding.jpawarehose.utility.CompanionValues
import com.hoomanholding.jpawarehose.utility.SingleLiveEvent
import com.hoomanholding.jpawarehose.utility.hilt.ResourcesProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
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

    val loginLiveDate = SingleLiveEvent<String?>()
    val userName = MutableLiveData<String>()
    val password = MutableLiveData<String>()


    //---------------------------------------------------------------------------------------------- requestLogin
    fun requestLogin() {
        job = CoroutineScope(IO + exceptionHandler()).launch {
            if (userName.value.isNullOrEmpty() || password.value.isNullOrEmpty())
                setMessage(resourcesProvider.getString(R.string.dataSendingIsEmpty))
            else {
                val response = repository.requestLogin(
                    LoginRequestModel(userName.value!!, password.value!!)
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
    fun setUserNamePasswordFromSharePreferences() {
        userName.value = sharedPreferences.getString(CompanionValues.userName, "")
        password.value = sharedPreferences.getString(CompanionValues.password, "")
    }
    //---------------------------------------------------------------------------------------------- setUserNamePasswordFromSharePreferences


    //---------------------------------------------------------------------------------------------- saveUserNameAndPassword
    private suspend fun saveUserNameAndPassword(token: String?) {
        sharedPreferences
            .edit()
            .putString(CompanionValues.TOKEN, token)
            .putString(CompanionValues.userName, userName.value)
            .putString(CompanionValues.password, password.value)
            .apply()
        withContext(Main){ loginLiveDate.value = token }
    }
    //---------------------------------------------------------------------------------------------- saveUserNameAndPassword


    //---------------------------------------------------------------------------------------------- saveNewIp
    fun saveNewIp(ip : String?) {
        sharedPreferences
            .edit()
            .putString(CompanionValues.URL, ip)
            .apply()
    }
    //---------------------------------------------------------------------------------------------- saveNewIp

}