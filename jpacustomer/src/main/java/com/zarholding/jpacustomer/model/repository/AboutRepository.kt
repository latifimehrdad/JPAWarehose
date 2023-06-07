package com.zarholding.jpacustomer.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.zar.core.tools.api.apiCall
import javax.inject.Inject

/**
 * Created by m-latifi on 6/6/2023.
 */

class AboutRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository
) {

    //---------------------------------------------------------------------------------------------- getAbout
    suspend fun getAbout() =
        apiCall { api.getAbout(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- getAbout

}