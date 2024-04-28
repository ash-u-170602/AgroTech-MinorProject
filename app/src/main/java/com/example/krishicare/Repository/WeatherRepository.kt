package com.example.KrishiCare.Repository

import com.example.KrishiCare.api.RetrofitInstance

class WeatherRepository() {
    suspend fun getCurrentWeather() =
      RetrofitInstance.api.getCurrentWeather()
}
