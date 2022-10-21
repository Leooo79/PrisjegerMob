package no.usn.rygleo.prisjegermobv1

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
 *
 * NY LOGIKK 21.10.22 : Henter alle varenavn fra server. Legger i default handleliste på lokal disk.
 * Viser alle varer i default handleliste som livedata. Endringer kjører update mot lokal DB
 * TODO: update av server via API
 */
@Composable
fun HandlelisteScreen(
    prisjegerViewModel: PrisjegerViewModel,
    modifier: Modifier = Modifier
) {
    var handleModus by rememberSaveable { mutableStateOf(true) }
    val uiStateNy by prisjegerViewModel.uiStateNy.collectAsState()
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    // Lokal DB ROOM
    val vareListe by prisjegerViewModel.alleVarer.observeAsState(initial = emptyList())
    // Lister fra API
    val listeAPI by prisjegerViewModel.hentVarerAPI.observeAsState(initial = emptyArray())

    //   val listeAPI by prisjegerViewModel.varerAPI.observeAsState(initial = emptyArray())


    Column(Modifier
        .background(MaterialTheme.colors.secondary)
    ) {

        HeaderVisning(
            listeAPI,
            uiStateNy, // MÅ SENDE STATEVARIABEL FOR REKOMP VED LISTEBYTTE
            vareListe,
            prisjegerViewModel,
        ) { handleModus = false }
        Sokefelt(textState)
        ListeVisning(vareListe, listeAPI, state = textState, prisjegerViewModel) // ORIGINAL: vareListe

    }
}




/**
 * Funksjon for å bygge opp og vise header med valg og aggregerte data
 */
