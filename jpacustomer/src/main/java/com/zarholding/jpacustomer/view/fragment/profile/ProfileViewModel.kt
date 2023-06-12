package com.zarholding.jpacustomer.view.fragment.profile

import androidx.lifecycle.MutableLiveData
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by m-latifi on 5/23/2023.
 */

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) : JpaViewModel(){

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

}