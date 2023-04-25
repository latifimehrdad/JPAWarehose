package com.hoomanholding.applibrary.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.data.request.DownloadFileRequestModel
import javax.inject.Inject
import javax.inject.Named


/**
 * create by m-latifi on 4/25/2023
 */

class DownloadFileRepository @Inject constructor(
    private val api: Api
) {

    //---------------------------------------------------------------------------------------------- downloadFile
    suspend fun downloadFile(request: DownloadFileRequestModel) =
        api.downloadFile(request)
    //---------------------------------------------------------------------------------------------- downloadFile

}