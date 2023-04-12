package com.hoomanholding.jpamanager.view.activity

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.hoomanholding.applibrary.tools.CompanionValues
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
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


}