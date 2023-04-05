package com.hoomanholding.jpamanager.view.fragment.invoice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.request.OrderRequestModel
import com.hoomanholding.applibrary.model.data.response.order.DetailOrderModel
import com.hoomanholding.applibrary.model.data.response.order.OrderModel
import com.hoomanholding.jpamanager.model.repository.OrderRepository
import com.hoomanholding.applibrary.model.data.enums.EnumCheckType
import com.hoomanholding.applibrary.model.data.enums.EnumState
import com.hoomanholding.applibrary.model.data.request.FilterCustomerRequest
import com.hoomanholding.applibrary.model.data.request.OrderToggleStateRequest
import com.hoomanholding.applibrary.model.data.response.customer.CustomerFinancialDetailModel
import com.hoomanholding.applibrary.model.data.response.customer.CustomerFinancialModel
import com.hoomanholding.applibrary.model.data.response.customer.CustomerModel
import com.hoomanholding.applibrary.model.data.response.reason.DisApprovalReasonModel
import com.hoomanholding.applibrary.model.data.response.visitor.VisitorModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.hoomanholding.jpamanager.model.repository.CustomerRepository
import com.hoomanholding.jpamanager.model.repository.ReasonRepository
import com.hoomanholding.jpamanager.model.repository.VisitorRepository


/**
 * create by m-latifi on 3/12/2023
 */

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val visitorRepository: VisitorRepository,
    private val reasonRepository: ReasonRepository,
    private val customerRepository: CustomerRepository
) : JpaViewModel() {

    val orderLiveData: MutableLiveData<List<OrderModel>> by lazy {
        MutableLiveData<List<OrderModel>>()
    }

    val detailOrderLiveData: MutableLiveData<List<DetailOrderModel>> by lazy {
        MutableLiveData<List<DetailOrderModel>>()
    }

    val visitorLiveData: MutableLiveData<List<VisitorModel>> by lazy {
        MutableLiveData<List<VisitorModel>>()
    }

    val disApprovalReasons: MutableLiveData<List<DisApprovalReasonModel>> by lazy {
        MutableLiveData<List<DisApprovalReasonModel>>()
    }

    val customerLiveData: MutableLiveData<List<CustomerModel>> by lazy {
        MutableLiveData<List<CustomerModel>>()
    }

    val customerFinancialLiveData: MutableLiveData<CustomerFinancialModel> by lazy {
        MutableLiveData<CustomerFinancialModel>()
    }

    val customerFinancialDetailLiveData: MutableLiveData<List<CustomerFinancialDetailModel>> by
    lazy { MutableLiveData<List<CustomerFinancialDetailModel>>() }

    val orderToggleStateLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    //---------------------------------------------------------------------------------------------- requestGetOrder
    fun requestGetOrder() {
        viewModelScope.launch(IO + exceptionHandler()){
            val request = OrderRequestModel(
                "14011001", "14011230", 0, 0
            )
            val response = checkResponse(orderRepository.requestGetOrder(request))
            response?.let { orderLiveData.postValue(it) }
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetOrder


    //---------------------------------------------------------------------------------------------- requestOrderDetail
    fun requestOrderDetail(orderId: Int) {
        viewModelScope.launch(IO + exceptionHandler()){
            val response = checkResponse(orderRepository.requestOrderDetail(orderId))
            response?.let { detailOrderLiveData.postValue(it) }
        }
    }
    //---------------------------------------------------------------------------------------------- requestOrderDetail


    //---------------------------------------------------------------------------------------------- requestGetVisitor
    fun requestGetVisitor() {
        viewModelScope.launch(IO + exceptionHandler()){
            val response = checkResponse(visitorRepository.requestGetVisitor())
            response?.let { visitorLiveData.postValue(it) }
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetVisitor


    //---------------------------------------------------------------------------------------------- requestDisApprovalReasons
    fun requestDisApprovalReasons() {
        viewModelScope.launch(IO + exceptionHandler()){
            val response = checkResponse(reasonRepository.requestDisApprovalReasons())
            response?.let { disApprovalReasons.postValue(it) }
        }
    }
    //---------------------------------------------------------------------------------------------- requestDisApprovalReasons


    //---------------------------------------------------------------------------------------------- requestGetCustomer
    fun requestGetCustomer() {
        viewModelScope.launch(IO + exceptionHandler()){
            val request = FilterCustomerRequest(0,"حامد آزاد")
            val response = checkResponse(customerRepository.requestGetCustomer(request))
            response?.let { customerLiveData.postValue(it) }
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetCustomer


    //---------------------------------------------------------------------------------------------- requestGetCustomerFinancial
    fun requestGetCustomerFinancial(customerId: Int) {
        viewModelScope.launch(IO + exceptionHandler()){
            val response = checkResponse(customerRepository.requestGetCustomerFinancial(customerId))
            response?.let { customerFinancialLiveData.postValue(it) }
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetCustomerFinancial


    //---------------------------------------------------------------------------------------------- requestGetCustomerFinancialDetail
    fun requestGetCustomerFinancialDetail(customerId: Int, checkType: EnumCheckType) {
        viewModelScope.launch(IO + exceptionHandler()){
            val response = checkResponse(
                customerRepository.requestGetCustomerFinancialDetail(customerId, checkType))
            response?.let { customerFinancialDetailLiveData.postValue(it) }
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetCustomerFinancialDetail


    //---------------------------------------------------------------------------------------------- requestOrderToggleState
    fun requestOrderToggleState() {
        viewModelScope.launch(IO + exceptionHandler()){
            val request = OrderToggleStateRequest(
                EnumState.Reject,
                "تست رد PostMan",
                listOf(2150298,
                    2150297,
                    2150296,
                    2150295,
                    2150294,
                    2150293,
                    2150292,
                    2150290,
                    2150289,
                    2150284)
            )
            val response = checkResponse(orderRepository.requestOrderToggleState(request))
            response?.let { orderToggleStateLiveData.postValue(it) }
        }
    }
    //---------------------------------------------------------------------------------------------- requestOrderToggleState


}