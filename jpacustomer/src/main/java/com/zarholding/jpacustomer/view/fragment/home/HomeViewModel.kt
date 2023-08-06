package com.zarholding.jpacustomer.view.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.response.order.CustomerOrderModel
import com.hoomanholding.applibrary.tools.SharedPreferencesManager
import com.hoomanholding.applibrary.tools.SingleLiveEvent
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zar.core.tools.manager.DeviceManager
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

    @Inject lateinit var sharedPreferencesManager: SharedPreferencesManager
    @Inject lateinit var deviceManager: DeviceManager

    val orderLiveData: MutableLiveData<List<CustomerOrderModel>> by lazy {
        MutableLiveData<List<CustomerOrderModel>>()
    }

    var selectedOrder: CustomerOrderModel? = null
    val newVersionLiveData = SingleLiveEvent<String>()
    val basketCountLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }


    //---------------------------------------------------------------------------------------------- getCustomerOrders
    fun getCustomerOrders(checkEmptyOrder: Boolean = true) {
        if (orderLiveData.value.isNullOrEmpty() || !checkEmptyOrder)
            viewModelScope.launch(IO + exceptionHandler()) {
                callApi(
                    request = orderRepository.requestCustomerGetOrder(),
                    onReceiveData = {
                        orderLiveData.postValue(it)
                        getBasketCount()
                    }
                )
            }
    }
    //---------------------------------------------------------------------------------------------- getCustomerOrders


    //---------------------------------------------------------------------------------------------- getBasketCount
    private fun getBasketCount() {
        viewModelScope.launch(IO + exceptionHandler()) {
            callApi(
                request = basketRepository.requestGetBasketCount(),
                onReceiveData = { basketCountLiveData.postValue(it) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- getBasketCount



    //---------------------------------------------------------------------------------------------- checkVersionIsNew
    fun checkVersionIsNew() {
        viewModelScope.launch(IO + exceptionHandler()) {
            val saveVersion = sharedPreferencesManager.getVersion()
            val currentVersion = deviceManager.appVersionCode()
            if (saveVersion < currentVersion) {
                sharedPreferencesManager.setVersion(currentVersion)
                newVersionLiveData.postValue(newFeature())
            }
        }
    }
    //---------------------------------------------------------------------------------------------- checkVersionIsNew


    //---------------------------------------------------------------------------------------------- newFeature
    private fun newFeature(): String {
        var feature = "امکانات و تغییرات نسخه ${deviceManager.appVersionName()}"
        feature += System.getProperty("line.separator")
        feature += "-بهبود دریافت فایل pdf از گزارشات"
        feature += System.getProperty("line.separator")
        feature += "-ارسال انتقادات و پیشنهادات"
        feature += System.getProperty("line.separator")
        feature += "-امکان اضافه نمودن توضیحات در زمان ثبت سفارش"
        return feature
    }
    //---------------------------------------------------------------------------------------------- newFeature

}