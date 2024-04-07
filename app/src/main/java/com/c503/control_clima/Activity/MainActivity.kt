package com.c503.control_clima.Activity

import android.graphics.Color
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.view.PointerIcon
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.c503.control_clima.R
import com.c503.control_clima.Viewmodel.WeatherViewModel
import com.c503.control_clima.databinding.ActivityMainBinding
import com.c503.control_clima.model.CurrentResponseApi
import com.github.matteobattilana.weather.PrecipType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
private val weatherViewModel : WeatherViewModel by viewModels()
    private val calendar by lazy { Calendar.getInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }
        binding.apply {

            var lat = 14.969125
            var lon = -89.535122
            var name = "Zacapa"

            ciudadtext.text = name
            progressBar.visibility = View.VISIBLE
            weatherViewModel.loadCurrentWeather(lat, lon, "metric").enqueue(object :
                retrofit2.Callback<CurrentResponseApi>{
                override fun onResponse(
                    call: Call<CurrentResponseApi>,
                    response: Response<CurrentResponseApi>
                ) {

                    if(response.isSuccessful){
                        val data = response.body()
                        progressBar.visibility = View.GONE
                        detailLayout.visibility = View.VISIBLE
                        data?.let {
                            statustext.text = it.weather?.get(0)?.main ?: "-"
                           kmtext.text = it.wind?.speed?.let { Math.round(it).toString() } + "km"
                            porcentajehumedad.text = it.main?.humidity?.toString() +"%"
                            textView6.text = it.main?.temp?.let { Math.round(it).toString() } +"°"
                            maxtempt.text = it.main?.tempMax?.let { Math.round(it).toString() } +"°"
                            mintempt.text = it.main?.tempMin?.let { Math.round(it).toString() } +"°"

                            val drawable = if(isNightNow()) R.drawable.night_bg
                            else{
                                setDynamicallyWallpaper(it.weather?.get(0)?.icon?: "-")
                            }
                            bgImagen.setImageResource(drawable)
                            setEffectRainSnow(it.weather?.get(0)?.icon?: "-")
                        }


                    }
                }

                override fun onFailure(call: Call<CurrentResponseApi>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT).show()
                }

            })
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
    private fun isNightNow(): Boolean{
        return calendar.get(Calendar.HOUR_OF_DAY) >= 18

    }

    private fun setDynamicallyWallpaper(icon: String): Int{

        return when(icon.dropLast(1)){
            "01" ->{
                initWeatherView(PrecipType.CLEAR)
                R.drawable.snow_bg
            }
            "02", "03", "04" ->{
                initWeatherView(PrecipType.CLEAR)
                R.drawable.cloudy_bg
            }
            "09", "10", "11" ->{
                initWeatherView(PrecipType.RAIN)
                R.drawable.rainy_bg
            }
            "13" ->{
                initWeatherView(PrecipType.SNOW)
                R.drawable.snow_bg
            }
            "50" ->{
                initWeatherView(PrecipType.CLEAR)
                R.drawable.haze_bg
            }
            else -> 0
        }
    }
    private fun setEffectRainSnow(icon: String){

         when(icon.dropLast(1)){
            "01" ->{
                initWeatherView(PrecipType.CLEAR)

            }
            "02", "03", "04" ->{
                initWeatherView(PrecipType.CLEAR)

            }
            "09", "10", "11" ->{
                initWeatherView(PrecipType.RAIN)

            }
            "13" ->{
                initWeatherView(PrecipType.SNOW)

            }
            "50" ->{
                initWeatherView(PrecipType.CLEAR)

            }

        }
    }
    private fun initWeatherView(type: PrecipType){
        binding.WeatherView.apply {
            setWeatherData(type)
            angle = 20
            emissionRate = 100.0f
        }
    }
}