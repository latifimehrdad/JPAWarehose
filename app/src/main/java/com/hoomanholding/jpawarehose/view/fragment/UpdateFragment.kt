package com.hoomanholding.jpawarehose.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hoomanholding.jpawarehose.databinding.FragmentUpdateBinding
import com.hoomanholding.jpawarehose.view.activity.MainActivity
import com.hoomanholding.jpawarehose.viewmodel.UpdateViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Create by Mehrdad on 1/18/2023
 */

@AndroidEntryPoint
class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val updateViewModel : UpdateViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.viewModel = updateViewModel
        return binding.root
    }
    //---------------------------------------------------------------------------------------------- onCreateView


    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        updateViewModel.successLiveData.observe(viewLifecycleOwner) {
            binding.buttonDoUpdate.stopLoading()
            binding.gifImageView.visibility = View.GONE
            (activity as MainActivity).showMessage(it)
        }

        binding.buttonDoUpdate.setOnClickListener {
            if (binding.buttonDoUpdate.isLoading)
                return@setOnClickListener
            binding.gifImageView.visibility = View.VISIBLE
            binding.buttonDoUpdate.startLoading("شکیبا باشید")
            updateViewModel.requestGetData()
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