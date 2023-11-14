package pe.upc.tourist_location.ui

import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import pe.upc.tourist_location.R

@Composable
fun NoPermissionScreen(requestLocationPermission: () -> Unit) {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "The app needs location permission.",
      fontSize = 16.sp,
      modifier = Modifier.padding(16.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Button(
      onClick = { requestLocationPermission() },
      modifier = Modifier.padding(8.dp),
    ) {
      Text(
        text = "Request location permission",
        fontSize = 17.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(5.dp)
      )
    }

  }
}
@Composable
fun LocationScreen(location: Location?) {
  println(location)
  if (location != null) {
    Text(
      modifier = Modifier.fillMaxWidth().padding(5.dp),
      textAlign = TextAlign.Center,
      color = MaterialTheme.colors.primary,
      text = "Latitude: ${location.latitude}, Longitude: ${location.longitude}"
    )
  } else {
    Text(
      modifier = Modifier.fillMaxWidth(),
      textAlign = TextAlign.Center,
      color = MaterialTheme.colors.primary,
      text = stringResource(R.string.location_not_available)
    )
  }
}
