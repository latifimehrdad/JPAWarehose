package com.hoomanholding.jpamanager.view.fragment.invoice.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.response.order.DetailOrderModel
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentInvoiceDetailBinding
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.hoomanholding.jpamanager.view.adapter.recycler.DetailOrderAdapter
import dagger.hilt.android.AndroidEntryPoint


/**
 * create by m-latifi on 4/10/2023
 */

@AndroidEntryPoint
class InvoiceDetailFragment(override var layout: Int = R.layout.fragment_invoice_detail) :
    JpaFragment<FragmentInvoiceDetailBinding>() {

    private val viewModel: InvoiceDetailViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.constraintLayoutParent.visibility = View.GONE
        binding.shimmerViewContainer.config(getShimmerBuild())
        observeLiveData()
        getOrderModerFromBundle()
        setListener()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- getOrderModerFromBundle
    private fun getOrderModerFromBundle() {
        arguments?.let { bundle ->
            val orderId = bundle.getLong(CompanionValues.ORDER_IR,0)
            val customerId = bundle.getInt(CompanionValues.CUSTOMER_ID,0)
            if (customerId == 0 || orderId == 0L){
                activity?.onBackPressedDispatcher?.onBackPressed()
                return
            }
            viewModel.customerId = customerId
            viewModel.orderId = orderId
            binding.shimmerViewContainer.startLoading()
            viewModel.requestOrderDetail()
        } ?: run { activity?.onBackPressedDispatcher?.onBackPressed() }
    }
    //---------------------------------------------------------------------------------------------- getOrderModerFromBundle


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        binding.shimmerViewContainer.stopLoading()
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- observeLiveData
    private fun observeLiveData() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }

        viewModel.detailOrderLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            binding.constraintLayoutParent.visibility = View.VISIBLE
            setProductAdapter(it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveData


    //---------------------------------------------------------------------------------------------- setProductAdapter
    private fun setProductAdapter(products: List<DetailOrderModel>) {
        if (context == null)
            return
        val adapter = DetailOrderAdapter(products)
        val manager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerViewProduct.layoutManager = manager
        binding.recyclerViewProduct.adapter = adapter
    }
    //---------------------------------------------------------------------------------------------- setProductAdapter


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.buttonLogin.setOnClickListener {
            val customer = viewModel.customerId
            val bundle = Bundle()
            bundle.putInt(CompanionValues.CUSTOMER_ID, customer)
            gotoFragment(R.id.action_InvoiceFragmentDetail_to_CustomerFinancialFragment, bundle)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener
}