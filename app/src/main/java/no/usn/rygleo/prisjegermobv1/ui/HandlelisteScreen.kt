package no.usn.rygleo.prisjegermobv1.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.usn.rygleo.prisjegermobv1.R
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

    val uiState by prisjegerViewModel.uiStateNy.collectAsState()
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    // Alle varelinjer/ handlelister lagret i lokal DB (PK = varenavn + listenavn)
    val vareListe by prisjegerViewModel.alleVarer.observeAsState(initial = emptyList())
    // Alle butikker fra server:
    val valgbare by prisjegerViewModel.butikkerAPI.observeAsState(initial = emptyArray())

    // Innhold for komposisjon
    Column(Modifier
        .background(MaterialTheme.colors.secondary)
    ) {
        // MÅ SENDE STATEVARIABEL TIL HEADER FOR REKOMP VED LISTEBYTTE
        HeaderDialog(
            uiState, // kun for rekomp ved oppdatering av state
            prisjegerViewModel,
            valgbare
        )
        Sokefelt(
            textState
        )
        Overskrift()
        ListeVisning(
            vareListe,
            state = textState,
            prisjegerViewModel,
            valgbare
        )
    }
}





/**
 * Funksjonen tegner en overskriftsrad for bruk i Header
 */
@Composable
fun Overskrift() {
    Card(modifier = Modifier
        .padding(horizontal = 10.dp),
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        Row(
            modifier = Modifier
                .padding(start = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(3F)
            ) {
                Text("Vare")
            }
            Column(
                modifier = Modifier
                    .weight(1F)
            ) {
                Text(stringResource(id = R.string.price))
            }
            Column(
                modifier = Modifier
                    .weight(1F)
            ) {
                Text("Antall")
            }
        }
    }
}





/**
 * Funksjonen viser dialogvindu med valg for handleliste
 * Kaller videre på HeaderInnhold
 * // OBS: MÅ MOTTA STATEVARIABEL FOR REKOMP VED LISTEBYTTE
 * TODO: Bør deles i 3
 */
@Composable
private fun HeaderDialog(
    uiState: VarerUiState,
    prisjegerViewModel: PrisjegerViewModel,
    valgbare: Array<String>) {

    // BER OM BEKREFTELSE PÅ SLETTING
    if (prisjegerViewModel.vilSletteDialog.value) {
        BekreftelseBruker(
            prisjegerViewModel,
         //   vilSlette = { prisjegerViewModel.vilSletteDialog.value = !prisjegerViewModel.vilSletteDialog.value }
        )
    }
    // VISER DIALOG MED VALG
    if (prisjegerViewModel.valgDialog.value) {
        VisValg(prisjegerViewModel)
    }

    // VISER DIALOG MED TOTALSUM PR HANDLELISTE PR BUTIKK
    if (prisjegerViewModel.butikkDialog.value) {
        VisSumPrButikk(prisjegerViewModel, valgbare)
    }
    // INNHOLD I HEADER
    HeaderInnhold(
        prisjegerViewModel,
        valgbare,
        alertDialog = {prisjegerViewModel.valgDialog.value = !prisjegerViewModel.valgDialog.value},
        butikkDialog = {prisjegerViewModel.butikkDialog.value = !prisjegerViewModel.butikkDialog.value}
    )
} // slutt HeaderVisning







/**
 * Funksjonen viser Alertdialog med sum pr liste pr butikk
 */
@Composable
fun VisSumPrButikk(prisjegerViewModel: PrisjegerViewModel, valgbare: Array<String>) {
    AlertDialog(
        onDismissRequest = {
            prisjegerViewModel.butikkDialog.value = false
        },
        title = {
            Text(text = "Totalsum pr butikk")
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 28.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {prisjegerViewModel.butikkDialog.value = false}
                ) {
                    Text("Tilbake")
                }
            }
            Row(modifier = Modifier
                .padding(all = 8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .weight(3F)
                        .padding(start = 20.dp)
                ) {
                    Text(
                        text = "Butikker",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.size(10.dp))
                    for (butikker in valgbare) {// looper ut butikknavn
                        Text(butikker)
                        Divider(color = MaterialTheme.colors.primary, thickness = 2.dp)
                        Spacer(Modifier.size(10.dp))
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(2F)
                        .padding(end = 20.dp)
                ) {
                    Text(text = "Totalsum",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.size(10.dp))
                    for (butikker in valgbare) {// looper ut enhetspriser
                        Text(prisjegerViewModel.finnSumPrButikk(butikker).toString())
                        Divider(color = MaterialTheme.colors.primary, thickness = 2.dp)
                        Spacer(Modifier.size(10.dp))
                    }
                    Spacer(Modifier.size(30.dp))
                }
            }
        }
    )
} // slutt butikkDialog










