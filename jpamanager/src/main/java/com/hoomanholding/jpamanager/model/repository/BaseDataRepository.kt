package com.hoomanholding.jpamanager.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.data.enums.EnumBaseDataType
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.zar.core.tools.api.apiCall
import javax.inject.Inject


/**
 * create by m-latifi on 4/5/2023
 */

class BaseDataRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository
) {

    //---------------------------------------------------------------------------------------------- requestDisApprovalReasons
    suspend fun requestBaseDataComb(type: EnumBaseDataType) =
        apiCall { api.requestBaseDataComb(
            type = type.getValue(),
            tokenRepository.getBearerToken()
        ) }
    //---------------------------------------------------------------------------------------------- requestDisApprovalReasons

}