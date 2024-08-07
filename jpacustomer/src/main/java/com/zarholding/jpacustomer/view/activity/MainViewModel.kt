package com.zarholding.jpacustomer.view.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zar.core.tools.manager.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val themeManager: ThemeManager,
    private val userRepository: UserRepository
) : JpaViewModel() {

    val userInfoLiveData: MutableLiveData<UserInfoEntity?> by lazy {
        MutableLiveData<UserInfoEntity?>()
    }


    //---------------------------------------------------------------------------------------------- deleteAllData
    fun deleteAllData() {
        viewModelScope.launch(IO + exceptionHandler()) {
            userRepository.deleteUser()
        }
    }
    //---------------------------------------------------------------------------------------------- deleteAllData



    //---------------------------------------------------------------------------------------------- getUserInfo
    fun getUserInfo() {
        viewModelScope.launch(IO + exceptionHandler()) {
            val userInfo = userRepository.getUser()
            userInfoLiveData.postValue(userInfo)
        }
    }
    //---------------------------------------------------------------------------------------------- getUserInfo


    //---------------------------------------------------------------------------------------------- getBearerToke
    fun getBearerToke() = tokenRepository.getBearerToken()
    //---------------------------------------------------------------------------------------------- getBearerToke


    //---------------------------------------------------------------------------------------------- applicationTheme
    fun applicationTheme() = themeManager.applicationTheme()
    //---------------------------------------------------------------------------------------------- applicationTheme

}