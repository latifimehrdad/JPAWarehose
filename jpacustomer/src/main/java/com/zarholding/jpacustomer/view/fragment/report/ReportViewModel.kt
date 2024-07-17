package com.zarholding.jpacustomer.view.fragment.report

import android.os.Bundle
import com.hoomanholding.applibrary.model.data.enums.EnumReportType
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.tools.RoleManager
import com.hoomanholding.applibrary.view.fragment.JpaViewModel
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.model.ReportItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val roleManager: RoleManager
) : JpaViewModel() {


    //---------------------------------------------------------------------------------------------- getReport
    fun getReport(): List<ReportItem> {
        val items = mutableListOf<ReportItem>()
        items.add(feedback())
        items.add(factor())
        items.add(customerBalance())
        if (roleManager.isAccessToCustomerOrderReport())
            items.add(customerOrder())
        items.add(customerBasketReturnReport())
        return items
    }
    //---------------------------------------------------------------------------------------------- getReport


    //---------------------------------------------------------------------------------------------- customerOrder
    private fun customerOrder() = ReportItem(
        title = resourcesProvider.getString(R.string.customerOrderReport),
        bundle = null,
        action = R.id.action_goto_customerOrderReportFragment
    )
    //---------------------------------------------------------------------------------------------- customerOrder


    //---------------------------------------------------------------------------------------------- customerBalance
    private fun customerBalance() = ReportItem(
        title = resourcesProvider.getString(R.string.customerBalanceReport),
        bundle = null,
        action = R.id.action_profileFragment_to_customerBalanceReportFragment
    )
    //---------------------------------------------------------------------------------------------- customerBalance


    //---------------------------------------------------------------------------------------------- factor
    private fun factor(): ReportItem {
        val bundle = Bundle()
        bundle.putString(CompanionValues.REPORT_TYPE, EnumReportType.Billing.name)
        return ReportItem(
            title = resourcesProvider.getString(R.string.factorReport),
            bundle = bundle,
            action = R.id.action_profileFragment_to_billingReturnReportFragment
        )
    }
    //---------------------------------------------------------------------------------------------- factor


    //---------------------------------------------------------------------------------------------- feedback
    private fun feedback(): ReportItem {
        val bundle = Bundle()
        bundle.putString(CompanionValues.REPORT_TYPE, EnumReportType.Billing.name)
        return ReportItem(
            title = resourcesProvider.getString(R.string.feedbackReport),
            bundle = bundle,
            action = R.id.action_profileFragment_to_billingReturnReportFragment
        )
    }
    //---------------------------------------------------------------------------------------------- feedback


    //---------------------------------------------------------------------------------------------- customerBasketReturnReport
    private fun customerBasketReturnReport() = ReportItem(
        title = resourcesProvider.getString(R.string.customerBasketReturnReport),
        bundle = null,
        action = R.id.action_goto_customerBasketReturnReportFragment
    )
    //---------------------------------------------------------------------------------------------- customerBasketReturnReport


}




