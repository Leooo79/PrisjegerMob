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
        FiltrerListe(
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
        BekreftelseBruker(prisjegerViewModel)
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
    )
} // slutt HeaderVisning







/**
 * Funksjonen viser Alertdialog med totalsum pr liste pr butikk
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
                        Text(prisjegerViewModel.finnSumPrButikk(butikker))
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
                        } else if (text.length > 16){ // antall tillate tegn
                            tittel = "Listenavn kan maksimalt bestå av 16 tegn"
                            text = ""
                        } else {
                            prisjegerViewModel.setListeNavn(text) // endrer listenavn
                            prisjegerViewModel.oppdaterAlleDataFraApi() // oppdaterer alle data
                            prisjegerViewModel.valgDialog.value = false
                            prisjegerViewModel.setButikknavn("Velg butikk") // nullstill butikk
                        }
                    }
                ) {
                    Text("Lagre ny liste")
                }
            }
            //  Ekstra knapper
            Button( // Knapp for å filtrerte varer i handleliste. Antall > 0.
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                onClick = {
                    prisjegerViewModel.filtrerEtterAntall.value = true
                    prisjegerViewModel.valgDialog.value = false
                }
            ) {
                Text("Vis bare valgte")
            }
            Button( // Knapp for å vise alle. Inkludert antall == 0.
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                onClick = {
                    prisjegerViewModel.filtrerEtterAntall.value = false
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
            Button( // Knapp for å slette handleliste fra lokal DB. Egen alert med bekreftelse.
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                onClick = {
                    prisjegerViewModel.vilSletteDialog.value = true
                    prisjegerViewModel.valgDialog.value = false
                }
            ) {
                Text("Slett handleliste")
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
fun BekreftelseBruker(prisjegerViewModel: PrisjegerViewModel) {

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
                            tittel = "Handleliste "+prisjegerViewModel.currentListenavn+
                                    " ble slettet"
                            prisjegerViewModel.slettHandleliste() // slett liste lokalt/sentralt
                            prisjegerViewModel.vilSletteDialog.value =
                                !prisjegerViewModel.vilSletteDialog.value
                            prisjegerViewModel.setListeNavn("MinHandleliste") // nullstill listenavn
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
@Composable
private fun HeaderInnhold(
    prisjegerViewModel: PrisjegerViewModel,
    valgbare: Array<String>,
) {

    Card(
        backgroundColor = MaterialTheme.colors.secondary
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 6.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 10.dp, end = 10.dp)
            ) {
                Column(Modifier
                    .weight(3F)) {
                    // DropDown for valg av butikk
                    VelgButikk(prisjegerViewModel, valgbare)
                }
                Spacer(modifier = Modifier.weight(0.1F))
                Column(Modifier
                    .weight(4F)) {
                    // DropDown for valg av handleliste
                    VelgHandleliste(prisjegerViewModel)
                }
                Spacer(modifier = Modifier.weight(0.1F))
                Column(modifier = Modifier
                    .background(MaterialTheme.colors.primary)
                    .padding(10.dp)
                    .weight(3F)
                    .fillMaxWidth()
                    .clickable {
                        prisjegerViewModel.butikkDialog.value =
                            !prisjegerViewModel.butikkDialog.value
                    }
                ) {
                    Text(modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                        color = Color.White,
                        text = // viser totalsum pr handleliste pr butikk
                        prisjegerViewModel.finnSumPrButikk(prisjegerViewModel.currentButikk)
                    )
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

    //   val valgbareToast = LocalContext.current.applicationContext
    var tekst = prisjegerViewModel.currentButikk // for rekomp
    var aktiv by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .background(MaterialTheme.colors.primary)
        .fillMaxWidth()
        .padding(10.dp)
        .clickable { aktiv = !aktiv },
    ) {
        // knapp for å åpne nedtrekksmeny
            Text(modifier = Modifier
                .align(Alignment.CenterHorizontally),
                color = Color.White,
                text = tekst
            )
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

    Column(modifier = Modifier
        .background(MaterialTheme.colors.primary)
        //   .width(120.dp)
        .fillMaxWidth()
        .padding(10.dp)
        .clickable { aktiv = !aktiv },
        ) {  // navnet på listen som vises
            Text(modifier = Modifier
                .align(Alignment.CenterHorizontally),
                color = Color.White,
                text = tekst
            )
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
private fun FiltrerListe(
    vareListe: List<Varer>,
    state: MutableState<TextFieldValue>,
    prisjegerViewModel: PrisjegerViewModel,
    valgbare: Array<String>
) {
    val visRettListe = ArrayList<Varer>()
    // skal kun varer med antall > 0 vises?
    if (prisjegerViewModel.filtrerEtterAntall.value) {
        for (varer in vareListe) {
            if (varer.listenavn == prisjegerViewModel.currentListenavn && varer.antall > 0) {
                visRettListe.add(varer)
            }
        }
    }
    else {
        // Kun varelinjer tilhørende inneværende handleliste(navn) vises
        for (varer in vareListe) {
            if (varer.listenavn == prisjegerViewModel.currentListenavn) {
                visRettListe.add(varer)
            }
        }
    }
    var filtrerteVarer: ArrayList<Varer>
    // bygger LazyList - viser treff fra filteret eller hele handlelisten
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        val leterEtter = state.value.text // lytter på søkefelt, viser alle om felt er tomt
        filtrerteVarer = if (leterEtter.isEmpty()) {
            visRettListe
        } else {
            val treffListe = ArrayList<Varer>()
            // filter for sammenligning av tekst. Søker på kombinasjoner av char
            for (varer in visRettListe) {
                if (varer.varenavn.lowercase().contains(leterEtter.lowercase())) {
                    treffListe.add(varer)
                }
            }
            treffListe
        } // etablerer items basert på hva som skal vises og sender til VarelisteDialog for comp
        items(filtrerteVarer, {filtrerteVarer: Varer -> // Key = varenavn + listenavn
            filtrerteVarer.varenavn + filtrerteVarer.listenavn}) { filtrerte ->
                VarelisteDialog(filtrerte, prisjegerViewModel, valgbare)
        }
    }
}






/**
 * Funksjonen håndterer hva som skal vises fra Handleliste og bygger SwipeToDismiss
 * Kontrollerer for visning av Alertdialog med detaljer om hver vare
 * Videreformidler innhold til items/ rader
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

    // Antall pr vare (rad) settes til 0 ved left-swipe
    if (dismissState.currentValue != DismissValue.Default) {
        prisjegerViewModel.settAntallTilNull(vare) // 0 i lokal DB, slettes fra sentral DB
        LaunchedEffect(Unit) { // for suspendert kall
            dismissState.reset() // resetter etter animasjon
        }
    }

    // Viser detaljer om hver vare. Aktiveres av onClick knapp "Vis detaljer"
    if (visDetaljer) {
        visDetaljer(
            vare,
            prisjegerViewModel,
            valgbare,
            visDetaljer = { visDetaljer = !visDetaljer }
        )
    }
    // Innhold som kan swipes. Bygger "ramme" med animasjon og kaller videre på radinnhold.
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
            // Ekstra ytterramme for mindre mellomrom ved touch/ swipe, ikon for sletting
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
            // VarelisteItem bygger opp hver rad med innhold
            VarelisteItem(
                vare,
                prisjegerViewModel,
                visDetaljer = { visDetaljer = !visDetaljer }
            )
        }
    )
}








/**
 * Funksjonen viser innholdet (items/ rader) i handleliste.
 * Består av datafelt fra class/entity Varer lagret i lokal DB (emittes som Flow/ LiveData)
 */
@Composable
private fun VarelisteItem(
    vare: Varer,
    prisjegerViewModel: PrisjegerViewModel,
    visDetaljer: () -> Unit
)
{
    // viser ekstra rad med knapper i expandedState
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
            // Når ikke expandedState
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
            // Når expandedState:
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
                                if (vare.antall >= 1) {
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
                            // minimum 0 vare. Kontrolleres også av lokal DB ved update
                            if (vare.antall >= 1) {
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

















