package com.hoomanholding.jpamanager.view.activity

import androidx.lifecycle.MutableLiveData
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository
) : JpaViewModel() {

    val userInfoLiveData: MutableLiveData<UserInfoEntity?> by lazy {
        MutableLiveData<UserInfoEntity?>()
    }


    //---------------------------------------------------------------------------------------------- deleteAllData
    fun deleteAllData() {
        userRepository.deleteUser()
    }
    //---------------------------------------------------------------------------------------------- deleteAllData



    //---------------------------------------------------------------------------------------------- getUserInfo
    fun getUserInfo() {
        val userInfo = userRepository.getUser()
        userInfoLiveData.postValue(userInfo)
    }
    //---------------------------------------------------------------------------------------------- getUserInfo


    //---------------------------------------------------------------------------------------------- getBearerToke
    fun getBearerToke() = tokenRepository.getBearerToken()
    //---------------------------------------------------------------------------------------------- getBearerToke

}