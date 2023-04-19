package com.hoomanholding.jpamanager.view.fragment.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentHomeBinding
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.hoomanholding.jpamanager.view.adapter.recycler.HomeItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpamanager.model.data.other.HomeReportItemModel


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
        getHomeReport()
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



    //---------------------------------------------------------------------------------------------- getHomeReport
    private fun getHomeReport() {
        binding.shimmerViewContainer.startLoading()
        viewModel.requestFirstPageReport()
    }
    //---------------------------------------------------------------------------------------------- getHomeReport


    //---------------------------------------------------------------------------------------------- observeLiveData
    private fun observeLiveData() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }


        viewModel.homeReportLiveData.observe(viewLifecycleOwner){
            binding.shimmerViewContainer.stopLoading()
            setAdapter(it)
        }

    }
    //---------------------------------------------------------------------------------------------- observeLiveData


    //---------------------------------------------------------------------------------------------- setAdapter
    private fun setAdapter(items: List<HomeReportItemModel>) {
        if (context == null)
            return
        val adapter = HomeItemAdapter(items)
        val manager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerItem.layoutManager = manager
        binding.recyclerItem.adapter = adapter
    }
    //---------------------------------------------------------------------------------------------- setAdapter


}