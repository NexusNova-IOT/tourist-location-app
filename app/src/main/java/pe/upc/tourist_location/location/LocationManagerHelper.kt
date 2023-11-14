package pe.upc.tourist_location.location
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.compose.runtime.MutableState
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
class LocationManagerHelper (private val context: Context) {
  private val locationManager: LocationManager by lazy {
    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
  }
  fun has2LocationPermission(): Boolean {
    // Lógica para verificar permisos de ubicación
    return true;
  }
  fun hasLocationPermission(): Boolean {
    val fineLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
    val coarseLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
    //hasRequestedPermissions = fineLocationPermission == PackageManager.PERMISSION_GRANTED && coarseLocationPermission == PackageManager.PERMISSION_GRANTED;
    return true;
    //return hasRequestedPermissions;
  }

  fun requestLocationPermission() {
    // Lógica para solicitar permisos de ubicación
  }

  fun startLocationUpdates(callback: LocationCallback) {
    // TODO: Lógica para iniciar actualizaciones de ubicación
  }

  fun stopLocationUpdates(callback: LocationCallback) {
    // TODO: Lógica para detener actualizaciones de ubicación
  }
}