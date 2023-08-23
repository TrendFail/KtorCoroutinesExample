package com.example.myapplication.weather.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherByCoordinates (
    @SerialName("hourly_units") val units: WeatherUnits,
    @SerialName("hourly") val hourly:WeatherHourly
)

@Serializable
data class WeatherHourly(
    @SerialName("temperature_2m") val temperatureList:List<Float>
)

@Serializable
data class WeatherUnits(
    @SerialName("temperature_2m") val temperatureUnit:String
)