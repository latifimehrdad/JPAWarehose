package com.zarholding.jpacustomer.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.data.request.AddToBasket
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.zar.core.tools.api.apiCall
import javax.inject.Inject

/**
 * Created by m-latifi on 6/6/2023.
 */

class RecordCheckRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository
) {


    //---------------------------------------------------------------------------------------------- requestGetNotRecordCheck
    suspend fun requestGetNotRecordCheck() =
        apiCall { api.requestGetNotRecordCheck(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- requestGetNotRecordCheck

}