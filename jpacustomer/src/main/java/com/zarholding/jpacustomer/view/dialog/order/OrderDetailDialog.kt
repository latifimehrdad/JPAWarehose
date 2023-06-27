package com.zarholding.jpacustomer.view.dialog.order

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
import com.hoomanholding.applibrary.ext.setAmount
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.response.order.CustomerOrderDetailModel
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.zar.core.enums.EnumApiError
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.DialogOrderDetailBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.adapter.recycler.OrderDetailAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by m-latifi on 5/27/2023.
 */

@AndroidEntryPoint
class OrderDetailDialog(
    private val orderId: Long,
    private val orderAmount: Long
) : DialogFragment() {


    private val viewModel: OrderDetailViewModel by viewModels()
    private lateinit var binding: DialogOrderDetailBinding

    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    //---------------------------------------------------------------------------------------------- onCreateView


    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lp = WindowManager.LayoutParams()
        val window = dialog?.window
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 50)
        window?.setBackgroundDrawable(inset)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes = lp
        (activity as MainActivity?)?.hideFragmentContainer()
        binding.shimmerViewContainer.config(getShimmerBuild())
        observeLiveDate()
        getOrderDetail()
        setListener()
    }
    //---------------------------------------------------------------------------------------------- onCreateView



    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        activity?.let { (it as MainActivity).showMessage(message) }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.imageViewClose.setOnClickListener { dismiss() }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
            dismiss()
        }

        viewModel.orderDetailLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            setAdapter(it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate



    //---------------------------------------------------------------------------------------------- getOrderDetail
    private fun getOrderDetail() {
        binding.shimmerViewContainer.startLoading()
        viewModel.getCustomerOrderDetail(orderId)
    }
    //---------------------------------------------------------------------------------------------- getOrderDetail



    //---------------------------------------------------------------------------------------------- setAdapter
    private fun setAdapter(items: List<CustomerOrderDetailModel>) {
        if (context == null || items.isEmpty())
            return
        val first = items.first()
        binding.textViewNumber.setTitleAndValue(
            title = getString(R.string.orderNumber),
            splitter = getString(R.string.colon),
            value = first.billingNumber
        )
        binding.textViewDate.setTitleAndValue(
            title = getString(R.string.date),
            splitter = getString(R.string.colon),
            value = first.billingDate
        )
        binding.textviewTotal.setAmount(
            value = orderAmount,
            getString(R.string.rial)
        )
        val adapter = OrderDetailAdapter(items)
        val manager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerViewOrder.layoutManager = manager
        binding.recyclerViewOrder.adapter = adapter
    }
    //---------------------------------------------------------------------------------------------- setAdapter



    //---------------------------------------------------------------------------------------------- onDismiss
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.let {
            (it as MainActivity).showFragmentContainer()
        }
    }
    //---------------------------------------------------------------------------------------------- onDismiss

}