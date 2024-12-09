package com.example.vaishnavikandoi_comp304_lab03.Model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI
{
    @GET("/v1/current.json")
    suspend fun getWeather(
        @Query("key") APIKey : String,
        @Query("q") city: String
    ) : Response<WeatherModel>
}