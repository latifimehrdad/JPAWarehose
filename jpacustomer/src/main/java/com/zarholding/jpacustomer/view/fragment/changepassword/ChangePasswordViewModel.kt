package com.zarholding.jpacustomer.view.fragment.changepassword

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.R
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.tools.SingleLiveEvent
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * create by m-latifi on 5/3/2023
 */

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences
) : JpaViewModel() {

    var token: String? = null
    val passwordChangeLiveData: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>() }
    val oldPasswordError: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }
    val passwordError: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }
    val rePasswordError: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }
    val oldPassword: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val password: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val rePassword: MutableLiveData<String> by lazy { MutableLiveData<String>() }


    //---------------------------------------------------------------------------------------------- changePassword
    fun changePassword() {

        var valueIsEmpty = false
        if (oldPassword.value.isNullOrEmpty()) {
            oldPasswordError.postValue(resourcesProvider.getString(R.string.passcodeIsEmpty))
            valueIsEmpty = true
        }
        if (password.value.isNullOrEmpty()) {
            passwordError.postValue(resourcesProvider.getString(R.string.passcodeIsEmpty))
            valueIsEmpty = true
        }
        if (rePassword.value.isNullOrEmpty()) {
            rePasswordError.postValue(resourcesProvider.getString(R.string.passcodeIsEmpty))
            valueIsEmpty = true
        }
        if (!password.value.equals(rePassword.value)){
            rePasswordError.postValue(resourcesProvider.getString(R.string.rePasswordIsNotEqual))
            valueIsEmpty = true
        }

        if (!valueIsEmpty)
            changePasswordRequest()
    }
    //---------------------------------------------------------------------------------------------- changePassword


    //---------------------------------------------------------------------------------------------- changePasswordRequest
    private fun changePasswordRequest() {
        viewModelScope.launch(IO + exceptionHandler()) {
            if (oldPassword.value.isNullOrEmpty() || password.value.isNullOrEmpty())
                setMessage(
                    resourcesProvider.getString(
                        R.string.dataSendingIsEmpty
                    )
                )
            else {
                val old = oldPassword.value!!
                val password = password.value!!

                val response = checkResponse(userRepository.requestChangePassword(
                    old,
                    password,
                    "Bearer $token"
                ))
                response?.let {
                    saveUserNameAndPassword(it)
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------- changePasswordRequest



    //---------------------------------------------------------------------------------------------- saveToken
    private fun saveUserNameAndPassword(boolean: Boolean) {
        if (boolean)
            sharedPreferences
                .edit()
                .putString(CompanionValues.TOKEN, token)
                .apply()
        passwordChangeLiveData.postValue(boolean)
    }
    //---------------------------------------------------------------------------------------------- saveToken


}