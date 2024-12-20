package com.hoomanholding.jpamanager.view.fragment.invoice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.enums.EnumBaseDataType
import com.hoomanholding.applibrary.model.data.request.OrderRequestModel
import com.hoomanholding.applibrary.model.data.response.order.OrderModel
import com.hoomanholding.jpamanager.model.repository.OrderRepository
import com.hoomanholding.applibrary.model.data.enums.EnumState
import com.hoomanholding.applibrary.model.data.request.OrderToggleStateRequest
import com.hoomanholding.applibrary.model.data.response.basedata.ComboModel
import com.hoomanholding.applibrary.model.data.response.customer.CustomerModel
import com.hoomanholding.applibrary.model.data.response.visitor.VisitorModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.model.data.other.DateFilterModel
import com.hoomanholding.jpamanager.model.repository.BaseDataRepository
import com.zar.core.tools.extensions.toSolarDate
import kotlinx.coroutines.delay
import java.time.LocalDateTime


/**
 * create by m-latifi on 3/12/2023
 */

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val baseDataRepository: BaseDataRepository
) : JpaViewModel() {

    var disApprovalReasonModel: List<ComboModel>? = null

    val filterCustomerLiveData: MutableLiveData<CustomerModel?> by lazy {
        MutableLiveData<CustomerModel?>()
    }


    val filterDateLiveData: MutableLiveData<DateFilterModel?> by lazy {
        MutableLiveData<DateFilterModel?>()
    }


    val filterVisitorLiveData: MutableLiveData<VisitorModel?> by lazy {
        MutableLiveData<VisitorModel?>()
    }

    val filterStateLiveData: MutableLiveData<Int?> by lazy {
        MutableLiveData<Int?>()
    }

    val orderLiveData: MutableLiveData<List<OrderModel>> by lazy {
        MutableLiveData<List<OrderModel>>()
    }


    val orderToggleStateLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }


    //---------------------------------------------------------------------------------------------- setCustomerForFilter
    fun setCustomerForFilter(customer: CustomerModel?) {
        filterCustomerLiveData.postValue(customer)
        viewModelScope.launch(IO) {
            delay(500)
            requestGetOrder()
        }
    }
    //---------------------------------------------------------------------------------------------- setCustomerForFilter


    //---------------------------------------------------------------------------------------------- setCustomerForFilter
    fun setVisitorForFilter(visitor: VisitorModel?) {
        filterVisitorLiveData.postValue(visitor)
        viewModelScope.launch(IO) {
            delay(500)
            requestGetOrder()
        }
    }
    //---------------------------------------------------------------------------------------------- setCustomerForFilter



    //---------------------------------------------------------------------------------------------- setStateForFilter
    fun setStateForFilter(state: Int?) {
        filterStateLiveData.postValue(state)
        viewModelScope.launch(IO) {
            delay(500)
            requestGetOrder()
        }
    }
    //---------------------------------------------------------------------------------------------- setStateForFilter



    //---------------------------------------------------------------------------------------------- setDateForFilter
    fun setDateForFilter(date: DateFilterModel?) {
        filterDateLiveData.postValue(date)
        viewModelScope.launch(IO) {
            delay(500)
            requestGetOrder()
        }
    }
    //---------------------------------------------------------------------------------------------- setDateForFilter


    //---------------------------------------------------------------------------------------------- requestGetOrder
    fun requestGetOrder() {
        viewModelScope.launch(IO + exceptionHandler()) {
            val dateNow = LocalDateTime.now().toSolarDate()?.getSolarDate() ?: "1400/01/01"
            val date30DayAgo = LocalDateTime.now().minusDays(120).toSolarDate()?.getSolarDate()
                ?: "1400/01/01"
            var startDate = filterDateLiveData.value?.startDate ?: date30DayAgo
            var endDate = filterDateLiveData.value?.endDate ?: dateNow
            startDate = startDate.replace("/","")
            endDate = endDate.replace("/","")
            val request = OrderRequestModel(
                startDate, endDate,
                filterCustomerLiveData.value?.id ?: 0,
                filterVisitorLiveData.value?.id ?: 0,
                filterStateLiveData.value ?: 0
            )
            callApi(
                request = orderRepository.requestGetOrder(request),
                onReceiveData = { orderLiveData.postValue(it) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetOrder


    //---------------------------------------------------------------------------------------------- requestDisApprovalReasons
    fun requestDisApprovalReasons() {
        viewModelScope.launch(IO + exceptionHandler()) {
            callApi(
                request = baseDataRepository.requestBaseDataComb(type = EnumBaseDataType.DisapprovalReason),
                onReceiveData = { disApprovalReasonModel = it }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- requestDisApprovalReasons


    //---------------------------------------------------------------------------------------------- requestOrderToggleState
    fun requestOrderToggleState(positionReason: Int, description: String, state: EnumState) {
        viewModelScope.launch(IO + exceptionHandler()) {
            val orderSelected = orderLiveData.value?.filter {
                it.select
            }
            val orders = orderSelected?.map {
                it.id
            }
            if (orders.isNullOrEmpty())
                setMessage(resourcesProvider.getString(R.string.orderSelectedIsEmpty))
            else{
                if (disApprovalReasonModel == null){
                    setMessage(resourcesProvider.getString(R.string.disApprovalReasonIsEmpty))
                } else {
                    val reasonId = when (state) {
                        EnumState.Confirmed -> null
                        EnumState.Reject -> disApprovalReasonModel?.get(positionReason)?.id
                    }
                    val request = OrderToggleStateRequest(state, description, orders, reasonId)
                    callApi(
                        request = orderRepository.requestOrderToggleState(request),
                        onReceiveData = { orderToggleStateLiveData.postValue(it) }
                    )
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestOrderToggleState


}