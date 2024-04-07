package com.c503.control_clima.Repository

import com.c503.control_clima.Server.ApiServicios

class WeatherRepository(val api: ApiServicios) {

    fun getCurrentWeather(lat: Double, lng: Double, unit: String) =
        api.getCurrentWeather(lat, lng, unit, "54e09bec87fa3a086e6d1c9f57ba49aa")
}