package com.zarholding.jpacustomer.view.fragment.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.tools.SingleLiveEvent
import com.hoomanholding.applibrary.view.fragment.SplashViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by m-latifi on 6/19/2023.
 */

@HiltViewModel
class CustomerSplashViewModel @Inject constructor() : SplashViewModel() {

    val fireBaseTokenLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val subscribeToTopic = SingleLiveEvent<Boolean>()

    //---------------------------------------------------------------------------------------------- fireBaseToken
    fun fireBaseToken() {
        viewModelScope.launch(IO + exceptionHandler()) {
//            FirebaseMessaging.getInstance().deleteToken()
            delay(1000)
            var newToken = ""
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                newToken = if (!task.isSuccessful)
                    ""
                else
                    task.result
                if (newToken.isNotEmpty()) {
                    Log.e("meri", "fireBaseToken : $newToken")
                    sharedPreferences
                        .edit()
                        .putString(CompanionValues.FIREBASE_TOKEN, newToken)
                        .apply()
                    fireBaseTokenLiveData.postValue(newToken)
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------- fireBaseToken


    //---------------------------------------------------------------------------------------------- subscribeToTopic
    fun subscribeToTopic() {
        viewModelScope.launch(IO + exceptionHandler()) {
            val user = userRepository.getUser()
            user?.userTopicstr?.let {
                val list = it.split(",")
                list.forEach { topic ->
                    delay(500)
                    Firebase.messaging.subscribeToTopic(topic.trimStart().trimEnd())
                        .addOnCompleteListener {
                            Log.e("meri", "subscribeToTopic $topic")
                        }
                }
                delay(1000)
                subscribeToTopic.postValue(true)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- subscribeToTopic

}