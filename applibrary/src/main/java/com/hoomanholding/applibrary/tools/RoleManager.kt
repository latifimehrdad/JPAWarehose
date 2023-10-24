package com.hoomanholding.applibrary.tools

import com.hoomanholding.applibrary.model.data.database.dao.RoleDao
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

/**
 * Created by m-latifi on 11/26/2022.
 */

@Module
@InstallIn(SingletonComponent::class)
class RoleManager @Inject constructor(
    private val roleDao: RoleDao
) {

    private val customerOrderReport = "Reports.Sales.customerOrderReport" // نمایش دکمه گزارش سفارش مشتری


    //---------------------------------------------------------------------------------------------- accessByRole
    private fun accessByRole(role: String) =
        roleDao.getPermission(role)?.let { true } ?: run { false }
    //---------------------------------------------------------------------------------------------- accessByRole


    //---------------------------------------------------------------------------------------------- isAccessToAdminBus
    fun isAccessToCustomerOrderReport() = accessByRole(customerOrderReport)
    //---------------------------------------------------------------------------------------------- isAccessToAdminBus


}