/**
 * Funksjonen viser AlertDialog med valg for handleliste
 */
@Composable
fun VisValg(prisjegerViewModel: PrisjegerViewModel) {
    var text by remember { mutableStateOf("") }
    var tittel by remember { mutableStateOf("Skriv inn navn på ny handleliste")}

    AlertDialog(
        onDismissRequest = {
            prisjegerViewModel.valgDialog.value = false
        },
        title = {
            Text(tittel)
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
                        if (prisjegerViewModel.kontrollerListenavn(text)) {
                            tittel = "Listen finnes fra før, velg et annet navn"
                            text = ""
                        } else if (text.length > 20){ // antall tillate tegn
                            tittel = "Listenavn kan maksimalt bestå av 20 tegn, " +
                                    "velg et annet navn"
                            text = ""
                        } else {
                            prisjegerViewModel.setListeNavn(text) // endrer listenavn
                            prisjegerViewModel.oppdaterAlleDataFraApi() // oppdaterer alle data fra server
                            prisjegerViewModel.valgDialog.value = false
                            prisjegerViewModel.setButikknavn("Velg butikk")
                        }
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
                    prisjegerViewModel.valgDialog.value = false
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
                    prisjegerViewModel.valgDialog.value = false
                }
            ) {
                Text("Vis alle varer")
            }
            Button( // Knapp for å hente nye varer fra server (komplett liste - IGNORE)
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                onClick = {
                    prisjegerViewModel.oppdaterAlleDataFraApi() // oppdaterer alle data fra server
                    prisjegerViewModel.valgDialog.value = false
                }
            ) {
                Text("Oppdater handleliste")
            }
            Button( // Knapp for å slette handleliste fra lokal DB
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                onClick = {
                    prisjegerViewModel.vilSletteDialog.value = true
                    prisjegerViewModel.valgDialog.value = false
                }
            ) {
                Text("Slett handleliste !")
            }
            Button( // Knapp for å gå tilbake/ lukke alert
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                onClick = {
                    prisjegerViewModel.valgDialog.value = false
                }
            ) {
                Text("Tilbake")
            }
        }
    )
} // VisValg








/**
 * Funksjonen viser AlertDialog hvor bruker må bekrefte sletting av
 * handleliste
 */
@Composable
fun BekreftelseBruker(
    prisjegerViewModel: PrisjegerViewModel,
   // vilSlette: () -> Unit
) {

    var tittel by remember {
        mutableStateOf(
            "Vil du virkelig slette handleliste "
                    +prisjegerViewModel.currentListenavn
                    +" ?"
        )
    }
    AlertDialog(
        onDismissRequest = {prisjegerViewModel.vilSletteDialog.value =
            !prisjegerViewModel.vilSletteDialog.value},
        title = {
            Text(text = tittel)
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 28.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Column() {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {prisjegerViewModel.vilSletteDialog.value =
                            !prisjegerViewModel.vilSletteDialog.value}
                    ) {
                        Text("Angre sletting")
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            // TODO: setter currentListenavn tilbake til standard
                            tittel = "Handleliste "+prisjegerViewModel.currentListenavn+
                                    " ble slettet"
                            prisjegerViewModel.slettHandleliste()
                            prisjegerViewModel.vilSletteDialog.value =
                                !prisjegerViewModel.vilSletteDialog.value
                            prisjegerViewModel.setListeNavn("MinHandleliste")
                        }
                    ) {
                        Text("Slett "+prisjegerViewModel.currentListenavn)
                    }
                }

            }
        }
    )
} // slutt bekreftelseBruker









/**
 * Funksjonen vises innholdet i Header, menyknapp,
 * dropdown for butikk/ handleliste (egne funksjoner)
 *
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HeaderInnhold(
    prisjegerViewModel: PrisjegerViewModel,
    valgbare: Array<String>,
    alertDialog: () -> Unit,
    butikkDialog: () -> Unit
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
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp),
                onClick = butikkDialog,
            ) {
                Text(
                    text = "Totalsum : "
                            +prisjegerViewModel.finnSumPrButikk(prisjegerViewModel.currentButikk)
                    /*
                    "Handleliste: "
                            + prisjegerViewModel.currentListenavn
                            + "| Butikk: "
                            + prisjegerViewModel.currentButikk
                            + "| Totalsum: "
                            + prisjegerViewModel.sumPrHandleliste()
                     */

                )
            }
            Row {
                Column {
                    // DROPDOWN FOR VALG AV HANDLELISTE -> VISER VARER PR LISTENAVN FRA LOKAL DB
                    VelgButikk(prisjegerViewModel, valgbare)
                    //    VelgHandleliste(prisjegerViewModel)
                }
                Spacer(Modifier
                    .size(10.dp))
                Column {
                    //         VelgButikk(prisjegerViewModel, valgbare)
                    // DROPDOWN FOR VALG AV HANDLELISTE -> VISER VARER PR LISTENAVN FRA LOKAL DB
                    VelgHandleliste(prisjegerViewModel)
                }
            }
        }
    }
}







