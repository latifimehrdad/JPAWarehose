package com.hoomanholding.jpamanager.view.fragment.profile

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.model.data.enums.EnumEntityType
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.tools.CompanionValues
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.hoomanholding.jpamanager.model.repository.UploadFileRepository
import com.zar.core.tools.api.ProgressRequestBodyManager
import com.zar.core.tools.api.interfaces.UploadCallBack
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


/**
 * create by m-latifi on 3/7/2023
 */

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    tokenRepository: TokenRepository,
    private val uploadFileRepository: UploadFileRepository
) : JpaViewModel() {

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


    //---------------------------------------------------------------------------------------------- uploadProfileImage
    fun uploadProfileImage(file: File) {
        viewModelScope.launch(IO + exceptionHandler()) {
            val body = ProgressRequestBodyManager(file, "image", object : UploadCallBack {
                override fun onUploadPercent(percent: Int) {
                    Log.e("meri", "progress = $percent")
                }
            })
            val entityType = EnumEntityType
                .ProfileImage.name.toRequestBody("mutlipart/form-data".toMediaTypeOrNull())
            val image = MultipartBody.Part.createFormData("File", file.name, body)
            val response = checkResponse(uploadFileRepository.uploadProfileImage(entityType, image))
            response?.let {
                Log.e("meri", "upload")
            }
        }
    }
    //---------------------------------------------------------------------------------------------- uploadProfileImage

}