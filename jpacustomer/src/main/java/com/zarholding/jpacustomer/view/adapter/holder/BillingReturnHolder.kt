package com.zarholding.jpacustomer.view.adapter.holder

import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.response.report.BillingAndReturnDetailReportModel
import com.hoomanholding.applibrary.model.data.response.report.BillingAndReturnReportModel
import com.zar.core.tools.extensions.split
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ItemReportCustomerBillingReturnBinding
import com.zarholding.jpacustomer.view.adapter.recycler.BillingReturnDetailAdapter

/**
 * Created by m-latifi on 6/8/2023.
 */

class BillingReturnHolder(
    private val binding: ItemReportCustomerBillingReturnBinding,
    private val click: Click
) : RecyclerView.ViewHolder(binding.root) {

    interface Click {
        fun detailPdf(id: Long, textView: TextView)
    }

    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: BillingAndReturnReportModel) {
        setValueToXml(item)
        setListener(item)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: BillingAndReturnReportModel) {
        binding.textViewFinalPrice.text = item.finalAmount.split()
        binding.textViewOrderNumber.setTitleAndValue(
            title = binding.textViewOrderNumber.context.getString(R.string.orderNumber),
            splitter = binding.textViewOrderNumber.context.getString(R.string.colon),
            value = item.billingNumber
        )
        binding.textViewDate.setTitleAndValue(
            title = binding.textViewDate.context.getString(R.string.orderDate),
            splitter = binding.textViewDate.context.getString(R.string.colon),
            value = item.billingDate
        )
        binding.textViewCustomer.setTitleAndValue(
            title = binding.textViewCustomer.context.getString(R.string.buyerName),
            splitter = binding.textViewCustomer.context.getString(R.string.colon),
            value = "${item.customerName} - ${item.customerCode}"
        )
        if (binding.expandableMore.isExpanded)
            binding.expandableMore.collapse()
    }
    //---------------------------------------------------------------------------------------------- setValueToXml


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener(item: BillingAndReturnReportModel) {
        binding.imageViewMore.setOnClickListener { clickShowMore(item) }
        binding.textViewShowMore.setOnClickListener { clickShowMore(item) }
        binding.textViewReport.setOnClickListener { click.detailPdf(item.id, binding.textViewReport) }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- clickShowMore
    private fun clickShowMore(item: BillingAndReturnReportModel) {
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
    private fun setDetailAdapter(items: List<BillingAndReturnDetailReportModel>) {
        val adapter = BillingReturnDetailAdapter(items)
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