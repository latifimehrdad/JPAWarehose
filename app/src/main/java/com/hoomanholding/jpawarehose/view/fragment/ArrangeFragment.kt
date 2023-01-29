package com.hoomanholding.jpawarehose.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hoomanholding.jpawarehose.databinding.FragmentArrangeBinding
import com.hoomanholding.jpawarehose.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Create by Mehrdad on 1/18/2023
 */

@AndroidEntryPoint
class ArrangeFragment : Fragment() {

    private var _binding: FragmentArrangeBinding? = null
    private val binding get() = _binding!!


    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArrangeBinding.inflate(inflater, container, false)
        return binding.root
    }
    //---------------------------------------------------------------------------------------------- onCreateView


    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- onDestroyView
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //---------------------------------------------------------------------------------------------- onDestroyView


}