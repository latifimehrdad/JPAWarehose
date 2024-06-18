package com.zarholding.jpacustomer.view.adapter.holder

import android.content.Context
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.model.data.enums.EnumState
import com.hoomanholding.applibrary.model.data.response.basket.subuser.SubUserOrderDetailModel
import com.hoomanholding.applibrary.model.data.response.basket.subuser.SubUserOrderModel
import com.hoomanholding.applibrary.model.data.response.report.ReportCustomerOrderDetailModel
import com.zar.core.tools.extensions.split
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.ItemSubCustomerOrderBinding
import com.zarholding.jpacustomer.view.adapter.recycler.SubCustomerOrderDetailAdapter
import com.zarholding.jpacustomer.view.dialog.ConfirmDialog

class SubCustomerOrderHolder(
    private val binding: ItemSubCustomerOrderBinding,
    private val onClick: (state: EnumState, item: SubUserOrderModel, position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: SubUserOrderModel, position: Int) {
        setValueToXml(item)
        setListener(item = item, position = position)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: SubUserOrderModel) {

        binding.textViewDate.setTitleAndValue(
            title = binding.textViewDate.context.getString(R.string.orderDate),
            splitter = binding.textViewDate.context.getString(R.string.colon),
            value = item.basketDate
        )

        binding.textViewAmount.setTitleAndValue(
            title = binding.textViewAmount.context.getString(R.string.amount),
            splitter = binding.textViewAmount.context.getString(R.string.colon),
            value = item.basketAmount.split()
        )

        binding.textViewName.setTitleAndValue(
            title = binding.textViewName.context.getString(R.string.fullName),
            splitter = binding.textViewName.context.getString(R.string.colon),
            value = item.fullName
        )
        binding.textViewBasketNumber.setTitleAndValue(
            title = binding.textViewBasketNumber.context.getString(R.string.basketNumber),
            splitter = binding.textViewBasketNumber.context.getString(R.string.colon),
            value = item.basketNumber
        )

        if (binding.expandableMore.isExpanded)
            binding.expandableMore.collapse()
    }
    //---------------------------------------------------------------------------------------------- setValueToXml


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener(item: SubUserOrderModel, position: Int) {
        binding.imageViewMore.setOnClickListener { clickShowMore(item) }
        binding.textViewShowMore.setOnClickListener { clickShowMore(item) }
        binding.buttonYes.setOnClickListener {
            showConfirmDialog(
                context = binding.buttonYes.context,
                state = EnumState.Confirmed,
                item = item,
                position = position
            )
        }
        binding.buttonNo.setOnClickListener {
            showConfirmDialog(
                context = binding.buttonNo.context,
                state = EnumState.Reject,
                item = item,
                position = position
            )
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- clickShowMore
    private fun clickShowMore(item: SubUserOrderModel) {
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
    private fun setDetailAdapter(items: List<SubUserOrderDetailModel>) {
        val adapter = SubCustomerOrderDetailAdapter(items)
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


    //---------------------------------------------------------------------------------------------- showConfirmDialog
    private fun showConfirmDialog(context: Context, state: EnumState, item: SubUserOrderModel, position: Int) {
        val title = when(state) {
            EnumState.Confirmed -> { context.getString(R.string.areYouSureToConfirmTheOrder) }
            EnumState.Reject -> { context.getString(R.string.areYouSureToRejectTheOrder) }
        }

        ConfirmDialog(
            context = context,
            title = title,
            false
        ){
            when(state){
                EnumState.Confirmed -> {
                    binding.buttonYes.startLoading(context.getString(R.string.bePatient))
                }
                EnumState.Reject -> {
                    binding.buttonNo.startLoading(context.getString(R.string.bePatient))
                }
            }
            onClick.invoke(state, item, position)
        }.show()
    }
    //---------------------------------------------------------------------------------------------- showConfirmDialog


}