@Composable
private fun HeaderVisning(
    listeApi: Array<String>,
    uiStateNy: VarerUiState, // MÅ MOTTA STATEVARIABEL FOR REKOMP VED LISTEBYTTE
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
            Row {
                Button(
                    modifier = Modifier.padding(vertical = 6.dp),
                  //  onClick = iHandleModus,
                onClick = {
               //     prisjegerViewModel.visAPI = true
                    prisjegerViewModel.lagListe()

                }
                ) {
                    Text("Hent API")
                }
                Spacer(Modifier.size(10.dp))
                Button(
                    modifier = Modifier
                        .padding(vertical = 6.dp),
                    onClick = {
               //         prisjegerViewModel.visAPI = false
                //       iHandleModus
                       // prisjegerViewModel.lagTestliste() // FOR TESTING - OPPRETTER TO HANDLELISTER MED LITT DATA
                        //    prisjegerViewModel.oppfrisk()
                        //    prisjegerViewModel.hentApi()

                       // prisjegerViewModel.lagListe(listeApi)
                    }
                ) {

                     Text("Hent lokal DB")// EGEN STATEVARIABEL
                }
            }
            Row {
                Text(
                    text = "Total sum : " + (Math.round(
                        prisjegerViewModel
                            .sumPrHandleliste() * 100.00
                    ) / 100.0).toString(),
                    color = MaterialTheme.colors.primary,
                    fontSize = 18.sp,
                )
            }
            Row {
                Column {
                    Button(
                        onClick = {
                         //   prisjegerViewModel.insertEnVare("nyttListeNavn") // FOR TESTING - OPPRETTER TO HANDLELISTER MED LITT DATA
                            //    prisjegerViewModel.oppfrisk()
                        }
                    ) {
                        Text("Ny handleliste")
                    }
                }
                Spacer(Modifier.size(10.dp))
                Column {
                    VelgHandleliste(prisjegerViewModel)
                }
                Spacer(Modifier.size(10.dp))
                Column {
                    VelgButikk(prisjegerViewModel)
                }
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
 */
@Composable
fun VelgButikk(prisjegerViewModel: PrisjegerViewModel) {
    //   val valgbare = arrayOf("Rema 1000", "Kiwi", "Meny", "Spar")
    val valgbare by prisjegerViewModel.butikkerAPI.observeAsState(initial = null)
    val valgbareToast = LocalContext.current.applicationContext
    var tekst by rememberSaveable { mutableStateOf("Velg butikk") }
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
            valgbare?.forEachIndexed { itemIndex, itemValue ->
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


/**
 * Funksjon for å velge hvilken handleliste som skal vises
 * Endrer variabel currentListenavn i vM som er grunnlag for sortering av liste fra DB
 */
@Composable
fun VelgHandleliste(prisjegerViewModel: PrisjegerViewModel) {
    val valgbare = arrayOf("RoomListe1", "RoomListe2", "nyttListeNavn")
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
 * Dersom filter er deaktivert (tomt, uten tekst) vises hele listen pr listenavn
 * Filteret sorterer ut varelinjer for hver handleliste(navn)
 */
@Composable
fun ListeVisning(
    vareListe: List<Varer>,
    listeApi: Array<String>,
    state: MutableState<TextFieldValue>,
    prisjegerViewModel: PrisjegerViewModel
) {

    var visAPI by rememberSaveable { mutableStateOf(prisjegerViewModel.visAPI) }

    val vareliste = ArrayList<Varer>()
    if (visAPI) {
        for (varer in listeApi) {
            // OBS! TILPASSET DATA FRA API. BYGGER VARER-OBJEKT BASERT PÅ VARENAVN
            vareliste.add(Varer(prisjegerViewModel.currentListenavn, varer, 0.0, 0))
        }
    }
    else {
        for (varer in vareListe) {
            // Kun varelinjer tilhørende inneværende liste(navn) vises
            if (varer.listenavn.equals(prisjegerViewModel.currentListenavn)) {
                vareliste.add(varer)
            }
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
            for (varer in vareliste) {
                if (varer.varenavn.lowercase().contains(searchedText.lowercase())
                    && varer.listenavn.equals(prisjegerViewModel.currentListenavn)) {
                    treffListe.add(varer)
                }
            }
            treffListe
            // TODO utmarkert under er tilpasset local DB
        } // OBS: Må bruke både varenavn og listenavn som key for id av unike

            /*
        } else {
            val treffListe = ArrayList<Varer>()
            for (varer in vareListe) {
                if (varer.varenavn.lowercase().contains(searchedText.lowercase())
                    && varer.listenavn.equals(prisjegerViewModel.currentListenavn)) {
                    treffListe.add(varer)
                }
            }
            treffListe
        } // OBS: Må bruke både varenavn og listenavn som key for id av unike

             */
        items(filtrerteVarer, {filtrerteVarer:Varer->
            filtrerteVarer.varenavn + filtrerteVarer.listenavn}) { filtrerte ->
            VarelisteItem(filtrerte, prisjegerViewModel)
        }
    }
}




/**
 * Funksjonen bygger opp og viser handlelister/ varelister
 * Kolonner består av datafelt fra Varer objekt/ entitet (lokal DB)
 * Events:
 * - Legge til/ trekke fra antall -> oppdatering av DB + sumPrVare + sumPrHandleliste
 * - Vise detaljer om hver vare -> utvider rad og henter inn tekst (bilde?)
 * - Slette rad med swipe -> varen slettes fra lokal DB + visning oppdateres
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VarelisteItem(
    vare: Varer,
    prisjegerViewModel: PrisjegerViewModel,
) {

    var expanded by rememberSaveable { mutableStateOf(false) }
    var bredKolonne = 2F
    var smalKolonne = 1F
    val dismissState = rememberDismissState()

    // TODO: Insert av samme vare til samme handleliste etter delete gir utfordinger
    // TODO fordi key huskes av lazycolumn.item (key = varenavn+varelistenavn)
    // TODO Å kjøre kode i LaunchedEffect løser problemet, men gir rar animasjon
    // TODO Alternativ løsning er å opprette egen unik PK for hver rad i E Varer
    // TODO og benytte denne som Key i Lazycolumn.item. Er ganske kompliserende og kanskje ikke nødvendig?

    if (dismissState.currentValue != DismissValue.Default) {
        LaunchedEffect(Unit) { // for suspendert kall
            dismissState.reset() // må resette plassering/ state FØR SLETTING for å kunne vise samme key senere
            prisjegerViewModel.slettVare(vare) // sletter rad fra handleliste
        }
    }

    // Rader slettes ved venstre-swipe. Visuelt + delete fra lokal DB via vM
    SwipeToDismiss(
        state = dismissState,
        modifier = Modifier
            .padding(vertical = Dp(1f)),
        directions = setOf(
            DismissDirection.EndToStart
        ),
        dismissThresholds = { direction ->
            FractionalThreshold(
                if (direction == DismissDirection.EndToStart) 0.1f
                else 0.05f
            )
        },
        background = { // animasjon for endring av farge+
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> Color.White
                    else -> Color(0xFFF44336)
                }
            )
            val alignment = Alignment.CenterEnd
            val icon = Icons.Default.Delete

            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.75f
                else 1f
            )
            // ekstra ytterramme for mindre mellomrom ved touch/ swipe, ikon for sletting
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = Dp(20f)),
                contentAlignment = alignment
            ) {
                Icon(
                    icon,
                    contentDescription = "Delete Icon",
                    modifier = Modifier.scale(scale)
                )
            }
        }, // slutt background for swipe
        dismissContent = {

            Card(
                elevation = animateDpAsState(
                    if (dismissState.dismissDirection != null) 4.dp
                    else 0.dp
                ).value,
                backgroundColor = MaterialTheme.colors.primary,
                //   modifier = Modifier.padding(vertical = 4.dp, horizontal = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colors.primary)
                        .padding(vertical = 4.dp, horizontal = 8.dp)
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
                            .weight(bredKolonne)
                            .padding(2.dp)
                            .align(Alignment.CenterVertically)
                            .clickable(onClick = { expanded = !expanded })
                    ) {
                        vare.varenavn.let { Text(text = it) }
                        if (expanded) {
                            //    bredKolonne = 4F
                            //   smalKolonne = 0.25F
                            Text(text = ("Mer informasjon om vare, " +
                                    "bilder av vare?. ").repeat(3),

                                )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(smalKolonne)
                            .padding(2.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(text = vare.enhetspris.toString())
                    }
                    Column( // sumPrVare
                        modifier = Modifier
                            .weight(smalKolonne)
                            .padding(2.dp)
                            .align(Alignment.CenterVertically)
                    ) { // kontroll for null, utregning av sumPrVare, avrunding 2 desimal
                        if (vare.antall == null) Text("")
                        else Text(text = (Math.round((vare.enhetspris?.times(vare.antall))?.times(
                            100.00
                        ) ?: 0.0) / 100.0).toString()) // KAN OVERLATES TIL VIEWMODELL, MEN TRENGER INDEKS
                    }
                    Column(
                        modifier = Modifier
                            .weight(smalKolonne)
                            .padding(2.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Button( // knapp for å trekke fra
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFF44336),
                                contentColor = Color.White
                            ),
                            onClick = {
                                if (vare.antall!! > 0) {
                                    vare.antall.let {
                                        vare.varenavn.let { it1 ->
                                            prisjegerViewModel.oppdaterVare(it.minus(1),
                                                it1, prisjegerViewModel.currentListenavn)
                                        }
                                    }
                                }
                            }
                        ) {
                            Text(vare.antall.toString())
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(smalKolonne)
                            .padding(2.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Button( // knapp for å legge til
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primaryVariant,
                                contentColor = Color.White
                            ),
                            onClick = {
                                // TODO: if (vare ikke finnes i handeliste fra før) -> insert(vare)
                                // TODO: if (vare finnes fra før) -> update
                            //    prisjegerViewModel.insertVare(vare)
                                vare.antall?.let {
                                    vare.varenavn.let { it1 ->
                                        prisjegerViewModel.oppdaterVare(it.plus(1),
                                            it1, prisjegerViewModel.currentListenavn)
                                    }
                                }


                            }
                        ) {
                            Text(vare.antall.toString())
                        }
                    }
                }
            }
        } // slutt dismissContent (Card)
    ) // slutt SwipeToDismiss
} // slutt fun VarelisteItem










