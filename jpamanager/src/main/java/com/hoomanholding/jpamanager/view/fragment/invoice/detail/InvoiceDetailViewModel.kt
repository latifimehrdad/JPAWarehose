package com.hoomanholding.jpamanager.view.fragment.invoice.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.order.DetailOrderModel
import com.hoomanholding.applibrary.model.data.response.order.OrderModel
import com.hoomanholding.jpamanager.model.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.hoomanholding.applibrary.view.fragment.JpaViewModel


/**
 * create by m-latifi on 3/12/2023
 */

@HiltViewModel
class InvoiceDetailViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : JpaViewModel() {

    val invoiceLiveData: MutableLiveData<OrderModel> by lazy {
        MutableLiveData<OrderModel>()
    }


    val detailOrderLiveData: MutableLiveData<List<DetailOrderModel>> by lazy {
        MutableLiveData<List<DetailOrderModel>>()
    }

    //---------------------------------------------------------------------------------------------- setOrderModel
    fun setOrderModel(model: OrderModel){
        invoiceLiveData.postValue(model)
        requestOrderDetail(model.id)
    }
    //---------------------------------------------------------------------------------------------- setOrderModel


    //---------------------------------------------------------------------------------------------- requestOrderDetail
    private fun requestOrderDetail(orderId: Int) {
        viewModelScope.launch(IO + exceptionHandler()){
            val response = checkResponse(orderRepository.requestOrderDetail(orderId))
            response?.let { detailOrderLiveData.postValue(it) }
        }
    }
    //---------------------------------------------------------------------------------------------- requestOrderDetail


}