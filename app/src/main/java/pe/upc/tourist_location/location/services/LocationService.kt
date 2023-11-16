package pe.upc.tourist_location.location.services

import pe.upc.tourist_location.location.model.TouristLocation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT

interface LocationService {
    @PUT("gps/update-location/1")
    fun updateLocation(
        @Header("Authorization") token: String,
        @Body location: TouristLocation
    ): Call<Void>
}