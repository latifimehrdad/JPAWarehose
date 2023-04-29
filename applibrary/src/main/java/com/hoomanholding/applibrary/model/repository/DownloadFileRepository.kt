package com.hoomanholding.applibrary.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.data.enums.EnumEntityType
import javax.inject.Inject


/**
 * create by m-latifi on 4/25/2023
 */

class DownloadFileRepository @Inject constructor(
    private val api: Api
) {

    //---------------------------------------------------------------------------------------------- downloadApkFile
    suspend fun downloadApkFile(systemType: String, fileName: String) =
        api.downloadApkFile(systemType, EnumEntityType.APK, fileName)
    //---------------------------------------------------------------------------------------------- downloadApkFile

}