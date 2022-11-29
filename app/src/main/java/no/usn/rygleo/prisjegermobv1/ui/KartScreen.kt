package no.usn.rygleo.prisjegermobv1.ui

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import no.usn.rygleo.prisjegermobv1.R

@Composable
fun KartScreen(prisjegerViewModel: PrisjegerViewModel){
    val latRema = 59.41045516399832
    val lonRema = 9.066418297439066
    val latKiwi =59.41377452116864
    val lonKiwi =9.067920334446951
    val latMeny = 59.41132797949191
    val lonMeny = 9.070526839182124
    val latXtra =59.414156786719886
    val lonXtra = 9.063982865007917

    val meny = LatLng(59.41132797949191,9.070526839182124)
    val rema = LatLng( 59.41045516399832,9.066418297439066)
    val kiwi = LatLng(59.41377452116864,9.067920334446951 )
    val xtra = LatLng(59.414156786719886,9.063982865007917 )

    val lat = prisjegerViewModel.lat
    val lon = prisjegerViewModel.lon
    println(lat)
    val test = LatLng(lat, lon)
    val cameraPositionState = rememberCameraPositionState {
        position =CameraPosition.fromLatLngZoom(test , 14f)
    }

      GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ){
          Marker(
              state = MarkerState(position = kiwi),
              title = "Kiwi",
              snippet = "nærmeste Kiwi"
          )
          Marker(
              state = MarkerState(position = meny),
              title = "Meny",
              snippet = "nærmeste Meny",

              )
          Marker(
              state = MarkerState(position = xtra),
              title = "extra",
              snippet = "nærmeste xtra"
          )
          Marker(
              state = MarkerState(position = rema),
              title = "Rema 1000",
              snippet = "nærmeste rema"
          )
          Marker(
              state = MarkerState(position = test),
              title = "du er her",
              snippet = "du er her",
              icon = (bitmapDescriptorFromVector(LocalContext.current, R.drawable.ic_baseline_my_location_24) )
          )


      }




}
/**
 * brukte stoff fra https://towardsdev.com/jetpack-compose-custom-google-map-marker-erselan-khan-e6e04178a30b
 * til å oprette et custom markør ikon i GoogleMap objektet
 */
fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {

    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}

