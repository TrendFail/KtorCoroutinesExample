package com.example.myapplication.weather.domain

import com.example.myapplication.weather.domain.model.CitySearchResult
import com.example.myapplication.weather.domain.model.WeatherByCoordinates
import com.example.myapplication.weather.model.SearchCityResult
import kotlinx.coroutines.flow.Flow

interface WeatherUseCase {

    /**
     * Поиск информации о городе по его названию
     */
    fun searchCityUseCase(cityName: String): Flow<Unit>

    /**
     * Получение погоды по координатам
     *
     */
    fun getWeatherByCoordinatesUseCase(
        latitude: Double,
        longitude: Double,
    ): Flow<WeatherByCoordinates>

    /**
     * Пример любой долгой flow задачи
     */

    suspend fun searchSuspendCity(cityName:String): SearchCityResult

    suspend fun getSuspendByCoordinates(
        latitude: Double,
        longitude: Double,
    ): WeatherByCoordinates
}