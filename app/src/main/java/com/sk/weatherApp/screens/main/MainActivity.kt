package com.sk.weatherApp.screens.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.sk.weatherApp.screens.main.mapView.ui.MapViewScreen
import com.sk.weatherApp.screens.main.mapView.viewModel.MapViewViewModel
import com.sk.weatherApp.screens.main.weatherInfo.ui.WeatherInfoScreen
import com.sk.weatherApp.ui.theme.Weather_AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Weather_AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val locationManager: LocationManager =
                        getSystemService(Context.LOCATION_SERVICE) as LocationManager

                    val permission = listOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )

                    val permissionLauncher = rememberLauncherForActivityResult(
                        ActivityResultContracts.RequestMultiplePermissions()
                    ) {}

                    val locationPermission =
                        rememberMultiplePermissionsState(permissions = permission)

                    var gpsPermission by remember {
                        mutableStateOf(false)
                    }

                    val gpsPermissionForResultLauncher = rememberLauncherForActivityResult(
                        ActivityResultContracts.StartActivityForResult()
                    ) {
                        val isGPSEnabled = locationManager.isProviderEnabled("gps")
                        if (isGPSEnabled) {
                            gpsPermission = true
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Please Enable Gps Location First and re-try !!",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }
                    }

                    val locationPermissionForResultLauncher = rememberLauncherForActivityResult(
                        ActivityResultContracts.StartActivityForResult()
                    ) {}


                    if (locationPermission.allPermissionsGranted) {
                        Log.d("Permission", "Ganted")

                        val isGPSEnabled = locationManager.isProviderEnabled("gps")

                        if (!isGPSEnabled) {
                            Log.d("GPS", "GPS DieEnabled")
                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            SideEffect {
                                gpsPermissionForResultLauncher.launch(intent)
                            }
                        } else {
                            gpsPermission = true
                        }
                        // Permissions granted, proceed with your UI

                    } else if (locationPermission.shouldShowRationale) {

                        Log.d("Permission", "shouldShowRationale")

                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        val uri = Uri.fromParts(
                            "package",
                            packageName, null
                        )
                        intent.setData(uri)
                        SideEffect {
                            locationPermissionForResultLauncher.launch(intent)
                        }
                        Toast.makeText(
                            this@MainActivity,
                            "Location Permission need to show your location  !!",
                            Toast.LENGTH_LONG
                        ).show()
                        // Show rationale or explanation to the user
                    } else {
                        SideEffect {
                            permissionLauncher.launch(permission.toTypedArray())

                        }
                    }


                    if (gpsPermission) {
                        ChooseLocation()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseLocation() {
    val context = LocalContext.current

    val mapViewViewModel: MapViewViewModel = hiltViewModel()
    mapViewViewModel.getCurrentLocation(context)

    val navController = rememberNavController()

    val routeName = listOf(
        "MapView",
        "WeatherInfo"
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = if (currentRoute.equals(routeName[0])) "Choose Location" else "Weather Info") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp),
                titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        )
    }) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            NavHost(navController = navController, startDestination = routeName[0]) {
                composable(route = routeName[0])
                {
                    MapViewScreen(mapViewViewModel) { latLng ->
                        navController.navigate("${routeName[1]}/${latLng.latitude}/${latLng.longitude}") {
                            popUpTo(routeName[1]) { inclusive = true }
                        }
                    }
                }
                composable(route = "${routeName[1]}/{latitude}/{longitude}")
                {
                    BackHandler(
                        enabled = true//condition if you want.
                    ) {
                        navController.popBackStack()
                    }
                    val latitude = it.arguments?.getString("latitude")?.toDouble() ?: 0.0
                    val longitude = it.arguments?.getString("longitude")?.toDouble() ?: 0.0
                    WeatherInfoScreen(latitude, longitude)
                }
            }
        }
    }
}

