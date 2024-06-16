package com.zarholding.jpacustomer.view.fragment.product

import android.animation.Animator
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.response.category.CategoryModel
import com.hoomanholding.applibrary.model.data.response.product.ProductModel
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import com.zar.core.enums.EnumApiError
import com.zarholding.jpacustomer.CircleAnimationUtil
import dagger.hilt.android.AndroidEntryPoint
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentProductBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.adapter.holder.CategoryHolder
import com.zarholding.jpacustomer.view.adapter.holder.ProductHolder
import com.zarholding.jpacustomer.view.adapter.recycler.CategoryAdapter
import com.zarholding.jpacustomer.view.adapter.recycler.ProductAdapter
import com.zarholding.jpacustomer.view.dialog.product.ProductDetailDialog


/**
 * Created by m-latifi on 11/8/2022.
 */


@AndroidEntryPoint
class ProductFragment(override var layout: Int = R.layout.fragment_product) :
    JpaFragment<FragmentProductBinding>() {

    private val viewModel: ProductViewModel by viewModels()
    private var imageViewSelect: ImageView? = null

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmerViewContainer.config(getShimmerBuild())
        binding.shimmerViewContainerCategory.config(getShimmerBuild())
        initView()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        binding.shimmerViewContainer.stopLoading()
        binding.shimmerViewContainerCategory.stopLoading()
        activity?.let { (it as MainActivity).showMessage(message) }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- initView
    private fun initView() {
        binding.imageViewClearText.visibility = View.GONE
        observeLiveDate()
        setListener()
        getProduct()
        setSortSpinner()
        setCategoryLevelSpinner()
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
                    requireContext().getColorStateList(R.color.color3)
                binding.buttonNewProduct.backgroundTintList =
                    requireContext().getColorStateList(R.color.primaryColor)
            } else {
                binding.buttonNewProduct.backgroundTintList =
                    requireContext().getColorStateList(R.color.color3)
                binding.buttonAllProduct.backgroundTintList =
                    requireContext().getColorStateList(R.color.primaryColor)
            }
        }


        viewModel.productLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            setProductAdapter(it)
            binding.spinnerSort.selectItemByIndex(viewModel.getSortIndex())
            binding.spinnerCategory.selectItemByIndex(viewModel.getCategoryLevelIndex())
        }

        viewModel.basketCountLiveData.observe(viewLifecycleOwner) {
            animationToCart(imageViewSelect, it)
        }

        viewModel.categoryLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainerCategory.stopLoading()
            setCategoryAdapter(it)
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {

        binding.imageViewClearFilter.setOnClickListener {
            viewModel.clearFilter()
            binding.editTextSearch.setText("")
            binding.spinnerSort.clearSelectedItem()
        }

        binding.imageViewClearText.setOnClickListener {
            binding.editTextSearch.setText("")
        }

        binding.buttonAllProduct.setOnClickListener {
            viewModel.setFilterByProductNew(false)
        }

        binding.buttonNewProduct.setOnClickListener {
            viewModel.setFilterByProductNew(true)
        }

        binding.editTextSearch.addTextChangedListener {
            val text = it.toString()
            if (text.isEmpty())
                binding.imageViewClearText.visibility = View.GONE
            else
                binding.imageViewClearText.visibility = View.VISIBLE
            viewModel.setFilterByProductName(text)
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- setSortSpinner
    private fun setSortSpinner() {

        binding.spinnerSort.apply {
            setSpinnerAdapter(IconSpinnerAdapter(this))
            setItems(viewModel.getItemsSort())
            getSpinnerRecyclerView().layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            lifecycleOwner = viewLifecycleOwner
            setOnSpinnerOutsideTouchListener { _, _ -> dismiss() }
            setOnSpinnerItemSelectedListener<IconSpinnerItem> { oldIndex, _, newIndex, _ ->
                if (oldIndex != newIndex)
                    viewModel.setSortIndex(index = newIndex)
            }
        }

        binding.spinnerSort.selectItemByIndex(viewModel.getSortIndex())
    }
    //---------------------------------------------------------------------------------------------- setSortSpinner



    //---------------------------------------------------------------------------------------------- setSortSpinner
    private fun setCategoryLevelSpinner() {

        binding.spinnerCategory.apply {
            setSpinnerAdapter(IconSpinnerAdapter(this))
            setItems(viewModel.getCategoryLevel())
            getSpinnerRecyclerView().layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            lifecycleOwner = viewLifecycleOwner
            setOnSpinnerOutsideTouchListener { _, _ -> dismiss() }
            setOnSpinnerItemSelectedListener<IconSpinnerItem> { oldIndex, _, newIndex, _ ->
                if (oldIndex != newIndex) {
                    binding.shimmerViewContainerCategory.startLoading()
                    viewModel.setCategoryLevel(index = newIndex)
                }
            }
        }

        binding.spinnerCategory.selectItemByIndex(viewModel.getCategoryLevelIndex())
    }
    //---------------------------------------------------------------------------------------------- setSortSpinner


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
        val click = object : ProductHolder.Click {
            override fun selectProduct(item: ProductModel, imageView: ImageView) {
                showProductDetailDialog(item, imageView)
            }
        }
        val adapter = ProductAdapter(items, click)
        val manager = LinearLayoutManager(
            requireContext(),LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerViewProduct.adapter = adapter
        binding.recyclerViewProduct.layoutManager = manager
    }
    //---------------------------------------------------------------------------------------------- setProductAdapter



    //---------------------------------------------------------------------------------------------- setProductAdapter
    private fun setCategoryAdapter(items: List<CategoryModel>) {
        if (context == null)
            return
        binding.recyclerViewCategory.adapter = null
        CategoryAdapter.selectedPosition = viewModel.getCategoryIndex()
        val click = object : CategoryHolder.Click {
            override fun click(position: Int, item: CategoryModel) {
                viewModel.setCategoryIndex(index = position)
                CategoryAdapter.selectedPosition = position
                binding.recyclerViewCategory.adapter?.notifyItemRangeChanged(0, items.size)
            }
        }
        val adapter = CategoryAdapter(items, click)
        val manager = LinearLayoutManager(
                requireContext(),LinearLayoutManager.HORIZONTAL, true
        )
        binding.recyclerViewCategory.adapter = adapter
        binding.recyclerViewCategory.layoutManager = manager
    }
    //---------------------------------------------------------------------------------------------- setProductAdapter




    //---------------------------------------------------------------------------------------------- showProductDetailDialog
    private fun showProductDetailDialog(item: ProductModel, imageView: ImageView) {
        val select = object : ProductDetailDialog.Click{
            override fun select() {
                imageViewSelect = imageView
                viewModel.getBasketCount()
            }
        }
        ProductDetailDialog(select, item).show(childFragmentManager, "product")
    }
    //---------------------------------------------------------------------------------------------- showProductDetailDialog


    //---------------------------------------------------------------------------------------------- animationToCart
    private fun animationToCart(imageView: ImageView?, count: Int) {

        if (activity == null && imageView == null)
            return

        val destView = (activity as MainActivity).getCartView()
        CircleAnimationUtil()
            .attachActivity(requireActivity())
            .setTargetView(imageView)
            .setDestView(destView)
            .setMoveDuration(800)
            .setCircleDuration(800)
            .setAnimationListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    (activity as MainActivity?)?.setCartBadge(count)

                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            }).startAnimation()
    }
    //---------------------------------------------------------------------------------------------- animationToCart

}