package com.hoomanholding.applibrary.model.data.response.customer


/**
 * create by m-latifi on 4/5/2023
 */

data class CustomerFinancialDetailModel(
    val customerCode: String?,
    val customerName: String?,
    val storeName: String?,
    val checkNumber: String?,
    val checkRecivedDate: String?,
    val checkDueDate: String?,
    val checkStateDate: String?,
    val checkAmount: Long,
    val description: String?,
    val checkState: String?,
    val reason: String?,
    val stateCode: Int,
    val id: Long,
    val checkBankName: String?,
    val checkBranchName: String?,
    val checkAccountNumber: String?,
    val checkOwnerName: String?,
    val year: Int,
    val ordersAmount: Long
)
