package com.hoomanholding.jpamanager.view.activity

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
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

    val userInfoLiveData: MutableLiveData<String?> by lazy {
        MutableLiveData<String?>()
    }


    //---------------------------------------------------------------------------------------------- deleteAllData
    fun deleteAllData() {
        sharedPreferences
            .edit()
            .putString(CompanionValues.TOKEN, null)
            .apply()
    }
    //---------------------------------------------------------------------------------------------- deleteAllData



    //---------------------------------------------------------------------------------------------- getUserInfo
    fun getUserInfo() {
        val userInfo = userRepository.getUser()
        userInfoLiveData.postValue(userInfo?.fullName)
    }
    //---------------------------------------------------------------------------------------------- getUserInfo


}