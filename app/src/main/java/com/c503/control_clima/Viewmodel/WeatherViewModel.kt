package com.c503.control_clima.Viewmodel

import androidx.lifecycle.ViewModel
import com.c503.control_clima.Repository.WeatherRepository
import com.c503.control_clima.Server.ApiServicios
import com.c503.control_clima.Server.Apicliente
import retrofit2.create

class WeatherViewModel(val repository: WeatherRepository): ViewModel() {

    constructor(): this(WeatherRepository(Apicliente().getClient().create(ApiServicios::class.java)))

    fun loadCurrentWeather(lat: Double, lng: Double, unit: String)=
        repository.getCurrentWeather(lat,lng,unit)
}
