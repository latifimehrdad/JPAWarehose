package com.zarholding.jpacustomer.view.fragment.video

import androidx.lifecycle.MutableLiveData
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by m-latifi 5/24/2023.
 */

@HiltViewModel
class VideoViewModel @Inject constructor(): JpaViewModel() {

    val categoryLiveData: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }


    //---------------------------------------------------------------------------------------------- getCategory
    fun getCategory() {
        val item = mutableListOf("مدیران", "محصولات", "پرسنل", "کارمندان", "شرکت", "ماکارونی", "روغن")
        categoryLiveData.postValue(item)
    }
    //---------------------------------------------------------------------------------------------- getCategory

}