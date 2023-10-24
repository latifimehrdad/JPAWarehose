package com.zarholding.jpacustomer.view.fragment.report

import android.os.Bundle
import android.view.View
import com.hoomanholding.applibrary.model.data.enums.EnumReportType
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.tools.RoleManager
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentReportBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by m-latifi on 6/7/2023.
 */

@AndroidEntryPoint
class ReportFragment(
    override var layout: Int = R.layout.fragment_report
): JpaFragment<FragmentReportBinding>() {

    @Inject
    lateinit var roleManager: RoleManager

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        if (roleManager.isAccessToCustomerOrderReport())
            binding.cardViewCustomerOrderReport.visibility = View.VISIBLE
        else
            binding.cardViewCustomerOrderReport.visibility = View.GONE
    }
    //---------------------------------------------------------------------------------------------- onViewCreated



    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.cardViewCustomerOrderReport.setOnClickListener {
            gotoFragment(R.id.action_goto_customerOrderReportFragment)
        }

        binding.cardViewCustomerBalanceReport.setOnClickListener {
            gotoFragment(R.id.action_profileFragment_to_customerBalanceReportFragment)
        }

        binding.cardViewFactorReport.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(CompanionValues.REPORT_TYPE, EnumReportType.Billing.name)
            gotoFragment(R.id.action_profileFragment_to_billingReturnReportFragment, bundle)
        }

        binding.cardViewFeedbackReport.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(CompanionValues.REPORT_TYPE, EnumReportType.Return.name)
            gotoFragment(R.id.action_profileFragment_to_billingReturnReportFragment, bundle)
        }

    }
    //---------------------------------------------------------------------------------------------- setListener

}