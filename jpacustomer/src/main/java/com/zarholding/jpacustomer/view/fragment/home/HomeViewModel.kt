package com.zarholding.jpacustomer.view.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.request.AddToBasket
import com.hoomanholding.applibrary.model.data.response.order.CustomerOrderModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zarholding.jpacustomer.model.repository.BasketRepository
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
    private val orderRepository: OrderRepository,
    private val basketRepository: BasketRepository
) : JpaViewModel() {

    val orderLiveData: MutableLiveData<List<CustomerOrderModel>> by lazy {
        MutableLiveData<List<CustomerOrderModel>>()
    }

    val basketCountLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    //---------------------------------------------------------------------------------------------- getCustomerOrders
    fun getCustomerOrders() {
        if (orderLiveData.value.isNullOrEmpty())
            viewModelScope.launch(IO + exceptionHandler()) {
                val response = checkResponse(orderRepository.requestCustomerGetOrder())
                response?.let {
                    orderLiveData.postValue(it)
                    getBasketCount()
                }
            }
    }
    //---------------------------------------------------------------------------------------------- getCustomerOrders


    //---------------------------------------------------------------------------------------------- getBasketCount
    private fun getBasketCount() {
        viewModelScope.launch(IO + exceptionHandler()) {
            val response = checkResponse(basketRepository.requestGetBasketCount())
            response?.let { basketCountLiveData.postValue(it) }
        }
    }
    //---------------------------------------------------------------------------------------------- getBasketCount

}