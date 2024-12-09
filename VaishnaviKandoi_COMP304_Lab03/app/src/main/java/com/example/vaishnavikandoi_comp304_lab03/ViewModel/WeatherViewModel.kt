package com.example.vaishnavikandoi_comp304_lab03.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vaishnavikandoi_comp304_lab03.Model.NetworkResponse
import com.example.vaishnavikandoi_comp304_lab03.Model.RetrofitInstance
import com.example.vaishnavikandoi_comp304_lab03.Model.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel()
{

    val  APIKey = "83ce5dd2819645318fa21613240911"

    private val weatherAPI = RetrofitInstance.weatherAPI
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult


    fun getData(city: String)
    {
        //before it is a success or fail, it will load the data
        _weatherResult.value = NetworkResponse.Loading

        try {
            //Wrap it in a scope because its a suspend function
            viewModelScope.launch {
                val response = weatherAPI.getWeather(APIKey, city)

                if(response.isSuccessful)
                {
                    //Check if there is a body it connection is successful, if there it do:
                    response.body()?.let{
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                }else
                {
                    _weatherResult.value = NetworkResponse.Error("Please Enter a Valid City")

                }
            }
        }catch (e : Exception)
        {
            _weatherResult.value = NetworkResponse.Error("Failed To Load Data")
        }

    }
}