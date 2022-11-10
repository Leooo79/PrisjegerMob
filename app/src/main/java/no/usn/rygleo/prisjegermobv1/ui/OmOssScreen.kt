package no.usn.rygleo.prisjegermobv1.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

data class Person(val name: Int, val description: Int, val profilePic: Int)
val PersonListe = listOf(
    Person(R.string.project, R.string.aboutProject , R.drawable.prisjegerlogo),
    Person(R.string.Gaute, R.string.aboutGaute, R.drawable.gaute),
    Person(R.string.Leonard, R.string.aboutLeonard, R.drawable.leonard),
    Person(R.string.Dmitriy, R.string.aboutDmitriy, R.drawable.dmitriy),
    Person(R.string.Daniel, R.string.aboutDaniel, R.drawable.daniel),
    Person(R.string.Tore, R.string.aboutTore, R.drawable.tore))

@Composable
fun OmOss() {
    PrisjegerMobV1Theme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier.padding(bottom = 0.dp)
            ){
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ){
                    items(items = PersonListe){
                        person -> makeAbout(person)
                    }
                }
            }
        }
    }
}

@Composable
private fun makeAboutProject(content: @Composable () -> Unit = {}) {
    Column(
        modifier = Modifier
            .padding(vertical = 5.dp, horizontal = 2.dp)
    ) {
        Row(
        ) {
            Column(modifier = Modifier
                .padding(24.dp)
                .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = stringResource(id = R.string.about))
                Text(text = stringResource(id = R.string.aboutProject))
                addImage(name = "logo", profilePic = R.drawable.prisjegerlogo)
            }

        }

    }
}

@Composable
private fun makeAbout(person: Person) {
    var expanded by remember {mutableStateOf(false)}
    val extraPadding by animateDpAsState(
        if (expanded) 40.dp else 10.dp,
            animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )


    Column(
        modifier = Modifier
            .padding(vertical = 5.dp, horizontal = 2.dp)
            .clickable(onClick = { expanded = !expanded }),


        //color = MaterialTheme.colors.surface

    ) {
        Row(
        ) {
            Column(modifier = Modifier
                .padding(24.dp)
                .padding(bottom = extraPadding)
                .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = stringResource(id = R.string.about) + " " + stringResource(id = person.name))
                Text(if (expanded) stringResource(person.description) else "")
                if (expanded) addImage(name = stringResource(id = person.name), person.profilePic)
            }
        }

    }
}




@Composable
fun addImage(name: String, profilePic: Int) {
    Image(painter = painterResource(id = profilePic),
        contentDescription = "Profile picture for $name"
    )
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Preview(showBackground = true, widthDp = 300, heightDp = 400)
@Composable
fun DefaultPreview() {
    OmOss()
}
