package com.hoomanholding.jpawarehose.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.databinding.FragmentArrangeBinding
import com.hoomanholding.jpawarehose.model.database.entity.ReceiptEntity
import com.hoomanholding.jpawarehose.model.database.join.ReceiptWithProduct
import com.hoomanholding.jpawarehose.view.activity.MainActivity
import com.hoomanholding.jpawarehose.view.adapter.ReceiptProductAdapter
import com.hoomanholding.jpawarehose.view.adapter.ReceiptSpinnerAdapter
import com.hoomanholding.jpawarehose.view.adapter.holder.ReceiptProductHolder
import com.hoomanholding.jpawarehose.viewmodel.ArrangeViewModel
import com.zar.core.tools.loadings.LoadingManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/18/2023
 */

@AndroidEntryPoint
class ArrangeFragment : Fragment() {

    private var _binding: FragmentArrangeBinding? = null
    private val binding get() = _binding!!

    private val arrangeViewModel : ArrangeViewModel by viewModels()

    @Inject
    lateinit var loadingManager : LoadingManager

    private var productAdapter : ReceiptProductAdapter? = null

    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArrangeBinding.inflate(inflater, container, false)
        binding.viewModel = arrangeViewModel
        return binding.root
    }
    //---------------------------------------------------------------------------------------------- onCreateView


    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        observeLiveData()
        setListener()
        arrangeViewModel.getOldData()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated



    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
        binding.buttonSave.stopLoading()
    }
    //---------------------------------------------------------------------------------------------- showMessage



    //---------------------------------------------------------------------------------------------- observeErrorLiveDate
    private fun observeErrorLiveDate() {
        arrangeViewModel.errorLiveDate.observe(viewLifecycleOwner) {
            loadingManager.stopLoadingView()
            loadingManager.stopLoadingRecycler()
            showMessage(it.message)
        }
    }
    //---------------------------------------------------------------------------------------------- observeErrorLiveDate



    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.powerSpinnerReceipt.setOnClickListener { powerSpinnerReceiptClick() }
    }
    //---------------------------------------------------------------------------------------------- setListener



    //---------------------------------------------------------------------------------------------- observeLiveData
    private fun observeLiveData() {
        observeErrorLiveDate()
        arrangeViewModel.receiptLiveData.observe(viewLifecycleOwner) {
            initReceiptSpinner(it)
        }

        arrangeViewModel.receiptDetailLiveData.observe(viewLifecycleOwner) {
            setProductAdapter(it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveData


    //---------------------------------------------------------------------------------------------- powerSpinnerReceiptClick
    private fun powerSpinnerReceiptClick() {
        if (context == null)
            return
        if (arrangeViewModel.receiptDetailLiveData.value != null)
            return
        arrangeViewModel.receiptLiveData.value?.let {
            if (binding.powerSpinnerReceipt.isShowing)
                binding.powerSpinnerReceipt.dismiss()
            else
                binding.powerSpinnerReceipt.show()
        } ?: run {
            binding.powerSpinnerReceipt.show()
            loadingManager.setViewLoading(
                binding.powerSpinnerReceipt.getSpinnerBodyView(),
                R.layout.item_loading,
                R.color.recyclerLoadingShadow,
            )
            arrangeViewModel.requestGetReceipts()
        }
    }
    //---------------------------------------------------------------------------------------------- powerSpinnerReceiptClick


    //---------------------------------------------------------------------------------------------- initReceiptSpinner
    private fun initReceiptSpinner(receipt : List<ReceiptEntity>) {
        binding.powerSpinnerReceipt.dismiss()
        binding.powerSpinnerReceipt.apply {
            setSpinnerAdapter(ReceiptSpinnerAdapter(this))
            setItems(receipt)
            getSpinnerRecyclerView().layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            lifecycleOwner = viewLifecycleOwner

            setOnSpinnerItemSelectedListener<ReceiptEntity> { _, _, newIndex, _ ->
                binding.powerSpinnerReceipt.showArrow = true
                loadReceiptDetail(newIndex)
            }
        }
        if (receipt.size == 1) {
            binding.powerSpinnerReceipt.selectItemByIndex(0)
            binding.powerSpinnerReceipt.isEnabled = false
        } else binding.powerSpinnerReceipt.isEnabled = true
    }
    //---------------------------------------------------------------------------------------------- initReceiptSpinner


    //---------------------------------------------------------------------------------------------- loadReceiptDetail
    private fun loadReceiptDetail(index : Int) {
        if (arrangeViewModel.receiptDetailLiveData.value != null)
            return
        binding.recyclerDetail.adapter = null
        loadingManager.setRecyclerLoading(
            binding.recyclerDetail,
            R.layout.item_loading,
            R.color.recyclerLoadingShadow,
            1
        )
        arrangeViewModel.requestGerReceiptDetail(index)
    }
    //---------------------------------------------------------------------------------------------- loadReceiptDetail


    //---------------------------------------------------------------------------------------------- setProductAdapter
    private fun setProductAdapter(product : List<ReceiptWithProduct>) {
        if (context == null)
            return
        loadingManager.stopLoadingRecycler()
        productAdapter = ReceiptProductAdapter(product)
        val manager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false)
        ReceiptProductHolder.productPosition = -1
        binding.recyclerDetail.adapter = productAdapter
        binding.recyclerDetail.layoutManager = manager
    }
    //---------------------------------------------------------------------------------------------- setProductAdapter


    //---------------------------------------------------------------------------------------------- onDestroyView
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //---------------------------------------------------------------------------------------------- onDestroyView


}