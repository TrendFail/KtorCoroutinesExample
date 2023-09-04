package com.example.myapplication.weather.domain

import com.example.myapplication.httpbuilder.HttpBuilder
import com.example.myapplication.httpbuilder.networkGet
import com.example.myapplication.httpbuilder.networkGetNotFlow
import com.example.myapplication.weather.domain.model.CitySearchResult
import com.example.myapplication.weather.domain.model.WeatherByCoordinates
import com.example.myapplication.weather.domain.model.toSearchCityResult
import com.example.myapplication.weather.model.SearchCityResult
import kotlinx.coroutines.flow.Flow

/**
 * Доменный слой, позволяющий выполнять шаблонные методы, вместо генерации параметров
 * на стороне viewmodel. Таким образом мы сосредотачиваемся на API сервера без дополнительных
 * комбинаций в разных местах. Один запрос - один ответ
 *
 */
class WeatherNetworkImpl : HttpBuilder(), WeatherUseCase {

    /**
     * Поиск информации о городе по его названию
     */
    override fun searchCityUseCase(cityName: String): Flow<CitySearchResult> =
        networkGet(
            url = "https://geocoding-api.open-meteo.com/v1/search",
            parameters = listOf("name" to cityName)
        )

    /**
     * Получение погоды по координатам
     *
     */
    override fun getWeatherByCoordinatesUseCase(
        latitude: Double,
        longitude: Double,
    ): Flow<WeatherByCoordinates> =
        networkGet(
            url = "https://api.open-meteo.com/v1/forecast",
            parameters = listOf(
                "latitude" to latitude,
                "longitude" to longitude,
                "hourly" to "temperature_2m"
            )
        )

    override suspend fun searchSuspendCity(cityName: String): SearchCityResult =
        networkGetNotFlow<CitySearchResult>(
            url = "https://geocoding-api.open-meteo.com/v1/search",
            parameters = listOf("name" to cityName)
        ).toSearchCityResult()

    override suspend fun getSuspendByCoordinates(
        latitude: Double,
        longitude: Double,
    ): WeatherByCoordinates = networkGetNotFlow(
        url = "https://api.open-meteo.com/v1/forecast",
        parameters = listOf(
            "latitude" to latitude,
            "longitude" to longitude,
            "hourly" to "temperature_2m"
        )
    )


}