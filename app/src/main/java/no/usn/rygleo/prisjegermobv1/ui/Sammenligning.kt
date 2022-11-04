package no.usn.rygleo.prisjegermobv1.ui

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.usn.rygleo.prisjegermobv1.roomDB.Varer
import no.usn.rygleo.prisjegermobv1.ui.graph.Chart
import no.usn.rygleo.prisjegermobv1.ui.theme.PrisjegerMobV1Theme

@Composable
fun SammenligningScreen(prisjegerViewModel: PrisjegerViewModel) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val textFieldFocus = remember { mutableStateOf(false) }
    val valgtVare = rememberSaveable { mutableStateOf("Ingen") }
    val vareListe by prisjegerViewModel.alleVarer.observeAsState(initial = emptyList())
    val butikkListe by prisjegerViewModel.butikkerAPI.observeAsState(initial = emptyArray())

    PrisjegerMobV1Theme {
        // A surface container using the 'background' color from the theme
        val scrollState = rememberScrollState()
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .scrollable(state = scrollState, orientation = Orientation.Vertical)
                    .padding(vertical = 6.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Varesammenligning",
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                )
                Text(
                    text = valgtVare.value,
                    color = MaterialTheme.colors.primary,
                    fontSize = 20.sp,
                )
                Sokefelt(
                    textState,
                    textFieldFocus
                )
                sokeFeltVisning(
                    vareListe,
                    state = textState,
                    prisjegerViewModel,
                    textFieldFocus,
                    valgtVare
                )
                if (valgtVare.value != "Ingen") {
                    tabellItem(vareListe, butikkListe, valgtVare, prisjegerViewModel)
                    MainChart()
                }
            }
        }
    }
}


@Composable
fun MainChart() {
    Chart(
        data = mapOf(
            Pair(0.2f, "Rema 1000"),
            Pair(0.3f, "Kiwi"),
            Pair(0.5f, "Joker"),
            Pair(0.7f, "Meny"),
            Pair(0.9f, "Test"),
        ), max_value = 1000
    )
}

fun test(test : String) {
    println(test)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Sokefelt(state: MutableState<TextFieldValue>, textFieldFocus : MutableState<Boolean>) {
    // oppretter referanse til keybord
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    TextField(
        value = state.value,
        onValueChange = { value -> state.value = value },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 2.dp)
            //Sjekker om textfielder er i fokus
            .onFocusChanged { focusState ->
                when {
                    focusState.isFocused -> {
                        updateTextFieldFocus(textFieldFocus)
                    }
                }
            }
            .clickable(onClick = {

            })
        ,
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        // søkeikon for å indikere søkefelt
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        // ikon for å resette skriving/ tømme søkefelt
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value = TextFieldValue("")
                        keyboardController?.hide()
                        updateTextFieldFocus(textFieldFocus)
                        focusManager.clearFocus()
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        // bør testes på håndholdt enhet :
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White,
            backgroundColor = MaterialTheme.colors.secondaryVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {keyboardController?.hide()})
    )
}

