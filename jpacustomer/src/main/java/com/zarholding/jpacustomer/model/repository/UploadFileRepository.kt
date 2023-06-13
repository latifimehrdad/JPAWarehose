package com.zarholding.jpacustomer.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.data.response.GeneralResponse
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.zar.core.tools.api.apiCall
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject


/**
 * create by m-latifi on 4/30/2023
 */

class UploadFileRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository
) {


    //---------------------------------------------------------------------------------------------- uploadProfileImage
    suspend fun uploadProfileImage(entityType: RequestBody, File: MultipartBody.Part):
            Response<GeneralResponse<Boolean?>>? {
        val header = HashMap<String, String>()
        header["Authorization"] = tokenRepository.getBearerToken()
        return apiCall { api.uploadProfileImage(entityType, File, header) }
    }
    //---------------------------------------------------------------------------------------------- uploadProfileImage


}