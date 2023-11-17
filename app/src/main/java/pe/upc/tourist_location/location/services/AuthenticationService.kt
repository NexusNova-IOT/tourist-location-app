package pe.upc.tourist_location.location.services

import pe.upc.tourist_location.location.model.AuthRequest
import pe.upc.tourist_location.location.model.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationService {
    @POST("v1/accounts:signInWithPassword")
    fun login(
        @Query("key") apiKey: String,
        @Body authRequest: AuthRequest
    ): Call<AuthResponse>
}