package com.zarholding.jpacustomer.view.dialog.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.order.CustomerOrderDetailModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zarholding.jpacustomer.model.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by m-latifi on 5/27/2023.
 */

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val orderRepository: OrderRepository
): JpaViewModel() {


    val orderDetailLiveData: MutableLiveData<List<CustomerOrderDetailModel>> by lazy {
        MutableLiveData<List<CustomerOrderDetailModel>>()
    }


    //---------------------------------------------------------------------------------------------- getCustomerOrderDetail
    fun getCustomerOrderDetail(orderId: Long) {
        viewModelScope.launch(IO + exceptionHandler()) {
            callApi(
                request = orderRepository.requestGetCustomerOrderDetail(orderId),
                onReceiveData = { orderDetailLiveData.postValue(it) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- getCustomerOrderDetail



}