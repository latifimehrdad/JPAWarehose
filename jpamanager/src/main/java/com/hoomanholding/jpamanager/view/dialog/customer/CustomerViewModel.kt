package com.hoomanholding.jpamanager.view.dialog.customer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.request.FilterCustomerRequest
import com.hoomanholding.applibrary.model.data.response.customer.CustomerModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.hoomanholding.jpamanager.model.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * create by m-latifi on 4/12/2023
 */

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : JpaViewModel() {

    val customerLiveData: MutableLiveData<List<CustomerModel>> by lazy {
        MutableLiveData<List<CustomerModel>>()
    }


    //---------------------------------------------------------------------------------------------- requestGetCustomer
    fun requestGetCustomer(search: String) {
        viewModelScope.launch(IO + exceptionHandler()){
            val request = FilterCustomerRequest(0,search)
            val response = checkResponse(customerRepository.requestGetCustomer(request))
            response?.let { customerLiveData.postValue(it) }
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetCustomer

}