/**
 * Enkel nedtrekksmeny for å velge butikk
 * Butikknavn hentes fra backend API ved oppstart
 * TODO: oppdaterer seg ikke når ønskelig - ny handleliste - må lytte på currentButikk i vM
 */
@Composable
private fun VelgButikk(prisjegerViewModel: PrisjegerViewModel, valgbare: Array<String>) {

    //  val valgbare by prisjegerViewModel.butikkerAPI.observeAsState(initial = null)
    //   val valgbareToast = LocalContext.current.applicationContext
    var tekst = prisjegerViewModel.currentButikk // for rekomp
    var aktiv by remember { mutableStateOf(false) }

    //  Text("Velg butikk")
    Box(
        contentAlignment = Alignment.Center
    ) {
        // knapp for å åpne nedtrekksmeny

        OutlinedButton(
            onClick = {
                aktiv = true
            }) {
            Text(text = "Butikker :     \n\n$tekst")
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
                        // Oppdaterer pris når bytt butikk
                        prisjegerViewModel.getAPIPriserPrButikk() // oppdaterer priser fra server
                        prisjegerViewModel.setButikknavn(tekst) // henter priser for valgt butikk
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
        OutlinedButton(
            onClick = {
                aktiv = true
            }
        ) {  // navnet på listen som vises
            Text(text = "Handlelister :\n\n$tekst")
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
           // TODO her eller kontinuerlig?             prisjegerViewModel.getAPIPriserPrButikk() // oppdaterer priser
                        prisjegerViewModel.oppdaterVarerFraApi() // oppdaterer vareliste
                        prisjegerViewModel.oppdaterListeFraApi() // oppdaterer handleliste
                        prisjegerViewModel.setListeNavn(tekst) // oppdaterer listenavn
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
            .padding(vertical = 6.dp, horizontal = 10.dp),
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
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
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

    // HVIS BRUKER DRAR VENSTRE FOR SLETTING AV RAD
    if (dismissState.currentValue != DismissValue.Default) {
        prisjegerViewModel.settAntallTilNull(vare) // 0 i lokal DB, slettes fra sentral DB
        LaunchedEffect(Unit) { // for suspendert kall
            dismissState.reset() // resetter etter animasjon
        }
    }

    // HVIS BRUKER ØNSKER Å SE FLERE DETALJER OM HVER ENKELT VARE: UTLØSES AV ONCLICK VIS DETALJER
    if (visDetaljer) {
        visDetaljer(
            vare,
            prisjegerViewModel,
            valgbare,
            visDetaljer = { visDetaljer = !visDetaljer }
        )
    }

    // INNHOLD SOM KAN DRAS MOT VENSTRE FOR DELETE.
    // ANTALL SETTES TIL 0 I LOKAL DB, VARE SLETTES FRA HANDLELISTE
    // I SENTRAL DB
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
                    tint = MaterialTheme.colors.background,
                    contentDescription = "Delete Icon",
                    modifier = Modifier
                        .scale(scale)
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
 * Funksjonen viser innholdet (rader) i handleliste
 */
@Composable
private fun VarelisteItem(
    vare: Varer,
    prisjegerViewModel: PrisjegerViewModel,
    visDetaljer: () -> Unit
)
{
    var expandedState by remember { mutableStateOf(false) }

    Card(modifier = Modifier
        .fillMaxWidth()
        .animateContentSize(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing
            )
        ),
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        Column()
        {
            /** WHEN NOT EXPANDED */
            Row() {
                Column(
                    modifier = Modifier
                        .weight(3F)
                        .clickable { expandedState = !expandedState }
                ) {
                    //viser varenavn
                    Text(modifier = Modifier
                        .padding(13.dp),
                        text = vare.varenavn)
                }
                Column(
                    modifier = Modifier
                        .weight(1.2F)
                        .clickable { expandedState = !expandedState }
                ) {
                    Text(modifier = Modifier
                        .padding(13.dp),
                        text = prisjegerViewModel
                            .finnPrisPrVare(prisjegerViewModel
                                .currentButikk, vare.varenavn)+",-")
                }
                Column() {
                    TextButton( // knapp for å legge til
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (vare.antall < 1)
                                    (MaterialTheme.colors.primary)
                            else Color(0xF00ff00),
                            contentColor = MaterialTheme.colors.secondary
                        ),
                        onClick = {
                            prisjegerViewModel.inkementerVareAntall(
                                vare.varenavn,
                                vare.listenavn
                            )
                        }
                    ) {  // viser antall pr vare/ liste
                        Text(vare.antall.toString() + " +")
                    }
                }
            }
            /** WHEN EXPANDED */
            Row() {
                if (expandedState){
                    Column(
                        modifier = Modifier
                            .weight(3F)
                            .clickable { expandedState = !expandedState }
                    ) {
                        //viser vis detaljer-knapp
                        TextButton( // knapp for å legge til
                            modifier = Modifier
                                .padding(8.dp, top = 0.dp),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colors.secondary,
                                backgroundColor = MaterialTheme.colors.secondaryVariant
                            ),
                            onClick = visDetaljer,

                            ) {  // viser antall pr vare/ liste
                            Text(stringResource(id = R.string.showDetailsButton))
                        }

                    }
                    Column(
                        modifier = Modifier.weight(1.2F)
                    ) {
                        //viser varepris
                        Button( // knapp for å legge til
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colors.secondary,
                                backgroundColor = MaterialTheme.colors.secondaryVariant
                            ),
                            onClick = {
                                if (vare.antall >= 1) { // TODO: OBS! må være 1 dersom update lokal DB
                                    prisjegerViewModel.dekrementerVareAntall(
                                        vare.varenavn,
                                        vare.listenavn,
                                    )
                                }
                            }
                        ) {  // viser antall pr vare/ liste
                            Text("- 1")
                        }

                    }
                    Column() {
                        Button( // knapp for å legge til
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colors.secondary,
                                backgroundColor = MaterialTheme.colors.secondaryVariant
                            ),
                            onClick = {
                                prisjegerViewModel.inkementerVareAntall(
                                    vare.varenavn,
                                    vare.listenavn)
                            }
                        ) {  // viser antall pr vare/ liste
                            Text("+ 1")
                        }
                    }
                }
            }
        }
    }

}






