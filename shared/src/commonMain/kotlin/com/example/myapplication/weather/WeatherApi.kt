package com.example.myapplication.weather

import com.example.myapplication.httpbuilder.NetworkHelper
import com.example.myapplication.weather.domain.WeatherNetworkImpl
import com.example.myapplication.weather.domain.WeatherUseCase
import com.example.myapplication.weather.domain.model.toSearchCityResult
import com.example.myapplication.weather.model.SearchCityResult
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class WeatherApi : NetworkHelper() {

    private val networkImplementation: WeatherUseCase by lazy { WeatherNetworkImpl() }

    /**
     * Пример единичного запроса
     * выполнение запроса происходит асинхронно. В callback возвращается смапленный от сервера результат
     * В данном случае производим поиск городов по названию, после чего возвращаем первый попавшийся результат
     */
    fun searchCity(cityName: String, onResult: (SearchCityResult?) -> Unit) {
        withAsync {
            networkImplementation.searchCityUseCase(cityName).collect {
                onResult(it.toSearchCityResult())
            }
        }
    }

    /**
     * Пример выполнения последовательных запросов, когда второй запрос зависит от результатов первого
     * В данном случае сначала ищем город по названию, получаем его координаты
     * затем запрашиваем погоду для указанных координат
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun searchCityAndGetWeather(
        cityName: String,
        onResult: (temperature: String, unit: String) -> Unit,
    ) {
        withAsync {
            //Первый запрос
            networkImplementation.searchCityUseCase(cityName)
                .flatMapLatest { searchResult ->
                    //Можем обработать первый запрос
                    onResult("city", searchResult.result.first().name)
                    //Добавим задержку для имитации долгой работы
                    println("wait 3 seconds")
                    delay(3000)
                    println("3 seconds complete")
                    //Второй запрос, для которого нужны значения из первого
                    networkImplementation.getWeatherByCoordinatesUseCase(
                        searchResult.result.first().latitude,
                        searchResult.result.first().longitude
                    )
                }.collect {
                    onResult(
                        it.hourly.temperatureList.last().toString(),
                        it.units.temperatureUnit
                    )
                }
        }
    }

    /**
     * Пример параллельных множественных запросов. Каждый запрос выполняется асинхронно и бросает
     * свой результат в callback
     */
    fun combinedApi(onResult: (String) -> Unit) {
        withAsync {
            networkImplementation.longTimeRequestUseCase(100L, 1)
                .collect { onResult(it.toString()) }
        }
        withAsync {
            networkImplementation.longTimeRequestUseCase(2500L, 2)
                .collect { onResult(it.toString()) }
        }
        withAsync {
            networkImplementation.longTimeRequestUseCase(300L, 3)
                .collect { onResult(it.toString()) }
        }
        withAsync {
            networkImplementation.longTimeRequestUseCase(3000L, 4)
                .collect { onResult(it.toString()) }
        }
        withAsync {
            networkImplementation.longTimeRequestUseCase(1500L, 5)
                .collect { onResult(it.toString()) }
        }
        withAsync {
            networkImplementation.longTimeRequestUseCase(500L, 6)
                .collect { onResult(it.toString()) }
        }
        withAsync {
            networkImplementation.longTimeRequestUseCase(1000L, 7)
                .collect { onResult(it.toString()) }
        }
        withAsync {
            networkImplementation.longTimeRequestUseCase(200L, 8)
                .collect { onResult(it.toString()) }
        }
        withAsync {
            networkImplementation.longTimeRequestUseCase(2000L, 9)
                .collect { onResult(it.toString()) }
        }
        withAsync {
            networkImplementation.longTimeRequestUseCase(400L, 10)
                .collect { onResult(it.toString()) }
        }
    }

    @Throws(CustomThrow::class, CancellationException::class)
    suspend fun getWeatherSuspend(cityName: String): String {
        coroutineScope {
            launch {
                println("async work 1 start")
                delay(3000)
                println("async work 1 end")
            }

            launch {
                println("async work 2 start")
                delay(500)
                println("async work 2 end")
            }
        }
        println("Start common work")
        val cityInfo = networkImplementation.searchSuspendCity(cityName)
        val weather = networkImplementation.getSuspendByCoordinates(
            latitude = cityInfo.latitude,
            longitude = cityInfo.longitude
        )
        return "${weather.hourly.temperatureList.last()}, ${weather.units.temperatureUnit}"
    }




}