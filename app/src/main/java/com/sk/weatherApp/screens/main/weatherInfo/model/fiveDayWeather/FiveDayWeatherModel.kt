package com.sk.weatherApp.screens.main.weatherInfo.model.fiveDayWeather

data class FiveDayWeatherModel(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<list>,
    val message: Int
)