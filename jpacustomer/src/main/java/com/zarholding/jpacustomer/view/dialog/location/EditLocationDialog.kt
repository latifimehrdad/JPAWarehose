package com.zarholding.jpacustomer.view.dialog.location

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.zar.core.enums.EnumApiError
import com.zar.core.tools.manager.ThemeManager
import com.zarholding.jpacustomer.R
import com.zarholding.jpacustomer.databinding.DialogChooseLocationBinding
import com.zarholding.jpacustomer.tools.OsmManager
import com.zarholding.jpacustomer.view.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

/**
 * Created by m-latifi on 5/27/2023.
 */

@AndroidEntryPoint
class EditLocationDialog() : DialogFragment() {


    private val viewModel: EditLocationViewModel by viewModels()
    private lateinit var binding: DialogChooseLocationBinding
    private lateinit var osmManager: OsmManager
    @Inject
    lateinit var themeManagers: ThemeManager

    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogChooseLocationBinding.inflate(inflater, container, false)
        return binding.root
    }
    //---------------------------------------------------------------------------------------------- onCreateView


    //---------------------------------------------------------------------------------------------- onCreateView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lp = WindowManager.LayoutParams()
        val window = dialog?.window
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 50)
        window?.setBackgroundDrawable(inset)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes = lp
        (activity as MainActivity?)?.hideFragmentContainer()
        osmManager = OsmManager(binding.mapView)
        osmManager.mapInitialize(themeManagers.applicationTheme())
        observeLiveDate()
        setListener()
    }
    //---------------------------------------------------------------------------------------------- onCreateView



    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        activity?.let { (it as MainActivity).showMessage(message) }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- observeLiveDate
    private fun observeLiveDate() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
            when (it.type) {
                EnumApiError.UnAuthorization -> (activity as MainActivity?)?.gotoFirstFragment()
                else -> {}
            }
            dismiss()
        }

        viewModel.editLiveData.observe(viewLifecycleOwner) {
            dismiss()
        }
    }
    //---------------------------------------------------------------------------------------------- observeLiveDate



    //---------------------------------------------------------------------------------------------- setListener
    private fun setListener() {
        binding.buttonChoose.setOnClickListener {
            if (binding.buttonChoose.isLoading)
                return@setOnClickListener
            binding.buttonChoose.startLoading(getString(R.string.bePatient))
            val center =
                GeoPoint(binding.mapView.mapCenter.latitude, binding.mapView.mapCenter.longitude)
            viewModel.requestEditCustomerLocation(center.latitude, center.longitude)
        }

        binding.imageViewClose.setOnClickListener {
            dismiss()
        }
    }
    //---------------------------------------------------------------------------------------------- setListener



    //---------------------------------------------------------------------------------------------- onDismiss
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.let {
            (it as MainActivity).showFragmentContainer()
        }
    }
    //---------------------------------------------------------------------------------------------- onDismiss

}