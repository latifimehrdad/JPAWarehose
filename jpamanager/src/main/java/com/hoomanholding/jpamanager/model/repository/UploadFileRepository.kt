package com.hoomanholding.jpamanager.model.repository

import android.net.Uri
import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.data.enums.EnumEntityType
import com.hoomanholding.applibrary.model.data.response.GeneralResponse
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.zar.core.tools.api.apiCall
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject


/**
 * create by m-latifi on 4/30/2023
 */

class UploadFileRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository
) {


    suspend fun uploadProfileImage(entityType: RequestBody, File: MultipartBody.Part):
            Response<GeneralResponse<Boolean?>>? {
        val header = HashMap<String, String>()
        header["Authorization"] = tokenRepository.getBearerToken()
        return apiCall { api.uploadProfileImage(entityType, File, header) }
    }


}