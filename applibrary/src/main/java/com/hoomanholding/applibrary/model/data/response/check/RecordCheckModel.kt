package com.hoomanholding.applibrary.model.data.response.check

data class RecordCheckModel(
    var id: Long,
    val checkNumber: String?,
    val shomarehSayadi: String?,
    val checkDate: String?,
    val amount: Long,
    val diffDay: Int
)
