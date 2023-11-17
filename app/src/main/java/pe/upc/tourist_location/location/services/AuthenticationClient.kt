package pe.upc.tourist_location.location.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthenticationClient {
    companion object {
        private const val BASE_URL = "https://identitytoolkit.googleapis.com/"
        fun create(): AuthenticationService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(AuthenticationService::class.java)
        }
    }
}