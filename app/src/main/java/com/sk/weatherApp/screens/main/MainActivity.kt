package com.sk.weatherApp.screens.main

import android.Manifest
import android.os.Bundle
import android.util.Log
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
import com.sk.weatherApp.screens.main.mapView.viewModel.MapViewViewModel
import com.sk.weatherApp.screens.main.mapView.ui.MapViewScreen
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
//                    val storagePermission = Manifest.permission.ACCESS_COARSE_LOCATION

                    val permission = listOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )

                    val permissionLauncher = rememberLauncherForActivityResult(
                        ActivityResultContracts.RequestMultiplePermissions()
                    ) {

                    }

                    val externalStoragePermission = rememberMultiplePermissionsState(permissions = permission)


                    if (externalStoragePermission.allPermissionsGranted) {
                        Log.d("Permision","Ganted")
                        // Permissions granted, proceed with your UI
                        ChooseLocation()

                    } else if (externalStoragePermission.shouldShowRationale) {
                        // Show rationale or explanation to the user
                    } else {
                        SideEffect {
                            permissionLauncher.launch(permission.toTypedArray())

                        }
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

