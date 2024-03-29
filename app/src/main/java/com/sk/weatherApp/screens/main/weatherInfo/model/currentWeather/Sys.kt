package com.sk.weatherApp.screens.main.weatherInfo.model.currentWeather

data class Sys(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)