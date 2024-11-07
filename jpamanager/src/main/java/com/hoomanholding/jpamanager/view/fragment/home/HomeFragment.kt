package com.hoomanholding.jpamanager.view.fragment.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.response.basedata.ComboModel
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentHomeBinding
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.hoomanholding.jpamanager.view.adapter.recycler.HomeItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpamanager.model.data.other.HomeReportItemModel
import com.hoomanholding.jpamanager.view.adapter.StringSpinnerAdapter


/**
 * create by m-latifi on 3/6/2023
 */

@AndroidEntryPoint
class HomeFragment(override var layout: Int = R.layout.fragment_home) :
    JpaFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()


    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmerViewContainer.config(getShimmerBuild())
        observeLiveData()
        getUserInfoInActivity()
        getCurrency()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        binding.shimmerViewContainer.stopLoading()
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- getUserInfoInActivity
    private fun getUserInfoInActivity() {
        activity?.let {
            (it as MainActivity).getUserInfo()
        }
    }
    //---------------------------------------------------------------------------------------------- getUserInfoInActivity



    //---------------------------------------------------------------------------------------------- getCurrency
    private fun getCurrency() {
        binding.shimmerViewContainer.startLoading()
        viewModel.requestGetCurrency()
    }
    //---------------------------------------------------------------------------------------------- getCurrency


    //---------------------------------------------------------------------------------------------- observeLiveData
    private fun observeLiveData() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }


        viewModel.homeReportLiveData.observe(viewLifecycleOwner){
            binding.shimmerViewContainer.stopLoading()
            setAdapter(it)
        }

        viewModel.currencyLiveData.observe(viewLifecycleOwner){
            setSpinnerCurrencyItems(it)
        }

    }
    //---------------------------------------------------------------------------------------------- observeLiveData



    //---------------------------------------------------------------------------------------------- setSpinnerCurrencyItems
    private fun setSpinnerCurrencyItems(items: List<ComboModel>){
        val stringArray = items.map { it.value }
        binding.powerSpinnerCurrency.apply {
            setSpinnerAdapter(StringSpinnerAdapter(this))
            setItems(stringArray)
            getSpinnerRecyclerView().layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setOnSpinnerItemSelectedListener<String> { _, _, newIndex, _ ->
                getHomeReport(newIndex)
            }
        }
        binding.powerSpinnerCurrency.selectItemByIndex(0)
    }
    //---------------------------------------------------------------------------------------------- setSpinnerCurrencyItems



    //---------------------------------------------------------------------------------------------- setAdapter
    private fun setAdapter(items: List<HomeReportItemModel>) {
        if (context == null)
            return
        val adapter = HomeItemAdapter(items, viewModel.currencyTypeText)
        val manager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerItem.layoutManager = manager
        binding.recyclerItem.adapter = adapter
    }
    //---------------------------------------------------------------------------------------------- setAdapter



    //---------------------------------------------------------------------------------------------- getHomeReport
    private fun getHomeReport(currencyPosition: Int) {
        binding.shimmerViewContainer.startLoading()
        viewModel.requestFirstPageReport(currencyPosition)
    }
    //---------------------------------------------------------------------------------------------- getHomeReport


}