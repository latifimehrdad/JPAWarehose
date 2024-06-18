package com.zarholding.jpacustomer.view.fragment.dashboard.subuser.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.enums.EnumState
import com.hoomanholding.applibrary.model.data.response.basket.subuser.SubUserOrderModel
import com.hoomanholding.applibrary.model.data.response.report.ReportCustomerOrderModel
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zar.core.tools.api.apiCall
import com.zarholding.jpacustomer.model.repository.BasketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubUserOrderViewModel @Inject constructor(
    private val basketRepository: BasketRepository
) : JpaViewModel() {

    val subsetBasketLiveData: MutableLiveData<List<SubUserOrderModel>> by lazy {
        MutableLiveData<List<SubUserOrderModel>>()
    }


    //---------------------------------------------------------------------------------------------- requestSubsetBasket
    fun requestSubsetBasket() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler()) {
            callApi(
                request = basketRepository.requestSubsetBasket()
            ) {
                subsetBasketLiveData.postValue(it)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- requestSubsetBasket



    //---------------------------------------------------------------------------------------------- requestSubmitSubUserBasket
    fun requestSubmitSubUserBasket(id: String, state: EnumState) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler()){
            callApi(
                request = basketRepository.requestSubmitSubUserBasket(
                    id = id,
                    state = state
                ),
                showMessageAfterSuccessResponse = true,
                onReceiveData = {
                    subsetBasketLiveData.postValue(emptyList())
                    requestSubsetBasket()
                }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- requestSubmitSubUserBasket


}