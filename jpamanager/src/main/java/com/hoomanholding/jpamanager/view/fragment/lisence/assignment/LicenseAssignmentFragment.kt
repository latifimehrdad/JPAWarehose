package com.hoomanholding.jpamanager.view.fragment.lisence.assignment

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.applibrary.ext.config
import com.hoomanholding.applibrary.ext.startLoading
import com.hoomanholding.applibrary.ext.stopLoading
import com.hoomanholding.applibrary.model.data.response.user.UserModel
import com.hoomanholding.applibrary.tools.getShimmerBuild
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentLicenseAssignmentBinding
import com.hoomanholding.jpamanager.view.activity.MainActivity
import com.hoomanholding.jpamanager.view.adapter.recycler.UserAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * create by m-latifi on 3/6/2023
 */

@AndroidEntryPoint
class LicenseAssignmentFragment(override var layout: Int = R.layout.fragment_license_assignment) :
    JpaFragment<FragmentLicenseAssignmentBinding>() {

    private val viewModel: LicenseAssignmentViewModel by viewModels()

    private var job: Job? = null

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        binding.buttonConfirm.stopLoading()
        binding.shimmerViewContainer.stopLoading()
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- showMessage
    private fun initView() {
        binding.shimmerViewContainer.config(getShimmerBuild())
        setListener()
        observeLiveData()
        getAllCustomer()
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.textInputEditTextSearch.addTextChangedListener {
            job?.cancel()
            createJobForSearch(it.toString())
        }

        binding.buttonConfirm.setOnClickListener {
            if (binding.buttonConfirm.isLoading)
                return@setOnClickListener
            binding.buttonConfirm.startLoading(getString(R.string.bePatient))
            viewModel.requestAddCustomerLicensing()
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- observeLiveData
    private fun observeLiveData() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            showMessage(it.message)
        }


        viewModel.usersLiveData.observe(viewLifecycleOwner) {
            binding.shimmerViewContainer.stopLoading()
            setItemAdapter(it)
        }

        viewModel.successLiveDate.observe(viewLifecycleOwner) {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveData


    //---------------------------------------------------------------------------------------------- createJobForSearch
    private fun createJobForSearch(search: String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            delay(800)
            withContext(Dispatchers.Main) {
                viewModel.searchUser(search)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- createJobForSearch


    //---------------------------------------------------------------------------------------------- getAllCustomer
    private fun getAllCustomer() {
        binding.shimmerViewContainer.startLoading()
        viewModel.getAllCustomers(binding.textInputEditTextSearch.text.toString())
    }
    //---------------------------------------------------------------------------------------------- getAllCustomer


    //---------------------------------------------------------------------------------------------- setItemAdapter
    private fun setItemAdapter(items: List<UserModel>?) {
        if (context == null)
            return
        if (items.isNullOrEmpty()) {
            binding.recyclerItem.adapter = null
            return
        }

        val adapter = UserAdapter(items)
        val manager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerItem.layoutManager = manager
        binding.recyclerItem.adapter = adapter
    }
    //---------------------------------------------------------------------------------------------- setItemAdapter


    //---------------------------------------------------------------------------------------------- onDestroyView
    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()
    }
    //---------------------------------------------------------------------------------------------- onDestroyView
}