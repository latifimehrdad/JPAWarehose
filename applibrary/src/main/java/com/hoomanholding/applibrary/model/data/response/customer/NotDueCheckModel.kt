package com.hoomanholding.applibrary.model.data.response.customer

/**
 * Created by m-latifi on 6/3/2023.
 */

data class NotDueCheckModel(
   val id: Int,
   val checkNumber: String?,
   val checkDate: String?,
   val amount: Long,
   val diffday: Int
)
