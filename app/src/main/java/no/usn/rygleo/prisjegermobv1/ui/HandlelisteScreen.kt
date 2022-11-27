package no.usn.rygleo.prisjegermobv1.ui

import android.content.res.Resources
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
        if (!prisjegerViewModel.handleModus.value) {
            HeaderDialog(
                uiState, // kun for rekomp ved oppdatering av state
                prisjegerViewModel,
                valgbare
            )
            Sokefelt(
                textState
            )
        }
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
        .padding(start = 10.dp, end = 10.dp, top = 9.dp, bottom = 0.dp),
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        Row(
            modifier = Modifier
                .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(3F)
            ) {
                Text(stringResource(id = R.string.item), fontWeight = FontWeight.Bold)
            }
            Column(
                modifier = Modifier
                    .weight(1F)
            ) {
                Text(stringResource(id = R.string.price), fontWeight = FontWeight.Bold)
            }
            Column(
                modifier = Modifier
                    .weight(1F)
            ) {
                Text(stringResource(id = R.string.amount), fontWeight = FontWeight.Bold)
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
            Text(text = stringResource(id = R.string.totalSumStores))
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
                    Text(stringResource(id = R.string.goBack))
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
                        text = stringResource(id = R.string.store),
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
                    Text(text = stringResource(id = R.string.totalsum),
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

    /** Språkvariabler */
    val listAlreadyExistLabel = stringResource(id = R.string.listAlreadyExists)
    val max16lettersLabel = stringResource(id = R.string.max16letters)
    val chooseStoreLabel = stringResource(id = R.string.chooseStore)
    val newNameLabel = stringResource(id = R.string.newNameforShoppingList)

    //Disse to variablene under var begge
    //val tittel/text by remember { mutableStateOf("")}
    //Tror ikke det er nødvendig, så endret det
  //  var text =""
  //  var tittel = newNameLabel
 // TODO: endret tilbake etter Gaute
    var text by remember {mutableStateOf("")}
    var tittel by remember {mutableStateOf("")}



    AlertDialog(

        onDismissRequest = {
            prisjegerViewModel.valgDialog.value = false
        },

        title = {
            Text(tittel)
        },
        /*text = {
            Column() {
                TextField(
                    value = text,
                    onValueChange = { text = it }
                )
            }
        },*/
        buttons = {
            Column(
                modifier = Modifier
                    .verticalScroll(state = ScrollState(2000)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it }
                )
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) { // Bekreftelse fra bruker
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { // Bekreftelse lukker alert og oppretter ny liste i lokal DB
                            if (prisjegerViewModel.kontrollerListenavn(text)) {

                                tittel = listAlreadyExistLabel
                                text = ""
                            } else if (text.length > 16 || text.isEmpty()){ // antall tillate tegn
                                tittel = max16lettersLabel
                                text = ""
                            } else {
                                prisjegerViewModel.setListeNavn(text) // endrer listenavn
                                prisjegerViewModel.oppdaterAlleDataFraApi() // oppdaterer alle data
                                prisjegerViewModel.valgDialog.value = false
                   // TODO: trenger ikke lengre             prisjegerViewModel.setButikknavn("Velg butikk") // nullstill butikk
                            }
                        }
                    ) {
                        Text(stringResource(id = R.string.saveNewList))
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
                    Text(stringResource(id = R.string.showOnlySelected))
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
                    Text(stringResource(id = R.string.showAllItems))
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
                    Text(stringResource(id = R.string.updateShoppinglist))
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
                    Text(stringResource(id = R.string.deleteShoppinglist))
                }
                Button( // Knapp for å gå tilbake/ lukke alert
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    onClick = {
                        prisjegerViewModel.valgDialog.value = false
                    }
                ) {
                    Text(stringResource(id = R.string.goBack))

                }
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

    /** språkvariabler */
    val confirmDeleteLabel = stringResource(id = R.string.confirmDelete)
    val shoppingListLabel = stringResource(id = R.string.shoppingList)
    val wasDeletedLabel = stringResource(id = R.string.wasDeleted)
    val deleteLabel = stringResource(id = R.string.delete)
    
    
    
    var tittel by remember {
        mutableStateOf(
            "" + confirmDeleteLabel +  " "
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
                        Text(stringResource(id = R.string.CancelDelete))
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            tittel = shoppingListLabel + " " + prisjegerViewModel.currentListenavn + " " +
                                    wasDeletedLabel
                            prisjegerViewModel.slettHandleliste() // slett liste lokalt/sentralt
                            prisjegerViewModel.vilSletteDialog.value =
                                !prisjegerViewModel.vilSletteDialog.value
                            prisjegerViewModel.setListeNavn("MinHandleliste") // nullstill listenavn
                            // TODO: dersom 1 handleliste vises: oppdater visning
                            prisjegerViewModel.getLokaleVarer(prisjegerViewModel.currentListenavn)
                        }
                    ) {
                        Text(deleteLabel+ " " +prisjegerViewModel.currentListenavn)
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
                        color = MaterialTheme.colors.onPrimary,
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
                color = MaterialTheme.colors.onPrimary,
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
                //        prisjegerViewModel.hentUtPriserPrButikk(tekst) // TODO: ny metode
                        prisjegerViewModel.setButikknavn(tekst) // henter priser for valgt butikk
                    },
                ) {
                    Text(text = itemValue, color = MaterialTheme.colors.onSecondary)
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
                color = MaterialTheme.colors.onPrimary,
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
                        // TODO: Det vises nå kun en handleliste av gangen
                        prisjegerViewModel.getLokaleVarer(tekst) // oppdaterer Flow fra DB
                    },
                ) {
                    Text(text = itemValue, color = MaterialTheme.colors.onSecondary)
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
        textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
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
        // bør testes på håndholdt enhet :"
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colors.onSecondary,
            cursorColor = MaterialTheme.colors.onSecondary,
            leadingIconColor = MaterialTheme.colors.onSecondary,
            trailingIconColor = MaterialTheme.colors.onSecondary,
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
    var visRettListe = ArrayList<Varer>()
    for (varer in vareListe) { // vis alle varer i handleliste,
        if (!prisjegerViewModel.filtrerEtterAntall.value) {
            visRettListe.add(varer)
        } // eller bare varer med antall > 0
        else if (varer.antall > 0) {
            visRettListe.add(varer)
        }
    }

    /*
// Viser kun varelinjer for inneværende handlelsite og med antall > 0
if (prisjegerViewModel.filtrerEtterAntall.value) {
    for (varer in vareListe) {
       // TODO: dersom alle lister vises: if (varer.listenavn == prisjegerViewModel.currentListenavn && varer.antall > 0) {
        if (varer.antall > 0) { // TODO: dersom kun 2 handleliste vises
            visRettListe.add(varer)
        }
    }
}
else {
    // Viser kun varelinjer for inneværende handlelsite
    for (varer in vareListe) {
        if (varer.listenavn == prisjegerViewModel.currentListenavn) {
            visRettListe.add(varer)
        }
    }
}

 */
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
        // TODO: om kun 1 handleliste vises, trenger ikke listenavn inngå i Key:
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
    var endreFarge by remember { mutableStateOf(false) }
    val dismissState = rememberDismissState()

    // Suspendert kode som reaksjon på swipe (kjører i LaunchedEffect) :
        // Antall pr vare/ rad settes til 0 ved left-swipe, animasjon reset()
    if (dismissState.currentValue != DismissValue.Default && !prisjegerViewModel.handleModus.value) {
        prisjegerViewModel.settAntallTilNull(vare) // antall -> 0 i lokal DB, slettes fra sentral DB
        LaunchedEffect(Unit) { // for suspendert kall
            dismissState.reset() // resetter etter animasjon
        } // i handlemodus endres farge, antall beholdes
    } else if (dismissState.currentValue != DismissValue.Default) {
        LaunchedEffect(Unit) { // for suspendert kall
            dismissState.reset() // resetter etter animasjon
            endreFarge = true // endrer farge for å indikere at vare er handlet
        }
    }

    // Om bruker går ut av handlemodus får alle rader tilbake opprinnelig farge
    if (!prisjegerViewModel.handleModus.value) endreFarge = false

    // Viser detaljer om hver vare. Aktiveres av onClick knapp "Vis detaljer"
    if (visDetaljer) {
        visDetaljer(
            vare,
            prisjegerViewModel,
            valgbare,
            visDetaljer = { visDetaljer = !visDetaljer }
        )
    }

    // Innhold som kan swipes. Bygger animert "ramme" og kaller videre på radinnhold.
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
            if (prisjegerViewModel.handleModus.value) {
                HandleItem( // TODO: Her kan man kalle på ny composable for handlemodus
                    vare,
                    prisjegerViewModel,
                    visDetaljer = { visDetaljer = !visDetaljer },
                    endreFarge
                )
            } else {
                // VarelisteItem bygger opp hver rad med innhold
                VarelisteItem(
                    vare,
                    prisjegerViewModel,
                    visDetaljer = { visDetaljer = !visDetaljer },
                    endreFarge
                )
            }
        }
    )
}



