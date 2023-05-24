package com.hoomanholding.jpawarehose.view.fragment.savereceipt.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.databinding.FragmentHistorySaveReceiptBinding
import com.hoomanholding.applibrary.model.data.database.join.HistorySaveReceiptWithSupplier
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.jpawarehose.view.activity.MainActivity
import com.hoomanholding.jpawarehose.view.adapter.HistorySaveReceiptAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by m-latifi on 2/20/2023.
 */

@AndroidEntryPoint
class HistorySaveReceiptFragment(override var layout: Int = R.layout.fragment_history_save_receipt) :
    JpaFragment<FragmentHistorySaveReceiptBinding>() {

    private val viewModel: HistorySaveReceiptViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmerViewContainer.config(getShimmerBuild())
        observeLiveDate()
        getSaveReceipt()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        binding.shimmerViewContainer.stopLoading()
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }

        viewModel.saveReceiptsLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            setReceiptAdapter(it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- getSaveReceipt
    private fun getSaveReceipt() {
        binding.shimmerViewContainer.startLoading()
        viewModel.getSaveReceipts()
    }
    //---------------------------------------------------------------------------------------------- getSaveReceipt


    //---------------------------------------------------------------------------------------------- setReceiptAdapter
    private fun setReceiptAdapter(items: List<HistorySaveReceiptWithSupplier>) {
        if (context == null)
            return
        val adapter = HistorySaveReceiptAdapter(items)
        val manager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerViewReceipt.adapter = adapter
        binding.recyclerViewReceipt.layoutManager = manager
    }
    //---------------------------------------------------------------------------------------------- setReceiptAdapter

}