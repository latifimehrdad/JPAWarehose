package com.hoomanholding.jpawarehose.view.fragment.savereceipt.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.jpawarehose.JpaFragment
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.databinding.FragmentHistorySaveReceiptBinding
import com.hoomanholding.jpawarehose.model.data.database.join.HistorySaveReceiptWithSupplier
import com.hoomanholding.jpawarehose.view.activity.MainActivity
import com.hoomanholding.jpawarehose.view.adapter.HistorySaveReceiptAdapter
import com.zar.core.tools.loadings.LoadingManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by zar on 2/20/2023.
 */

@AndroidEntryPoint
class HistorySaveReceiptFragment(override var layout: Int = R.layout.fragment_history_save_receipt) :
    JpaFragment<FragmentHistorySaveReceiptBinding>() {

    private val viewModel: HistorySaveReceiptViewModel by viewModels()

    @Inject
    lateinit var loadingManager: LoadingManager

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveDate()
        getSaveReceipt()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
        loadingManager.stopLoadingRecycler()
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }

        viewModel.saveReceiptsLiveData.observe(viewLifecycleOwner) {
            setReceiptAdapter(it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- getSaveReceipt
    private fun getSaveReceipt() {
        loadingManager.setRecyclerLoading(
            binding.recyclerViewReceipt,
            R.layout.item_loading,
            R.color.recyclerLoadingShadow,
            1
        )
        viewModel.getSaveReceipts()
    }
    //---------------------------------------------------------------------------------------------- getSaveReceipt


    //---------------------------------------------------------------------------------------------- setReceiptAdapter
    private fun setReceiptAdapter(items: List<HistorySaveReceiptWithSupplier>) {
        loadingManager.stopLoadingRecycler()
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