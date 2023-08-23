package com.example.myapplication.weather.domain.model

import com.example.myapplication.weather.model.SearchCityResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CitySearchResult(
    @SerialName("results") val result:List<CitySearchResultDetail> = emptyList()
)

@Serializable
data class CitySearchResultDetail (
    @SerialName("id") val id: Int,
    @SerialName("name") val name:String,
    @SerialName("latitude") val latitude:Double,
    @SerialName("longitude") val longitude:Double,
    @SerialName("feature_code") val featureCode:String,
    @SerialName("country_code") val countryCode:String,
    @SerialName("country") val country:String
)

fun CitySearchResult.toSearchCityResult() = SearchCityResult(
    cityName = this.result.first().name,
    latitude = this.result.first().latitude,
    longitude = this.result.first().longitude,
    country = this.result.first().country
)