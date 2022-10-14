package no.usn.rygleo.prisjegermobv1

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.usn.rygleo.prisjegermobv1.data.VarerUiState
import no.usn.rygleo.prisjegermobv1.roomDB.Varer
import no.usn.rygleo.prisjegermobv1.ui.PrisjegerViewModel

/*
TODO: sveipe til venstre for å slette rad/ vare fra lista ?
TODO: overføring mellom lister ?
TODO: legge inn detaljer i utvidet visning ?
 */


/**
 * Funksjon for å bygge opp og vise handleliste
 * Benytter en rekke hjelpemetoder
 * Hovekomponenter er header, søkefelt (TF) og listevisning (LazyC)
 */
@Composable
fun HandlelisteScreen(
    prisjegerViewModel: PrisjegerViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var handleModus by rememberSaveable { mutableStateOf(true) }
    val uiStateNy by prisjegerViewModel.uiStateNy.collectAsState()
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val vareListe by prisjegerViewModel.alleVarer.observeAsState(initial = emptyList())


    Column(Modifier
        .background(MaterialTheme.colors.secondary)
    ) {
        HeaderVisning(
            uiStateNy, // MÅ SENDE STATEVARIABEL FOR REKOMP VED LISTEBYTTE
            vareListe,
            prisjegerViewModel,
            iHandleModus = { handleModus = false },
        )
        Sokefelt(textState)
        ListeVisning(vareListe, state = textState, prisjegerViewModel)
    }
}




/**
 * Funksjon for å bygge opp og vise header med valg og aggregerte data
 */
@Composable
private fun HeaderVisning(
    uiStateNy: VarerUiState,
    vareListe: List<Varer>,
    prisjegerViewModel: PrisjegerViewModel,
    iHandleModus: () -> Unit,
) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }

    Card(
        backgroundColor = MaterialTheme.colors.secondary,
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 6.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier.padding(vertical = 6.dp),
                onClick = iHandleModus,
            ) {
                Text("Nå er vi i handlemodus")
            }
            Button(
                modifier = Modifier.padding(vertical = 6.dp),
                onClick = {
                    prisjegerViewModel.lagTestliste() // FOR TESTING - OPPRETTER TO HANDLELISTER MED LITT DATA
                }
            ) {
                if (vareListe.isEmpty())
                    Text("")
                else Text(uiStateNy.listenavn) // EGEN STATEVARIABEL
            }
            Column {
                Text(
                    text = "Total sum : " + (Math.round(prisjegerViewModel
                        .sumPrHandleliste() * 100.00) / 100.0).toString(),
                    color = MaterialTheme.colors.primary,
                    fontSize = 18.sp,
                )
            }
            Column {
                VelgHandleliste(prisjegerViewModel)
            }
            Column {
                VelgButikk()
            }
        }
    }
}




/**
 * Enkel nedtrekksmeny for å velge butikk
 * Visning med Toast
 * Events:
 * - Aktivere/ deaktivere meny
 * - Velge butikk -> listen viser priser fra valgt butikk
 * TODO: - Opprette og navngi egne handlelister
 * TODO: - Bytte mellom to ulike visninger: handlemodus / lageHandlelisteModus
 * TODO: - Egen knapp for å legge til flere varer i listen (kanskje ikke nødvendig, må testes)
 *
 * TODO: indirekte kall på API / Datakilde(r) via viewModel for å oppdatere priser
 * TODO: etablere listeinnhold som ressurser (egen tabell?)
 * TODO: bedre tilpasning til bakgrunn, dimensjoner box/ knapp
 */
@Composable
fun VelgButikk() {
    val valgbare = arrayOf("Rema 1000", "Kiwi", "Meny", "Spar")
    val valgbareToast = LocalContext.current.applicationContext
    var tekst by rememberSaveable { mutableStateOf("Rema 1000") }
    var aktiv by remember {mutableStateOf(false)
    }
    Box(
        contentAlignment = Alignment.Center,
    ) {
        // knapp for å åpne nedtrekksmeny
        Button(
            onClick = {
                aktiv = true
            }) {
            Text(text = tekst)
        }
        // nedtrekksmeny
        DropdownMenu(
            expanded = aktiv,
            onDismissRequest = {
                aktiv = false
            }
        ) {
            // legger inn items og viser ved onClick
            valgbare.forEachIndexed { itemIndex, itemValue ->
                DropdownMenuItem(
                    onClick = {
                        Toast.makeText(valgbareToast, itemValue, Toast.LENGTH_SHORT)
                            .show()
                        aktiv = false
                        tekst = itemValue
                    },
                ) {
                    Text(text = itemValue)
                }
            }
        }
    }
}


