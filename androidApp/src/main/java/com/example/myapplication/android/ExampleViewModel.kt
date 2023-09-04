package com.example.myapplication.android

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.weather.WeatherApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ExampleViewModel : ViewModel() {

    private val weatherApi by lazy { WeatherApi() }

    var requestStatus by mutableStateOf("")
    var countyResult by mutableStateOf("")
    var weatherResult by mutableStateOf("")

    private var cancellable: Job? = null

    fun stopRequestManually() {
        cancellable?.cancel()
        requestStatus = "cancelled"
        countyResult = ""
        weatherResult = ""
    }

    fun getCountryByCity() {
        viewModelScope.launch {
            requestStatus = "get country..."
            weatherApi.getCountryByCity("Moscow") { result ->
                requestStatus = "Complete"
                countyResult = result
            }
        }
    }

    fun getWeatherByCity() {
        viewModelScope.launch {
            requestStatus = "get weather..."
            cancellable = weatherApi.getWeatherByCity("Moscow") {result ->
                requestStatus = "Complete"
                weatherResult = result
            }
        }
    }


}