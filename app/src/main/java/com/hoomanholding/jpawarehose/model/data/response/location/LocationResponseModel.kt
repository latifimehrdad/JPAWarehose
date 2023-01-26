package com.hoomanholding.jpawarehose.model.data.response.location

import com.hoomanholding.jpawarehose.model.data.response.BaseResponseAbstractModel
import com.hoomanholding.jpawarehose.model.database.entity.LocationEntity

/**
 * Create by Mehrdad on 1/18/2023
 */
data class LocationResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data : List<LocationEntity>?

) : BaseResponseAbstractModel()
