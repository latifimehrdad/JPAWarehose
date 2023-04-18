package com.hoomanholding.jpamanager.view.fragment.customer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.enums.EnumCheckType
import com.hoomanholding.applibrary.model.data.response.customer.CustomerFinancialDetailModel
import com.hoomanholding.applibrary.model.data.response.customer.CustomerFinancialModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.hoomanholding.jpamanager.model.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * create by m-latifi on 4/17/2023
 */

@HiltViewModel
class CustomerFinancialViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : JpaViewModel() {

    var customerId: Int = 0

    val customerFinancialLiveData: MutableLiveData<CustomerFinancialModel> by lazy {
        MutableLiveData<CustomerFinancialModel>()
    }

    val customerFinancialDetailLiveData: MutableLiveData<List<CustomerFinancialDetailModel>> by
    lazy { MutableLiveData<List<CustomerFinancialDetailModel>>() }


    //---------------------------------------------------------------------------------------------- requestGetCustomerFinancial
    fun requestGetCustomerFinancial() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler()) {
            val response = checkResponse(customerRepository.requestGetCustomerFinancial(customerId))
            response?.let { customerFinancialLiveData.postValue(it) }
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetCustomerFinancial


    //---------------------------------------------------------------------------------------------- requestGetCustomerFinancialDetail
    fun requestGetCustomerFinancialDetail(checkType: EnumCheckType) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler()) {
            val response = checkResponse(
                customerRepository.requestGetCustomerFinancialDetail(customerId, checkType)
            )
            response?.let { customerFinancialDetailLiveData.postValue(it) }
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetCustomerFinancialDetail

}