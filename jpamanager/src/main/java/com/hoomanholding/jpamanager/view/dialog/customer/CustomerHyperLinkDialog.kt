package com.hoomanholding.jpamanager.view.dialog.customer

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.enums.EnumCheckType
import com.hoomanholding.applibrary.model.data.response.customer.CustomerFinancialDetailModel
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.DialogHyperlinkBinding
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.hoomanholding.jpamanager.view.adapter.recycler.CustomerHyperLinkAdapter
import com.hoomanholding.jpamanager.view.fragment.customer.CustomerFinancialViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * create by m-latifi on 4/18/2023
 */

@AndroidEntryPoint
class CustomerHyperLinkDialog(
    private val customerId: Int,
    private val type: EnumCheckType
) : DialogFragment() {

    lateinit var binding: DialogHyperlinkBinding
    private val viewModel: CustomerFinancialViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogHyperlinkBinding.inflate(inflater, container, false)
        return binding.root
    }
    //---------------------------------------------------------------------------------------------- onCreateView


    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmerViewContainer.config(getShimmerBuild())
        (activity as MainActivity?)?.hideFragmentContainer()
        viewModel.customerId = customerId
        val lp = WindowManager.LayoutParams()
        val window = dialog?.window
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 50)
        window?.setBackgroundDrawable(inset)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = lp
        initView()
    }
    //---------------------------------------------------------------------------------------------- onCreateView


    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        setListener()
        observeUserMutableLiveData()
        when (type) {
            EnumCheckType.NotRegisteredCheck ->
                binding.textviewTitle.text = getString(R.string.notRegisteredCheck)
            EnumCheckType.BouncedCheck ->
                binding.textviewTitle.text = getString(R.string.bouncedCheck)
            EnumCheckType.GuaranteeCheck ->
                binding.textviewTitle.text = getString(R.string.guaranteeCheck)
            EnumCheckType.PayedCheck ->
                binding.textviewTitle.text = getString(R.string.payedCheck)
            EnumCheckType.NotDueCheck ->
                binding.textviewTitle.text = getString(R.string.notDueCheck)
            EnumCheckType.OrderDetails ->
                getString(R.string.netSales)

        }
        getCustomerFinancialDetail()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        binding.shimmerViewContainer.stopShimmer()
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.imageViewClose.setOnClickListener {
            dismiss()
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- observeUserMutableLiveData
    private fun observeUserMutableLiveData() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }

        viewModel.customerFinancialDetailLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            setAdapter(it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeUserMutableLiveData


    //---------------------------------------------------------------------------------------------- getCustomerFinancialDetail
    private fun getCustomerFinancialDetail() {
        binding.shimmerViewContainer.startLoading()
        viewModel.requestGetCustomerFinancialDetail(type)
    }
    //---------------------------------------------------------------------------------------------- getCustomerFinancialDetail


    //---------------------------------------------------------------------------------------------- setAdapter
    private fun setAdapter(it: List<CustomerFinancialDetailModel>) {
        val adapter = CustomerHyperLinkAdapter(it, type)
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerItem.adapter = adapter
        binding.recyclerItem.layoutManager = manager
    }
    //---------------------------------------------------------------------------------------------- setAdapter


    //---------------------------------------------------------------------------------------------- onDismiss
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as MainActivity?)?.showFragmentContainer()
    }
    //---------------------------------------------------------------------------------------------- onDismiss

}