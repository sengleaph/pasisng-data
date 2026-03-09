package com.sifu.passdatascreen.locatioin

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng

data class MapMarker(
    val id: Int,
    val position: LatLng,
    val title: String,
    val snippet: String,
    val hue: Float = BitmapDescriptorFactory.HUE_RED
)

val sampleMarkers = listOf(
    MapMarker(1,
        LatLng(37.7749, -122.4194), "San Francisco", "The Golden Gate City", BitmapDescriptorFactory.HUE_RED),
    MapMarker(2, LatLng(37.8044, -122.2712), "Oakland",       "East Bay Hub",         BitmapDescriptorFactory.HUE_AZURE),
    MapMarker(3, LatLng(37.3382, -121.8863), "San Jose",      "Silicon Valley Core",  BitmapDescriptorFactory.HUE_GREEN),
    MapMarker(4, LatLng(37.5485, -121.9886), "Fremont",       "Innovation City",      BitmapDescriptorFactory.HUE_ORANGE),
    MapMarker(5, LatLng(37.6879, -122.4702), "Daly City",     "Northern Gateway",     BitmapDescriptorFactory.HUE_VIOLET),
)