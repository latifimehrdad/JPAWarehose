package com.hoomanholding.applibrary.model.data.enums

enum class EnumBaseDataType(val type: Int) {
    DisapprovalReason(168),
    Currency(5),
    ReturnReason(63);

    fun getValue(): Int {
        return type
    }
}