/**
 * Funksjonen viser detaljer om aktuell vare i AlertDialog
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
                text = vare.varenavn,
                fontWeight = FontWeight.Bold,
            )
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 28.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = visDetaljer
                ) {
                    Text("Tilbake")
                }
            }
            Row(modifier = Modifier
                .padding(all = 8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .weight(2.5F)
                        .padding(start = 20.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Butikker",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.size(10.dp))
                    for (butikker in valgbare) {// looper ut butikknavn
                        Text(butikker)
                        Divider(color = MaterialTheme.colors.primary, thickness = 2.dp)
                        Spacer(Modifier.size(10.dp))
                    }
                    Spacer(Modifier.size(10.dp))
                    Text("Antall i handleliste")
                    Text("Summert for vare")
                    Spacer(Modifier.size(10.dp))

                    Button( // knapp for å trekke fra
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colors.secondary,
                            backgroundColor = MaterialTheme.colors.secondaryVariant
                        ),
                        onClick = {
                            // minimum 0 vare.
                            // TODO: Klarer ikke alltid å hente med, kontrolleres
                            // TODO: nå også av lokal DB
                            if (vare.antall >= 1) { //TODO: OBS !!!!   1/ 0
                                prisjegerViewModel.dekrementerVareAntall(
                                    vare.varenavn,
                                    vare.listenavn,
                                )
                            }
                        }
                    ) {
                        Text("- 1")
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(2.5F)
                        .padding(end = 20.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Enhetspris",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.size(10.dp))
                    for (butikker in valgbare) {// looper ut enhetspriser
                        Text(
                            prisjegerViewModel
                                .finnPrisPrVare(butikker, vare.varenavn)
                        )
                        Divider(color = MaterialTheme.colors.primary, thickness = 2.dp)
                        Spacer(Modifier.size(10.dp))
                    }
                    Spacer(Modifier.size(10.dp))
                    Text(": "+vare.antall)
                    Text(": "+prisjegerViewModel.sumPrVare(vare)+",-")
                    Spacer(Modifier.size(10.dp))

                    Button( // knapp for å legge til
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colors.secondary,
                            backgroundColor = MaterialTheme.colors.secondaryVariant
                        ),
                        onClick = {
                            prisjegerViewModel.inkementerVareAntall(
                                vare.varenavn,
                                vare.listenavn
                            )
                        }
                    ) {
                        Text("+ 1")
                    }
                    Spacer(Modifier.size(30.dp))
                }
            }
        }
    )
}

















