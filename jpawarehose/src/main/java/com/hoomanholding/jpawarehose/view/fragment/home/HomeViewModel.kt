package com.hoomanholding.jpawarehose.view.fragment.home

import androidx.lifecycle.MutableLiveData
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.tools.SharedPreferencesManager
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sharedPreferencesManager: SharedPreferencesManager
) : JpaViewModel() {

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
    fun isBiometricEnable() = sharedPreferencesManager.isBiometricEnable()
    //---------------------------------------------------------------------------------------------- isBiometricEnable


    //---------------------------------------------------------------------------------------------- changeBiometricEnable
    fun changeBiometricEnable() {
        val biometric = !isBiometricEnable()
        sharedPreferencesManager.changeBiometricEnable(biometric)
    }
    //---------------------------------------------------------------------------------------------- changeBiometricEnable

}