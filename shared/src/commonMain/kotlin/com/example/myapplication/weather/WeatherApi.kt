package com.example.myapplication.weather

import com.example.myapplication.isAndroid
import com.example.myapplication.weather.domain.WeatherNetworkImpl
import com.example.myapplication.weather.domain.WeatherUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class WeatherApi {

    private val networkImplementation: WeatherUseCase by lazy { WeatherNetworkImpl() }


    /**
     * Пример единичного запроса
     *
     * @param cityName
     * @param success
     * @receiver
     */
    suspend fun getCountryByCity(cityName: String, success: (String) -> Unit) = kmpScope {
        val country = networkImplementation.searchSuspendCity(cityName).country
        success("Country: $country")
    }

    /**
     * Пример работы с flow + имитация долгого запроса
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getWeatherByCity(cityName: String, success: (String) -> Unit) = kmpScope {
        success("get coordinates...")
        networkImplementation.searchCityUseCase(cityName).flatMapLatest { searchResult ->
            success("get weather")
            delay(700)
            success("get weather.")
            delay(700)
            success("get weather..")
            delay(700)
            success("get weather...")
            delay(700)
            networkImplementation.getWeatherByCoordinatesUseCase(
                searchResult.result.first().latitude,
                searchResult.result.first().longitude
            )
        }.collect {
            success("${it.hourly.temperatureList.last()} ${it.units.temperatureUnit}")
        }
    }


}

/**
 * Это выносится в отдельный kmm блок с утилитами
 *
 * @param block
 * @receiver
 * @return
 */
suspend fun kmpScope(block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(
        if (isAndroid()) {
            coroutineContext
        } else {
            SupervisorJob() + Dispatchers.Main
        }
    ).launch(block = block)
}