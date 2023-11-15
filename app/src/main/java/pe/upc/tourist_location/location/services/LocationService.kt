package pe.upc.tourist_location.location.services

import pe.upc.tourist_location.location.model.Location
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT

interface LocationService {
    @PUT("gps/update-location/1")
    suspend fun updateLocation(
        @Header("Authorization") token: String,
        @Body location: Location
    ): Response<Location>
}