package com.hoomanholding.jpamanager.view.fragment.home

import android.accounts.AccountManager
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.jpamanager.JpaFragment
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentHomeBinding
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.hoomanholding.jpamanager.view.adapter.recycler.HomeItemAdapter
import dagger.hilt.android.AndroidEntryPoint


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
        observeLiveData()
        getUserInfoInActivity()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
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


    //---------------------------------------------------------------------------------------------- observeLiveData
    private fun observeLiveData() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }
        setAdapter()
    }
    //---------------------------------------------------------------------------------------------- observeLiveData


    //---------------------------------------------------------------------------------------------- setAdapter
    private fun setAdapter() {
        if (context == null)
            return
        val adapter = HomeItemAdapter()
        val manager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerItem.layoutManager = manager
        binding.recyclerItem.adapter = adapter
    }
    //---------------------------------------------------------------------------------------------- setAdapter


}