package com.hoomanholding.jpawarehose.view.fragment.arrange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.databinding.FragmentArrangeBinding
import com.hoomanholding.jpawarehose.model.data.database.entity.ReceiptEntity
import com.hoomanholding.jpawarehose.model.data.database.join.ReceiptWithProduct
import com.hoomanholding.jpawarehose.view.activity.MainActivity
import com.hoomanholding.jpawarehose.view.adapter.ReceiptProductAdapter
import com.hoomanholding.jpawarehose.view.adapter.ReceiptSpinnerAdapter
import com.hoomanholding.jpawarehose.view.adapter.holder.ReceiptLocationHolder
import com.hoomanholding.jpawarehose.view.adapter.holder.ReceiptProductHolder
import com.zar.core.tools.loadings.LoadingManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanCustomCode
import io.github.g00fy2.quickie.config.ScannerConfig
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/18/2023
 */

@AndroidEntryPoint
class ArrangeFragment : Fragment() {

    private var _binding: FragmentArrangeBinding? = null
    private val binding get() = _binding!!

    private val arrangeViewModel: ArrangeViewModel by viewModels()

    @Inject
    lateinit var loadingManager: LoadingManager

    private var productAdapter: ReceiptProductAdapter? = null

    private val scanCustomCode =
        registerForActivityResult(ScanCustomCode()) { result -> handleResult(result) }

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
        binding.buttonScanQR.setOnClickListener { startQRCodeReader() }
        binding.buttonSave.setOnClickListener { productCompletingOnReceipt() }
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

        arrangeViewModel.locationFindLiveData.observe(viewLifecycleOwner) {
            productAdapter?.let { adapter ->
                adapter.notifyItemRangeChanged(0, adapter.itemCount)
                binding.recyclerDetail.smoothScrollToPosition(it)
            }
        }

        arrangeViewModel.sendReceiptToServer.observe(viewLifecycleOwner) {
            showMessage(it)
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveData


    //---------------------------------------------------------------------------------------------- startQRCodeReader
    private fun startQRCodeReader() {
        scanCustomCode.launch(
            ScannerConfig.build {
                setOverlayStringRes(R.string.scanQRCode) // string resource used for the scanner overlay
                setOverlayDrawableRes(R.drawable.ic_qr_code) // drawable resource used for the scanner overlay
                setShowTorchToggle(true)
                setUseFrontCamera(false) // use the front camera
            }
        )
    }
    //---------------------------------------------------------------------------------------------- startQRCodeReader


    //---------------------------------------------------------------------------------------------- handleResult
    private fun handleResult(result: QRResult) {
        when (result) {
            is QRResult.QRSuccess -> {
                result.content.rawValue
                try {
                    val id = result.content.rawValue.toLong()
                    arrangeViewModel.findLocation(id)
                } catch (e: NumberFormatException) {
                    showMessage(getString(R.string.qrCodeNotId))
                }
            }
            else -> {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }
    //---------------------------------------------------------------------------------------------- handleResult


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
    private fun initReceiptSpinner(receipt: List<ReceiptEntity>) {
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
        } else {
            binding.powerSpinnerReceipt.isEnabled = true
            binding.powerSpinnerReceipt.show()
        }
    }
    //---------------------------------------------------------------------------------------------- initReceiptSpinner


    //---------------------------------------------------------------------------------------------- loadReceiptDetail
    private fun loadReceiptDetail(index: Int) {
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
    private fun setProductAdapter(product: List<ReceiptWithProduct>) {
        binding.recyclerDetail.adapter = null
        productAdapter = null
        if (context == null)
            return
        val clickForReplaceOnLocation = object : ReceiptLocationHolder.Click {
            override fun addToLocation(count: String) {
                replaceOnLocation(count)
            }
        }
        loadingManager.stopLoadingRecycler()
        productAdapter = ReceiptProductAdapter(product, clickForReplaceOnLocation)
        val manager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        ReceiptProductHolder.productPosition = -1
        ReceiptProductHolder.locationPosition = -1
        binding.recyclerDetail.adapter = productAdapter
        binding.recyclerDetail.layoutManager = manager
    }
    //---------------------------------------------------------------------------------------------- setProductAdapter


    //---------------------------------------------------------------------------------------------- replaceOnLocation
    private fun replaceOnLocation(amount: String) {
        if (!amount.isDigitsOnly()) {
            showMessage(getString(R.string.countIsInvalidate))
            return
        }
        arrangeViewModel.replaceOnLocation(amount.toInt())
    }
    //---------------------------------------------------------------------------------------------- replaceOnLocation


    //---------------------------------------------------------------------------------------------- productCompletingOnReceipt
    private fun productCompletingOnReceipt() {
        if (binding.buttonSave.isLoading)
            return
        arrangeViewModel.productCompletingOnReceipt()
    }
    //---------------------------------------------------------------------------------------------- productCompletingOnReceipt


    //---------------------------------------------------------------------------------------------- onDestroyView
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //---------------------------------------------------------------------------------------------- onDestroyView


}