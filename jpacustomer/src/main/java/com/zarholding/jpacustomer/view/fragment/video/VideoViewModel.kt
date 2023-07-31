package com.zarholding.jpacustomer.view.fragment.video

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.di.Providers
import com.hoomanholding.applibrary.model.data.response.video.VideoCategoryModel
import com.hoomanholding.applibrary.model.data.response.video.VideoModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zarholding.jpacustomer.model.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by m-latifi 5/24/2023.
 */

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val videoRepository: VideoRepository
) : JpaViewModel() {

    val selectVideoLiveData: MutableLiveData<VideoModel> by lazy {
        MutableLiveData<VideoModel>()
    }
    val categoryLiveData: MutableLiveData<List<VideoCategoryModel>> by lazy {
        MutableLiveData<List<VideoCategoryModel>>()
    }
    val videoLiveData: MutableLiveData<List<VideoModel>> by lazy {
        MutableLiveData<List<VideoModel>>()
    }


    //---------------------------------------------------------------------------------------------- getCategory
    fun getCategory() {
        viewModelScope.launch(IO + exceptionHandler()) {
            callApi(
                request = videoRepository.getVideoCategory(),
                onReceiveData = {
                    categoryLiveData.postValue(it)
                    if (it.isNotEmpty())
                        getVideo(it[0].id)
                }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- getCategory


    //---------------------------------------------------------------------------------------------- getCategory
    fun getVideo(videoGroupId: Int) {
        viewModelScope.launch(IO + exceptionHandler()) {
            callApi(
                request = videoRepository.getVideo(videoGroupId),
                onReceiveData = { videoLiveData.postValue(it) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- getCategory


    //---------------------------------------------------------------------------------------------- selectVideo
    fun selectVideo(position: Int) {
        if (videoLiveData.value.isNullOrEmpty())
            return
        selectVideoLiveData.postValue(videoLiveData.value!![position])
    }
    //---------------------------------------------------------------------------------------------- selectVideo



    //---------------------------------------------------------------------------------------------- getVideoLink
    fun getVideoLink(): String? {
        selectVideoLiveData.value?.videoFileName?.let {
            return "${Providers.url}$it"
        } ?: run {
            return null
        }
    }
    //---------------------------------------------------------------------------------------------- getVideoLink

}