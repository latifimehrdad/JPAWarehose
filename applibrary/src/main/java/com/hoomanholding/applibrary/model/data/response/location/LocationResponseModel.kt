package com.hoomanholding.applibrary.model.data.response.location

import com.hoomanholding.applibrary.model.data.database.entity.LocationEntity
import com.hoomanholding.applibrary.model.data.response.BaseResponseAbstractModel

/**
 * Create by Mehrdad on 1/18/2023
 */
data class LocationResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data : List<LocationEntity>?

) : BaseResponseAbstractModel()
