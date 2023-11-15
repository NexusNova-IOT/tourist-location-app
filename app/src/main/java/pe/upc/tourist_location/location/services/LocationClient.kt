package pe.upc.tourist_location.location.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LocationClient {
    private const val BASE_URL = "https://lifetravel-iot-api.azurewebsites.net/api/v1/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val locationService: LocationService = retrofit.create(LocationService::class.java)
}