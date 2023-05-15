package com.zarholding.jpacustomer.view.fragment.product

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.response.product.ProductModel
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import dagger.hilt.android.AndroidEntryPoint
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentProductBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.adapter.recycler.ProductAdapter


/**
 * Created by m-latifi on 11/8/2022.
 */


@AndroidEntryPoint
class ProductFragment(override var layout: Int = R.layout.fragment_product) :
    JpaFragment<FragmentProductBinding>() {

    private val viewModel: ProductViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmerViewContainer.config(getShimmerBuild())
        initView()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        binding.shimmerViewContainer.stopLoading()
        activity?.let { (it as MainActivity).showMessage(message) }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        observeLiveDate()
        setListener()
        getProduct()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
        }

        viewModel.productNewLiveData.observe(viewLifecycleOwner){
            if (context == null)
                return@observe
            if (it){
                binding.buttonAllProduct.backgroundTintList =
                    requireContext().getColorStateList(R.color.buttonDisable)
                binding.buttonNewProduct.backgroundTintList =
                    requireContext().getColorStateList(R.color.primaryColor)
            } else {
                binding.buttonNewProduct.backgroundTintList =
                    requireContext().getColorStateList(R.color.buttonDisable)
                binding.buttonAllProduct.backgroundTintList =
                    requireContext().getColorStateList(R.color.primaryColor)
            }
        }


        viewModel.productLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            setProductAdapter(it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {

        binding.buttonAllProduct.setOnClickListener {
            viewModel.setFilterByProductNew(false)
        }

        binding.buttonNewProduct.setOnClickListener {
            viewModel.setFilterByProductNew(true)
        }

        binding.editTextSearch.addTextChangedListener {
            val text = it.toString()
            viewModel.setFilterByProductName(text)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener



    //---------------------------------------------------------------------------------------------- getProduct
    private fun getProduct() {
        binding.shimmerViewContainer.startLoading()
        viewModel.getProduct()
    }
    //---------------------------------------------------------------------------------------------- getProduct



    //---------------------------------------------------------------------------------------------- setProductAdapter
    private fun setProductAdapter(items: List<ProductModel>) {
        if (context == null)
            return
        binding.recyclerViewProduct.adapter = null
        val adapter = ProductAdapter(items)
        val manager = LinearLayoutManager(
            requireContext(),LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerViewProduct.adapter = adapter
        binding.recyclerViewProduct.layoutManager = manager
    }
    //---------------------------------------------------------------------------------------------- setProductAdapter
}