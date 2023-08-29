package com.example.myapplication.android

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.weather.CustomThrow
import com.example.myapplication.weather.WeatherApi
import kotlinx.coroutines.launch

class ExampleViewModel:ViewModel() {

    val weatherApi = WeatherApi()

    var firstResultText by mutableStateOf("empty")
    var secondResultText by  mutableStateOf("empty")
    var thirdResultText by  mutableStateOf("empty")

    fun getCity() {
        weatherApi.searchCity("Moscow") {
            firstResultText = it?.cityName.orEmpty()
        }
    }

    fun getWeather() {
        weatherApi.searchCityAndGetWeather("Moscow") { temperature, unit ->
            secondResultText = "$temperature, $unit"
        }
    }

    fun getSuspendWeather() {
        viewModelScope.launch {
            thirdResultText = try {
                //Пример работы с ошибкой через Throwable. Вполне удобно в Android,
                // но совсем неудобно в iOS
                weatherApi.getWeatherSuspend("Moscow")
            } catch (e:CustomThrow) {
                "${e.msg} ${e.code}"
            }
        }
    }



}