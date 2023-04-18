package com.hoomanholding.jpamanager.view.fragment.customer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.model.data.enums.EnumCheckType
import com.hoomanholding.applibrary.model.data.response.customer.CustomerFinancialModel
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentCustomerFinancialBinding
import com.hoomanholding.jpamanager.model.data.other.CustomerFinancialItemModel
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.hoomanholding.jpamanager.view.adapter.holder.CustomerFinancialDetailHolder
import com.hoomanholding.jpamanager.view.adapter.recycler.CustomerFinancialAdapter
import com.hoomanholding.jpamanager.view.dialog.CustomerHyperLinkDialog
import com.hoomanholding.jpamanager.view.dialog.customer.PeopleDialog
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
        observeLiveData()
        getCustomerIdFromArgument()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- observeLiveData
    private fun observeLiveData() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }

        viewModel.customerFinancialLiveData.observe(viewLifecycleOwner) {
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
        binding.shimmerViewContainer.stopShimmer()
        if (context == null)
            return
        binding.item = item
        binding.textViewCustomerName.isSelected = true
        binding.textViewAmountOfDebt.isSelected = true
        val detailList = mutableListOf<CustomerFinancialItemModel>()
        detailList.add(
            CustomerFinancialItemModel(
                getString(R.string.purchaseAmount),
                item.purchaseAmount, null,
                getString(R.string.rial)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                getString(R.string.factorCount),
                item.billingCount, null,
                getString(R.string.space)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                getString(R.string.dateOfFirstPurchase),
                item.firstBillingDate, null,
                getString(R.string.space)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                getString(R.string.cashDebitAmount),
                item.cashDebitAmount, null,
                getString(R.string.rial)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                getString(R.string.notRegisteredCheckAmount),
                item.notRegisteredCheckAmount, null,
                getString(R.string.rial)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                getString(R.string.notRegisteredCheckCount),
                item.notRegisteredCheckCount, EnumCheckType.NotRegisteredCheck,
                getString(R.string.space)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                getString(R.string.bouncedCheckAmount),
                item.bouncedCheckAmount, null,
                getString(R.string.rial)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                getString(R.string.bouncedCheckCount),
                item.bouncedCheckCount, EnumCheckType.BouncedCheck,
                getString(R.string.space)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                getString(R.string.payedCheckAmount),
                item.payedCheckAmount, null,
                getString(R.string.rial)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                getString(R.string.payedCheckCount),
                item.payedCheckCount, EnumCheckType.PayedCheck,
                getString(R.string.space)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                getString(R.string.notDueCheckAmount),
                item.notDueCheckAmount, null,
                getString(R.string.rial)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                getString(R.string.notDueCheckCount),
                item.notDueCheckCount, EnumCheckType.NotDueCheck,
                getString(R.string.space)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                getString(R.string.guaranteeCheckAmount),
                item.guaranteeCheckAmount, null,
                getString(R.string.rial)
            )
        )
        detailList.add(
            CustomerFinancialItemModel(
                getString(R.string.guaranteeCheckCount),
                item.guaranteeCheckCount, EnumCheckType.GuaranteeCheck,
                getString(R.string.space)
            )
        )
        val click = object : CustomerFinancialDetailHolder.Click{
            override fun moreDetail(type: EnumCheckType?) {
                showMoreDialog(type)
            }
        }

        val adapter = CustomerFinancialAdapter(detailList, click)
        val manager = LinearLayoutManager(
            requireContext(),LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerViewItem.adapter = adapter
        binding.recyclerViewItem.layoutManager = manager
    }
    //---------------------------------------------------------------------------------------------- setCustomerFinancialModel


    //---------------------------------------------------------------------------------------------- showMoreDialog
    private fun showMoreDialog(type: EnumCheckType?){
        if (type == null)
            return
        CustomerHyperLinkDialog(viewModel.customerId, type).show(childFragmentManager, "dialog")
    }
    //---------------------------------------------------------------------------------------------- showMoreDialog

}