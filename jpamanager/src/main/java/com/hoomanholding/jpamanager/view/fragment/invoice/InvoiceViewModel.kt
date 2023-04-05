package com.hoomanholding.jpamanager.view.fragment.invoice

import androidx.lifecycle.MutableLiveData
import com.hoomanholding.applibrary.model.data.request.OrderRequestModel
import com.hoomanholding.applibrary.model.data.response.order.DetailOrderModel
import com.hoomanholding.applibrary.model.data.response.order.OrderModel
import com.hoomanholding.jpamanager.model.repository.OrderRepository
import com.hoomanholding.applibrary.di.ResourcesProvider
import com.hoomanholding.applibrary.model.data.enums.EnumCheckType
import com.hoomanholding.applibrary.model.data.request.FilterCustomerRequest
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
    private val resourcesProvider: ResourcesProvider,
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

    //---------------------------------------------------------------------------------------------- requestGetOrder
    fun requestGetOrder() {
        job = CoroutineScope(IO + exceptionHandler()).launch {
            val request = OrderRequestModel(
                "14011001", "14011230", 0, 0
            )
            val response = orderRepository.requestGetOrder(request)
            if (response?.isSuccessful == true) {
                val body = response.body()
                body?.let {
                    if (!it.hasError)
                        it.data?.let { data ->
                            orderLiveData.postValue(data)
                        } ?: run {
                            setMessage(
                                resourcesProvider.getString(
                                    com.hoomanholding.applibrary.R.string.dataReceivedIsEmpty
                                )
                            )
                        }
                    else
                        setMessage(it.message)
                } ?: run {
                    setMessage(
                        resourcesProvider.getString(
                            com.hoomanholding.applibrary.R.string.dataReceivedIsEmpty
                        )
                    )
                }
            } else
                setMessage(response)
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetOrder


    //---------------------------------------------------------------------------------------------- requestOrderDetail
    fun requestOrderDetail(orderId: Int) {
        job = CoroutineScope(IO + exceptionHandler()).launch {
            val response = orderRepository.requestOrderDetail(orderId)
            if (response?.isSuccessful == true) {
                val body = response.body()
                body?.let {
                    if (!it.hasError)
                        it.data?.let { data ->
                            detailOrderLiveData.postValue(data)
                        } ?: run {
                            setMessage(
                                resourcesProvider.getString(
                                    com.hoomanholding.applibrary.R.string.dataReceivedIsEmpty
                                )
                            )
                        }
                    else
                        setMessage(it.message)
                } ?: run {
                    setMessage(
                        resourcesProvider.getString(
                            com.hoomanholding.applibrary.R.string.dataReceivedIsEmpty
                        )
                    )
                }
            } else
                setMessage(response)
        }
    }
    //---------------------------------------------------------------------------------------------- requestOrderDetail


    //---------------------------------------------------------------------------------------------- requestGetVisitor
    fun requestGetVisitor() {
       job = CoroutineScope(IO + exceptionHandler()).launch {
           val response = visitorRepository.requestGetVisitor()
           if (response?.isSuccessful == true) {
               response.body()?.let { body ->
                   if (!body.hasError)
                       body.data?.let {
                           visitorLiveData.postValue(it)
                       } ?: run {
                           setMessage(body.message)
                       }
               } ?: run {
                   setMessage(
                       resourcesProvider.getString(
                           com.hoomanholding.applibrary.R.string.dataReceivedIsEmpty
                       )
                   )
               }
           } else setMessage(response)
       }
    }
    //---------------------------------------------------------------------------------------------- requestGetVisitor


    //---------------------------------------------------------------------------------------------- requestDisApprovalReasons
    fun requestDisApprovalReasons() {
        job = CoroutineScope(IO + exceptionHandler()).launch {
            val response = reasonRepository.requestDisApprovalReasons()
            if (response?.isSuccessful == true) {
                response.body()?.let { body ->
                    if (!body.hasError)
                        body.data?.let {
                            disApprovalReasons.postValue(it)
                        } ?: run {
                            setMessage(body.message)
                        }
                } ?: run {
                    setMessage(
                        resourcesProvider.getString(
                            com.hoomanholding.applibrary.R.string.dataReceivedIsEmpty
                        )
                    )
                }
            } else setMessage(response)
        }
    }
    //---------------------------------------------------------------------------------------------- requestDisApprovalReasons


    //---------------------------------------------------------------------------------------------- requestGetCustomer
    fun requestGetCustomer() {
        job = CoroutineScope(IO + exceptionHandler()).launch {
            val request = FilterCustomerRequest(0,"حامد آزاد")
            val response = customerRepository.requestGetCustomer(request)
            if (response?.isSuccessful == true) {
                response.body()?.let { body ->
                    if (!body.hasError)
                        body.data?.let {
                            customerLiveData.postValue(it)
                        } ?: run {
                            setMessage(body.message)
                        }
                } ?: run {
                    setMessage(
                        resourcesProvider.getString(
                            com.hoomanholding.applibrary.R.string.dataReceivedIsEmpty
                        )
                    )
                }
            } else setMessage(response)
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetCustomer


    //---------------------------------------------------------------------------------------------- requestGetCustomerFinancial
    fun requestGetCustomerFinancial(customerId: Int) {
        job = CoroutineScope(IO + exceptionHandler()).launch {
            val response = customerRepository.requestGetCustomerFinancial(customerId)
            if (response?.isSuccessful == true) {
                response.body()?.let { body ->
                    if (!body.hasError)
                        body.data?.let {
                            customerFinancialLiveData.postValue(it)
                        } ?: run {
                            setMessage(body.message)
                        }
                } ?: run {
                    setMessage(
                        resourcesProvider.getString(
                            com.hoomanholding.applibrary.R.string.dataReceivedIsEmpty
                        )
                    )
                }
            } else setMessage(response)
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetCustomerFinancial


    //---------------------------------------------------------------------------------------------- requestGetCustomerFinancialDetail
    fun requestGetCustomerFinancialDetail(customerId: Int, checkType: EnumCheckType) {
        job = CoroutineScope(IO + exceptionHandler()).launch {
            val response = customerRepository
                .requestGetCustomerFinancialDetail(customerId, checkType)
            if (response?.isSuccessful == true) {
                response.body()?.let { body ->
                    if (!body.hasError)
                        body.data?.let {
                            customerFinancialDetailLiveData.postValue(it)
                        } ?: run {
                            setMessage(body.message)
                        }
                } ?: run {
                    setMessage(
                        resourcesProvider.getString(
                            com.hoomanholding.applibrary.R.string.dataReceivedIsEmpty
                        )
                    )
                }
            } else setMessage(response)
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetCustomerFinancialDetail

}