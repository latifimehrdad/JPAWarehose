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
    private val detailOfOrderInHome = "Customers.OrderDetails"// نمایش دکمه جزئیات سفارش صفحه اصلی
    private val detailOfUserInProfile = "Customers.Details"// نمایش دکمه وضعیت من در صفحه پروفایل
    private val basketMenu = "Customer.Basket"// نمایش دکمه سبد خرید در منو
    private val reportMenu = "Reports.Sales"// نمایش دکمه گزارشات در منو
    private val customerReturnBasket = "Customer.ReturnBasket"// نمایش دکمه ثبت مرجوعی در کاستومر
    private val customerCustomers = "Customer.Customers"// نمایش لیست مشتریان در گزارش



    //---------------------------------------------------------------------------------------------- accessByRole
    private fun accessByRole(role: String) =
        roleDao.getPermission(role)?.let { true } ?: run { false }
    //---------------------------------------------------------------------------------------------- accessByRole


    //---------------------------------------------------------------------------------------------- isAccessToCustomerOrderReport
    fun isAccessToCustomerOrderReport() = accessByRole(customerOrderReport)
    //---------------------------------------------------------------------------------------------- isAccessToCustomerOrderReport


    //---------------------------------------------------------------------------------------------- isAccessToDetailOfOrderInHome
    fun isAccessToDetailOfOrderInHome() = accessByRole(detailOfOrderInHome)
    //---------------------------------------------------------------------------------------------- isAccessToDetailOfOrderInHome



    //---------------------------------------------------------------------------------------------- isAccessToDetailOfUserInProfile
    fun isAccessToDetailOfUserInProfile() = accessByRole(detailOfUserInProfile)
    //---------------------------------------------------------------------------------------------- isAccessToDetailOfUserInProfile


    //---------------------------------------------------------------------------------------------- isAccessToBasketMenu
    fun isAccessToBasketMenu() = accessByRole(basketMenu)
    //---------------------------------------------------------------------------------------------- isAccessToBasketMenu


    //---------------------------------------------------------------------------------------------- isAccessToReportMenu
    fun isAccessToReportMenu() = accessByRole(reportMenu)
    //---------------------------------------------------------------------------------------------- isAccessToReportMenu

    //---------------------------------------------------------------------------------------------- isCustomerReturnBasket
    fun isCustomerReturnBasket() = accessByRole(customerReturnBasket)
    //---------------------------------------------------------------------------------------------- isCustomerReturnBasket

    //---------------------------------------------------------------------------------------------- isCustomerCustomers
    fun isCustomerCustomers() = accessByRole(customerCustomers)
    //---------------------------------------------------------------------------------------------- isCustomerCustomers


}