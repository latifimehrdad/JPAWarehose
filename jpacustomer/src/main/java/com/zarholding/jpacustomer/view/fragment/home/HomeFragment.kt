package com.zarholding.jpacustomer.view.fragment.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.enums.EnumOrderState
import com.hoomanholding.applibrary.model.data.response.order.CustomerOrderModel
import com.hoomanholding.applibrary.tools.RoleManager
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import dagger.hilt.android.AndroidEntryPoint
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentHomeBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.adapter.IntSpinnerAdapter
import com.zarholding.jpacustomer.view.adapter.holder.OrderHolder
import com.zarholding.jpacustomer.view.adapter.recycler.OrderAdapter
import com.zarholding.jpacustomer.view.dialog.ConfirmDialog
import com.zarholding.jpacustomer.view.dialog.order.OrderDetailDialog
import javax.inject.Inject


/**
 * Created by m-latifi on 11/8/2022.
 */


@AndroidEntryPoint
class HomeFragment(override var layout: Int = R.layout.fragment_home) :
    JpaFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var roleManager: RoleManager

    private val viewModel: HomeViewModel by viewModels()
    private var adapter: OrderAdapter? = null


    //---------------------------------------------------------------------------------------------- OnBackPressedCallback
    private val backClick = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            context?.let {
                ConfirmDialog(
                    it,
                    getString(R.string.doYouWantToExitApp)){ activity?.finish() }.show()
            }
        }
    }
    //---------------------------------------------------------------------------------------------- OnBackPressedCallback


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

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backClick)
        binding.shimmerViewContainer.config(getShimmerBuild())
        binding.swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
        checkPermissions()
        observeLiveDate()
        resetSteps()
        setListener()
        (activity as MainActivity?)?.getUserInfo()
        viewModel.checkVersionIsNew()
        getCustomerOrders()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- checkPermissions
    private fun checkPermissions() {
        if (roleManager.isAccessToDetailOfOrderInHome())
            binding.buttonShowOrderDetail.visibility = View.VISIBLE
        else
            binding.buttonShowOrderDetail.visibility = View.INVISIBLE
    }
    //---------------------------------------------------------------------------------------------- checkPermissions


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            binding.swipeContainer.isRefreshing = false
            showMessage(it.message)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }

        viewModel.orderLiveData.observe(viewLifecycleOwner) {
            binding.swipeContainer.isRefreshing = false
            binding.shimmerViewContainer.stopLoading()
            resetSteps()
            setOrderAdapter(it)
        }

        viewModel.basketCountLiveData.observe(viewLifecycleOwner) {
            (activity as MainActivity?)?.setCartBadge(it)
        }

        viewModel.newVersionLiveData.observe(viewLifecycleOwner) {
            showNewFeatureDialog(it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- getCustomerOrders
    private fun getCustomerOrders(checkEmptyOrder: Boolean = true) {
        binding.shimmerViewContainer.startLoading()
        viewModel.getCustomerOrders(checkEmptyOrder)
    }
    //---------------------------------------------------------------------------------------------- getCustomerOrders


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.constraintLayoutPrimary.setOnClickListener {
            binding.spinnerChooseChart.dismiss()
        }
        binding.constraintLayoutStep.setOnClickListener {
            binding.spinnerChooseChart.dismiss()
        }
        binding.swipeContainer.setOnRefreshListener { getCustomerOrders(false) }

        binding.buttonShowOrderDetail.setOnClickListener {
            viewModel.selectedOrder?.let {
                OrderDetailDialog(it.orderId, it.finalAmount).show(childFragmentManager, "order")
            }
        }

        binding.cardViewPriceInquiry.setOnClickListener {
            gotoFragment(R.id.action_homeFragment_to_productFragment)
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
        if (items.isEmpty()) {
            binding.constraintLayoutStep.visibility = View.INVISIBLE
            return
        }
        binding.constraintLayoutStep.visibility = View.VISIBLE
        setOrderNumberSpinner(items)
        selectOrder(0, items[0])
        if (context == null)
            return
        val click = object : OrderHolder.Click {
            override fun click(position: Int, item: CustomerOrderModel) {
                selectOrder(position, item)
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
        viewModel.selectedOrder = item
        setStep(item.orderStates)
        OrderAdapter.selectedPosition = position
        binding.spinnerChooseChart.selectItemByIndex(position)
        adapter?.notifyItemRangeChanged(0, adapter?.itemCount ?: 0)
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
            setOnSpinnerItemSelectedListener<Int> { oldIndex, _, newIndex, _ ->
                if (oldIndex != newIndex)
                    selectOrder(newIndex, items[newIndex])
            }
        }
    }
    //---------------------------------------------------------------------------------------------- setOrderNumberSpinner


    //---------------------------------------------------------------------------------------------- showNewFeatureDialog
    private fun showNewFeatureDialog(feature: String) {
        if (context == null)
            return
        ConfirmDialog(
            requireContext(),
            feature,
            true
        ){  }.show()
    }
    //---------------------------------------------------------------------------------------------- showNewFeatureDialog




    //---------------------------------------------------------------------------------------------- onDestroyView
    override fun onDestroyView() {
        super.onDestroyView()
        binding.spinnerChooseChart.dismiss()
    }
    //---------------------------------------------------------------------------------------------- onDestroyView
}