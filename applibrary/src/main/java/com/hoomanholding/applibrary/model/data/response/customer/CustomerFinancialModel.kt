package com.hoomanholding.applibrary.model.data.response.customer


/**
 * create by m-latifi on 4/5/2023
 */

data class CustomerFinancialModel(
    val customerId: Int,
    val customerCode: String?,
    val customerName: String?,
    val storeName: String?,
    val visitorName: String?,
    val routeName: String?,
    val address: String,
    val purchaseAmount: Long,
    val billingCount: Long,
    val firstBillingDate: String?,
    val lastBillingDate: String?,
    val debitAmount: Long,
    val cashDebitAmount: Long,
    val notRegisteredCheckAmount: Long,
    val notRegisteredCheckCount: Int,
    val bouncedCheckAmount: Long,
    val bouncedCheckCount: Int,
    val payedCheckAmount: Long,
    val payedCheckCount: Int,
    val notDueCheckAmount: Long,
    val notDueCheckCount: Int,
    val guaranteeCheckAmount: Long,
    val guaranteeCheckCount: Int
)
