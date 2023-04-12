package com.hoomanholding.jpamanager.view.fragment.invoice

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.enums.EnumPeopleType
import com.hoomanholding.applibrary.model.data.enums.EnumState
import com.hoomanholding.applibrary.model.data.response.customer.CustomerModel
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentInvoiceBinding
import com.hoomanholding.applibrary.model.data.response.order.OrderModel
import com.hoomanholding.applibrary.model.data.response.visitor.VisitorModel
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.hoomanholding.jpamanager.view.adapter.holder.OrderHolder
import com.hoomanholding.jpamanager.view.adapter.recycler.OrderAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpamanager.model.data.other.DateFilterModel
import com.hoomanholding.jpamanager.view.dialog.ConfirmOrderDialog
import com.hoomanholding.jpamanager.view.dialog.customer.PeopleDialog
import com.zar.core.view.picker.date.customviews.DateRangeCalendarView
import com.zar.core.view.picker.date.dialog.DatePickerDialog


/**
 * create by m-latifi on 3/12/2023
 */

@AndroidEntryPoint
class InvoiceFragment(override var layout: Int = R.layout.fragment_invoice) :
    JpaFragment<FragmentInvoiceBinding>() {

    private val viewModel: InvoiceViewModel by viewModels()

    private var adapter: OrderAdapter? = null

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmerViewContainer.config(getShimmerBuild())
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
        binding.buttonConfirmFactor.visibility = View.GONE
        binding.buttonRejectFactor.visibility = View.GONE
        resetDateFilter()
        resetVisitorFilter()
        resetCustomerFilter()
        setListener()
        observeLiveData()
        getOrder()
        viewModel.requestDisApprovalReasons()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.linearLayoutVisitor.setOnClickListener {
            if (viewModel.filterVisitorLiveData.value == null) {
                showPeopleDialog(EnumPeopleType.Visitor)
            } else viewModel.setVisitorForFilter(null)
        }

        binding.linearLayoutDate.setOnClickListener {
            if (viewModel.filterDateLiveData.value == null) {
                showDatePickerDialog()
            } else viewModel.setDateForFilter(null)
        }

        binding.linearLayoutCustomer.setOnClickListener {
            if (viewModel.filterCustomerLiveData.value == null) {
                showPeopleDialog(EnumPeopleType.Customer)
            } else viewModel.setCustomerForFilter(null)
        }

        binding.buttonConfirmFactor.setOnClickListener {
            showDialogConformToChangeStatusOrders(EnumState.Confirmed)
        }

        binding.buttonRejectFactor.setOnClickListener {
            showDialogConformToChangeStatusOrders(EnumState.Reject)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- observeLiveData
    private fun observeLiveData() {
        viewModel.filterCustomerLiveData.observe(viewLifecycleOwner) {
            it?.let { selectCustomerFilter(it) } ?: run { resetCustomerFilter() }
        }

        viewModel.filterDateLiveData.observe(viewLifecycleOwner) {
            it?.let { selectDateFilter(it) } ?: run { resetDateFilter() }
        }

        viewModel.filterVisitorLiveData.observe(viewLifecycleOwner) {
            it?.let { selectVisitorFilter(it) } ?: run { resetVisitorFilter() }
        }

        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }

        viewModel.orderLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            setAdapter(it)
        }

        viewModel.orderToggleStateLiveData.observe(viewLifecycleOwner){
            getOrder()
        }

        viewModel.customerFinancialDetailLiveData.observe(viewLifecycleOwner) {
            showMessage("count of visitor is ${it.size} $it")
        }

        viewModel.customerFinancialLiveData.observe(viewLifecycleOwner) {
            showMessage("count of visitor is $it")
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveData


    //---------------------------------------------------------------------------------------------- showPeopleDialog
    private fun showPeopleDialog(peopleType: EnumPeopleType) {
        val click = object : PeopleDialog.Click {
            override fun selectCustomer(item: CustomerModel) {
                binding.recyclerItem.adapter = null
                viewModel.setCustomerForFilter(item)
            }

            override fun selectVisitor(item: VisitorModel) {
                binding.recyclerItem.adapter = null
                viewModel.setVisitorForFilter(item)
            }
        }
        PeopleDialog(peopleType, click).show(childFragmentManager, "people dialog")
    }
    //---------------------------------------------------------------------------------------------- showPeopleDialog


    //---------------------------------------------------------------------------------------------- getOrder
    private fun getOrder() {
        adapter = null
        binding.recyclerItem.adapter = null
        binding.buttonConfirmFactor.visibility = View.GONE
        binding.buttonRejectFactor.visibility = View.GONE
        binding.shimmerViewContainer.startShimmer()
        viewModel.requestGetOrder()
    }
    //---------------------------------------------------------------------------------------------- getOrder


    //---------------------------------------------------------------------------------------------- setAdapter
    private fun setAdapter(items: List<OrderModel>) {
        val detail = object : OrderHolder.Click {
            override fun orderDetail(item: OrderModel) {
                val bundle = Bundle()
                bundle.putParcelable(CompanionValues.SHARE_MODEL, item)
                findNavController()
                    .navigate(R.id.action_InvoiceFragment_to_InvoiceFragmentDetail, bundle)
            }
        }
        adapter = OrderAdapter(items, detail)
        val manager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerItem.adapter = adapter
        binding.recyclerItem.layoutManager = manager
        if (items.isNotEmpty()) {
            binding.buttonConfirmFactor.visibility = View.VISIBLE
            binding.buttonRejectFactor.visibility = View.VISIBLE
        }
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


    //---------------------------------------------------------------------------------------------- showDatePickerDialog
    private fun showDatePickerDialog() {
        if (context == null)
            return
        val datePickerDialog = DatePickerDialog(requireContext())
        datePickerDialog.selectionMode = DateRangeCalendarView.SelectionMode.Range
        datePickerDialog.isDisableDaysAgo = false
        datePickerDialog.acceptButtonColor =
            resources.getColor(R.color.datePickerConfirmButtonBackColor, requireContext().theme)
        datePickerDialog.headerBackgroundColor =
            resources.getColor(R.color.datePickerConfirmButtonBackColor, requireContext().theme)
        datePickerDialog.headerTextColor =
            resources.getColor(R.color.white, requireContext().theme)
        datePickerDialog.weekColor =
            resources.getColor(R.color.dismiss, requireContext().theme)
        datePickerDialog.disableDateColor =
            resources.getColor(R.color.dismiss, requireContext().theme)
        datePickerDialog.defaultDateColor =
            resources.getColor(R.color.datePickerDateBackColor, requireContext().theme)
        datePickerDialog.selectedDateCircleColor =
            resources.getColor(R.color.datePickerConfirmButtonBackColor, requireContext().theme)
        datePickerDialog.selectedDateColor =
            resources.getColor(R.color.white, requireContext().theme)
        datePickerDialog.rangeDateColor =
            resources.getColor(R.color.datePickerConfirmButtonBackColor, requireContext().theme)
        datePickerDialog.rangeStripColor =
            resources.getColor(R.color.datePickerRangeColor, requireContext().theme)
        datePickerDialog.holidayColor =
            resources.getColor(R.color.rejectFactorText, requireContext().theme)
        datePickerDialog.textSizeWeek = 12.0f
        datePickerDialog.textSizeDate = 14.0f
        datePickerDialog.textSizeTitle = 18.0f
        datePickerDialog.setCanceledOnTouchOutside(true)
        datePickerDialog.onSingleDateSelectedListener =
            DatePickerDialog.OnSingleDateSelectedListener { }
        datePickerDialog.onRangeDateSelectedListener =
            DatePickerDialog.OnRangeDateSelectedListener { startDate, endDate ->
                val dateModel =
                    DateFilterModel(startDate.persianShortDate, endDate.persianShortDate)
                viewModel.setDateForFilter(dateModel)
            }

        datePickerDialog.showDialog()

    }
    //---------------------------------------------------------------------------------------------- showDatePickerDialog


    //---------------------------------------------------------------------------------------------- resetVisitorFilter
    private fun resetVisitorFilter() {
        resetFilterView(
            binding.linearLayoutVisitor,
            binding.viewVisitor,
            binding.textViewVisitor,
            binding.textViewVisitorFilter
        )
        binding.textViewVisitor.text = getString(R.string.visitorName)
        binding.textViewVisitor.isSelected = false
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
        binding.textViewDate.text = getString(R.string.startEndDate)
        binding.textViewDate.isSelected = false
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
        binding.textViewCustomer.text = getString(R.string.customerName)
        binding.textViewCustomer.isSelected = false
    }
    //---------------------------------------------------------------------------------------------- resetCustomerFilter


    //---------------------------------------------------------------------------------------------- selectCustomerFilter
    private fun selectCustomerFilter(customerModel: CustomerModel) {
        selectFilterView(
            binding.linearLayoutCustomer,
            binding.viewCustomer,
            binding.textViewCustomer,
            binding.textViewCustomerFilter
        )
        binding.textViewCustomer.text = customerModel.customerName
        binding.textViewCustomer.isSelected = true
    }
    //---------------------------------------------------------------------------------------------- selectCustomerFilter


    //---------------------------------------------------------------------------------------------- selectDateFilter
    private fun selectDateFilter(dateFilterModel: DateFilterModel) {
        selectFilterView(
            binding.linearLayoutDate,
            binding.viewDate,
            binding.textViewDate,
            binding.textViewDateFilter
        )
        binding.textViewDate.text =
            getString(R.string.setDateFrom, dateFilterModel.startDate, dateFilterModel.endDate)
        binding.textViewDate.isSelected = true
    }
    //---------------------------------------------------------------------------------------------- selectDateFilter


    //---------------------------------------------------------------------------------------------- selectVisitorFilter
    private fun selectVisitorFilter(visitorModel: VisitorModel) {
        selectFilterView(
            binding.linearLayoutVisitor,
            binding.viewVisitor,
            binding.textViewVisitor,
            binding.textViewVisitorFilter
        )
        binding.textViewVisitor.text = visitorModel.visitorName
        binding.textViewVisitor.isSelected = true
    }
    //---------------------------------------------------------------------------------------------- selectVisitorFilter


    //---------------------------------------------------------------------------------------------- showDialogConformToChangeStatusOrders
    private fun showDialogConformToChangeStatusOrders(state: EnumState) {
        if (viewModel.disApprovalReasonModel.isNullOrEmpty())
            return
        if (context == null)
            return

        val click = object : ConfirmOrderDialog.Click{
            override fun clickYes(position: Int, description: String) {
                viewModel.requestOrderToggleState(position, description, state)
            }
        }

        val confirm = ConfirmOrderDialog(
            requireContext(),
            click,
            viewModel.disApprovalReasonModel,
            state
        )
        confirm.show()
    }
    //---------------------------------------------------------------------------------------------- showDialogConformToChangeStatusOrders



}