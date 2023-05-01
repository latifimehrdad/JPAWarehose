package com.hoomanholding.jpamanager.view.fragment.customer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.enums.EnumCheckType
import com.hoomanholding.applibrary.model.data.response.customer.CustomerFinancialDetailModel
import com.hoomanholding.applibrary.model.data.response.customer.CustomerFinancialModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.model.data.other.CustomerFinancialItemModel
import com.hoomanholding.jpamanager.model.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
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
        if (customerFinancialLiveData.value == null)
            viewModelScope.launch(IO + exceptionHandler()) {
                val response =
                    checkResponse(customerRepository.requestGetCustomerFinancial(customerId))
                response?.let {
                    customerFinancialLiveData.postValue(it)
                }
            }
    }
    //---------------------------------------------------------------------------------------------- requestGetCustomerFinancial


    //---------------------------------------------------------------------------------------------- createCustomerFinancialList
    fun createCustomerFinancialList(item: CustomerFinancialModel): List<CustomerFinancialItemModel> {
        val detailList = mutableListOf<CustomerFinancialItemModel>()
        detailList.add(
            CustomerFinancialItemModel(
                resourcesProvider.getString(R.string.purchaseAmount),
                item.purchaseAmount, EnumCheckType.OrderDetails,
                resourcesProvider.getString(R.string.rial)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                resourcesProvider.getString(R.string.orderCount),
                item.billingCount, null,
                ""
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                resourcesProvider.getString(R.string.dateOfFirstPurchase),
                item.firstBillingDate, null,
                ""
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                resourcesProvider.getString(R.string.dateOfLastPurchase),
                item.lastBillingDate, null,
                ""
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                resourcesProvider.getString(R.string.cashDebitAmount),
                item.cashDebitAmount, null,
                resourcesProvider.getString(R.string.rial)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                resourcesProvider.getString(R.string.notRegisteredCheckAmount),
                item.notRegisteredCheckAmount, null,
                resourcesProvider.getString(R.string.rial)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                resourcesProvider.getString(R.string.notRegisteredCheckCount),
                item.notRegisteredCheckCount, EnumCheckType.NotRegisteredCheck,
                ""
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                resourcesProvider.getString(R.string.bouncedCheckAmount),
                item.bouncedCheckAmount, null,
                resourcesProvider.getString(R.string.rial)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                resourcesProvider.getString(R.string.bouncedCheckCount),
                item.bouncedCheckCount, EnumCheckType.BouncedCheck,
                ""
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                resourcesProvider.getString(R.string.payedCheckAmount),
                item.payedCheckAmount, null,
                resourcesProvider.getString(R.string.rial)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                resourcesProvider.getString(R.string.payedCheckCount),
                item.payedCheckCount, null,
                ""
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                resourcesProvider.getString(R.string.notDueCheckAmount),
                item.notDueCheckAmount, null,
                resourcesProvider.getString(R.string.rial)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                resourcesProvider.getString(R.string.notDueCheckCount),
                item.notDueCheckCount, EnumCheckType.NotDueCheck,
                ""
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                resourcesProvider.getString(R.string.guaranteeCheckAmount),
                item.guaranteeCheckAmount, null,
                resourcesProvider.getString(R.string.rial)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                resourcesProvider.getString(R.string.guaranteeCheckCount),
                item.guaranteeCheckCount, EnumCheckType.GuaranteeCheck,
                ""
            )
        )
        return detailList
    }
    //---------------------------------------------------------------------------------------------- createCustomerFinancialList


    //---------------------------------------------------------------------------------------------- requestGetCustomerFinancialDetail
    fun requestGetCustomerFinancialDetail(checkType: EnumCheckType) {
        viewModelScope.launch(IO + exceptionHandler()) {
            val response = checkResponse(
                customerRepository.requestGetCustomerFinancialDetail(customerId, checkType)
            )
            response?.let { customerFinancialDetailLiveData.postValue(it) }
        }
    }
    //---------------------------------------------------------------------------------------------- requestGetCustomerFinancialDetail

}