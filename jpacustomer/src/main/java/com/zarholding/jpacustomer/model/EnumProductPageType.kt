package com.zarholding.jpacustomer.model

enum class EnumProductPageType(val type: Int) {
    Product(0),
    Return(1);

    companion object {
        fun getEnum(type: Int) = when (type) {
            Return.type -> Return
            else -> Product
        }
    }
}