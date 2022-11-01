package no.usn.rygleo.prisjegermobv1.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.usn.rygleo.prisjegermobv1.data.VarerUiState
import no.usn.rygleo.prisjegermobv1.roomDB.Varer

/*
TODO: overføring mellom lister ?
TODO: legge inn detaljer i utvidet visning ?
TODO: trenger en sorteringsfunksjon for å sortere liste etter slett/ hent flere varer
 */


/**
 * Funksjon for å bygge opp og vise handleliste
 * Benytter en rekke hjelpemetoder
 * Hovekomponenter er header, søkefelt (TF) og listevisning (LazyC)
 *
 * NY LOGIKK 21.10.22 : Henter alle varenavn fra server. Legger i default handleliste på lokal disk.
 * Viser alle varer i default handleliste som livedata. Endringer kjører update mot lokal DB
 * NYTT 21.10.22 : NYE DATA HENTES FRA API VED OPPSTART OG LEGGES I LOKAL DB (CONFLICT = IGNORE)
 * TODO: update av server via API
 */
@Composable
fun HandlelisteScreen(prisjegerViewModel: PrisjegerViewModel) {

    val uiStateNy by prisjegerViewModel.uiStateNy.collectAsState()
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    // Alle varelinjer/ handlelister lagret i lokal DB (PK = varenavn + listenavn)
    val vareListe by prisjegerViewModel.alleVarer.observeAsState(initial = emptyList())
    // Alle butikker fra server:
    val valgbare by prisjegerViewModel.butikkerAPI.observeAsState(initial = emptyArray())

    // Innhold på denne siden : Header + Søkefelt + Listevisning
    Column(Modifier
        .background(MaterialTheme.colors.secondary)
    ) {
        // MÅ SENDE STATEVARIABEL TIL HEADER FOR REKOMP VED LISTEBYTTE
        HeaderDialog(
            uiStateNy,
            prisjegerViewModel,
            valgbare
        )
        Sokefelt(
            textState
        )
        ListeVisning(
            vareListe,
            state = textState,
            prisjegerViewModel,
            valgbare
        )
    }
}







/**
 * Funksjon for å bygge opp og vise header med valg og aggregerte data
 * // MÅ MOTTA STATEVARIABEL FOR REKOMP VED LISTEBYTTE
 */
/**
 * Funksjon for å bygge opp og vise header med valg og aggregerte data
 * // MÅ MOTTA STATEVARIABEL FOR REKOMP VED LISTEBYTTE
 */
@Composable
private fun HeaderDialog(
    uiStateNy: VarerUiState,
    prisjegerViewModel: PrisjegerViewModel,
    valgbare: Array<String>) {

    val alertDialog = remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

    // Åpner alertDialog for nytt listenavn fra bruker ved behov
    if (alertDialog.value) {
        AlertDialog(
            onDismissRequest = {
                alertDialog.value = false
            },
            title = {
                Text(text = "Skriv inn navn på ny handleliste")
            },
            text = {
                Column() {
                    TextField(
                        value = text,
                        onValueChange = { text = it }
                    )
                }
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) { // Bekreftelse fra bruker
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { // Bekreftelse lukker alert og oppretter ny liste i lokal DB
                            alertDialog.value = false
                            prisjegerViewModel.setListeNavn(text) // endrer listenavn
                            prisjegerViewModel.oppdaterListeFraApi() // henter alle varer
                        }
                    ) {
                        Text("Lagre ny liste")
                    }
                }
                //  Ekstra knapper
                Button( // Knapp for å sortere varer i handleliste. Antall > 0
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    onClick = {
                        prisjegerViewModel.getSortertLokaleVarer()
                        alertDialog.value = false
                    }
                ) {
                    Text("Vis bare valgte")
                }
                Button( // Knapp for å vise alle varer. Inkludert antall == 0
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    onClick = {
                        prisjegerViewModel.getLokaleVarer()
                        alertDialog.value = false
                    }
                ) {
                    Text("Vis alle varer")
                }
                Button( // Knapp for å hente nye varer fra server (komplett liste - IGNORE)
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    onClick = {
                        prisjegerViewModel.oppdaterListeFraApi()
                        alertDialog.value = false
                    }
                ) {
                    Text("Oppdater handleliste")
                }
                Button( // Knapp for å slette handleliste fra lokal DB
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    onClick = {
                        prisjegerViewModel.slettHandleliste()
                        alertDialog.value = false
                    }
                ) {
                    Text("Slett handleliste !")
                }
                Button( // Knapp for å gå tilbake/ lukke alert
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    onClick = {
                        alertDialog.value = false
                    }
                ) {
                    Text("Tilbake")
                }
            }
        )
    } // slutt alertDialog
    HeaderInnhold(
        prisjegerViewModel,
        valgbare,
        alertDialog = {alertDialog.value = !alertDialog.value}
    )
} // slutt HeaderVisning







