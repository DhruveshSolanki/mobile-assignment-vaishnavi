package com.vaishnavikandoi.exercise1.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    val locationFlow = MutableStateFlow<LatLng?>(null)

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            location?.let {
                locationFlow.value = LatLng(it.latitude, it.longitude)
            }
        }
    }

    fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(5000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .build()

            try{
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null // Runs on the main thread
                )
            } catch (e: SecurityException) {
                Log.e("Location", "Permission not granted: ${e.message}")
            }
    }

    override fun onCleared() {
        super.onCleared()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}

//catch (e: SecurityException) {
//    Log.e("Location", "Permission not granted: ${e.message}")
//}
