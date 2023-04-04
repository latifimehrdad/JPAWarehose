package com.hoomanholding.jpamanager.view.fragment.invoice

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentInvoiceBinding
import com.hoomanholding.applibrary.model.data.response.order.OrderModel
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.hoomanholding.jpamanager.view.adapter.holder.OrderHolder
import com.hoomanholding.jpamanager.view.adapter.recycler.OrderAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.hoomanholding.applibrary.view.fragment.JpaFragment


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
        observeLiveData()
        setListener()
        getOrder()
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



    //---------------------------------------------------------------------------------------------- observeLiveData
    private fun observeLiveData() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }

        viewModel.orderLiveData.observe(viewLifecycleOwner){
            binding.shimmerViewContainer.stopShimmer()
            setAdapter(it)
        }

        viewModel.detailOrderLiveData.observe(viewLifecycleOwner){
            showMessage("count of product is ${it.size}")
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveData




    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {

    }
    //---------------------------------------------------------------------------------------------- setListener



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


}