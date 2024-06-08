package com.zarholding.jpacustomer.view.adapter.holder

import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.enums.EnumOrderState
import com.hoomanholding.applibrary.model.data.response.report.ReportCustomerOrderDetailModel
import com.hoomanholding.applibrary.model.data.response.report.ReportCustomerOrderModel
import com.zar.core.tools.extensions.split
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ItemReportCustomerOrderBinding
import com.zarholding.jpacustomer.view.adapter.recycler.ReportCustomerOrderDetailAdapter

/**
 * Created by m-latifi on 6/8/2023.
 */

class ReportCustomerOrderHolder(
    private val binding: ItemReportCustomerOrderBinding
) : RecyclerView.ViewHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: ReportCustomerOrderModel) {
        setValueToXml(item)
        setListener(item)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: ReportCustomerOrderModel) {
        binding.textViewOrderNumber.setTitleAndValue(
            title = binding.textViewOrderNumber.context.getString(R.string.orderNumber),
            splitter = binding.textViewOrderNumber.context.getString(R.string.colon),
            value = item.orderNumber
        )
        binding.textViewDate.setTitleAndValue(
            title = binding.textViewDate.context.getString(R.string.orderDate),
            splitter = binding.textViewDate.context.getString(R.string.colon),
            value = item.orderDate
        )
        binding.textViewCustomerCode.setTitleAndValue(
            title = binding.textViewDate.context.getString(R.string.customerCode),
            splitter = binding.textViewDate.context.getString(R.string.colon),
            value = item.customerCode
        )
        binding.textViewStoreName.setTitleAndValue(
            title = binding.textViewDate.context.getString(R.string.shopName),
            splitter = binding.textViewDate.context.getString(R.string.colon),
            value = item.storeName
        )
        binding.textViewCustomerName.setTitleAndValue(
            title = binding.textViewDate.context.getString(R.string.buyerName),
            splitter = binding.textViewDate.context.getString(R.string.colon),
            value = item.customerName
        )

        binding.textViewAmount.text = item.orderAmount.split()

        val state = when (item.orderStates) {
            EnumOrderState.None -> binding.textViewState.context.getString(R.string.none)
            EnumOrderState.Rejected -> binding.textViewState.context.getString(R.string.rejected)
            EnumOrderState.Confirmed -> binding.textViewState.context.getString(R.string.confirmed)
            EnumOrderState.Packing -> binding.textViewState.context.getString(R.string.packing)
            EnumOrderState.Billing -> binding.textViewState.context.getString(R.string.billing)
            EnumOrderState.Loading -> binding.textViewState.context.getString(R.string.loading)
            EnumOrderState.Sent -> binding.textViewState.context.getString(R.string.sent)
        }

        binding.textViewState.setTitleAndValue(
            title = binding.textViewState.context.getString(R.string.status),
            splitter = binding.textViewState.context.getString(R.string.colon),
            value = state
        )

        if (binding.expandableMore.isExpanded)
            binding.expandableMore.collapse()
    }
    //---------------------------------------------------------------------------------------------- setValueToXml


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener(item: ReportCustomerOrderModel) {
        binding.imageViewMore.setOnClickListener { clickShowMore(item) }
        binding.textViewShowMore.setOnClickListener { clickShowMore(item) }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- clickShowMore
    private fun clickShowMore(item: ReportCustomerOrderModel) {
        if (binding.expandableMore.isExpanded) {
            binding.recyclerDetail.adapter = null
            binding.expandableMore.collapse()
            hideMore()
        } else {
            item.items?.let {
                setDetailAdapter(it)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- clickShowMore


    //---------------------------------------------------------------------------------------------- setDetailAdapter
    private fun setDetailAdapter(items: List<ReportCustomerOrderDetailModel>) {
        val adapter = ReportCustomerOrderDetailAdapter(items)
        val manager = LinearLayoutManager(
            binding.recyclerDetail.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerDetail.adapter = adapter
        binding.recyclerDetail.layoutManager = manager
        binding.expandableMore.expand()
        showMore()
    }
    //---------------------------------------------------------------------------------------------- setDetailAdapter


    //---------------------------------------------------------------------------------------------- showMore
    private fun showMore() {
        binding.expandableMore.expand()

        val rotate = RotateAnimation(
            0f,
            -90f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 350
        rotate.interpolator = LinearInterpolator()
        rotate.fillAfter = true
        binding.imageViewMore.startAnimation(rotate)
    }
    //---------------------------------------------------------------------------------------------- showMore


    //---------------------------------------------------------------------------------------------- hideMore
    private fun hideMore() {
        binding.expandableMore.collapse()
        val rotate = RotateAnimation(
            -90f,
            0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 400
        rotate.interpolator = LinearInterpolator()
        rotate.fillAfter = true
        binding.imageViewMore.startAnimation(rotate)
    }
    //---------------------------------------------------------------------------------------------- hideMore

}