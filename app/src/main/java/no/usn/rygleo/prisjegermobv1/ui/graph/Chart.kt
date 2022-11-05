package no.usn.rygleo.prisjegermobv1.ui.graph

import android.content.res.Resources
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.usn.rygleo.prisjegermobv1.ui.graph.Counter.count
import no.usn.rygleo.prisjegermobv1.ui.graph.Counter.countClear
import no.usn.rygleo.prisjegermobv1.ui.graph.Counter.counter


@Composable
fun Chart(
    data: Map<Float, String>,
    max_value: Int
) {
    //Resetter counter hver gang søylen blir restartet
    countClear()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(50.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(50.dp),
                contentAlignment = Alignment.BottomCenter
            ) {

                // scale
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = max_value.toString())
                    Spacer(modifier = Modifier.fillMaxHeight())
                }

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = (max_value / 2).toString())
                    Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                }

            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .background(Color.Black)
            )

            //Søyler
            data.forEach {
                Box(
                    modifier = Modifier
                        .padding(start = lagSøylePadding().dp)
                        .width(20.dp)
                        .fillMaxHeight(it.key)
                        .background(hentFarge())
                        .clickable {
                            Toast
                                .makeText(context, it.key.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.Black)
        )

        Row(
            modifier = Modifier
                .padding(start = (lagSøylePadding() + 10).dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            data.values.forEach {
                Text(
                    modifier = Modifier
                        .width(100.dp),
                    text = it.toString(),
                    textAlign = TextAlign.Center
                )
            }

        }

    }
}

@Composable
fun hentFarge(): Color {
    when (counter) {
        1 -> {
            count()
            return Color.Red
        }
        2 -> {
            count()
            return Color.Green
        }
        3 -> {
            count()
            return Color.Blue
        }
        4 -> {
            count()
            return Color.White
        }
        5 -> {
            count()
            return Color.Gray
        }
    }
    return Color.Black
}

//Table

//Lager en static counter for å lage forskjellige farget søyler
object Counter {
    var counter = 1
    fun count(): Int = counter++
    fun countClear() { counter = 1 }
}

//Henter bredden på skjermen
fun hentScreenWidth(): Int {
    return Resources.getSystem().getDisplayMetrics().widthPixels
}

//Henter høyden på skjermen
fun lagSøylePadding(): Int {
    var screenWidth = hentScreenWidth()
    screenWidth /= 45
    println("Bredden til søylen: " + screenWidth)
    println("Bredden til skjermen: " + hentScreenWidth())
    return screenWidth
}