package com.zarholding.jpacustomer.view.fragment.basket

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.setTitleAndValue
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.request.AddToBasket
import com.hoomanholding.applibrary.model.data.response.basket.DetailBasketModel
import com.hoomanholding.applibrary.tools.CompanionValues
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.custom.JpaButton
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.zar.core.enums.EnumApiError
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.FragmentBasketBinding
import com.zarholding.jpacustomer.model.EnumProductPageType
import com.zarholding.jpacustomer.view.activity.MainActivity
import com.zarholding.jpacustomer.view.adapter.holder.BasketHolderNormal
import com.zarholding.jpacustomer.view.adapter.recycler.BasketAdapter
import com.zarholding.jpacustomer.view.dialog.ConfirmDialog
import com.zarholding.jpacustomer.view.dialog.SubmitBasketDialog
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
    private var viewType = EnumProductPageType.Product

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
            requireContext().getColorStateList(R.color.a_buttonUnselect)
        binding.buttonList.backgroundTintList =
            requireContext().getColorStateList(R.color.primaryColor)
        observeLiveDate()
        setListener()
        checkShowListType()
        getProduct()
    }
    //---------------------------------------------------------------------------------------------- initView


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            binding.buttonSubmit.stopLoading()
            binding.buttonDeleteAll.stopLoading()
            itemButton?.stopLoading()
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
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab == null)
                    return
                viewType = if (tab.position == 0)
                    EnumProductPageType.Product
                else
                    EnumProductPageType.Return
                getProduct()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.imageViewClearText.setOnClickListener {
            binding.editTextSearch.setText("")
        }

        binding.editTextSearch.addTextChangedListener {
            val text = it.toString()
            viewModel.setFilterByProductName(text, type = viewType)
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

        binding.buttonDeleteAll.setOnClickListener {
            showDialogConfirmToDeleteBasket()
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
                requireContext().getColorStateList(R.color.color3)
        }
        adapter?.productShowListType = viewModel.productShowListType
        adapter?.notifyItemRangeChanged(0, adapter?.itemCount ?: 0)
    }
    //---------------------------------------------------------------------------------------------- checkShowListType



    //---------------------------------------------------------------------------------------------- getProduct
    private fun getProduct() {
        val visible = when(viewType) {
            EnumProductPageType.Product -> View.VISIBLE
            EnumProductPageType.Return -> View.GONE
        }
        binding.textViewTotalAmountOfCashProduct.visibility = visible
        binding.textViewTotalAmount.visibility = visible
//        binding.checkboxExhibit.visibility = visible
        if (visible == View.VISIBLE)
            binding.buttonSubmit.setText(R.string.saveOrder)
        else
            binding.buttonSubmit.setText(R.string.requestReturn)

        binding.cardViewCalculateBasket.visibility = View.GONE
        binding.recyclerViewProduct.adapter = null
        binding.shimmerViewContainer.startLoading()
        viewModel.getBasket(type = viewType)
    }
    //---------------------------------------------------------------------------------------------- getProduct



    //---------------------------------------------------------------------------------------------- setProductAdapter
    private fun setProductAdapter(items: List<DetailBasketModel>) {
        if (context == null)
            return
        binding.recyclerViewProduct.adapter = null
        if (items.isNotEmpty())
            binding.cardViewCalculateBasket.visibility = View.VISIBLE
        val click = object : BasketHolderNormal.Click {
            override fun click(item: DetailBasketModel, button: JpaButton, position: Int, count: Int) {
                if (button.isLoading)
                    return
                itemButton = button
                itemPosition = position
                itemCount = count
                val request = AddToBasket(item.productId, count,item.price)
                button.startLoading(getString(R.string.bePatient))
                when(viewType) {
                    EnumProductPageType.Product -> viewModel.requestAddToBasket(request)
                    EnumProductPageType.Return -> viewModel.requestAddToReturn(request)
                }
            }

            override fun deleteItem(item: DetailBasketModel) {
                adapter = null
                binding.recyclerViewProduct.adapter = null
                binding.cardViewCalculateBasket.visibility = View.GONE
                binding.shimmerViewContainer.startLoading()
                viewModel.requestDeleteBasket(item.productId, type = viewType)
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
        var cashAmount: Long = 0
        for (item in viewModel.productsList!!) {
            amount += (item.count * item.price)
            if (item.isCash)
                cashAmount += (item.count * item.price)
        }
/*        if (viewModel.isExhibitionActive)
            binding.checkboxExhibit.visibility = View.VISIBLE
        else
            binding.checkboxExhibit.visibility = View.GONE*/
        binding.textViewTotalAmount.setTitleAndValue(
            title = getString(R.string.totalAmount),
            splitter = getString(R.string.colon),
            last = getString(R.string.rial),
            value = amount
        )
        binding.textViewTotalAmountOfCashProduct.setTitleAndValue(
            title = getString(R.string.totalCashAmount),
            splitter = getString(R.string.colon),
            last = getString(R.string.rial),
            value = cashAmount
        )
    }
    //---------------------------------------------------------------------------------------------- calculateBasketAmount


    //---------------------------------------------------------------------------------------------- showDialogConfirmToSubmitBasket
    private fun showDialogConfirmToSubmitBasket() {
        if (context == null)
            return
        SubmitBasketDialog(requireContext()) { submitBasket(it) }.show()
    }
    //---------------------------------------------------------------------------------------------- showDialogConfirmToSubmitBasket


    //---------------------------------------------------------------------------------------------- showDialogConfirmToDeleteBasket
    private fun showDialogConfirmToDeleteBasket() {
        if (context == null)
            return
        ConfirmDialog(
            requireContext(),
            getString(R.string.doYouWantToAllBasketDelete),
            false
        ){
            submitDeleteBasket()
        }.show()
    }
    //---------------------------------------------------------------------------------------------- showDialogConfirmToDeleteBasket



    //---------------------------------------------------------------------------------------------- submitBasket
    private fun submitBasket(description: String) {
        if (binding.buttonSubmit.isLoading)
            return
        binding.buttonSubmit.startLoading(getString(R.string.bePatient))
        viewModel.requestSubmitBasket(
            description = description,
            exhibition = binding.checkboxExhibit.isChecked,
            type = viewType
        )
    }
    //---------------------------------------------------------------------------------------------- submitBasket





    //---------------------------------------------------------------------------------------------- submitDeleteBasket
    private fun submitDeleteBasket() {
        if (binding.buttonSubmit.isLoading)
            return
        binding.buttonDeleteAll.startLoading(getString(R.string.bePatient))
        viewModel.requestDeleteBasket(
            type = viewType
        )
    }
    //---------------------------------------------------------------------------------------------- submitDeleteBasket

}