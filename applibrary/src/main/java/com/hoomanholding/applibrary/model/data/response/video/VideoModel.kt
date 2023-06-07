package com.hoomanholding.applibrary.model.data.response.video

/**
 * Created by m-latifi on 6/6/2023.
 */

data class VideoModel(
    val id: Int,
    val videoTitle: String?,
    val videoDescription: String?,
    val videoFileName: String?,
    val videoGroupId: Int,
    val thumbnailNameAddress: String?
)
