package pe.upc.tourist_location.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import pe.upc.tourist_location.location.model.TouristLocation
import pe.upc.tourist_location.location.services.LocationClient
import pe.upc.tourist_location.ui.LocationScreen
import pe.upc.tourist_location.ui.NoPermissionScreen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class MainActivity : ComponentActivity() {
    private val locationManager: LocationManager by lazy {
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private var currentLocation by mutableStateOf<Location?>(null)

    private var hasRequestedPermissions by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        /*
        val token = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImE2YzYzNTNmMmEzZWMxMjg2NTA1MzBkMTVmNmM0Y2Y0NTcxYTQ1NTciLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vbGlmZXRyYXZlbC1hcHAiLCJhdWQiOiJsaWZldHJhdmVsLWFwcCIsImF1dGhfdGltZSI6MTcwMDEwNTEzMiwidXNlcl9pZCI6IjJpRmNqeTJVaHhOdkJET1pNclFkMlZEb0lzeTIiLCJzdWIiOiIyaUZjankyVWh4TnZCRE9aTXJRZDJWRG9Jc3kyIiwiaWF0IjoxNzAwMTA1MTMyLCJleHAiOjE3MDAxMDg3MzIsImVtYWlsIjoiaW90QGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJpb3RAZ21haWwuY29tIl19LCJzaWduX2luX3Byb3ZpZGVyIjoicGFzc3dvcmQifX0.pmAnl6682bgS7Me3J86GS79BG4Nd9bu0N872_GzsnVF7uDtoomr_FuSMmZzmdO5mLxa12sL8PbC2KhNjVuCO2cJELisX4cv37SLh7KJGK2W68P1N5mD0q6w_b-zO-7jNTcVSU8pCfS00XRVCKZxSOFRy4T-eZNcstSBHcFwzz-Q5E6URK3KIWp2tyxtE8eBm1KbZEt0niQ9FY48QcypQpq_Yk8P7tdpLTJOqpaSkEGU0agtVj-_UgtrwM62cutzFP5ycvMjnKhG0RTo38d7OSXKDe322l8SzCvta72mDSZbiuPH-JYERIA9BC4bej3yrL57KPfdV3GzRMl0MxWCetQ"
        super.onCreate(savedInstanceState)
        thread {
            // UPC
            //-12.05302653768251
            //-76.94262350458438
            val locationData = TouristLocation(latitude = -12.05302653768251, longitude = -76.94262350458438)

            val locationService = LocationClient.create()
            val call = locationService.updateLocation(token, locationData)

            //val call = locationService.updateLocation("your_token_here", locationData)

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            println("Operación exitosa.")
                        } else {
                            println("Error en la llamada: ${response.code()}")
                        }
                    } else {
                        println("Error en la llamada: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    println("Error en la llamada: ${t.message}")
                    println(t.stackTraceToString())

                    if (t is HttpException) {
                        val errorBody = t.response()?.errorBody()?.string()
                        println("Error body: $errorBody")
                    }
                }
            })
        }
         */
        super.onCreate(savedInstanceState)
        setContent {
            if (hasLocationPermission()) {
                LocationProvider {
                    LocationScreen(currentLocation)
                }
            } else {
                if (!hasRequestedPermissions) {
                    requestLocationPermission()
                }
                NoPermissionScreen(requestLocationPermission = ::requestLocationPermission)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                hasRequestedPermissions = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (hasLocationPermission()) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun hasLocationPermission(): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        hasRequestedPermissions = fineLocationPermission == PackageManager.PERMISSION_GRANTED && coarseLocationPermission == PackageManager.PERMISSION_GRANTED;
        return hasRequestedPermissions;
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun startLocationUpdates() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun stopLocationUpdates() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    @Composable
    fun LocationProvider(content: @Composable () -> Unit) {
        val location by rememberUpdatedState(currentLocation)
        content()
    }



    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation = locationResult.lastLocation
            if (currentLocation == null || hasLocationChanged(currentLocation, lastLocation)) {
                currentLocation = lastLocation
                sendLocationToBackend(currentLocation)
            }
        }
    }
    private val locationRequest = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private fun hasLocationChanged(oldLocation: Location?, newLocation: Location): Boolean {
        return oldLocation?.latitude != newLocation.latitude || oldLocation.longitude != newLocation.longitude
    }
    private fun sendLocationToBackend(location: Location?) {
        location?.let {
            val token = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImE2YzYzNTNmMmEzZWMxMjg2NTA1MzBkMTVmNmM0Y2Y0NTcxYTQ1NTciLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vbGlmZXRyYXZlbC1hcHAiLCJhdWQiOiJsaWZldHJhdmVsLWFwcCIsImF1dGhfdGltZSI6MTcwMDEwNTEzMiwidXNlcl9pZCI6IjJpRmNqeTJVaHhOdkJET1pNclFkMlZEb0lzeTIiLCJzdWIiOiIyaUZjankyVWh4TnZCRE9aTXJRZDJWRG9Jc3kyIiwiaWF0IjoxNzAwMTA1MTMyLCJleHAiOjE3MDAxMDg3MzIsImVtYWlsIjoiaW90QGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJpb3RAZ21haWwuY29tIl19LCJzaWduX2luX3Byb3ZpZGVyIjoicGFzc3dvcmQifX0.pmAnl6682bgS7Me3J86GS79BG4Nd9bu0N872_GzsnVF7uDtoomr_FuSMmZzmdO5mLxa12sL8PbC2KhNjVuCO2cJELisX4cv37SLh7KJGK2W68P1N5mD0q6w_b-zO-7jNTcVSU8pCfS00XRVCKZxSOFRy4T-eZNcstSBHcFwzz-Q5E6URK3KIWp2tyxtE8eBm1KbZEt0niQ9FY48QcypQpq_Yk8P7tdpLTJOqpaSkEGU0agtVj-_UgtrwM62cutzFP5ycvMjnKhG0RTo38d7OSXKDe322l8SzCvta72mDSZbiuPH-JYERIA9BC4bej3yrL57KPfdV3GzRMl0MxWCetQ"
            val locationData = TouristLocation(latitude = it.latitude, longitude = it.longitude)

            val locationService = LocationClient.create()
            val call = locationService.updateLocation(token, locationData)

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
}
