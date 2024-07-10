package com.zarholding.jpacustomer.view.dialog.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hoomanholding.applibrary.model.data.request.AddToBasket
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zarholding.jpacustomer.model.repository.BasketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by m-latifi on 6/4/2023.
 */

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val basketRepository: BasketRepository
): JpaViewModel() {

    val addToBasketLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean> () }


    //---------------------------------------------------------------------------------------------- getBearerToken
    fun getBearerToken() = tokenRepository.getBearerToken()
    //---------------------------------------------------------------------------------------------- getBearerToken


    //---------------------------------------------------------------------------------------------- requestAddToBasket
    fun requestAddToBasket(request: AddToBasket) {
        viewModelScope.launch(IO + exceptionHandler()){
            callApi(
                request = basketRepository.requestAddToBasket(request),
                onReceiveData = { addToBasketLiveData.postValue(it) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- requestAddToBasket


    //---------------------------------------------------------------------------------------------- requestAddToReturn
    fun requestAddToReturn(request: AddToBasket) {
        viewModelScope.launch(IO + exceptionHandler()){
            callApi(
                request = basketRepository.requestAddToReturnBasket(request),
                onReceiveData = { addToBasketLiveData.postValue(it) }
            )
        }
    }
    //---------------------------------------------------------------------------------------------- requestAddToReturn

}