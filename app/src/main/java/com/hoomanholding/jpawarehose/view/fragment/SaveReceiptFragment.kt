package com.hoomanholding.jpawarehose.view.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.databinding.FragmentSaveReceiptBinding
import com.hoomanholding.jpawarehose.model.database.entity.ProductSaveReceiptEntity
import com.hoomanholding.jpawarehose.model.database.entity.SupplierEntity
import com.hoomanholding.jpawarehose.view.activity.MainActivity
import com.hoomanholding.jpawarehose.view.adapter.ProductSaveReceiptAdapter
import com.hoomanholding.jpawarehose.view.adapter.SupplierSpinnerAdapter
import com.hoomanholding.jpawarehose.view.adapter.holder.ProductSaveReceiptHolder
import com.hoomanholding.jpawarehose.view.dialog.ConfirmDialog
import com.hoomanholding.jpawarehose.viewmodel.SaveReceiptViewModel
import com.zar.core.tools.loadings.LoadingManager
import com.zar.core.tools.manager.DialogManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject

/**
 * Create by Mehrdad on 1/18/2023
 */

@AndroidEntryPoint
class SaveReceiptFragment : Fragment() {

    private var _binding: FragmentSaveReceiptBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var loadingManager : LoadingManager

    private val saveReceiptViewModel: SaveReceiptViewModel by viewModels()

    private var productAdapter : ProductSaveReceiptAdapter? = null

