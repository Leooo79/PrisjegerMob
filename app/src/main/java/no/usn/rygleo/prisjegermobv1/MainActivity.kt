package no.usn.rygleo.prisjegermobv1

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import no.usn.rygleo.prisjegermobv1.navigasjon.MainScreenView
import no.usn.rygleo.prisjegermobv1.ui.PrisjegerViewModel
import no.usn.rygleo.prisjegermobv1.ui.theme.PrisjegerMobV1Theme

/**
 * Klasse MainActivity er appens entrypoint
 * Spør om tilgang og setter posisjon
 */
@ExperimentalPermissionsApi
class  MainActivity : ComponentActivity() {
    lateinit var fusedLocationClient: FusedLocationProviderClient

    /**
     * Override av onCreate med setContent
     */
    @RequiresApi(Build.VERSION_CODES.N)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContent {
            PrisjegerMobV1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreenView()
                    permission()
                }
            }
        }
    }

    /**
     * Funksjon for å spørre om tilgang til kamera og lokasjon
     * Inspirert av kodemønster
     * Kilde: https://github.com/philipplackner/PermissionHandlingCompose
     */
    @Composable
    fun permission() {
        val permissionsState = rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )
        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(
            key1 = lifecycleOwner,
            effect = {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_START) {
                        permissionsState.launchMultiplePermissionRequest()
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)

                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            permissionsState.permissions.forEach { perm ->
                when (perm.permission) {
                    Manifest.permission.ACCESS_FINE_LOCATION -> {
                        when {
                            perm.hasPermission -> {
                                println("fikk tilgang ")
                                hentSistePosition(fusedLocationClient)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Funksjon for å hente ut posisjon ved hjelp av enhetens GPS
     */
    @SuppressLint("MissingPermission")
    @Composable
    private fun hentSistePosition(fusedLocationProviderClient: FusedLocationProviderClient) {
        println("hentsiste pos starttet")
        val prisjegerViewModel1: PrisjegerViewModel = viewModel()
        val permissionCheck = ContextCompat.checkSelfPermission(applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION)
        val permissioncheck2 = ContextCompat.checkSelfPermission(applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED || permissioncheck2 == PackageManager.PERMISSION_GRANTED ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location : Location? ->
                location?.let {
                    println(location.longitude )
                    println(location.latitude)
                    prisjegerViewModel1.setLokasjon(location.longitude, location.latitude)
                }
            }
        }
    }
}
