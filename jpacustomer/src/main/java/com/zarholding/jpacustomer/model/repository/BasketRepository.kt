package com.zarholding.jpacustomer.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.data.request.AddToBasket
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.zar.core.tools.api.apiCall
import javax.inject.Inject

/**
 * Created by m-latifi on 6/6/2023.
 */

class BasketRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository
) {


    //---------------------------------------------------------------------------------------------- requestAddToBasket
    suspend fun requestAddToBasket(request: AddToBasket) =
        apiCall { api.requestAddToBasket(request, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestAddToBasket


    //---------------------------------------------------------------------------------------------- basket
    suspend fun requestGetDetailBasket() =
        apiCall { api.requestGetDetailBasket(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- basket


    //---------------------------------------------------------------------------------------------- requestGetBasketCount
    suspend fun requestGetBasketCount() =
        apiCall { api.requestGetBasketCount(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestGetBasketCount


    //---------------------------------------------------------------------------------------------- requestDeleteBasket
    suspend fun requestDeleteBasket(productId: Int) =
        apiCall { api.requestDeleteBasket(productId, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestDeleteBasket


    //---------------------------------------------------------------------------------------------- requestSubmitBasket
    suspend fun requestSubmitBasket(description: String, exhibition: Boolean) =
        apiCall { api.requestSubmitBasket(
            description = description,
            exhibition = exhibition,
            token = tokenRepository.getBearerToken()
        ) }
    //---------------------------------------------------------------------------------------------- requestSubmitBasket
}