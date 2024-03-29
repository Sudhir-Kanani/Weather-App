package com.sk.weatherApp.screens.main.weatherInfo.ui.currentTab

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.asFlow
import com.sk.weatherApp.screens.main.weatherInfo.api.MyResponse
import com.sk.weatherApp.screens.main.weatherInfo.viewModel.WeatherViewModel

@Composable
fun CurrentWeather(weatherViewModel: WeatherViewModel, latitude: Double, longitude: Double) {

    LaunchedEffect(key1 = latitude) {
        weatherViewModel.callCurrentWeatherAPi(latitude.toString(), longitude.toString())
    }

    val response by weatherViewModel.currentLiveData.asFlow()
        .collectAsState(initial = MyResponse.Loading())

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (response) {
            is MyResponse.Loading -> {
                CircularProgressIndicator(Modifier.size(40.dp))
            }

            is MyResponse.Success -> {
                val result = (response as MyResponse.Success).data!!
//                Log.d("response : ", "State : ${result}")
                WeatherData(result)
            }

            is MyResponse.Error -> {
                Log.d("response : ", "State : ${response}")
                Text(
                    text = "Error : ${response.error.toString()}",
                    style = TextStyle(fontSize = 15.sp)
                )


            }

            is MyResponse.Exception -> {
                Text(text = response.exception.toString(), style = TextStyle(fontSize = 15.sp))
                Log.d("response : ", "State : ${response.exception.toString()}")
            }
        }
    }
}


