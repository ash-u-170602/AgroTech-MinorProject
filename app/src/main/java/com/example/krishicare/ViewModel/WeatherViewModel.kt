package com.example.KrishiCare.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.KrishiCare.Repository.WeatherRepository
import com.example.KrishiCare.model.CurrentWeather
import com.example.KrishiCare.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    val currentWeatheer:MutableLiveData<Resource<CurrentWeather>> = MutableLiveData()
    fun getCurrentWeather() = viewModelScope.launch {
        currentWeatheer.postValue(Resource.Loading())
        val response = repository.getCurrentWeather()
        currentWeatheer.postValue(handleCurrentWeatherResponse(response))

    }

    private fun handleCurrentWeatherResponse(response: Response<CurrentWeather>) : Resource<CurrentWeather> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }

        }
        return Resource.Error(response.message())

    }
}