/**
 * Funksjonen vises innholdet i Header, menyknapp, dropdown for butikk/ handleliste
 */
@Composable
private fun HeaderInnhold(
    prisjegerViewModel: PrisjegerViewModel,
    valgbare: Array<String>,
    alertDialog: () -> Unit
) {

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
                        onClick = alertDialog
                    ) {
                        Text("Valg")
                    }
                }
                Spacer(Modifier
                    .size(10.dp))
                Column {
                    // DROPDOWN FOR VALG AV HANDLELISTE -> VISER VARER PR LISTENAVN FRA LOKAL DB
                    VelgHandleliste(prisjegerViewModel)
                }
                Spacer(Modifier
                    .size(10.dp))
                Column {
                    // DROPDOWN FOR VALG AV BUTIKK -> VISER AKTUELLE PRISER
                    VelgButikk(prisjegerViewModel, valgbare)
                }
            }
        }
    }
}







/**
 * Enkel nedtrekksmeny for å velge butikk
 * Butikknavn hentes fra backend API ved oppstart
 * TODO: trenger vi push fra server ved endring/ flere navn?
 */
@Composable
private fun VelgButikk(prisjegerViewModel: PrisjegerViewModel, valgbare: Array<String>) {

  //  val valgbare by prisjegerViewModel.butikkerAPI.observeAsState(initial = null)
 //   val valgbareToast = LocalContext.current.applicationContext
    var tekst by rememberSaveable { mutableStateOf("Velg butikk") }
    var aktiv by remember { mutableStateOf(false) }

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
                        //             Toast.makeText(valgbareToast, itemValue, Toast.LENGTH_SHORT)
                        //                 .show()
                        aktiv = false
                        tekst = itemValue
                        prisjegerViewModel.oppdaterPriserFraApi(itemValue)
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
 * Listenavn hentes fra lokal DB og oppdateres automatisk med LiveData
 */
@Composable
private fun VelgHandleliste(prisjegerViewModel: PrisjegerViewModel) {

    val valgbare by prisjegerViewModel.alleListenavn.observeAsState(initial = null)
   // val valgbareToast = LocalContext.current.applicationContext
    var tekst = prisjegerViewModel.currentListenavn // OBS!! DETTE GIR REKOMP
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
            valgbare?.forEachIndexed { itemIndex, itemValue ->
                DropdownMenuItem(
                    onClick = {
                 //       Toast.makeText(valgbareToast, itemValue, Toast.LENGTH_SHORT)
                //            .show()
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
private fun Sokefelt(state: MutableState<TextFieldValue>) {
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
private fun ListeVisning(
    vareListe: List<Varer>,
    state: MutableState<TextFieldValue>,
    prisjegerViewModel: PrisjegerViewModel,
    valgbare: Array<String>
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
        items(filtrerteVarer, {filtrerteVarer: Varer ->
            filtrerteVarer.varenavn + filtrerteVarer.listenavn}) { filtrerte ->
            VarelisteDialog(filtrerte, prisjegerViewModel, valgbare)
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
private fun VarelisteDialog(
    vare: Varer,
    prisjegerViewModel: PrisjegerViewModel,
    valgbare: Array<String>
) {

    var visDetaljer by rememberSaveable { mutableStateOf(false) }
    val dismissState = rememberDismissState()

    // TODO: Insert av samme vare til samme handleliste etter delete gir utfordinger
    // TODO fordi key huskes av lazycolumn.item (key = varenavn+varelistenavn)
    // TODO Å kjøre kode i LaunchedEffect løser problemet, men gir rar animasjon
    // TODO Alternativ løsning er å opprette egen unik PK for hver rad i E Varer
    // TODO og benytte denne som Key i Lazycolumn.item. Er ganske kompliserende og kanskje ikke nødvendig?

    // HVIS BRUKER SWIPE LEFT FOR SLETTING AV RAD
    if (dismissState.currentValue != DismissValue.Default) {
        LaunchedEffect(Unit) { // for suspendert kall
            // TODO: enten finne bedre key for lazycolumn eller endre animasjon
            dismissState.reset() // må dessverre resette plassering/ state FØR SLETTING for å kunne
                                // vise samme key senere. Skyldes PK = varenavn+listenavn
            prisjegerViewModel.slettVare(vare) // sletter rad fra handleliste
        }
    }

    // HVIS BRUKER ØNSKER Å SE FLERE DETALJER OM HVER ENKELT VARE: UTLØSES AV ONCLICK VARENAVN
    if (visDetaljer) {
        visDetaljer(
            vare,
            prisjegerViewModel,
            valgbare,
            visDetaljer = { visDetaljer = !visDetaljer }
        )
    }

    // INNHOLD SOM KAN DRAS MOT VENSTRE FOR DELETE. Visuelt + delete fra lokal DB via vM
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
            VarelisteItem(
                vare,
                prisjegerViewModel,
                visDetaljer = { visDetaljer = !visDetaljer }
            )
        } // slutt dismissContent
    ) // slutt SwipeToDismiss
} // slutt fun VarelisteItem







/**
 * Funksjonen viser innholdet i listen/ raden
 */
@Composable
private fun VarelisteItem(
    vare: Varer,
    prisjegerViewModel: PrisjegerViewModel,
    visDetaljer: () -> Unit
) {

    val bredKolonne = 2F
    val smalKolonne = 1F

    Card(
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .fillMaxWidth()
        ) {
            Column( // VARENAVN
                modifier = Modifier
                    .weight(bredKolonne)
                    .padding(2.dp)
                    .align(Alignment.CenterVertically)
                    .clickable(onClick = visDetaljer)
            ) {
                Text(text = vare.varenavn)
            }

            Column( // ENHETSPRIS
                modifier = Modifier
                    .weight(smalKolonne)
                    .padding(2.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = vare.enhetspris.toString())
            }

            Column( // SUM PR VARE
                modifier = Modifier
                    .weight(smalKolonne)
                    .padding(2.dp)
                    .align(Alignment.CenterVertically)
            ) { // kontroll for null, utregning av sumPrVare, avrunding 2 desimal
                Text(text = (Math.round(
                    (vare.antall.let { vare.enhetspris.times(it) }).times(
                        100.00)) / 100.0).toString()) // KAN OVERLATES TIL VIEWMODELL, MEN TRENGER INDEKS
            }

            Column( // KNAPP FOR Å DEKREMENTERE
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
                        // minimum 0 vare.
                        if (vare.antall >= 1) {
                            prisjegerViewModel.oppdaterVareAntall(
                                -1, // minus en i antall
                                vare.varenavn,
                                vare.listenavn,
                                false
                            )
                        }
                    }
                ) { // viser antall pr vare/ liste
                    Text(vare.antall.toString())
                }
            }

            Column( // KNAPP FOR Å INKREMENTERE
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
                        prisjegerViewModel.oppdaterVareAntall(
                            1, // pluss en i antall
                            vare.varenavn,
                            vare.listenavn,
                            true
                        )
                    }
                ) {  // viser antall pr vare/ liste
                    Text(vare.antall.toString())
                }
            }
        }
    }
}






/**
 * Funksjonen viser enhetspriser pr vare pr butikk i AlertDialog
 */
@Composable
private fun visDetaljer(
    vare: Varer,
    prisjegerViewModel: PrisjegerViewModel,
    valgbare: Array<String>,
    visDetaljer: () -> Unit) {

    AlertDialog(
        onDismissRequest = visDetaljer,
        title = {
            Text(
                text = "Vare: " + vare.varenavn,
                fontWeight = FontWeight.Bold,
            )
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = visDetaljer
                ) {
                    Text("Tilbake")
                }
            }
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier
                        .weight(2F)
                        .padding(2.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = "Butikker",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.size(10.dp))
                    for (butikker in valgbare) {// looper ut butikknavn
                        Text(butikker)
                        Spacer(Modifier.size(5.dp))
                    }
                }
                Spacer(Modifier.size(10.dp))
                Column(
                    modifier = Modifier
                        .weight(2F)
                        .padding(2.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(text = "Enhetspris",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.size(10.dp))
                    for (butikker in valgbare) {// looper ut enhetspriser
                        Text(
                            prisjegerViewModel
                                .finnPris(butikker, vare.varenavn)
                        )
                        Spacer(Modifier.size(5.dp))
                    }
                }
            }
        }
    )
}