@Composable
fun VelgHandleliste(prisjegerViewModel: PrisjegerViewModel) {
    val valgbare = arrayOf("RoomListe1", "RoomListe2")
    val valgbareToast = LocalContext.current.applicationContext
    var tekst by rememberSaveable { mutableStateOf(prisjegerViewModel.currentListenavn) }
    var aktiv by remember {mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
    ) {
        // knapp for å åpne nedtrekksmeny
        Button(
            onClick = {
                aktiv = true
            }
        ) {  // navnet på listen som vises
            Text(text = prisjegerViewModel.currentListenavn)
        }
        // nedtrekksmeny
        DropdownMenu(
            expanded = aktiv,
            onDismissRequest = {
                aktiv = false
            }
        ) {
            // legger inn items og viser ved onClick
            valgbare.forEachIndexed { itemIndex, itemValue ->
                DropdownMenuItem(
                    onClick = {
                        Toast.makeText(valgbareToast, itemValue, Toast.LENGTH_SHORT)
                            .show()
                        aktiv = false
                        tekst = itemValue
                        // Nytt DB/ API-kall + oppdatert visning ved bytte av liste(navn)
                        prisjegerViewModel.setListeNavn(tekst)
                    },
                ) {
                    Text(text = itemValue)
                }
            }
        }
    }
}




/**
 * Søkefelt for å søke på varenavn.
 * Bygger tabell som grunnlag for items i Lazycolumn
 * Skjuler tastatur ved resett og onDone
 * TODO: Burde søkefeltet falt ut av fokus også? Bør teste/ prioritere ulike use case
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Sokefelt(state: MutableState<TextFieldValue>) {
    // oppretter referanse til keybord
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = state.value,
        onValueChange = { value -> state.value = value },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 2.dp),
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




/**
 * Funksjonen bygger opp LazyColumn og viser varer fra filteret (Sokefelt)
 * Dersom filter er deaktivert (tomt, uten tekst) vises hele listen
 */
@Composable
fun ListeVisning(
    vareListe: List<Varer>,
    state: MutableState<TextFieldValue>,
    prisjegerViewModel: PrisjegerViewModel
) {

    val vareliste = ArrayList<Varer>()
    for (varer in vareListe) {
        if (varer.listenavn.equals(prisjegerViewModel.currentListenavn)) {
            vareliste.add(varer)
        }
    }
    var filtrerteVarer: ArrayList<Varer>

    // bygger LazyColumn - filtrerte treff eller hele lista
    LazyColumn(Modifier
        .fillMaxWidth()
    )
    {
        val searchedText = state.value.text
        filtrerteVarer = if (searchedText.isEmpty()) {
            vareliste
        } else {
            val treffListe = ArrayList<Varer>()
            for (varer in vareListe) {
                if (varer.varenavn.lowercase().contains(searchedText.lowercase())
                    && varer.listenavn.equals(prisjegerViewModel.currentListenavn)) {
                        treffListe.add(varer)
                }
            }
            treffListe
        }
        items(filtrerteVarer) { filtrerte ->
            VarelisteItem(filtrerte, prisjegerViewModel)
        }
    }
}




/**
 * Funksjonen bygger opp og viser handlelister/ varelister
 * Hver rad pakkes i Card for enkel spacing
 * Kolonner består av datafelt fra HandlelisteItems-objekt.
 * Events:
 * - Legge til/ trekke fra antall -> oppdatering av sumPrHandleliste
 * - Vise detaljer om hver vare -> utvider rad og henter inn tekst (bilde?)
 *
 */
@Composable
fun VarelisteItem(
    vareListe: Varer,
    prisjegerViewModel: PrisjegerViewModel) {

    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .clickable(onClick = { expanded = !expanded })
                .padding(2.dp)
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(2.dp)
                    .align(Alignment.CenterVertically)
            ) {
                vareListe.varenavn.let { Text(text = it) }
                if (expanded) {
                    Text(text = ("Mer informasjon om vare, " +
                            "bilder av vare?. ").repeat(3),
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = vareListe.enhetspris.toString())
            }
            Column( // sumPrVare
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp)
                    .align(Alignment.CenterVertically)
            ) { // kontroll for null, utregning av sumPrVare, avrunding 2 desimal
                if (vareListe.antall == null) Text("")
                else Text(text = (Math.round((vareListe.enhetspris?.times(vareListe.antall))?.times(
                    100.00
                ) ?: 0.0) / 100.0).toString()) // KAN OVERLATES TIL VIEWMODELL, MEN TRENGER INDEKS
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Button( // knapp for å trekke fra
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondaryVariant,
                        contentColor = Color.White
                    ),
                    onClick = {
                        if (vareListe.antall!! > 0) {
                            vareListe.antall.let {
                                vareListe.varenavn.let { it1 ->
                                    prisjegerViewModel.oppdaterVare(it.minus(1),
                                        it1, prisjegerViewModel.currentListenavn)
                                }
                            }
                        }
                    }
                ) {
                    Text(vareListe.antall.toString())
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Button( // knapp for å legge til
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primaryVariant,
                        contentColor = Color.White
                    ),
                   onClick = {
                       vareListe.antall?.let {
                           vareListe.varenavn.let { it1 ->
                               prisjegerViewModel.oppdaterVare(it.plus(1),
                                   it1, prisjegerViewModel.currentListenavn)
                           }
                       }
            }
                ) {
                    Text(vareListe.antall.toString())
                }
            }
        }
    }
}








