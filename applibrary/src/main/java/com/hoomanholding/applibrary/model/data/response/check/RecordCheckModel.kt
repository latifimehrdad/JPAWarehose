package com.hoomanholding.applibrary.model.data.response.check

import com.hoomanholding.applibrary.model.data.enums.EnumMontazerVosolReason

data class RecordCheckModel(
    var id: Long,
    val checkNumber: String?,
    val shomarehSayadi: String?,
    val checkDate: String?,
    val amount: Long,
    val diffDay: Int,
    val montazerVosolReason: EnumMontazerVosolReason
)
