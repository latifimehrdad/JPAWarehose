package com.zarholding.jpacustomer.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.zar.core.tools.api.apiCall
import javax.inject.Inject

/**
 * Created by m-latifi on 8/2/2023.
 */

class CriticRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository
) {


    //---------------------------------------------------------------------------------------------- addNewCritic
    suspend fun addNewCritic(tittle: String, message: String) =
        apiCall { api.addNewCritic(tittle, message, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- addNewCritic


    //---------------------------------------------------------------------------------------------- getCriticList
    suspend fun getCriticList() =
        apiCall { api.getCriticsList(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- getCriticList


    //---------------------------------------------------------------------------------------------- getCriticDetailList
    suspend fun getCriticDetailList(id: Long) =
        apiCall { api.getCriticDetailList(id, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- getCriticDetailList


    //---------------------------------------------------------------------------------------------- sendReplyCritic
    suspend fun sendReplyCritic(message: String, id: Long) =
        apiCall { api.sendReplyCritic(message, id, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- sendReplyCritic
}