package com.example.myapplication.weather.domain

import com.example.myapplication.weather.domain.model.CitySearchResult
import com.example.myapplication.weather.domain.model.WeatherByCoordinates
import kotlinx.coroutines.flow.Flow

internal interface WeatherUseCase {

    /**
     * Поиск информации о городе по его названию
     */
    suspend fun searchCityUseCase(cityName: String): Flow<CitySearchResult>

    /**
     * Получение погоды по координатам
     *
     */
    suspend fun getWeatherByCoordinatesUseCase(
        latitude: Double,
        longitude: Double,
    ): Flow<WeatherByCoordinates>

    /**
     * Пример любой долгой flow задачи
     */
    suspend fun longTimeRequestUseCase(delayTime: Long, count: Int): Flow<Int>
}