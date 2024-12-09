package com.example.vaishnavikandoi_comp304_lab03.Model

//T refers to WeatherModel
sealed class NetworkResponse<out T>
{
    data class Success<out T>(val data : T) : NetworkResponse<T>()
    data class Error(val message : String) : NetworkResponse<Nothing>()
    object Loading : NetworkResponse<Nothing>()
}