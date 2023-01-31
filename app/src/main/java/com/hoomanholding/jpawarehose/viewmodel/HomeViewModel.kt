package com.hoomanholding.jpawarehose.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hoomanholding.jpawarehose.model.database.entity.UserInfoEntity
import com.hoomanholding.jpawarehose.model.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : JpaViewModel() {

    val userLiveData = MutableLiveData<UserInfoEntity>()

    //---------------------------------------------------------------------------------------------- getUserInfo
    fun getUserInfo() {
        val userInfo = userRepository.getUser()
        userInfo?.let {
            userLiveData.value = it
        }
    }
    //---------------------------------------------------------------------------------------------- getUserInfo

}