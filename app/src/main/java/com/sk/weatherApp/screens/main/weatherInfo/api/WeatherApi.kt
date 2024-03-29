package com.sk.weatherApp.screens.main.weatherInfo.api

import com.sk.weatherApp.screens.main.weatherInfo.model.currentWeather.CurrentWeatherModel
import com.sk.weatherApp.screens.main.weatherInfo.model.fiveDayWeather.FiveDayWeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather?")
    suspend fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String,
    ): Response<CurrentWeatherModel>


    @GET("forecast?")
    suspend fun getFiveDayWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String,
    ): Response<FiveDayWeatherModel>
}