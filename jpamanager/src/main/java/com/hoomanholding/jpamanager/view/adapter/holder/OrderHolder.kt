package com.hoomanholding.jpamanager.view.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.jpamanager.databinding.ItemOrderBinding
import com.hoomanholding.applibrary.model.data.response.order.OrderModel
import com.hoomanholding.applibrary.tools.CreateDrawable
import com.hoomanholding.jpamanager.R


/**
 * create by m-latifi on 3/18/2023
 */

class OrderHolder(
    private val binding: ItemOrderBinding,
    private val click: Click
) : RecyclerView.ViewHolder(binding.root) {


    interface Click {
        fun orderDetail(item: OrderModel)
        fun customerFinancialDetail(customerId: Int)
    }


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: OrderModel) {
        binding.item = item

        when (item.saleOrderState) {
            0 -> binding.linearLayoutParent.background = CreateDrawable().getCornerRadius(
                binding.linearLayoutParent.context.getColor(R.color.dismiss)
            )
            1 -> binding.linearLayoutParent.background = CreateDrawable().getCornerRadius(
                binding.linearLayoutParent.context.getColor(R.color.confirmFactorText)
            )
            2 -> binding.linearLayoutParent.background = CreateDrawable().getCornerRadius(
                binding.linearLayoutParent.context.getColor(R.color.rejectFactorText)
            )
        }

        if (item.orderType == 2)
            binding.textviewName.setTextColor(binding.textviewName.context.getColor(R.color.confirmFactorText))

        binding.textViewDetail.setOnClickListener {
            click.orderDetail(item)
        }
        binding.textViewCustomerFinancial.setOnClickListener {
            click.customerFinancialDetail(item.customerId)
        }
        checkSelected(item)
        binding.root.setOnClickListener {
            item.select = !item.select
            checkSelected(item)
        }
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- checkSelected
    private fun checkSelected(item: OrderModel) {
        if (item.select)
            binding.viewSelect.visibility = View.VISIBLE
        else
            binding.viewSelect.visibility = View.GONE
    }
    //---------------------------------------------------------------------------------------------- checkSelected

}