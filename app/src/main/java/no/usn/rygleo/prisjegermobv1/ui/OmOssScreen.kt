package no.usn.rygleo.prisjegermobv1.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.usn.rygleo.prisjegermobv1.ui.theme.PrisjegerMobV1Theme
import no.usn.rygleo.prisjegermobv1.R
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import no.usn.rygleo.prisjegermobv1.data.Person

val PersonListe = listOf(
    Person(R.string.project, R.string.aboutProject ,
        R.drawable.prisjegerlogo),
    Person(R.string.Gaute, R.string.aboutGaute, R.drawable.gaute),
    Person(R.string.Leonard, R.string.aboutLeonard, R.drawable.leonard),
    Person(R.string.Dmitriy, R.string.aboutDmitriy, R.drawable.dmitriy),
    Person(R.string.Daniel, R.string.aboutDaniel, R.drawable.daniel),
    Person(R.string.Tore, R.string.aboutTore, R.drawable.tore))

/**
 * OmOss lager et view som skal fortelle mer om utviklerene for Prisjakt
 * og deres bidrag
 */
@Composable
fun OmOss() {
    PrisjegerMobV1Theme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.secondary
        ) {
            Column(
                modifier = Modifier.padding(bottom = 0.dp)
            ){
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ){
                    items(items = PersonListe){
                        person -> MakeAbout(person)
                    }
                }
            }
        }
    }
}

/**
 * Lager kortet for person
 */
@Composable
private fun MakeAbout(person: Person) {
    var expanded by remember {mutableStateOf(false)}
    val extraPaddingHor by animateDpAsState(
        if (expanded) 10.dp else 40.dp,
            animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    val extraPaddingVer by animateDpAsState(
        if (expanded) 20.dp else 10.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )
    val imageSize by animateDpAsState(
        if (expanded) 150.dp else 125.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )
    Card(modifier = Modifier
        .padding(horizontal = extraPaddingHor, vertical = extraPaddingVer)
        .fillMaxSize()
        .background(Color.Black.copy(alpha = 0f)),
        shape = RoundedCornerShape(size = 20.dp)
        ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .clickable(onClick = { expanded = !expanded }),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.about) + " " + stringResource(id = person.name),
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                AddImage(name = stringResource(id = person.name), person.profilePic, imageSize)
                Spacer(modifier = Modifier.padding(top = 130.dp))
            }
            Divider(color = MaterialTheme.colors.onPrimary, thickness = 3.dp)
            if (expanded) {
                Column(modifier = Modifier
                    .background(MaterialTheme.colors.secondaryVariant)
                    .padding(horizontal = extraPaddingHor, vertical = extraPaddingVer)
                    .fillMaxWidth()) {
                    Text(text = stringResource(person.description),
                        color = MaterialTheme.colors.onSecondary
                    )
                }
            }
        }
    }
}

/**
 * Lager image i et kort slik at den kan få en ramme og hjørner
 */
@Composable
fun AddImage(name: String, profilePic: Int, imageSize: Dp) {
    Card(modifier = Modifier
        .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
        .background(Color.Transparent)
        .border(
            border = BorderStroke(2.dp, MaterialTheme.colors.onPrimary),
            shape = RoundedCornerShape(25.dp)
        ),
        shape = RoundedCornerShape(25.dp)
    ) {
        Image(modifier = Modifier
            .height(imageSize)
            .background(Color.Transparent),
            painter = painterResource(id = profilePic),
            contentDescription = "Profile picture for $name",
        )
    }
}

/**
 * Preview
 */
@Preview(showBackground = true, widthDp = 300, heightDp = 400)
@Composable
fun DefaultPreview() {
    OmOss()
}
