package no.usn.rygleo.prisjegermobv1.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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



@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

data class Personer(val name: String, val description: String, val profilePic: Int)


@Composable
fun OmOss() {
    val PersonListe = listOf(
        Personer("Gaute", "Gaute har store muskler", R.drawable.gaute),
        Personer("Leonard", "Leonard har et stort ordforrÃ¥d", R.drawable.leonard),
        Personer("Dmitriy", "Dmitriy har en stor hjerne", R.drawable.dmitriy),
        Personer("Daniel", "Daniel har et stort hjerte", R.drawable.daniel),
        Personer("Tore", "Tore har en stor du vet hva ;)", R.drawable.tore)
    )

    PrisjegerMobV1Theme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.surface
        ) {
            Column(

            ){
                for(Personer in PersonListe){
                    makeAbout(name = Personer.name, description = Personer.description, profilePic = Personer.profilePic)
                }
            }
        }
    }
}

@Composable
private fun makeAbout(name: String, description: String, profilePic: Int ) {
    val expanded = remember {mutableStateOf(false)}
    Surface(
        modifier = Modifier.padding(vertical = 5.dp, horizontal = 2.dp),
        color = MaterialTheme.colors.primary
    ) {
        Row(
        ) {
            Column(modifier = Modifier
                .padding(24.dp)
                .weight(1f)) {
                Text(text = "About " + name)
                Text(if (expanded.value) description else "")
                if (expanded.value) addImage(name = name, profilePic)

            }
            OutlinedButton(
                modifier = Modifier
                    .padding(24.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary),
                onClick = { expanded.value = !expanded.value }) {
                Text(if (expanded.value) "Vis mindre" else "Vis mer" )
            }
        }

    }
}




@Composable
fun addImage(name: String, profilePic: Int) {

    //Last fra resource

    Image(painter = painterResource(id = profilePic),
        contentDescription = "Profile picture for $name"
    )
}

@Composable
fun makeCard(name: String, description: String) {
    /*Column(
        Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(10.dp)
    ) */
    Text(
        text = name
    )
    Text(
        text = description
    )

}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Preview(showBackground = true, widthDp = 300, heightDp = 400)
@Composable
fun DefaultPreview() {
    OmOss()
}