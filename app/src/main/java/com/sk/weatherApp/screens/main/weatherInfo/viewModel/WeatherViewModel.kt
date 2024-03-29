package com.sk.weatherApp.screens.main.weatherInfo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sk.weatherApp.screens.main.weatherInfo.api.MyResponse
import com.sk.weatherApp.screens.main.weatherInfo.model.currentWeather.CurrentWeatherModel
import com.sk.weatherApp.screens.main.weatherInfo.model.fiveDayWeather.FiveDayWeatherModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    val currentLiveData: LiveData<MyResponse<CurrentWeatherModel>>
        get() = weatherRepository.currentLiveData


    val fiveDayLiveData: LiveData<MyResponse<FiveDayWeatherModel>>
        get() = weatherRepository.fiveDayLiveData

    fun callCurrentWeatherAPi(
        lat: String,
        lon: String
    ) {
        viewModelScope.launch {
            weatherRepository.getCurrentWeather(lat, lon)
        }
    }
    fun callFiveDayWeatherAPi(
        lat: String,
        lon: String
    ) {
        viewModelScope.launch {
            weatherRepository.getFiveDayWeather(lat, lon)
        }
    }

}