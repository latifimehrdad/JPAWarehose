package com.hoomanholding.jpamanager.view.fragment.invoice

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.model.data.response.customer.CustomerModel
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentInvoiceBinding
import com.hoomanholding.applibrary.model.data.response.order.OrderModel
import com.hoomanholding.applibrary.model.data.response.visitor.VisitorModel
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.hoomanholding.jpamanager.view.adapter.holder.OrderHolder
import com.hoomanholding.jpamanager.view.adapter.recycler.OrderAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpamanager.model.data.other.DateFilterModel


/**
 * create by m-latifi on 3/12/2023
 */

@AndroidEntryPoint
class InvoiceFragment(override var layout: Int = R.layout.fragment_invoice) :
    JpaFragment<FragmentInvoiceBinding>() {

    private val viewModel: InvoiceViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmerViewContainer.setShimmer(getShimmerBuild())
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
        resetDateFilter()
        resetVisitorFilter()
        resetCustomerFilter()
        setListener()
        observeLiveData()
        getOrder()
    }
    //---------------------------------------------------------------------------------------------- initView



    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.linearLayoutVisitor.setOnClickListener {
            if (viewModel.filterVisitorLiveData.value == null){
                val model = VisitorModel(0,0,"name")
                viewModel.setVisitorForFilter(model)
            } else viewModel.setVisitorForFilter(null)
        }

        binding.linearLayoutDate.setOnClickListener {
            if (viewModel.filterDateLiveData.value == null){
                val model = DateFilterModel("start","end")
                viewModel.setDateForFilter(model)
            } else viewModel.setDateForFilter(null)
        }

        binding.linearLayoutCustomer.setOnClickListener {
            if (viewModel.filterCustomerLiveData.value == null){
                val model = CustomerModel(0,0,"","",0,"","","",0,"")
                viewModel.setCustomerForFilter(model)
            } else viewModel.setCustomerForFilter(null)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- observeLiveData
    private fun observeLiveData() {
        viewModel.filterCustomerLiveData.observe(viewLifecycleOwner) {
            it?.let { selectCustomerFilter() } ?: run { resetCustomerFilter() }
        }

        viewModel.filterDateLiveData.observe(viewLifecycleOwner) {
            it?.let { selectDateFilter() } ?: run { resetDateFilter() }
        }

        viewModel.filterVisitorLiveData.observe(viewLifecycleOwner) {
            it?.let { selectVisitorFilter() } ?: run { resetVisitorFilter() }
        }

        viewModel.errorLiveDate.observe(viewLifecycleOwner){
            showMessage(it.message)
        }

        viewModel.orderLiveData.observe(viewLifecycleOwner){
            binding.shimmerViewContainer.stopShimmer()
            setAdapter(it)
        }

        viewModel.detailOrderLiveData.observe(viewLifecycleOwner){
            showMessage("count of product is ${it.size}")
        }

        viewModel.visitorLiveData.observe(viewLifecycleOwner){
            showMessage("count of visitor is ${it.size}")
        }

        viewModel.disApprovalReasons.observe(viewLifecycleOwner){
            showMessage("count of visitor is ${it.size}")
        }

        viewModel.customerLiveData.observe(viewLifecycleOwner){
            showMessage("count of visitor is ${it.size} $it")
        }

        viewModel.customerFinancialDetailLiveData.observe(viewLifecycleOwner){
            showMessage("count of visitor is ${it.size} $it")
        }

        viewModel.customerFinancialLiveData.observe(viewLifecycleOwner){
            showMessage("count of visitor is $it")
        }

        viewModel.orderToggleStateLiveData.observe(viewLifecycleOwner){
            showMessage("count of visitor is $it")
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveData



    //---------------------------------------------------------------------------------------------- getOrder
    private fun getOrder() {
        binding.shimmerViewContainer.startShimmer()
        viewModel.requestGetOrder()
    }
    //---------------------------------------------------------------------------------------------- getOrder



    //---------------------------------------------------------------------------------------------- setAdapter
    private fun setAdapter(items: List<OrderModel>) {
        val detail = object : OrderHolder.Click {
            override fun orderDetail(item: OrderModel) {
                viewModel.requestOrderDetail(item.id)
            }
        }
        val adapter = OrderAdapter(items, detail)
        val manager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerItem.adapter = adapter
        binding.recyclerItem.layoutManager = manager
    }
    //---------------------------------------------------------------------------------------------- setAdapter


    //---------------------------------------------------------------------------------------------- resetFilterView
    private fun resetFilterView(layout: View, view: View, text: TextView, textFilter: TextView) {
        layout.background =
            AppCompatResources.getDrawable(requireContext(), R.drawable.tabs_selector)
        view.visibility = View.INVISIBLE
        text.setTextColor(requireContext().getColor(android.R.color.white))
        textFilter.setTextColor(requireContext().getColor(android.R.color.white))
    }
    //---------------------------------------------------------------------------------------------- resetFilterView



    //---------------------------------------------------------------------------------------------- selectFilterView
    private fun selectFilterView(layout: View, view: View, text: TextView, textFilter: TextView) {
        layout.background =
            AppCompatResources.getDrawable(requireContext(), R.drawable.drawable_tab_selected)
        view.visibility = View.VISIBLE
        text.setTextColor(requireContext().getColor(android.R.color.black))
        textFilter.setTextColor(requireContext().getColor(android.R.color.black))
    }
    //---------------------------------------------------------------------------------------------- selectFilterView



    //---------------------------------------------------------------------------------------------- resetVisitorFilter
    private fun resetVisitorFilter() {
        resetFilterView(
            binding.linearLayoutVisitor,
            binding.viewVisitor,
            binding.textViewVisitor,
            binding.textViewVisitorFilter
        )
    }
    //---------------------------------------------------------------------------------------------- resetVisitorFilter



    //---------------------------------------------------------------------------------------------- resetDateFilter
    private fun resetDateFilter() {
        resetFilterView(
            binding.linearLayoutDate,
            binding.viewDate,
            binding.textViewDate,
            binding.textViewDateFilter
        )
    }
    //---------------------------------------------------------------------------------------------- resetDateFilter



    //---------------------------------------------------------------------------------------------- resetCustomerFilter
    private fun resetCustomerFilter() {
        resetFilterView(
            binding.linearLayoutCustomer,
            binding.viewCustomer,
            binding.textViewCustomer,
            binding.textViewCustomerFilter
        )
    }
    //---------------------------------------------------------------------------------------------- resetCustomerFilter


    //---------------------------------------------------------------------------------------------- selectCustomerFilter
    private fun selectCustomerFilter() {
        selectFilterView(
            binding.linearLayoutCustomer,
            binding.viewCustomer,
            binding.textViewCustomer,
            binding.textViewCustomerFilter
        )
    }
    //---------------------------------------------------------------------------------------------- selectCustomerFilter


    //---------------------------------------------------------------------------------------------- selectDateFilter
    private fun selectDateFilter() {
        selectFilterView(
            binding.linearLayoutDate,
            binding.viewDate,
            binding.textViewDate,
            binding.textViewDateFilter
        )
    }
    //---------------------------------------------------------------------------------------------- selectDateFilter


    //---------------------------------------------------------------------------------------------- selectVisitorFilter
    private fun selectVisitorFilter() {
        selectFilterView(
            binding.linearLayoutVisitor,
            binding.viewVisitor,
            binding.textViewVisitor,
            binding.textViewVisitorFilter
        )
    }
    //---------------------------------------------------------------------------------------------- selectVisitorFilter

}