    private var job : Job? = null

    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveReceiptBinding.inflate(inflater, container, false)
        binding.viewModel = saveReceiptViewModel
        return binding.root
    }
    //---------------------------------------------------------------------------------------------- onCreateView


    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        observeLiveData()
        setListener()
        saveReceiptViewModel.getSuppliers()
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


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {

        binding.editTextReceiptNumber.addTextChangedListener {
            job?.cancel()
            createJobForUpdateReceiptNumber(it.toString())
        }

        binding.editTextSearch.addTextChangedListener {
            job?.cancel()
            createJobForSearch(it.toString())
        }

        binding.buttonSave.setOnClickListener {
            saveReceipt()
        }

        binding.imageviewDelete.setOnClickListener {
            createNewReceipt()
        }
    }
    //---------------------------------------------------------------------------------------------- setListener



    //---------------------------------------------------------------------------------------------- createJobForUpdateReceiptNumber
    private fun createJobForUpdateReceiptNumber(receiptNumber: String) {
        job = CoroutineScope(IO).launch {
            delay(1000)
            withContext(Main) {
                if (receiptNumber.isNotEmpty())
                    saveReceiptViewModel.updateLastSaveReceipt(receiptNumber)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- createJobForUpdateReceiptNumber



    //---------------------------------------------------------------------------------------------- createJobForUpdateReceiptNumber
    private fun createJobForSearch(search: String) {
        job = CoroutineScope(IO).launch {
            delay(1000)
            saveReceiptViewModel.searchProduct(search)
        }
    }
    //---------------------------------------------------------------------------------------------- createJobForUpdateReceiptNumber



    //---------------------------------------------------------------------------------------------- observeLiveData
    private fun observeLiveData() {
        saveReceiptViewModel.supplierLiveData.observe(viewLifecycleOwner) {
            initBrandsSpinner(it)
        }

        saveReceiptViewModel.productLiveData.observe(viewLifecycleOwner){
            setProductAdapter(it)
        }

        saveReceiptViewModel.adapterNotifyChangeLiveData.observe(viewLifecycleOwner) {
            productAdapter?.notifyItemChanged(it)
        }

        saveReceiptViewModel.sendReceiptToServer.observe(viewLifecycleOwner) {
            showMessage(it)
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveData


    //---------------------------------------------------------------------------------------------- saveReceipt
    private fun saveReceipt() {
        if (context == null)
            return

        if(binding.editTextReceiptNumber.text.isNullOrEmpty()) {
            showMessage(requireContext().getString(R.string.receiptNumberIsEmpty))
            binding.editTextReceiptNumber.error =
                requireContext().getString(R.string.receiptNumberIsEmpty)
            return
        }

        val dialog = DialogManager().createDialogHeightWrapContent(
            requireContext(),
            R.layout.dialog_confirm_save_receipt,
            Gravity.CENTER,
            0)

        val editText = dialog.findViewById<TextInputEditText>(R.id.textInputEditTextDescription)
        val buttonYes = dialog.findViewById<MaterialButton>(R.id.buttonYes)
        val buttonNo = dialog.findViewById<MaterialButton>(R.id.buttonNo)

        buttonNo.setOnClickListener {
            dialog.dismiss()
        }

        buttonYes.setOnClickListener {
            binding.buttonSave.startLoading(buttonYes.context.getString(R.string.bePatient))
            saveReceiptViewModel.sendReceipt(editText.text.toString())
            dialog.dismiss()
        }

        dialog.show()
    }
    //---------------------------------------------------------------------------------------------- saveReceipt



    //---------------------------------------------------------------------------------------------- initBrandsSpinner
    private fun initBrandsSpinner(suppliers : List<SupplierEntity>) {
        binding.powerSpinnerSupplier.apply {
            setSpinnerAdapter(SupplierSpinnerAdapter(this))
            setItems(suppliers)
            getSpinnerRecyclerView().layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            lifecycleOwner = viewLifecycleOwner

            setOnSpinnerItemSelectedListener<SupplierEntity> { _, _, newIndex, _ ->
                binding.powerSpinnerSupplier.showArrow = true
                loadProduct(newIndex)
            }
        }
        if (suppliers.size == 1) {
            binding.powerSpinnerSupplier.selectItemByIndex(0)
            binding.powerSpinnerSupplier.isEnabled = false
        } else binding.powerSpinnerSupplier.isEnabled = true
    }
    //---------------------------------------------------------------------------------------------- initBrandsSpinner



    //---------------------------------------------------------------------------------------------- loadProduct
    private fun loadProduct(supplierIndex : Int) {
        binding.recyclerProduct.adapter = null
        loadingManager.setRecyclerLoading(
            binding.recyclerProduct,
            R.layout.item_loading,
            R.color.recyclerLoadingShadow,
            1
        )
        saveReceiptViewModel.selectSupplier(supplierIndex)
    }
    //---------------------------------------------------------------------------------------------- loadProduct



    //---------------------------------------------------------------------------------------------- setProductAdapter
    private fun setProductAdapter(products : List<ProductSaveReceiptEntity>) {
        if (context == null)
            return
        loadingManager.stopLoadingRecycler()
        productAdapter = ProductSaveReceiptAdapter(products, clickProduct())
        val manager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false)
        binding.recyclerProduct.adapter = productAdapter
        binding.recyclerProduct.layoutManager = manager
    }
    //---------------------------------------------------------------------------------------------- setProductAdapter


    //---------------------------------------------------------------------------------------------- clickProduct
    private fun clickProduct() = object : ProductSaveReceiptHolder.Click{
        override fun addCarton(position: Int) {
            saveReceiptViewModel.addCarton(position)
        }

        override fun addPacket(position: Int) {
            saveReceiptViewModel.addPacket(position)
        }

        override fun minusCarton(position: Int) {
            saveReceiptViewModel.minusCarton(position)
        }

        override fun minusPacket(position: Int) {
            saveReceiptViewModel.minusPacket(position)
        }
    }
    //---------------------------------------------------------------------------------------------- clickProduct


    //---------------------------------------------------------------------------------------------- createNewReceipt
    private fun createNewReceipt() {
        val click = object : ConfirmDialog.Click{
            override fun clickYes() {
                binding.recyclerProduct.adapter = null
                binding.powerSpinnerSupplier.clearSelectedItem()
                saveReceiptViewModel.createNewReceipt()
            }
        }

        ConfirmDialog(
            requireContext(),
            getString(R.string.doYouWantToDeleteReceipt),
            click
        ).show()
    }
    //---------------------------------------------------------------------------------------------- createNewReceipt


    //---------------------------------------------------------------------------------------------- onDestroyView
    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()
        _binding = null
    }
    //---------------------------------------------------------------------------------------------- onDestroyView


}