package com.hoomanholding.applibrary.tools

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.hoomanholding.applibrary.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

/**
 * Created by m-latifi on 5/29/2023.
 */

class JpaLocationManager(
    private val context: Context,
    private val currentLocation: CurrentLocation
    ) {

    private var fusedLocationClient: FusedLocationProviderClient? = null

    interface CurrentLocation {
        fun findCurrentLocation(location: Location)
        fun failedMessage(message: String)
    }


    enum class LocationType{
        CURRENT
    }


    //---------------------------------------------------------------------------------------------- getCurrentLocation
    fun getCurrentLocation() {
        requestLocationPermission(LocationType.CURRENT)
    }
    //---------------------------------------------------------------------------------------------- getCurrentLocation


    //---------------------------------------------------------------------------------------------- requestLocationPermission
    private fun requestLocationPermission(type: LocationType) {
        Dexter.withContext(context)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    when(type){
                        LocationType.CURRENT -> findCurrentLocation()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                }
            })
            .check()
    }
    //---------------------------------------------------------------------------------------------- requestLocationPermission



    //---------------------------------------------------------------------------------------------- findCurrentLocation
    private fun findCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && checkLocationEnable()
        ) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient?.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                        CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                })?.addOnSuccessListener { location: Location? ->
                if (location == null)
                    currentLocation.failedMessage(context.getString(R.string.failedGetCurrentLocation))
                else currentLocation.findCurrentLocation(location)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- findCurrentLocation



    //---------------------------------------------------------------------------------------------- checkLocationEnable
    private fun checkLocationEnable(): Boolean {
        val mLocationManager = context
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            true
        else {
            currentLocation.failedMessage(context.getString(R.string.pleaseTurnOnGPS))
            false
        }
    }
    //---------------------------------------------------------------------------------------------- checkLocationEnable


}