package com.hoomanholding.jpamanager.view.fragment.profile

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.model.data.enums.EnumEntityType
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.tools.CompanionValues
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.hoomanholding.applibrary.view.fragment.JpaViewModel



/**
 * create by m-latifi on 3/7/2023
 */

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
): JpaViewModel() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    val userInfoLiveData: MutableLiveData<UserInfoEntity> by lazy {
        MutableLiveData<UserInfoEntity>()
    }

    val entityType = EnumEntityType.ProfileImage.name
    var bearerToken = tokenRepository.getBearerToken()


    //---------------------------------------------------------------------------------------------- getUserInfo
    fun getUserInfo() {
        val userInfo = userRepository.getUser()
        userInfo?.let {
            userInfoLiveData.postValue(it)
        }
    }
    //---------------------------------------------------------------------------------------------- getUserInfo


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