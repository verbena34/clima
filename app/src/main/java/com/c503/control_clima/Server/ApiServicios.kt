package com.c503.control_clima.Server

import android.telecom.Call
import com.c503.control_clima.model.CurrentResponseApi
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServicios {

    @GET("data/2.5/weather")

    fun getCurrentWeather(

        @Query("lat") lat:Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("Appid") Apikey: String,
    ): retrofit2.Call<CurrentResponseApi>
}