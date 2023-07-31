package com.zarholding.jpacustomer.view.fragment.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.model.data.enums.EnumEntityType
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.tools.SharedPreferencesManager
import com.hoomanholding.applibrary.tools.SingleLiveEvent
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zar.core.tools.api.ProgressRequestBodyManager
import com.zar.core.tools.api.interfaces.UploadCallBack
import com.zar.core.tools.manager.DeviceManager
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.model.repository.UploadFileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

/**
 * Created by m-latifi on 5/23/2023.
 */

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val uploadFileRepository: UploadFileRepository,
    private val sharedPreferencesManager: SharedPreferencesManager
) : JpaViewModel() {

    @Inject lateinit var deviceManager: DeviceManager

    val uploadPercentLiveData = SingleLiveEvent<Int>()

    val userInfoLiveData: MutableLiveData<UserInfoEntity> by lazy {
        MutableLiveData<UserInfoEntity>()
    }

    //---------------------------------------------------------------------------------------------- getUserInfo
    fun getUserInfo() {
        val userInfo = userRepository.getUser()
        userInfo?.let {
            userInfoLiveData.postValue(it)
        }
    }
    //---------------------------------------------------------------------------------------------- getUserInfo


    //---------------------------------------------------------------------------------------------- getBearerToken
    fun getBearerToken() = tokenRepository.getBearerToken()
    //---------------------------------------------------------------------------------------------- getBearerToken


    //---------------------------------------------------------------------------------------------- uploadProfileImage
    fun uploadProfileImage(file: File) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler()) {
            val body = ProgressRequestBodyManager(file, "image", object : UploadCallBack {
                override fun onUploadPercent(percent: Int) {
                    uploadPercentLiveData.postValue(percent)
                }
            })
            val entityType = EnumEntityType
                .ProfileImage.name.toRequestBody("mutlipart/form-data".toMediaTypeOrNull())
            val image = MultipartBody.Part.createFormData("File", file.name, body)
            callApi(
                request = uploadFileRepository.uploadProfileImage(entityType, image),
                onReceiveData = {
                    requestUserInfo(sharedPreferencesManager.getFirebaseToken())
                }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- uploadProfileImage


    //---------------------------------------------------------------------------------------------- changeBiometricEnable
    fun changeBiometricEnable() {
        val biometric = !isBiometricEnable()
        sharedPreferencesManager.changeBiometricEnable(biometric)
    }
    //---------------------------------------------------------------------------------------------- changeBiometricEnable


    //---------------------------------------------------------------------------------------------- isBiometricEnable
    fun isBiometricEnable() = sharedPreferencesManager.isBiometricEnable()
    //---------------------------------------------------------------------------------------------- isBiometricEnable


    //---------------------------------------------------------------------------------------------- requestUserInfo
    private fun requestUserInfo(fireBaseToken: String?): Job {
        return CoroutineScope(Dispatchers.IO + exceptionHandler()).launch {
            delay(500)
            callApi(
                request = userRepository.requestUserInfo(fireBaseToken),
                onReceiveData = {
                    userRepository.insertUserInfo(it)
                    uploadPercentLiveData.postValue(101)
                }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- requestUserInfo


    //---------------------------------------------------------------------------------------------- getAppVersion
    fun getAppVersion() = resourcesProvider.getString(
        R.string.version, deviceManager.appVersionName()
    )
    //---------------------------------------------------------------------------------------------- getAppVersion
}