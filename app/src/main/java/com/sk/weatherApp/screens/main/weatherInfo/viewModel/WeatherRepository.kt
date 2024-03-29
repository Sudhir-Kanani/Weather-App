package com.sk.weatherApp.screens.main.weatherInfo.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sk.weatherApp.screens.main.weatherInfo.api.MyResponse
import com.sk.weatherApp.screens.main.weatherInfo.api.WeatherApi
import com.sk.weatherApp.screens.main.weatherInfo.model.currentWeather.CurrentWeatherModel
import com.sk.weatherApp.screens.main.weatherInfo.model.fiveDayWeather.FiveDayWeatherModel
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherApi) {

    private val mutableCurrentLiveData = MutableLiveData<MyResponse<CurrentWeatherModel>>()

    private val mutableFiveDayLiveData = MutableLiveData<MyResponse<FiveDayWeatherModel>>()

    val currentLiveData: LiveData<MyResponse<CurrentWeatherModel>>
        get() = mutableCurrentLiveData


    val fiveDayLiveData: LiveData<MyResponse<FiveDayWeatherModel>>
        get() = mutableFiveDayLiveData

    suspend fun getCurrentWeather(
        lat: String,
        lon: String
    ) {
        mutableCurrentLiveData.postValue(MyResponse.Loading())
        try {
            val result = api.getCurrentWeather(lat, lon, "d06e863004d443cd14790086e0dc86ee")
            Log.d("ApiData=> ", "result : $result")
            if (result.body() != null) {
                mutableCurrentLiveData.postValue(MyResponse.Success(result.body()))
            } else if (result.code() == 429) {
                mutableCurrentLiveData.postValue(MyResponse.Error(result.message()))
            } else {
                mutableCurrentLiveData.postValue(MyResponse.Exception(NullPointerException()))
            }
        } catch (e: Exception) {
            mutableCurrentLiveData.postValue(MyResponse.Exception(e))
        }


    }

    suspend fun getFiveDayWeather(
        lat: String,
        lon: String
    ) {
        mutableFiveDayLiveData.postValue(MyResponse.Loading())
        try {
            val result = api.getFiveDayWeather(lat, lon, "d06e863004d443cd14790086e0dc86ee")
            Log.d("ApiData=> ", "result : $result")
            if (result.body() != null) {
                mutableFiveDayLiveData.postValue(MyResponse.Success(result.body()))
            } else if (result.code() == 429) {
                mutableFiveDayLiveData.postValue(MyResponse.Error(result.message()))
            } else {
                mutableFiveDayLiveData.postValue(MyResponse.Exception(NullPointerException()))
            }
        } catch (e: Exception) {
            mutableFiveDayLiveData.postValue(MyResponse.Exception(e))
        }


    }
}