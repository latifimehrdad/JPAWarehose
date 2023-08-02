package com.zarholding.jpacustomer.view.fragment.critic.detail

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.critic.CriticDetailModel
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zar.core.tools.extensions.toSolarDate
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.model.repository.CriticRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

/**
 * Created by m-latifi on 8/2/2023.
 */

@HiltViewModel
class CriticDetailViewModel @Inject constructor(
    private val criticRepository: CriticRepository
): JpaViewModel() {

    val criticListLiveData: MutableLiveData<List<CriticDetailModel>?> by lazy {
        MutableLiveData<List<CriticDetailModel>?>()
    }

    val criticTitleLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    var id: Long? = null

    //---------------------------------------------------------------------------------------------- setCriticDetail
    fun setCriticDetail(bundle: Bundle?) {
        viewModelScope.launch(IO + exceptionHandler()) {
            id = bundle?.getLong(CompanionValues.ID)
            var title = bundle?.getString(CompanionValues.SUBJECT, "") ?: ""
            if (title.isNotEmpty())
                title = "${resourcesProvider.getString(R.string.subject)} : $title"
            criticTitleLiveData.postValue(title)
            if (id != null)
                getCriticDetailList(id!!)
            else
                setMessage("Error Null")
        }
    }
    //---------------------------------------------------------------------------------------------- setCriticDetail



    //---------------------------------------------------------------------------------------------- getGetList
    private fun getCriticDetailList(id: Long) {
        viewModelScope.launch(IO + exceptionHandler()) {
            if (criticListLiveData.value == null) {
                callApi(request = criticRepository.getCriticDetailList(id)) {
                    criticListLiveData.postValue(it)
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getGetList


    //---------------------------------------------------------------------------------------------- replyCritic
    fun replyCritic(message: String) {
        viewModelScope.launch(IO + exceptionHandler()) {
            if (id != null) {
                val list = criticListLiveData.value?.toMutableList() ?: mutableListOf()
                val date = LocalDateTime.now()
                val time = date.format(DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault()))
                val item = CriticDetailModel(
                    0,
                    0,
                    message,
                    false,
                    date.toSolarDate()?.getSolarDate() ?: "",
                    time,
                    true
                )
                list.add(item)
                criticListLiveData.postValue(list)
                delay(2000)
                callApi(request = criticRepository.sendReplyCritic(message, id!!)) {
                    criticListLiveData.postValue(null)
                }
            }
        }

    }
    //---------------------------------------------------------------------------------------------- replyCritic

}