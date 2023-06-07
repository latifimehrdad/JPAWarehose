package com.hoomanholding.applibrary.model.data.response.about

/**
 * Created by m-latifi on 6/6/2023.
 */

data class AboutModel(
    val companyName: String?,
    val aboutUs: String?,
    val telephone: String?,
    val address: String?,
    val lat: Double,
    val long: Double,
    val imageName: String?,
    val videoName: String?
)
