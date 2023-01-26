package com.hoomanholding.jpawarehose.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoomanholding.jpawarehose.R
import com.hoomanholding.jpawarehose.databinding.FragmentHomeBinding
import com.hoomanholding.jpawarehose.databinding.FragmentSaveReceiptBinding
import com.hoomanholding.jpawarehose.viewmodel.SaveReceiptViewModel
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem
import dagger.hilt.android.AndroidEntryPoint

/**
 * Create by Mehrdad on 1/18/2023
 */

@AndroidEntryPoint
class SaveReceiptFragment : Fragment() {

    private var _binding: FragmentSaveReceiptBinding? = null
    private val binding get() = _binding!!

    private val saveReceiptViewModel : SaveReceiptViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }
    //---------------------------------------------------------------------------------------------- onCreateView


    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        setListener()
        initOriginSpinner()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated



    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {

    }
    //---------------------------------------------------------------------------------------------- setListener



    //---------------------------------------------------------------------------------------------- initOriginSpinner
    private fun initOriginSpinner() {

            val items = saveReceiptViewModel.getSuppliers().map {
                it.nameTaminKonandeh?.let { name ->
                    IconSpinnerItem(name)
                }
            }

            binding.powerSpinnerSupplier.apply {
                setSpinnerAdapter(IconSpinnerAdapter(this))
                setItems(items)
                getSpinnerRecyclerView().layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                lifecycleOwner = viewLifecycleOwner

                setOnSpinnerItemSelectedListener<IconSpinnerItem> { _, _, newIndex, _ ->
                    binding.powerSpinnerSupplier.showArrow = true
                    binding.powerSpinnerSupplier.setTextColor()
                }
            }
    }
    //---------------------------------------------------------------------------------------------- initOriginSpinner



/*
    //---------------------------------------------------------------------------------------------- powerSpinnerSupplierClick
    private fun powerSpinnerSupplierClick() {
        if (context == null)
            return
        taxiReservationViewModel.getOriginMarker()?.let {
            ConfirmDialog(requireContext(),
                ConfirmDialog.ConfirmType.DELETE,
                getString(R.string.originLocationIsSelect),
                object : ConfirmDialog.Click {
                    override fun clickYes() {
                        activity?.onBackPressedDispatcher?.onBackPressed()
                    }
                }).show()
        } ?: run {
            if (binding.powerSpinnerOrigin.isShowing)
                binding.powerSpinnerOrigin.dismiss()
            else
                binding.powerSpinnerOrigin.show()
        }
    }
    //---------------------------------------------------------------------------------------------- powerSpinnerSupplierClick

*/


    //---------------------------------------------------------------------------------------------- onDestroyView
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //---------------------------------------------------------------------------------------------- onDestroyView


}