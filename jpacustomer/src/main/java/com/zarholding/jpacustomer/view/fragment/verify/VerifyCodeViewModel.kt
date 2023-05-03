package com.zarholding.jpacustomer.view.fragment.verify

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * create by m-latifi on 5/3/2023
 */

@HiltViewModel
class VerifyCodeViewModel @Inject constructor(

): JpaViewModel() {

    val timerLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }


    fun startTimer() {
        viewModelScope.launch(IO + exceptionHandler()){
            repeat(120){
                delay(1000)
                timerLiveData.postValue(119 - it)
            }
            Log.e("meri", "timer End")
        }
    }

}