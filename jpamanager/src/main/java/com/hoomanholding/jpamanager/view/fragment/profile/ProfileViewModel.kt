package com.hoomanholding.jpamanager.view.fragment.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.model.data.enums.EnumEntityType
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.tools.SharedPreferencesManager
import com.hoomanholding.applibrary.tools.SingleLiveEvent
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
    private val uploadFileRepository: UploadFileRepository,
    private val sharedPreferencesManager: SharedPreferencesManager
) : JpaViewModel() {

    val userInfoLiveData: MutableLiveData<UserInfoEntity> by lazy {
        MutableLiveData<UserInfoEntity>()
    }

    val uploadPercentLiveData = SingleLiveEvent<Int>()

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
    fun isBiometricEnable() = sharedPreferencesManager.isBiometricEnable()
    //---------------------------------------------------------------------------------------------- isBiometricEnable


    //---------------------------------------------------------------------------------------------- changeBiometricEnable
    fun changeBiometricEnable() {
        val biometric = !isBiometricEnable()
        sharedPreferencesManager.changeBiometricEnable(biometric)
    }
    //---------------------------------------------------------------------------------------------- changeBiometricEnable


    //---------------------------------------------------------------------------------------------- uploadProfileImage
    fun uploadProfileImage(file: File) {
        viewModelScope.launch(IO + exceptionHandler()) {
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
                onReceiveData = { uploadPercentLiveData.postValue(101) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- uploadProfileImage

}