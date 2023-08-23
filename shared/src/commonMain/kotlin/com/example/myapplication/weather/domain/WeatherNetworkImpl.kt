package com.example.myapplication.weather.domain

import com.example.myapplication.httpbuilder.HttpBuilder
import com.example.myapplication.httpbuilder.networkGet
import com.example.myapplication.weather.domain.model.CitySearchResult
import com.example.myapplication.weather.domain.model.WeatherByCoordinates
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
    override suspend fun searchCityUseCase(cityName: String): Flow<CitySearchResult> =
        networkGet(
            url = "https://geocoding-api.open-meteo.com/v1/search",
            parameters = listOf("name" to cityName)
        )

    /**
     * Получение погоды по координатам
     *
     */
    override suspend fun getWeatherByCoordinatesUseCase(
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

    /**
     * Пример любой долгой flow задачи
     */
    override suspend fun longTimeRequestUseCase(delayTime: Long, count: Int): Flow<Int> = flow {
        delay(delayTime)
        emit(count)
    }

}