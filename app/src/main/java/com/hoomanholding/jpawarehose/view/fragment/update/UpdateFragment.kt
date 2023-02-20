package com.hoomanholding.jpawarehose.view.fragment.update

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.hoomanholding.jpawarehose.JpaFragment
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.databinding.FragmentUpdateBinding
import com.hoomanholding.jpawarehose.view.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Create by Mehrdad on 1/18/2023
 */

@AndroidEntryPoint
class UpdateFragment(override var layout: Int = R.layout.fragment_update) :
    JpaFragment<FragmentUpdateBinding>() {

    private val updateViewModel : UpdateViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = updateViewModel
        observeLiveData()
        setListener()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- observeLiveData
    private fun observeLiveData() {
        updateViewModel.successLiveData.observe(viewLifecycleOwner) {
            binding.buttonDoUpdate.stopLoading()
            binding.gifImageView.visibility = View.GONE
            binding.textViewPercent.visibility = View.GONE
            (activity as MainActivity).showMessage(it)
        }

        updateViewModel.percentUpdatingLiveData.observe(viewLifecycleOwner) {
            val title = "$it%"
            binding.textViewPercent.text = title
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveData


    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.buttonDoUpdate.setOnClickListener {
            if (binding.buttonDoUpdate.isLoading)
                return@setOnClickListener
            binding.gifImageView.visibility = View.VISIBLE
            binding.textViewPercent.visibility = View.VISIBLE
            binding.buttonDoUpdate.startLoading("شکیبا باشید")
            updateViewModel.requestGetData()
        }
    }
    //---------------------------------------------------------------------------------------------- setListener

}