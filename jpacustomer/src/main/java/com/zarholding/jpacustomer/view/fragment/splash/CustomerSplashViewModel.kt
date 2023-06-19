package com.zarholding.jpacustomer.view.fragment.splash

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.hoomanholding.applibrary.model.repository.UserRepository
import com.hoomanholding.applibrary.tools.SingleLiveEvent
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by m-latifi on 6/19/2023.
 */

@HiltViewModel
class CustomerSplashViewModel @Inject constructor(
    private val userRepository: UserRepository
) : JpaViewModel() {

    val subscribeToTopic = SingleLiveEvent<Boolean>()

    //---------------------------------------------------------------------------------------------- fireBaseToken
    fun fireBaseToken() {
        viewModelScope.launch(IO + exceptionHandler()) {
            val user = userRepository.getUser()
            user?.userTopicstr?.let { topics ->
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }
                    subscribeToTopic(topics)
                })
            }
        }
    }
    //---------------------------------------------------------------------------------------------- fireBaseToken


    //---------------------------------------------------------------------------------------------- subscribeToTopic
    private fun subscribeToTopic(topics: String) {
        viewModelScope.launch(IO + exceptionHandler()) {
            val list = topics.split(",")
            list.forEach { topic ->
                delay(500)
                Firebase.messaging.subscribeToTopic(topic.trimStart().trimEnd())
                    .addOnCompleteListener {task ->
                        var msg = "Subscribed $topic"
                        if (!task.isSuccessful) {
                            msg = "Subscribe failed  $topic"
                        }
                        Log.d("meri", msg)
                    }
            }
            delay(1000)
            subscribeToTopic.postValue(true)
        }
    }
    //---------------------------------------------------------------------------------------------- subscribeToTopic

}