package com.hoomanholding.jpawarehose.view.fragment.savereceipt

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.hoomanholding.jpawarehose.JpaFragment
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.databinding.FragmentSaveReceiptBinding
import com.hoomanholding.jpawarehose.model.data.database.entity.SupplierEntity
import com.hoomanholding.jpawarehose.model.data.database.join.ProductAmountModel
import com.hoomanholding.jpawarehose.model.data.enum.EnumSearchName
import com.hoomanholding.jpawarehose.model.data.enum.EnumSearchOrderType
import com.hoomanholding.jpawarehose.view.activity.MainActivity
import com.hoomanholding.jpawarehose.view.adapter.ProductSaveReceiptAdapter
import com.hoomanholding.jpawarehose.view.adapter.SupplierSpinnerAdapter
import com.hoomanholding.jpawarehose.view.adapter.holder.ProductSaveReceiptHolder
import com.hoomanholding.jpawarehose.view.dialog.ConfirmDialog
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
class SaveReceiptFragment(override var layout: Int = R.layout.fragment_save_receipt) :
    JpaFragment<FragmentSaveReceiptBinding>() {

    @Inject
    lateinit var loadingManager : LoadingManager

    private val saveReceiptViewModel: SaveReceiptViewModel by viewModels()

    private var productAdapter : ProductSaveReceiptAdapter? = null

    private var job : Job? = null


    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = saveReceiptViewModel
        observeErrorLiveDate()
        observeLiveData()
        setListener()
        saveReceiptViewModel.getSuppliers()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- observeErrorLiveDate
    private fun observeErrorLiveDate() {
        saveReceiptViewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }
    }
    //---------------------------------------------------------------------------------------------- observeErrorLiveDate


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
        binding.buttonSave.stopLoading()
        loadingManager.stopLoadingRecycler()
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {

        binding.editTextReceiptNumber.addTextChangedListener {
            job?.cancel()
            createJobForUpdateReceiptNumber(it.toString())
        }

        binding.editTextSearch.addTextChangedListener {
            if (it.toString().isEmpty()) {
                binding.imageviewSearchDelete.setImageResource(R.drawable.ic_search)
                binding.imageviewSearchDelete.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.primaryColor),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            } else {
                binding.imageviewSearchDelete.setImageResource(R.drawable.ic_close)
                binding.imageviewSearchDelete.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.red),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            job?.cancel()
            createJobForSearch()
        }

        binding.buttonSave.setOnClickListener {
            saveReceipt()
        }

        binding.imageviewDelete.setOnClickListener {
            createNewReceipt()
        }

        binding.textViewOrderType.setOnClickListener {
            saveReceiptViewModel.changeOrderType()
        }

        binding.textViewOrderName.setOnClickListener {
            saveReceiptViewModel.changeOrderName()
        }

        binding.imageviewSearchDelete.setOnClickListener {
            binding.editTextSearch.setText("")
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
    private fun createJobForSearch() {
        job = CoroutineScope(IO).launch {
            delay(1000)
            saveReceiptViewModel.searchProduct()
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
            binding.powerSpinnerSupplier.isEnabled = false
        }

        saveReceiptViewModel.sendReceiptToServer.observe(viewLifecycleOwner) {
            showMessage(it)
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        saveReceiptViewModel.orderChangeLiveData.observe(viewLifecycleOwner) {
            when(saveReceiptViewModel.orderName){
                EnumSearchName.nameKala ->
                    binding.textViewOrderName.text = getString(R.string.productName)
                EnumSearchName.codeKala ->
                    binding.textViewOrderName.text = getString(R.string.productCode)
            }
            when(saveReceiptViewModel.orderType) {
                EnumSearchOrderType.DESC ->
                    binding.textViewOrderType.text = getString(R.string.DESC)
                EnumSearchOrderType.ASC ->
                    binding.textViewOrderType.text = getString(R.string.ASC)
            }
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
    private fun setProductAdapter(products : List<ProductAmountModel>) {
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

        override fun setCarton(position: Int, amount: Int) {
            saveReceiptViewModel.setCarton(position, amount)
        }

        override fun setPacket(position: Int, amount: Int) {
            saveReceiptViewModel.setPacket(position, amount)
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


}