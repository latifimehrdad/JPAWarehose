package com.zarholding.jpacustomer.view.adapter.holder

import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.response.report.CustomerReturnBasketReportItemModel
import com.hoomanholding.applibrary.model.data.response.report.CustomerReturnBasketReportModel
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ItemReportCustomerBasketReturnBinding
import com.zarholding.jpacustomer.view.adapter.recycler.ReportCustomerBasketReturnDetailAdapter

class ReportCustomerBasketReturnHolder(
    private val binding: ItemReportCustomerBasketReturnBinding
) : RecyclerView.ViewHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: CustomerReturnBasketReportModel) {
        setValueToXml(item)
        setListener(item)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: CustomerReturnBasketReportModel) {

        binding.textViewDate.setTitleAndValue(
            title = binding.textViewDate.context.getString(R.string.orderDate),
            splitter = binding.textViewDate.context.getString(R.string.colon),
            value = item.returnBasketDate
        )

        binding.textViewState.setTitleAndValue(
            title = binding.textViewState.context.getString(R.string.status),
            splitter = binding.textViewState.context.getString(R.string.colon),
            value = item.txtState
        )

        binding.textViewReturnBasketNumber.setTitleAndValue(
            title = binding.textViewReturnBasketNumber.context.getString(R.string.basketNumber),
            splitter = binding.textViewReturnBasketNumber.context.getString(R.string.colon),
            value = item.returnBasketNumber.toString()
        )

        if (binding.expandableMore.isExpanded)
            binding.expandableMore.collapse()
    }
    //---------------------------------------------------------------------------------------------- setValueToXml


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener(item: CustomerReturnBasketReportModel) {
        binding.imageViewMore.setOnClickListener { clickShowMore(item) }
        binding.textViewShowMore.setOnClickListener { clickShowMore(item) }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- clickShowMore
    private fun clickShowMore(item: CustomerReturnBasketReportModel) {
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
    private fun setDetailAdapter(items: List<CustomerReturnBasketReportItemModel>) {
        val adapter = ReportCustomerBasketReturnDetailAdapter(items)
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
        binding.recyclerDetail.adapter = null
    }
    //---------------------------------------------------------------------------------------------- hideMore

}