@Composable
private fun sokeFeltVisning(
    vareListe: List<Varer>,
    state: MutableState<TextFieldValue>,
    prisjegerViewModel: PrisjegerViewModel,
    textFieldFocus: MutableState<Boolean>,
    valgtVare: MutableState<String>
) {
    val visRettListe = ArrayList<Varer>()
    // Kun varelinjer tilhørende inneværende liste(navn) vises
    for (varer in vareListe) {
        if (varer.listenavn == prisjegerViewModel.currentListenavn) {
            visRettListe.add(varer)
        }
    }
    var filtrerteVarer: ArrayList<Varer>
    // bygger LazyColumn - filtrerte treff eller hele lista
    LazyColumn(Modifier
        .fillMaxWidth()
    )
    {
        val leterEtter = state.value.text
        filtrerteVarer = if (leterEtter.isEmpty()) {
            if (textFieldFocus.value && leterEtter.isEmpty()) {
                for (varer in vareListe) {
                    visRettListe.add(varer)
                }
            }
            else {
                visRettListe.clear()
            }
            visRettListe
        } else {
            val treffListe = ArrayList<Varer>()
            // filter for sammenligning av strenger. Søker på kombinasjoner av char
            // ved å lytte på søkefelt
            for (varer in visRettListe) {
                if (varer.varenavn.lowercase().contains(leterEtter.lowercase())) {
                    treffListe.add(varer)
                }
            }
            treffListe
        } // OBS: Må bruke både varenavn og listenavn som key for id av unike
        println(filtrerteVarer.joinToString(" "))
        items(filtrerteVarer, {filtrerteVarer: Varer ->
            filtrerteVarer.varenavn + filtrerteVarer.listenavn}) { filtrerte ->
            sokliste(filtrerte, prisjegerViewModel, textFieldFocus, valgtVare)
        }
    }
}

@Composable
fun sokliste(
    filtrerte: Varer,
    prisjegerViewModel: PrisjegerViewModel,
    textFieldFocus: MutableState<Boolean>,
    valgtVare: MutableState<String>)
{
    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 20.dp, end = 20.dp, top = 0.dp),
        onClick =  {
            updateTextFieldFocus(textFieldFocus)
            valgtVare.value = filtrerte.varenavn
        }) {
        Text(filtrerte.varenavn)
    }
    Divider(color = MaterialTheme.colors.primary, thickness = 2.dp)
}

@Composable
private fun tabellItem(vareListe : List<Varer>, butikkListe: Array<String>, valgtVare: MutableState<String>, prisjegerViewModel: PrisjegerViewModel) {
    var datoListe = arrayOf("25/06/98","25/06/98","25/06/98","25/06/98","25/06/98","25/06/98")
    Row(modifier = Modifier
        .padding(all = 8.dp),
    ) {
        //Butikknavn
        Column(
            modifier = Modifier
                .weight(2F)
                .padding(start = 5.dp)
        ) {
            Text(
                text = "Butikk",
                fontWeight = FontWeight.Bold
            )
            for (butikker in butikkListe) {
                tabellItemButikk(butikker)
            }
        }
        //Pris
        Column(
            modifier = Modifier
                .weight(2F)
        ) {
            Text(text = "Pris",
                fontWeight = FontWeight.Bold
            )
            for (butikker in butikkListe) {
                tabellItemPris(prisjegerViewModel.finnPrisPrVare(butikker, valgtVare.value))
            }
        }
        //Dato
        Column(
            modifier = Modifier
                .weight(1F)
                .padding(end = 0.dp)
        ) {
            Text(text = "Dato",
                fontWeight = FontWeight.Bold
            )
            for (elements in datoListe) {
                tabellItemDato(elements)
            }
        }
        Spacer(Modifier.size(30.dp))
    }
}

@Composable
private fun tabellItemButikk(butikk : String) {
    Spacer(Modifier.size(10.dp))
    Text(butikk)
    Divider(color = MaterialTheme.colors.primary, thickness = 2.dp)
    Spacer(Modifier.size(10.dp))
}

@Composable
private fun tabellItemPris(pris: String) {
    Spacer(Modifier.size(10.dp))
    Text("$pris")
    Divider(color = MaterialTheme.colors.primary, thickness = 2.dp)
    Spacer(Modifier.size(10.dp))
}

@Composable
private fun tabellItemDato(dato : String) {
    Spacer(Modifier.size(10.dp))
    Text(dato)
    Divider(color = MaterialTheme.colors.primary, thickness = 2.dp)
    Spacer(Modifier.size(10.dp))
}

private fun updateTextFieldFocus(textFieldFocus: MutableState<Boolean>) {
    if (!textFieldFocus.value) {
        textFieldFocus.value = true
    }
    else if (textFieldFocus.value) {
        textFieldFocus.value = false
    }
}
