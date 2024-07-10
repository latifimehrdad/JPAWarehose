package com.zarholding.jpacustomer.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.data.enums.EnumState
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


    //---------------------------------------------------------------------------------------------- requestAddToReturnBasket
    suspend fun requestAddToReturnBasket(request: AddToBasket) =
        apiCall { api.requestAddToReturnBasket(request, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestAddToReturnBasket


    //---------------------------------------------------------------------------------------------- basket
    suspend fun requestGetDetailBasket() =
        apiCall { api.requestGetDetailBasket(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- basket


    //---------------------------------------------------------------------------------------------- requestGetDetailReturnBasket
    suspend fun requestGetDetailReturnBasket() =
        apiCall { api.requestGetDetailReturnBasket(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestGetDetailReturnBasket


    //---------------------------------------------------------------------------------------------- requestGetBasketCount
    suspend fun requestGetBasketCount() =
        apiCall { api.requestGetBasketCount(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestGetBasketCount


    //---------------------------------------------------------------------------------------------- requestDeleteBasket
    suspend fun requestDeleteBasket(productId: Int) =
        apiCall { api.requestDeleteBasket(productId, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestDeleteBasket


    //---------------------------------------------------------------------------------------------- requestDeleteAllBasket
    suspend fun requestDeleteAllBasket() =
        apiCall { api.requestDeleteAllBasket(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestDeleteAllBasket


    //---------------------------------------------------------------------------------------------- requestDeleteReturnAllBasket
    suspend fun requestDeleteReturnAllBasket() =
        apiCall { api.requestDeleteReturnAllBasket(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestDeleteReturnAllBasket


    //---------------------------------------------------------------------------------------------- requestDeleteReturnBasket
    suspend fun requestDeleteReturnBasket(productId: Int) =
        apiCall { api.requestDeleteReturnBasket(productId, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestDeleteReturnBasket


    //---------------------------------------------------------------------------------------------- requestSubmitBasket
    suspend fun requestSubmitBasket(description: String, exhibition: Boolean) =
        apiCall { api.requestSubmitBasket(
            description = description,
            exhibition = exhibition,
            token = tokenRepository.getBearerToken()
        ) }
    //---------------------------------------------------------------------------------------------- requestSubmitBasket


    //---------------------------------------------------------------------------------------------- requestSubmitReturnBasket
    suspend fun requestSubmitReturnBasket(description: String, exhibition: Boolean) =
        apiCall { api.requestSubmitReturnBasket(
            description = description,
            exhibition = exhibition,
            token = tokenRepository.getBearerToken()
        ) }
    //---------------------------------------------------------------------------------------------- requestSubmitReturnBasket


    //---------------------------------------------------------------------------------------------- requestSubsetBasket
    suspend fun requestSubsetBasket() =
        apiCall { api.requestSubsetBasket(token = tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestSubsetBasket


    //---------------------------------------------------------------------------------------------- requestSubmitSubUserBasket
    suspend fun requestSubmitSubUserBasket(id: String , state: EnumState) =
        apiCall { api.requestSubmitSubUserBasket(
            id = id,
            state = state,
            token = tokenRepository.getBearerToken()
        ) }
    //---------------------------------------------------------------------------------------------- requestSubmitSubUserBasket



}