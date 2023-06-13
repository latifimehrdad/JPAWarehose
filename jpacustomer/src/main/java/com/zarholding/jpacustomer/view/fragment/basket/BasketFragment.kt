package com.zarholding.jpacustomer.view.fragment.basket

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.request.AddToBasket
import com.hoomanholding.applibrary.model.data.response.basket.DetailBasketModel
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.custom.JpaButton
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentBasketBinding
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.adapter.holder.BasketHolderNormal
import com.zarholding.jpacustomer.view.adapter.recycler.BasketAdapter
import com.zarholding.jpacustomer.view.dialog.ConfirmDialog
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by m-latifi on 11/8/2022.
 */


@AndroidEntryPoint
class BasketFragment(override var layout: Int = R.layout.fragment_basket) :
    JpaFragment<FragmentBasketBinding>() {

    private val viewModel: BasketViewModel by viewModels()
    private var itemButton: JpaButton? = null
    private var itemPosition: Int = 0
    private var itemCount: Int = 0
    private var adapter: BasketAdapter? = null

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        binding.imageViewClearText.visibility = View.GONE
        binding.shimmerViewContainer.config(getShimmerBuild())
        binding.cardViewCalculateBasket.visibility = View.GONE
        binding.buttonList.backgroundTintList =
            requireContext().getColorStateList(R.color.buttonDisable)
        binding.buttonList.backgroundTintList =
            requireContext().getColorStateList(R.color.primaryColor)
        observeLiveDate()
        setListener()
        getProduct()
        checkShowListType()
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


        viewModel.productLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            adapter = null
            binding.recyclerViewProduct.adapter = null
            setProductAdapter(it)
            calculateBasketAmount()
        }

        viewModel.addToBasketLiveData.observe(viewLifecycleOwner) {
            itemButton?.stopLoading()
            itemButton?.visibility = View.GONE
            adapter?.changeCount(itemPosition, itemCount)
            adapter?.notifyItemChanged(itemPosition)
            calculateBasketAmount()
        }


        viewModel.basketCountLiveData.observe(viewLifecycleOwner) {
            (activity as MainActivity?)?.setCartBadge(it)
        }

        viewModel.submitBasketLiveData.observe(viewLifecycleOwner) {
            (activity as MainActivity?)?.gotoHomeFragment()
        }

    }
    //---------------------------------------------------------------------------------------------- observeLiveDate


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.imageViewClearText.setOnClickListener {
            binding.editTextSearch.setText("")
        }

        binding.editTextSearch.addTextChangedListener {
            val text = it.toString()
            viewModel.setFilterByProductName(text)
            if (text.isEmpty())
                binding.imageViewClearText.visibility = View.GONE
            else
                binding.imageViewClearText.visibility = View.VISIBLE
        }

        binding.buttonList.setOnClickListener {
            viewModel.productShowListType = !viewModel.productShowListType
            checkShowListType()
        }

        binding.buttonSubmit.setOnClickListener {
            showDialogConfirmToSubmitBasket()
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- checkShowListType
    private fun checkShowListType() {
        if (viewModel.productShowListType) {
            binding.buttonList.backgroundTintList =
                requireContext().getColorStateList(R.color.primaryColor)
        } else {
            binding.buttonList.backgroundTintList =
                requireContext().getColorStateList(R.color.buttonDisable)
        }
        adapter?.productShowListType = viewModel.productShowListType
        adapter?.notifyItemRangeChanged(0, adapter?.itemCount ?: 0)
    }
    //---------------------------------------------------------------------------------------------- checkShowListType



    //---------------------------------------------------------------------------------------------- getProduct
    private fun getProduct() {
        binding.shimmerViewContainer.startLoading()
        viewModel.getProduct()
    }
    //---------------------------------------------------------------------------------------------- getProduct



    //---------------------------------------------------------------------------------------------- setProductAdapter
    private fun setProductAdapter(items: List<DetailBasketModel>) {
        if (context == null)
            return
        binding.recyclerViewProduct.adapter = null
        val click = object : BasketHolderNormal.Click {
            override fun click(item: DetailBasketModel, button: JpaButton, position: Int, count: Int) {
                if (button.isLoading)
                    return
                itemButton = button
                itemPosition = position
                itemCount = count
                val request = AddToBasket(item.productId, count,item.price)
                button.startLoading(getString(R.string.bePatient))
                viewModel.requestAddToBasket(request)
            }

            override fun deleteItem(item: DetailBasketModel) {
                adapter = null
                binding.recyclerViewProduct.adapter = null
                binding.cardViewCalculateBasket.visibility = View.GONE
                binding.shimmerViewContainer.startLoading()
                viewModel.requestDeleteBasket(item.productId)
            }
        }
        adapter = BasketAdapter(items, click)
        val manager = LinearLayoutManager(
            requireContext(),LinearLayoutManager.VERTICAL, false
        )
        adapter?.productShowListType = viewModel.productShowListType
        binding.recyclerViewProduct.adapter = adapter
        binding.recyclerViewProduct.layoutManager = manager
    }
    //---------------------------------------------------------------------------------------------- setProductAdapter


    //---------------------------------------------------------------------------------------------- calculateBasketAmount
    private fun calculateBasketAmount() {
        if (viewModel.productsList.isNullOrEmpty()){
            binding.cardViewCalculateBasket.visibility = View.GONE
            return
        }
        var amount : Long = 0
        for (item in viewModel.productsList!!)
            amount += (item.count * item.price)
        binding.textViewTotalAmount.setTitleAndValue(
            title = getString(R.string.totalAmount),
            splitter = getString(R.string.colon),
            last = getString(R.string.rial),
            value = amount
        )
        binding.cardViewCalculateBasket.visibility = View.VISIBLE
    }
    //---------------------------------------------------------------------------------------------- calculateBasketAmount


    //---------------------------------------------------------------------------------------------- showDialogConfirmToSubmitBasket
    private fun showDialogConfirmToSubmitBasket() {
        if (context == null)
            return
        val click = object : ConfirmDialog.Click{
            override fun clickYes() {
                submitBasket()
            }
        }
        ConfirmDialog(
            requireContext(),
            getString(R.string.doYouWantToSubmitBasket),
            click,
            false
        ).show()
    }
    //---------------------------------------------------------------------------------------------- showDialogConfirmToSubmitBasket


    //---------------------------------------------------------------------------------------------- submitBasket
    private fun submitBasket() {
        if (binding.buttonSubmit.isLoading)
            return
        binding.buttonSubmit.startLoading(getString(R.string.bePatient))
        viewModel.requestSubmitBasket()
    }
    //---------------------------------------------------------------------------------------------- submitBasket

}