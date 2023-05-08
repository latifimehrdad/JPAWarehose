package com.zarholding.jpacustomer.view.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.order.CustomerOrderModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zarholding.jpacustomer.model.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * create by m-latifi on 5/7/2023
 */

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : JpaViewModel() {

    val orderLiveData: MutableLiveData<List<CustomerOrderModel>> by lazy {
        MutableLiveData<List<CustomerOrderModel>>()
    }

    //---------------------------------------------------------------------------------------------- getCustomerOrders
    fun getCustomerOrders() {
        if (orderLiveData.value.isNullOrEmpty())
            viewModelScope.launch(IO + exceptionHandler()) {
                val response = checkResponse(orderRepository.requestCustomerGetOrder())
                response?.let {
                    orderLiveData.postValue(it)
                }
            }
    }
    //---------------------------------------------------------------------------------------------- getCustomerOrders




}