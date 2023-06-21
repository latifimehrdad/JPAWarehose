package com.zarholding.jpacustomer.view.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.hoomanholding.applibrary.model.data.response.order.CustomerOrderDetailItemPDFModel
import com.zar.core.tools.extensions.split
import com.zarholding.jpacustomer.databinding.ItemReportPdfBillReturnDetailBinding

/**
 * Created by m-latifi on 6/8/2023.
 */

class BillingReturnDetailPDFHolder(
    private val binding: ItemReportPdfBillReturnDetailBinding
) : RecyclerView.ViewHolder(binding.root) {


    //---------------------------------------------------------------------------------------------- bind
    fun bind(item: CustomerOrderDetailItemPDFModel) {
        setValueToXml(item)
        binding.executePendingBindings()
    }
    //---------------------------------------------------------------------------------------------- bind


    //---------------------------------------------------------------------------------------------- setValueToXml
    private fun setValueToXml(item: CustomerOrderDetailItemPDFModel) {
        binding.textViewTotalAmount.text = item.productAmount.split()
        binding.textViewPrice.text = item.price.split()
        binding.textViewCount.text = item.count.split()
        binding.textViewProductName.text = item.productName
        binding.textViewProductCode.text = item.productCode
    }
    //---------------------------------------------------------------------------------------------- setValueToXml

}