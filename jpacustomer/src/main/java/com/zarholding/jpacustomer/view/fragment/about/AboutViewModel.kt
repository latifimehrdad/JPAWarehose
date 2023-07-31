package com.zarholding.jpacustomer.view.fragment.about

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.di.Providers
import com.hoomanholding.applibrary.model.data.response.about.AboutModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zarholding.jpacustomer.model.repository.AboutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by m-latifi on 6/6/2023.
 */

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val aboutRepository: AboutRepository
): JpaViewModel() {

    val aboutLiveData: MutableLiveData<AboutModel> by lazy { MutableLiveData<AboutModel>() }

    //---------------------------------------------------------------------------------------------- getAbout
    fun getAbout() {
        viewModelScope.launch(IO + exceptionHandler()) {
            delay(1000)
            callApi(
                request = aboutRepository.getAbout(),
                onReceiveData = { aboutLiveData.postValue(it) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- getAbout


    //---------------------------------------------------------------------------------------------- getVideoLink
    fun getVideoLink(): String? {
        aboutLiveData.value?.videoName?.let {
            return "${Providers.url}$it"
        } ?: run {
            return null
        }
    }
    //---------------------------------------------------------------------------------------------- getVideoLink


}