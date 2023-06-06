package com.hoomanholding.jpamanager.view.fragment.customer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.enums.EnumCheckType
import com.hoomanholding.applibrary.model.data.response.customer.CustomerFinancialModel
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentCustomerFinancialBinding
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.hoomanholding.jpamanager.view.adapter.holder.CustomerFinancialDetailHolder
import com.hoomanholding.jpamanager.view.adapter.recycler.CustomerFinancialAdapter
import com.hoomanholding.jpamanager.view.dialog.customer.CustomerHyperLinkDialog
import dagger.hilt.android.AndroidEntryPoint


/**
 * create by m-latifi on 4/17/2023
 */

@AndroidEntryPoint
class CustomerFinancialFragment(override var layout: Int = R.layout.fragment_customer_financial) :
    JpaFragment<FragmentCustomerFinancialBinding>() {

    private val viewModel: CustomerFinancialViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        binding.shimmerViewContainer.stopShimmer()
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        binding.shimmerViewContainer.config(getShimmerBuild())
        setListener()
        observeLiveData()
        getCustomerIdFromArgument()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.textViewAmountOfDebt.setOnClickListener { gotoCustomerBalanceDetail() }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- observeLiveData
    private fun observeLiveData() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }

        viewModel.customerFinancialLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            setCustomerFinancialModel(it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveData


    //---------------------------------------------------------------------------------------------- getCustomerIdFromArgument
    private fun getCustomerIdFromArgument() {
        arguments?.let {
            val customerId = it.getInt(CompanionValues.CUSTOMER_ID, 0)
            if (customerId == 0) {
                activity?.onBackPressedDispatcher?.onBackPressed()
                return
            }
            viewModel.customerId = customerId
            getCustomerFinancial()
        } ?: run {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }
    //---------------------------------------------------------------------------------------------- getCustomerIdFromArgument


    //---------------------------------------------------------------------------------------------- getCustomerFinancial
    private fun getCustomerFinancial() {
        binding.shimmerViewContainer.startLoading()
        viewModel.requestGetCustomerFinancial()
    }
    //---------------------------------------------------------------------------------------------- getCustomerFinancial


    //---------------------------------------------------------------------------------------------- setCustomerFinancialModel
    private fun setCustomerFinancialModel(item: CustomerFinancialModel) {
        if (context == null)
            return
        binding.item = item
        binding.textViewCustomerName.isSelected = true
        binding.textViewAmountOfDebt.isSelected = true
        if (item.debitAmount > 0)
            binding.textViewAmountOfDebt
                .setTextColor(requireContext().getColor(R.color.rejectFactorText))
        else
            binding.textViewAmountOfDebt
                .setTextColor(requireContext().getColor(R.color.confirmFactorText))
        val click = object : CustomerFinancialDetailHolder.Click {
            override fun moreDetail(type: EnumCheckType?) {
                showMoreDialog(type)
            }
        }
        val adapter = CustomerFinancialAdapter(viewModel.createCustomerFinancialList(item), click)
        val manager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerViewItem.adapter = adapter
        binding.recyclerViewItem.layoutManager = manager
    }
    //---------------------------------------------------------------------------------------------- setCustomerFinancialModel


    //---------------------------------------------------------------------------------------------- showMoreDialog
    private fun showMoreDialog(type: EnumCheckType?) {
        if (type == null)
            return
        CustomerHyperLinkDialog(viewModel.customerId, type).show(childFragmentManager, "dialog")
    }
    //---------------------------------------------------------------------------------------------- showMoreDialog


    //---------------------------------------------------------------------------------------------- gotoCustomerBalanceDetail
    private fun gotoCustomerBalanceDetail() {
        val item = viewModel.customerFinancialLiveData.value
        item?.let {
            val bundle = Bundle()
            bundle.putInt(CompanionValues.CUSTOMER_ID, item.customerId)
            bundle.putString(CompanionValues.CUSTOMER_NAME, item.customerName)
            bundle.putString(CompanionValues.CUSTOMER_CODE, item.customerCode)
            gotoFragment(R.id.action_customerFinancialFragment_to_customerBalanceDetailFragment,
                bundle)
        }
    }
    //---------------------------------------------------------------------------------------------- gotoCustomerBalanceDetail


}