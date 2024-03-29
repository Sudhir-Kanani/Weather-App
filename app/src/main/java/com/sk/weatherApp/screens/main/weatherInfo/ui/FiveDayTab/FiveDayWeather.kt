package com.sk.weatherApp.screens.main.weatherInfo.ui.FiveDayTab

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.asFlow
import com.sk.weatherApp.screens.main.weatherInfo.api.MyResponse
import com.sk.weatherApp.screens.main.weatherInfo.model.fiveDayWeather.FiveDayWeatherModel
import com.sk.weatherApp.screens.main.weatherInfo.viewModel.WeatherViewModel

@Composable
fun FiveDayWeather(weatherViewModel: WeatherViewModel, latitude: Double, longitude: Double) {

    LaunchedEffect(key1 = latitude) {
        weatherViewModel.callFiveDayWeatherAPi(latitude.toString(), longitude.toString())
    }

    val response by weatherViewModel.fiveDayLiveData.asFlow()
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
                DateList(result)

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

@Composable
fun DateList(result: FiveDayWeatherModel) {

    val dataList = result.list

    Log.d("dataList : ", "dataList : ${dataList.size}")

    var selectedData by remember {
        mutableIntStateOf(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyRow {
            itemsIndexed(dataList) { index, item ->
                DateListRow(index, item, selectedData) {
                    selectedData = it
                }
            }
        }

        Spacer(modifier = Modifier.size(20.dp))

        FiveDayWeatherCard(result = dataList.get(selectedData))
    }


}


