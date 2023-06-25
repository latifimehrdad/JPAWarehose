package com.zarholding.jpacustomer.tools

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Size
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.TilesOverlay
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow


/**
 * Created by m-latifi on 11/19/2022.
 */


class OsmManager(private val map: MapView) {

    //---------------------------------------------------------------------------------------------- mapInitialize
    fun mapInitialize(theme: Int) {
        Configuration.getInstance().userAgentValue = map.context.packageName
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        map.setMultiTouchControls(true)
        map.minZoomLevel = 5.0
        map.maxZoomLevel = 21.0
        map.controller.animateTo(GeoPoint(35.699668, 51.337249)
        ,18.0,1)
        if (theme == android.content.res.Configuration.UI_MODE_NIGHT_YES)
            map.overlayManager.tilesOverlay.setColorFilter(TilesOverlay.INVERT_COLORS) // dark
        map.onResume()
    }
    //---------------------------------------------------------------------------------------------- mapInitialize



    //---------------------------------------------------------------------------------------------- addMarker
    fun addMarker(icon: Drawable, position: GeoPoint, infoWindows: MarkerInfoWindow?): Marker {
        val marker = Marker(map, map.context)
        marker.icon = icon
        marker.position = position
        marker.setInfoWindow(infoWindows)
        map.overlayManager.add(marker)
        map.invalidate()
        moveCamera(position)
        return marker
    }
    //---------------------------------------------------------------------------------------------- addMarker



    //---------------------------------------------------------------------------------------------- createMarkerIconDrawable
    fun createMarkerIconDrawable(size: Size, icon: Int): Drawable {
        val iconStart = Bitmap
            .createScaledBitmap(
                BitmapFactory.decodeResource(map.context.resources, icon),
                size.width,
                size.height,
                true
            )
        return BitmapDrawable(map.context.resources, iconStart)
    }
    //---------------------------------------------------------------------------------------------- createMarkerIconDrawable


    //---------------------------------------------------------------------------------------------- moveCamera
    fun moveCamera(geoPoint: GeoPoint) {
        val mapController: IMapController = map.controller
        mapController.animateTo(geoPoint, 17.0, 1000)
    }
    //---------------------------------------------------------------------------------------------- moveCamera



}