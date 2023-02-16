package com.hoomanholding.jpawarehose.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.hoomanholding.jpawarehose.model.database.entity.UserInfoEntity
import com.hoomanholding.jpawarehose.model.repository.UserRepository
import com.hoomanholding.jpawarehose.utility.CompanionValues
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
            withContext(Main){ userLogOut.value = true}
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