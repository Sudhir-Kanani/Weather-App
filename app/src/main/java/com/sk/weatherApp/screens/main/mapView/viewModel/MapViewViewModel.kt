package com.sk.weatherApp.screens.main.mapView.viewModel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class MapViewViewModel @Inject constructor() : ViewModel() {

    private var liveAddress = MutableLiveData<String>()

    private var liveLatlng_ = MutableLiveData<LatLng>()

    val liveAdd: LiveData<String>
        get() = liveAddress


    val liveLatlng: LiveData<LatLng>
        get() = liveLatlng_

    fun getCurrentLocation(context: Context) {

        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled("gps")
        if (!isGPSEnabled) {
            Log.d("GPS", "GPS DieEnabled")
            return
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        locationManager.requestSingleUpdate(
            LocationManager.NETWORK_PROVIDER,
            { location ->

                liveLatlng_.value = LatLng(location.latitude, location.longitude)
//                Log.d("latLng : ", "latLng : ${latLng.latitude}")
//                Log.d("latLng : ", "latLng : ${latLng.longitude}")

                viewModelScope.launch {
                    getLocation(context, liveLatlng_.value)
                }

            },
            null
        )
    }

    suspend fun getLocation(context: Context, centerLatLong: LatLng?) {
        var addressLine = ""
        delay(2000)
        try {

            val fromLocation = centerLatLong?.let {
                Geocoder(context, Locale.getDefault())
                    .getFromLocation(it.latitude, it.longitude, 5)
            }
            if (fromLocation != null && fromLocation.size > 0 && fromLocation[0].getAddressLine(
                    0
                ) != null && fromLocation[0].getAddressLine(
                    0
                ).isNotEmpty()
            ) {
                addressLine = fromLocation[0].getAddressLine(0)

                Log.d("LiveLocation :", "==> addressLine : " + addressLine)
            }
        } catch (ex: Exception) {
            Log.d("LiveLocation :", "==> Exception : " + ex.toString())

            addressLine = ""
            ex.printStackTrace()
        }
        liveAddress.value = addressLine

    }

}