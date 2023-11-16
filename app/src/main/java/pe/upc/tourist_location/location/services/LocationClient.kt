package pe.upc.tourist_location.location.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocationClient {
    companion object {
        private const val BASE_URL = "https://lifetravel-iot-api.azurewebsites.net/api/v1/"
        fun create(): LocationService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(LocationService::class.java)
        }
    }
}