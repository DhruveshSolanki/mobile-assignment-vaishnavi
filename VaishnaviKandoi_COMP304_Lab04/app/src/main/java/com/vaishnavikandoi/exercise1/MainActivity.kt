package com.vaishnavikandoi.exercise1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.vaishnavikandoi.exercise1.ViewModel.LocationViewModel
import com.vaishnavikandoi.exercise1.ui.theme.VaishnaviKandoi_COMP304Lab4_Ex1Theme
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val workRequest = PeriodicWorkRequestBuilder<DataSyncWorker>(1, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)

        setContent {
            VaishnaviKandoi_COMP304Lab4_Ex1Theme {
                val locationViewModel: LocationViewModel = hiltViewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RequestLocationPermission {
                        // Start location updates after permission is granted
                        locationViewModel.startLocationUpdates()

                        // Observe location and display the map
                        val location = locationViewModel.locationFlow.collectAsState().value
                        RealTimeMap(location)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(onPermissionGranted: @Composable () -> Unit) {
    val locationPermissionState = rememberPermissionState(
        permission = android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    when (locationPermissionState.status) {
        is PermissionStatus.Granted -> {
            // Permission has been granted
            onPermissionGranted()
        }
        is PermissionStatus.Denied -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()  // Fill the entire screen
                    .padding(16.dp),  // Add some padding around
                horizontalAlignment = Alignment.CenterHorizontally,  // Center horizontally
                verticalArrangement = Arrangement.Center  // Center vertically
            ) {
                Text("Required Permission to access the location")
                Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                    Text("Allow me to access location")
                }
            }
        }
    }
}

@Composable
fun RealTimeMap(location: LatLng?) {
    val defaultLocation = LatLng(43.7735, -79.2578)

    // Camera position state
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
            location ?: defaultLocation, // Use real-time location or default
            12f
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // Add a marker if location is available
        location?.let {
            Marker(
                state = com.google.maps.android.compose.MarkerState(position = it),
                title = "You are here",
                snippet = "Your current location"
            )
        }
    }
}