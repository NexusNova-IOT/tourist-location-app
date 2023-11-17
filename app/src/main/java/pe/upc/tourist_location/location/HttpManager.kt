package pe.upc.tourist_location.location

import android.location.Location
import kotlinx.coroutines.ExperimentalCoroutinesApi
import pe.upc.tourist_location.location.model.AuthRequest
import pe.upc.tourist_location.location.model.TouristLocation
import pe.upc.tourist_location.location.services.AuthenticationClient
import pe.upc.tourist_location.location.services.LocationClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

suspend fun authAndGetToken(): String {
    return try {
        val authData = AuthRequest("iot@gmail.com", "12345678")
        val authenticationService = AuthenticationClient.create()
        val authResponse = authenticationService.login("AIzaSyAYCuRtIMwuPYgezV8R5-QD373Tx4nhJAg", authData)
        "Bearer ${authResponse.idToken}"
    } catch (e: Exception) {
        throw Exception("Authentication failed", e)
    }
}

fun sendLocationToBackend(location: Location?, token: String) {
    location?.let {
        val locationData = TouristLocation(latitude = it.latitude, longitude = it.longitude)

        val locationService = LocationClient.create()
        val call = locationService.updateLocation(1, token, locationData)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        println("Successful operation.")
                    } else {
                        println("Call error: ${response.code()}")
                    }
                } else {
                    println("Call error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Call error: ${t.message}")
                println(t.stackTraceToString())

                if (t is HttpException) {
                    val errorBody = t.response()?.errorBody()?.string()
                    println("Error body: $errorBody")
                }
            }
        })
    }
}