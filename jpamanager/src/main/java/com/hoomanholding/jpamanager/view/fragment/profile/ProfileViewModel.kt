package com.hoomanholding.jpamanager.view.fragment.profile

import androidx.lifecycle.MutableLiveData
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.model.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.hoomanholding.applibrary.view.fragment.JpaViewModel



/**
 * create by m-latifi on 3/7/2023
 */

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
): JpaViewModel() {


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



}