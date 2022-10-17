package no.usn.rygleo.prisjegermobv1.navigasjon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.chillibits.simplesettings.core.SimpleSettings
import com.chillibits.simplesettings.item.SimpleSwitchPreference
import no.usn.rygleo.prisjegermobv1.R

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Startside",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}


@Composable
fun AddPostScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .wrapContentSize(Alignment.Center)
    ) {
        SimpleSettings(LocalContext.current).show {
            Section {
                title = "Test section"
                for (i in 0..4) {
                    SwitchPref {
                        title = "Test 1.$i"
                        summary = "This is a Test 1.$i"
                        defaultValue = if(i % 2 == 0) SimpleSwitchPreference.ON else SimpleSwitchPreference.OFF
                    }
                }
                if(true) {
                    TextPref {
                        title = "Test 2"
                        summary = "This is a Test 2"
                    }
                }
            }
            Section {
                InputPref {
                    title = "Test 3"
                    summary = "This is a Test 3"
                }
            }
        }
    }
}