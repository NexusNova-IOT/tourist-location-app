package pe.upc.tourist_location.location

import android.location.Location
import pe.upc.tourist_location.location.model.AuthRequest
import pe.upc.tourist_location.location.model.AuthResponse
import pe.upc.tourist_location.location.model.TouristLocation
import pe.upc.tourist_location.location.services.AuthenticationClient
import pe.upc.tourist_location.location.services.LocationClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

fun authAndGetToken(): String {
    val authData = AuthRequest("iot@gmail.com", "12345678")
    val authenticationService = AuthenticationClient.create()
    val call = authenticationService.login("AIzaSyAYCuRtIMwuPYgezV8R5-QD373Tx4nhJAg",authData)
    var token = ""

    call.enqueue(object : Callback<AuthResponse> {
        override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
            if (response.isSuccessful) {
                if (response.code() == 200) {
                    val authResponse = response.body()
                    println("Token: ${authResponse?.idToken}")
                    token = authResponse?.idToken.toString();
                } else {
                    println("Call error: ${response.code()}")
                }
            } else {
                println("Call error: ${response.code()}")
            }
        }

        override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
            println("Call error: ${t.message}")
            println(t.stackTraceToString())

            if (t is HttpException) {
                val errorBody = t.response()?.errorBody()?.string()
                println("Error body: $errorBody")
            }
        }
    })
    return "Bearer $token";
}
fun sendLocationToBackend(location: Location?, token: String) {
    location?.let {
        //val token = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImE2YzYzNTNmMmEzZWMxMjg2NTA1MzBkMTVmNmM0Y2Y0NTcxYTQ1NTciLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vbGlmZXRyYXZlbC1hcHAiLCJhdWQiOiJsaWZldHJhdmVsLWFwcCIsImF1dGhfdGltZSI6MTcwMDE3OTE4MSwidXNlcl9pZCI6IjJpRmNqeTJVaHhOdkJET1pNclFkMlZEb0lzeTIiLCJzdWIiOiIyaUZjankyVWh4TnZCRE9aTXJRZDJWRG9Jc3kyIiwiaWF0IjoxNzAwMTc5MTgxLCJleHAiOjE3MDAxODI3ODEsImVtYWlsIjoiaW90QGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJpb3RAZ21haWwuY29tIl19LCJzaWduX2luX3Byb3ZpZGVyIjoicGFzc3dvcmQifX0.H6keAB6rT36PoE7z_v6WcjYCs7WqWU1wFbubXFv2d309ANdn74RKIpcoLOvSlpB6IEQ22KLSTVe0_ButAXxRYqQfs1MMk4ZINkaoqYHy5hAIbrCvBqt1dEh2HyTi619tXkZ4oZU7O55YRr0P_YJOSbP2gmfixMvPiUCej8el184MAOcJZzO65gA_Yq4pTDUG7qzuZEHZkxiLNEa-pxI1t6PyGu6P1TCwk2JHU5Zml4J_OKajzg4CYrN7-1RjuDU8YZS6an0sMkdKDymsqdAw8_1P6GY0yGW4wbuxyHDfreIfiLBHqMJ0s9kQ_9loeQrJ8i9MxNcVM15qGHfnoqEw4w"
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