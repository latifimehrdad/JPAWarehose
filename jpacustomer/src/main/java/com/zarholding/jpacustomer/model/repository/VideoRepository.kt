package com.zarholding.jpacustomer.model.repository

import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.repository.TokenRepository
import com.zar.core.tools.api.apiCall
import javax.inject.Inject

/**
 * Created by m-latifi on 6/6/2023.
 */

class VideoRepository @Inject constructor(
    private val api: Api,
    private val tokenRepository: TokenRepository
) {

    //---------------------------------------------------------------------------------------------- getVideoCategory
    suspend fun getVideoCategory() =
        apiCall { api.getVideoCategory(tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- getVideoCategory


    //---------------------------------------------------------------------------------------------- getVideo
    suspend fun getVideo(videoGroupId: Int) =
        apiCall { api.getVideo(videoGroupId, tokenRepository.getBearerToken()) }
    //---------------------------------------------------------------------------------------------- getVideo

}