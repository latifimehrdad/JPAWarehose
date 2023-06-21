package com.zarholding.jpacustomer.view.dialog.location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zarholding.jpacustomer.model.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by m-latifi on 5/27/2023.
 */

@HiltViewModel
class EditLocationViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
): JpaViewModel() {


    val editLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }


    //---------------------------------------------------------------------------------------------- requestEditCustomerLocation
    fun requestEditCustomerLocation(lat: Double, long: Double) {
        viewModelScope.launch(IO + exceptionHandler()) {
            val response = checkResponse(customerRepository.requestEditCustomerLocation(lat, long))
            response?.let {
                editLiveData.postValue(it)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestEditCustomerLocation


}