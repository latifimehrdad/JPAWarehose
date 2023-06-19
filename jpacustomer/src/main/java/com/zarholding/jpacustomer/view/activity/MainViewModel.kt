package com.zarholding.jpacustomer.view.activity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zar.core.tools.manager.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
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



    //---------------------------------------------------------------------------------------------- fireBaseToken
    fun fireBaseToken() {
        viewModelScope.launch(IO + exceptionHandler()) {
            FirebaseMessaging.getInstance().deleteToken()
            delay(3000)
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                Firebase.messaging.subscribeToTopic("amir")
                    .addOnCompleteListener {
                        var msg = "Subscribed"
                        if (!task.isSuccessful) {
                            msg = "Subscribe failed"
                        }
                        Log.d("meri", msg)
                    }
            })
        }
    }
    //---------------------------------------------------------------------------------------------- fireBaseToken

}