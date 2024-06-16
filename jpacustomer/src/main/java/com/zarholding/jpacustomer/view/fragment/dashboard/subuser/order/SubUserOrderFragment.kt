package com.zarholding.jpacustomer.view.fragment.dashboard.subuser.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.response.basket.subuser.SubUserOrderModel
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentSubuserOrderBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.adapter.recycler.SubCustomerOrderAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SubUserOrderFragment(override var layout: Int = R.layout.fragment_subuser_order) :
    JpaFragment<FragmentSubuserOrderBinding>() {

    private val viewModel: SubUserOrderViewModel by viewModels()
    private var itemSelectPosition: Int = 0

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        binding.shimmerViewContainer.stopLoading()
        activity?.let { (it as MainActivity).showMessage(message) }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        binding.shimmerViewContainer.config(getShimmerBuild())
        observeLiveDate()
        getOrders()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            binding.recyclerViewOrder.adapter?.notifyItemChanged(itemSelectPosition)
            showMessage(it.message)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }

        viewModel.subsetBasketLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            setAdapter(items = it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate



    //---------------------------------------------------------------------------------------------- getOrders
    private fun getOrders() {
        binding.shimmerViewContainer.startLoading()
        viewModel.requestSubsetBasket()
    }
    //---------------------------------------------------------------------------------------------- getOrders



    //---------------------------------------------------------------------------------------------- setAdapter
    private fun setAdapter(items: List<SubUserOrderModel>) {
        if (context == null)
            return
        val adapter = SubCustomerOrderAdapter(items = items) {state, item , position->
            itemSelectPosition = position
            viewModel.requestSubmitSubUserBasket(id = item.id, state = state)
        }
        val manager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerViewOrder.adapter = adapter
        binding.recyclerViewOrder.layoutManager = manager
    }
    //---------------------------------------------------------------------------------------------- setAdapter
}