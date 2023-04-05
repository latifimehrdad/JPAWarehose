package com.hoomanholding.applibrary.model.data.response.visitor

import com.hoomanholding.applibrary.model.data.response.BaseResponseAbstractModel


/**
 * create by m-latifi on 4/5/2023
 */

data class VisitorResponseModel(
    override val hasError: Boolean,
    override val message: String,
    val data: List<VisitorModel>?
): BaseResponseAbstractModel()