package com.hoomanholding.applibrary.model.data.response.report


/**
 * create by m-latifi on 5/1/2023
 */

data class CustomerBounceCheckReportModel(
   val customerCode: String?,
   val customerName: String?,
   val storeName: String?,
   val visitorName: String?,
   val checkNumber: String?,
   val dateReceived: String?,
   val dueDate: String?,
   val amount: Long,
   val reason: String?
)