@Composable
private fun HandleItem(
    vare: Varer,
    prisjegerViewModel: PrisjegerViewModel,
    visDetaljer: () -> Unit,
    endreFarge: Boolean
)
{
    // viser ekstra rad med knapper i expandedState
    var expandedState by remember { mutableStateOf(false) }

    Card(modifier = Modifier
        .fillMaxWidth(),
   //     .animateContentSize(
   //         animationSpec = tween(
  //              durationMillis = 300,
  //              easing = LinearOutSlowInEasing
  //          )
  //      ),
        backgroundColor =
        if (endreFarge)  {
            Color.DarkGray
        }
        else MaterialTheme.colors.secondaryVariant,
        )
    {
        Column()
        {
            // Når ikke expandedState
            Row() {
                Column(
                    modifier = Modifier
                        .weight(3F)
    //                    .clickable { expandedState = !expandedState }
                ) {
                    //viser varenavn
                    Text(modifier = Modifier
                        .padding(13.dp),
                        text = vare.varenavn)
                }
                Column(
                    modifier = Modifier
                        .weight(1.2F)
     //                   .clickable { expandedState = !expandedState }
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
           //                 .clickable { expandedState = !expandedState }
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
 * Funksjonen viser innholdet (items/ rader) i handleliste.
 * Består av datafelt fra class/entity Varer lagret i lokal DB (emittes som Flow/ LiveData)
 */
@Composable
private fun VarelisteItem(
    vare: Varer,
    prisjegerViewModel: PrisjegerViewModel,
    visDetaljer: () -> Unit,
    endreFarge: Boolean
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
        backgroundColor =
        if (endreFarge) Color.DarkGray
        else MaterialTheme.colors.secondaryVariant,
    ) {
        Column()
        {
            // Når ikke expandedState
            Row(modifier = Modifier
                .clickable { expandedState = !expandedState }) {
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
                                    (MaterialTheme.colors.secondaryVariant)
                            else Color(0x4300FF00),
                            contentColor = MaterialTheme.colors.secondary
                        ),
                        onClick = {
                            prisjegerViewModel.inkementerVareAntall(
                                vare.varenavn,
                                vare.listenavn
                            )
                        }
                    ) {  // viser antall pr vare/ liste
                        Text(vare.antall.toString() + " +", color = if (vare.antall < 1) {
                            MaterialTheme.colors.onSecondary
                        } else {
                            MaterialTheme.colors.onSecondary
                        })
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
                                contentColor = MaterialTheme.colors.onPrimary,
                                backgroundColor = MaterialTheme.colors.primary
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
            Column(modifier = Modifier
                .verticalScroll(state = ScrollState(2000))) {
                Row(
                    modifier = Modifier.padding(all = 28.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = visDetaljer
                    ) {
                        Text(stringResource(id = R.string.goBack))
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
                            text = stringResource(id = R.string.store),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.size(10.dp))
                        for (butikker in valgbare) {// looper ut butikknavn
                            Text(butikker)
                            Divider(color = MaterialTheme.colors.primary, thickness = 2.dp)
                            Spacer(Modifier.size(10.dp))
                        }
                        Spacer(Modifier.size(10.dp))
                        Text(stringResource(id = R.string.amount), color = MaterialTheme.colors.onSecondary)
                        Text(stringResource(id = R.string.totalsum), color = MaterialTheme.colors.onSecondary)
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
                            Text("- 1", color = MaterialTheme.colors.secondary)
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(2.5F)
                            .padding(end = 20.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.price),
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
        }
    )
}

















