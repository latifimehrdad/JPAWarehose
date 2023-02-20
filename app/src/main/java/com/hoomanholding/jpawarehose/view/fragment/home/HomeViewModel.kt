package com.hoomanholding.jpawarehose.view.fragment.home

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.hoomanholding.jpawarehose.model.repository.UserRepository
import com.hoomanholding.jpawarehose.utility.CompanionValues
import com.hoomanholding.jpawarehose.JpaViewModel
import com.hoomanholding.jpawarehose.model.data.database.entity.UserInfoEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : JpaViewModel() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    val userLiveData = MutableLiveData<UserInfoEntity>()
    val userLogOut = MutableLiveData<Boolean>()

    //---------------------------------------------------------------------------------------------- getUserInfo
    fun getUserInfo() {
        val userInfo = userRepository.getUser()
        userInfo?.let {
            userLiveData.value = it
        }
    }
    //---------------------------------------------------------------------------------------------- getUserInfo



    //---------------------------------------------------------------------------------------------- logOut
    fun logOut() {
        CoroutineScope(IO + exceptionHandler()).launch {
            userRepository.deleteUser()
            delay(300)
            userLogOut.postValue(true)
        }
    }
    //---------------------------------------------------------------------------------------------- logOut


    //---------------------------------------------------------------------------------------------- isBiometricEnable
    fun isBiometricEnable() = sharedPreferences.getBoolean(CompanionValues.biometric, false)
    //---------------------------------------------------------------------------------------------- isBiometricEnable


    //---------------------------------------------------------------------------------------------- changeBiometricEnable
    fun changeBiometricEnable() {
        val biometric = !isBiometricEnable()
        sharedPreferences.edit()
            .putBoolean(CompanionValues.biometric, biometric)
            .apply()
    }
    //---------------------------------------------------------------------------------------------- changeBiometricEnable

}