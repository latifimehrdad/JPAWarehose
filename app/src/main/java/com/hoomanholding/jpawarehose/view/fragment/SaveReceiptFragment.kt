package com.hoomanholding.jpawarehose.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.databinding.FragmentHomeBinding
import com.hoomanholding.jpawarehose.databinding.FragmentSaveReceiptBinding
import com.hoomanholding.jpawarehose.model.database.entity.BrandEntity
import com.hoomanholding.jpawarehose.view.adapter.SupplierSpinnerAdapter
import com.hoomanholding.jpawarehose.viewmodel.SaveReceiptViewModel
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import dagger.hilt.android.AndroidEntryPoint

/**
 * Create by Mehrdad on 1/18/2023
 */

@AndroidEntryPoint
class SaveReceiptFragment : Fragment() {

    private var _binding: FragmentSaveReceiptBinding? = null
    private val binding get() = _binding!!

    private val saveReceiptViewModel: SaveReceiptViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }
    //---------------------------------------------------------------------------------------------- onCreateView


    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        setListener()
        initBrandsSpinner()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {

    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- initBrandsSpinner
    private fun initBrandsSpinner() {

        val brands = saveReceiptViewModel.getBrands()
        val items = brands.map {
            it.brandName?.let { name ->
                IconSpinnerItem(name)
            }
        }

        binding.powerSpinnerBrands.apply {
            setSpinnerAdapter(SupplierSpinnerAdapter(this))
            setItems(brands)
            getSpinnerRecyclerView().layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            lifecycleOwner = viewLifecycleOwner

            setOnSpinnerItemSelectedListener<BrandEntity> { _, _, newIndex, _ ->
                binding.powerSpinnerBrands.showArrow = true
                val item = brands[newIndex]
            }
        }
    }
    //---------------------------------------------------------------------------------------------- initBrandsSpinner


    //---------------------------------------------------------------------------------------------- onDestroyView
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //---------------------------------------------------------------------------------------------- onDestroyView


}