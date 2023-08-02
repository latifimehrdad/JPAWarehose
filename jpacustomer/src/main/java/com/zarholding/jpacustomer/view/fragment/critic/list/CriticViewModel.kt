package com.zarholding.jpacustomer.view.fragment.critic.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.critic.CriticModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zarholding.jpacustomer.model.repository.CriticRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by m-latifi on 8/2/2023.
 */

@HiltViewModel
class CriticViewModel @Inject constructor(
    private val criticRepository: CriticRepository
): JpaViewModel() {


    val criticListLiveData: MutableLiveData<List<CriticModel>> by lazy {
        MutableLiveData<List<CriticModel>>()
    }


    //---------------------------------------------------------------------------------------------- addNewCritic
    fun addNewCritic(tittle: String, message: String) {
        viewModelScope.launch(IO + exceptionHandler()) {
            callApi(request = criticRepository.addNewCritic(tittle, message)) {
                getCriticList()
            }
        }
    }
    //---------------------------------------------------------------------------------------------- addNewCritic


    //---------------------------------------------------------------------------------------------- getCriticList
    fun getCriticList() {
        viewModelScope.launch(IO + exceptionHandler()) {
            callApi(request = criticRepository.getCriticList()){
                criticListLiveData.postValue(it)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- getCriticList


}