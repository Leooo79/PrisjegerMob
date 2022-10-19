package no.usn.rygleo.prisjegermobv1.navigasjon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chillibits.simplesettings.core.SimpleSettings
import com.chillibits.simplesettings.item.SimpleSwitchPreference
import no.usn.rygleo.prisjegermobv1.R
import no.usn.rygleo.prisjegermobv1.data.Butikk
import no.usn.rygleo.prisjegermobv1.data.TestAPI
import no.usn.rygleo.prisjegermobv1.ui.PrisjegerViewModel

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
fun visAPI(prisjegerViewModel: PrisjegerViewModel = viewModel()) {
    val tekst =   // prisjegerViewModel.varerAPI.observeAsState(initial = emptyList<TestAPI>())
        prisjegerViewModel.varerAPI.value?.type

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = tekst.toString(),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
     //   Button(onClick = { nyTekst = tekst[0].varenavn.toString() }) {

     //   }
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