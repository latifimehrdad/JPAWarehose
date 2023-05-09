package com.zarholding.jpacustomer.view.fragment.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.model.data.enums.EnumOrderState
import com.hoomanholding.applibrary.model.data.response.order.CustomerOrderModel
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import dagger.hilt.android.AndroidEntryPoint
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentHomeBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.adapter.IntSpinnerAdapter
import com.zarholding.jpacustomer.view.adapter.holder.OrderHolder
import com.zarholding.jpacustomer.view.adapter.recycler.OrderAdapter


/**
 * Created by m-latifi on 11/8/2022.
 */


@AndroidEntryPoint
class HomeFragment(override var layout: Int = R.layout.fragment_home) :
    JpaFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()
    private var adapter: OrderAdapter? = null

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        activity?.let { (it as MainActivity).showMessage(message) }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        observeLiveDate()
        resetSteps()
        setListener()
        (activity as MainActivity?)?.getUserInfo()
        viewModel.getCustomerOrders()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }

        viewModel.orderLiveData.observe(viewLifecycleOwner) {
            resetSteps()
            setOrderAdapter(it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.constraintLayoutStep.setOnClickListener {
            binding.spinnerChooseChart.dismiss()
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- resetSteps
    private fun resetSteps() {
        binding.stepNone.clearSelected()
        binding.stepRejected.clearSelected()
        binding.stepConfirmed.clearSelected()
        binding.stepPacking.clearSelected()
        binding.stepBilling.clearSelected()
        binding.stepLoading.clearSelected()
        binding.stepSent.clearSelected()
    }
    //---------------------------------------------------------------------------------------------- resetSteps


    //---------------------------------------------------------------------------------------------- setOrderAdapter
    private fun setOrderAdapter(items: List<CustomerOrderModel>) {
        if (items.isEmpty())
            return
        setOrderNumberSpinner(items)
        selectOrder(0, items[0])
        if (context == null)
            return
        val click = object : OrderHolder.Click{
            override fun click(position: Int, item: CustomerOrderModel) {
                selectOrder(position, item)
            }
            override fun clickDetail(item: CustomerOrderModel) {

            }
        }
        adapter = OrderAdapter(items, click)
        OrderAdapter.selectedPosition = 0
        val manager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        manager.reverseLayout = true
        binding.recyclerViewOldOrder.layoutManager = manager
        binding.recyclerViewOldOrder.adapter = adapter
    }
    //---------------------------------------------------------------------------------------------- setOrderAdapter


    //---------------------------------------------------------------------------------------------- selectOrder
    private fun selectOrder(position: Int, item: CustomerOrderModel) {
        resetSteps()
        setStep(item.orderStates)
        OrderAdapter.selectedPosition = position
        adapter?.notifyItemRangeChanged(0, adapter?.itemCount?:0)
    }
    //---------------------------------------------------------------------------------------------- selectOrder


    //---------------------------------------------------------------------------------------------- setStep
    private fun setStep(state: EnumOrderState) {
        binding.stepRejected.visibility = View.GONE
        binding.stepConfirmed.visibility = View.GONE
        when (state) {
            EnumOrderState.None -> {
                binding.textViewStep.text = getString(R.string.none)
                binding.stepConfirmed.visibility = View.VISIBLE
                binding.stepNone.selected()
            }
            EnumOrderState.Rejected -> {
                binding.textViewStep.text = getString(R.string.rejected)
                binding.stepRejected.visibility = View.VISIBLE
                binding.stepNone.selected()
                binding.stepRejected.selected()
            }
            EnumOrderState.Confirmed -> {
                binding.textViewStep.text = getString(R.string.confirmed)
                binding.stepConfirmed.visibility = View.VISIBLE
                binding.stepNone.selected()
                binding.stepConfirmed.selected()
            }
            EnumOrderState.Packing -> {
                binding.textViewStep.text = getString(R.string.packing)
                binding.stepConfirmed.visibility = View.VISIBLE
                binding.stepNone.selected()
                binding.stepConfirmed.selected()
                binding.stepPacking.selected()
            }
            EnumOrderState.Billing -> {
                binding.textViewStep.text = getString(R.string.billing)
                binding.stepConfirmed.visibility = View.VISIBLE
                binding.stepNone.selected()
                binding.stepConfirmed.selected()
                binding.stepPacking.selected()
                binding.stepBilling.selected()
            }
            EnumOrderState.Loading -> {
                binding.textViewStep.text = getString(R.string.loading)
                binding.stepConfirmed.visibility = View.VISIBLE
                binding.stepNone.selected()
                binding.stepConfirmed.selected()
                binding.stepPacking.selected()
                binding.stepBilling.selected()
                binding.stepLoading.selected()
            }
            EnumOrderState.Sent -> {
                binding.textViewStep.text = getString(R.string.sent)
                binding.stepConfirmed.visibility = View.VISIBLE
                binding.stepNone.selected()
                binding.stepConfirmed.selected()
                binding.stepPacking.selected()
                binding.stepBilling.selected()
                binding.stepLoading.selected()
                binding.stepSent.selected()
            }
        }
    }
    //---------------------------------------------------------------------------------------------- setStep


    //---------------------------------------------------------------------------------------------- setOrderNumberSpinner
    private fun setOrderNumberSpinner(items: List<CustomerOrderModel>) {
        val orderNumbers = items.map { it.orderNumber }
        binding.spinnerChooseChart.apply {
            setSpinnerAdapter(IntSpinnerAdapter(this))
            setItems(orderNumbers)
            getSpinnerRecyclerView().layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setOnSpinnerItemSelectedListener<Int> { _, _, newIndex, _ ->
                selectOrder(newIndex, items[newIndex])
            }
        }
    }
    //---------------------------------------------------------------------------------------------- setOrderNumberSpinner

}