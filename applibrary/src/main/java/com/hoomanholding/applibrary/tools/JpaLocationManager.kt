package com.hoomanholding.applibrary.tools

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.hoomanholding.applibrary.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by m-latifi on 5/29/2023.
 */

class JpaLocationManager @Inject constructor(
    @ApplicationContext val context: Context
) {

    private var fusedLocationClient: FusedLocationProviderClient? = null

    @Inject
    lateinit var permissionManager: PermissionManager


    //---------------------------------------------------------------------------------------------- getCurrentLocation
    fun getCurrentLocation(
        launcher: ActivityResultLauncher<Array<String>>,
        onFindCurrentLocation: (Location) -> Unit,
        onFailedMessage: (String) -> Unit
    ) {
        requestLocationPermission(
            launcher = launcher,
            onFindCurrentLocation = onFindCurrentLocation,
            onFailedMessage = onFailedMessage
        )
    }
    //---------------------------------------------------------------------------------------------- getCurrentLocation


    //---------------------------------------------------------------------------------------------- requestLocationPermission
    private fun requestLocationPermission(
        launcher: ActivityResultLauncher<Array<String>>,
        onFindCurrentLocation: (Location) -> Unit,
        onFailedMessage: (String) -> Unit
    ) {
        val permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val check = permissionManager.isPermissionGranted(permissions, launcher)
        if (check)
            findCurrentLocation(
                onFindCurrentLocation = onFindCurrentLocation,
                onFailedMessage = onFailedMessage
            )
    }
    //---------------------------------------------------------------------------------------------- requestLocationPermission


    //---------------------------------------------------------------------------------------------- findCurrentLocation
    private fun findCurrentLocation(
        onFindCurrentLocation: (Location) -> Unit,
        onFailedMessage: (String) -> Unit
    ) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && checkLocationEnable(onFailedMessage)
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
                    onFailedMessage(context.getString(R.string.failedGetCurrentLocation))
                else onFindCurrentLocation(location)
            }
        }
    }
    //---------------------------------------------------------------------------------------------- findCurrentLocation


    //---------------------------------------------------------------------------------------------- checkLocationEnable
    private fun checkLocationEnable(onFailedMessage: (String) -> Unit): Boolean {
        val mLocationManager = context
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            true
        else {
            onFailedMessage(context.getString(R.string.pleaseTurnOnGPS))
            false
        }
    }
    //---------------------------------------------------------------------------------------------- checkLocationEnable


}