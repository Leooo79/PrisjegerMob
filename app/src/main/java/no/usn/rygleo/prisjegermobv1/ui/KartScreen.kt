package no.usn.rygleo.prisjegermobv1.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun KartScreen(prisjegerViewModel: PrisjegerViewModel){


    val lat = prisjegerViewModel.lat
    val lon = prisjegerViewModel.lon
    println(lat)
   //STVG val tt = 58.97265328704611
    //STVG val t1 = 5.73938278829869
    val test = LatLng(lat, lon)
    val cameraPositionState = rememberCameraPositionState {
        position =CameraPosition.fromLatLngZoom(test , 10f)
      //  position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }
      GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ){

    }




}
