package com.hoomanholding.jpawarehose.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.databinding.FragmentSaveReceiptBinding
import com.hoomanholding.jpawarehose.model.database.entity.ProductSaveReceiptEntity
import com.hoomanholding.jpawarehose.model.database.entity.SupplierEntity
import com.hoomanholding.jpawarehose.view.adapter.ProductSaveReceiptAdapter
import com.hoomanholding.jpawarehose.view.adapter.SupplierSpinnerAdapter
import com.hoomanholding.jpawarehose.view.adapter.holder.ProductSaveReceiptHolder
import com.hoomanholding.jpawarehose.viewmodel.SaveReceiptViewModel
import com.zar.core.tools.loadings.LoadingManager
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


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {

        binding.editTextReceiptNumber.addTextChangedListener {
            job?.cancel()
            createJobForUpdateReceiptNumber(it.toString())
        }

        binding.textViewReceipt.setOnClickListener {
            saveReceiptViewModel.deleteReceipt()
        }


        binding.editTextSearch.addTextChangedListener {
            job?.cancel()
            createJobForSearch(it.toString())
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

/*
        saveReceiptViewModel.supplierSelectedLiveData.observe(viewLifecycleOwner) {
            if (it > -1) {
                val supplier = saveReceiptViewModel.supplierLiveData.value?.get(it)
                if (supplier != null) {
                    loadProduct(supplier)
                    binding.powerSpinnerSupplier.isEnabled = true
                } else

            } else {
                binding.powerSpinnerSupplier.isEnabled = false
            }

        }


*/


    }
    //---------------------------------------------------------------------------------------------- observeLiveData



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


    //---------------------------------------------------------------------------------------------- onDestroyView
    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()
        _binding = null
    }
    //---------------------------------------------------------------------------------------------- onDestroyView


}