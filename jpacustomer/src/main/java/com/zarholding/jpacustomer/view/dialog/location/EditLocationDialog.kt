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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.hoomanholding.applibrary.model.data.database.entity.UserInfoEntity
import com.hoomanholding.applibrary.tools.JpaLocationManager
import com.hoomanholding.applibrary.tools.PermissionManager
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

    @Inject
    lateinit var locationManager: JpaLocationManager

    @Inject
    lateinit var permissionManager: PermissionManager

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
        viewModel.getUserInfo()
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

        viewModel.userLiveData.observe(viewLifecycleOwner) {
            setUserLocationOnMap(it)
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

        binding.imageViewCurrentLocation.setOnClickListener {
            findMyLocation()
        }
    }
    //---------------------------------------------------------------------------------------------- setListener


    //---------------------------------------------------------------------------------------------- setUserLocationOnMap
    private fun setUserLocationOnMap(userInfo: UserInfoEntity) {
        val x = userInfo.x
        val y = userInfo.y
        if (x.compareTo(0) != 0 || y.compareTo(0) != 0){
            val point = GeoPoint(x, y)
            osmManager.moveCamera(point)
        }
    }
    //---------------------------------------------------------------------------------------------- setUserLocationOnMap




    //---------------------------------------------------------------------------------------------- locationPermissionLauncher
    private val locationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            permissionManager.checkPermissionResult(results) {
                if (it)
                    findMyLocation()
            }
        }
    //---------------------------------------------------------------------------------------------- locationPermissionLauncher



    //---------------------------------------------------------------------------------------------- findMyLocation
    private fun findMyLocation() {
        if (context == null)
            return
        locationManager.getCurrentLocation(
            launcher = locationPermissionLauncher,
            onFindCurrentLocation = {
                val current = GeoPoint(it.latitude, it.longitude)
                osmManager.moveCamera(current)
            },
            onFailedMessage = { showMessage(it) }
        )
    }
    //---------------------------------------------------------------------------------------------- findMyLocation



    //---------------------------------------------------------------------------------------------- onDismiss
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.let {
            (it as MainActivity).showFragmentContainer()
        }
    }
    //---------------------------------------------------------------------------------------------- onDismiss

}