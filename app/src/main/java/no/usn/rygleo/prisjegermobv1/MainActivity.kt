package no.usn.rygleo.prisjegermobv1

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import no.usn.rygleo.prisjegermobv1.navigasjon.MainScreenView
import no.usn.rygleo.prisjegermobv1.roomDB.AppDatabase
import no.usn.rygleo.prisjegermobv1.ui.theme.PrisjegerMobV1Theme

class MainActivity : ComponentActivity() {
 //   private lateinit var appDatabase:AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrisjegerMobV1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
              //      AppDatabase.getRoomDb(this)
                    MainScreenView()
                }
            }
        }
    }
}
