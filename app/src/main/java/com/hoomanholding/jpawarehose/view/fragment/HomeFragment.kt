package com.hoomanholding.jpawarehose.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hoomanholding.jpawarehose.databinding.FragmentHomeBinding
import com.hoomanholding.jpawarehose.view.activity.MainActivity
import com.hoomanholding.jpawarehose.viewmodel.ProductViewModel
import com.hoomanholding.jpawarehose.viewmodel.SupplierViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Create by Mehrdad on 1/18/2023
 */

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

//    private val supplierViewModel : SupplierViewModel by viewModels()
    private val productViewModel : ProductViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    //---------------------------------------------------------------------------------------------- onCreateView


    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        productViewModel.productsLiveData.observe(viewLifecycleOwner) {
            binding.btn1.stopLoading()
            (activity as MainActivity).showMessage("دریافت محصولات")
        }
        binding.btn1.setOnClickListener {
            if (binding.btn1.isLoading)
                return@setOnClickListener
            binding.btn1.startLoading("شکیبا باشید")
            productViewModel.requestGetProducts()
        }

    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- onDestroyView
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //---------------------------------------------------------------------------------------------- onDestroyView


}