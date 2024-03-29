package com.sk.weatherApp.screens.main.mapView.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState
import com.sk.weatherApp.R
import com.sk.weatherApp.screens.main.mapView.viewModel.MapViewViewModel

@Composable
fun MapViewScreen(mapViewViewModel: MapViewViewModel, onChooseCLick : (latlng : LatLng)->Unit) {

    val context = LocalContext.current

    var address by remember {
        mutableStateOf("")
    }

    var location by remember {
        mutableStateOf(LatLng(1.35, 103.87))
    }

    var selectedLatLng by remember {
        mutableStateOf(location)
    }

    val cameraPositionState = rememberCameraPositionState()

    mapViewViewModel.liveAdd.observe(LocalLifecycleOwner.current){
         address = it
    }
    mapViewViewModel.liveLatlng.observe(LocalLifecycleOwner.current){
        location= it

        Log.d("UpdateLoc : ","Location Update")

    }

    LaunchedEffect(key1 = location) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newCameraPosition(
                CameraPosition(location, 10F, 0f, 0f)
            ),
            durationMs = 1000
        )
    }


    Log.d("address","address : $address")

    LaunchedEffect(key1 = cameraPositionState.isMoving) {

        if (cameraPositionState.isMoving) {
            address = ""
        } else {
            val centerLatLong = cameraPositionState.projection?.visibleRegion?.latLngBounds?.center
            centerLatLong?.let {
                selectedLatLng = it
                mapViewViewModel.getLocation(context, it)
            }
//            Log.d("LiveLocation : ", "address : ${address}")
//            Log.d("LiveLocation : ", "latitude : ${centerLatLong?.latitude}")
//            Log.d("LiveLocation : ", "longitude : ${centerLatLong?.longitude}")
        }


    }

    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1F),
            contentAlignment = Alignment.Center
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = true
                )
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_map_pin), contentDescription = "LocationPin"
            )
        }
        Spacer(modifier = Modifier.size(20.dp))

        Text(
            text = "Selected Location",
            modifier = Modifier
                .padding(horizontal = 20.dp),
            textAlign = TextAlign.Start,
            style = TextStyle(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 17.sp,
            )
        )
        Spacer(modifier = Modifier.size(5.dp))

        Text(
            text = address,
            modifier = Modifier
                .padding(horizontal = 20.dp),
            textAlign = TextAlign.Start,
            maxLines = 2,
            style = TextStyle(
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                fontSize = 15.sp,
            )
        )

        Spacer(modifier = Modifier.size(20.dp))

        if (address.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.padding(start = 20.dp, top = 10.dp))
        } else {
            Button(
                onClick = {
                    onChooseCLick(selectedLatLng)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Choose",
                    style = TextStyle(
                        fontSize = 15.sp,
                    )
                )
            }
        }


        Spacer(modifier = Modifier.size(20.dp))

    }
}