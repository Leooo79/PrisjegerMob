package no.usn.rygleo.prisjegermobv1.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahmoud.composecharts.barchart.BarChart
import com.mahmoud.composecharts.barchart.BarChartEntity
import no.usn.rygleo.prisjegermobv1.R
import java.lang.Float.NEGATIVE_INFINITY
import java.lang.Float.POSITIVE_INFINITY
import kotlin.math.roundToInt

/**
 * SammenligningScreen inneholder søkefelt for å finne vare
 * Så presenteres dataen i en søylediagram og tabell
 * fun MainChart() inneholder søylediagrammet
 * fun Filtertabell(tabellItem) inneholder alt av filter og tabell
 * fun Sokefelt() inneholder textfieldet. Sokefeltvisning er itemene som blir vist under
 */
@Composable
fun SammenligningScreen(prisjegerViewModel: PrisjegerViewModel) {
    //Booleans for å passe på at riktige komponenter blir vist
    val textFieldFocus = remember { mutableStateOf(false) }
    val grafFokus = remember { mutableStateOf(false) }
    val filterLaget = remember { mutableStateOf(false) }

    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val valgtVare = rememberSaveable { mutableStateOf("") }

    //Data hentet fra lokal database
    val vareListe = prisjegerViewModel.hentVarerAPI.observeAsState(initial = emptyArray())
    val butikkListe by prisjegerViewModel.butikkerAPI.observeAsState(initial = emptyArray())

    //Listene for å lagre data
    val filterListe = rememberSaveable { mutableStateOf(ArrayList<String>()) }
    val filterteButikkListe = rememberSaveable { ArrayList<String>() }
    val filtertePrisListe = rememberSaveable { ArrayList<Float>() }

    //Flerspåklighet i string
    val searchForItemLabel = stringResource(id = R.string.searchForItem)
    val seeHistory = stringResource(id = R.string.seeHistory)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.secondary)
    ) {
        if (!grafFokus.value) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .background(MaterialTheme.colors.secondary)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Sokefelt(
                    textState,
                    textFieldFocus,
                    vareListe,
                    valgtVare
                )
                if (valgtVare.value == "") {
                    Text(
                        text = searchForItemLabel,
                        color = MaterialTheme.colors.onPrimary,
                        fontWeight = FontWeight.SemiBold,
                        style = TextStyle(
                            fontSize = 22.sp,
                            shadow = Shadow(
                                color = Color.Black,
                                blurRadius = 5f
                            )
                        )
                    )
                }
                if (valgtVare.value != "") {
                    Column(modifier = Modifier
                        .verticalScroll(ScrollState(1000)),
                        horizontalAlignment = Alignment.CenterHorizontally)
                    {
                        //SokNyVareButton(valgtVare)
                        Text(
                            text = valgtVare.value,
                            color = MaterialTheme.colors.onPrimary,
                            style = TextStyle(
                                fontSize = 22.sp,
                                shadow = Shadow(
                                    color = Color.Black,
                                    blurRadius = 5f
                                )
                            )
                        )
                        FilterTabell(butikkListe,
                            filterListe = filterListe,
                            filterLaget,
                            valgtVare,
                            prisjegerViewModel,
                            filterteButikkListe,
                            filtertePrisListe)
                        Button(
                            onClick = { grafFokus.value = true },
                            modifier = Modifier.padding(top = 10.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.onPrimary
                            )
                        ) {
                            Text(modifier = Modifier
                                .padding(10.dp),
                                text = seeHistory)
                        }
                    }
                }
                else {
                    Image(
                        painter = painterResource(id = R.drawable.searchhand2),
                        contentDescription = "Bilde av mann med statistikk",
                        modifier = Modifier
                            .height(1000.dp)
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }
            }
        }
        else {
            Column() {
                OutlinedButton(
                    onClick = { grafFokus.value = false },
                    contentPadding = PaddingValues(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 170.dp, vertical = 0.dp)
                        .fillMaxWidth()
                        .defaultMinSize(minWidth = 0.dp, minHeight = 0.dp),
                    elevation = ButtonDefaults.elevation(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
                    )
                ) {
                    Image(modifier = Modifier
                        .width(40.dp)
                        .height(40.dp),
                        painter = painterResource(R.drawable.leftarrow),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
                    )
                }
                Column(modifier = Modifier
                    .verticalScroll(state = ScrollState(2000))
                    .background(Color.White)) {
                    MainChart(filtertePrisListe, filterteButikkListe)
                }
            }
        }
    }
}

/**
 * Inneholder filteret for tabellen og starter opp TabellItem(),
 * som lager tabellen i visningen. Det er grunn av at endringer i filteret
 * skal lage tabellen på nytt basert på innstillingen på filteret
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FilterTabell(
    butikkListe: Array<String>,
    filterListe: MutableState<ArrayList<String>>,
    filterLaget: MutableState<Boolean>,
    valgtVare: MutableState<String>,
    prisjegerViewModel: PrisjegerViewModel,
    filterteButikkListe: ArrayList<String>,
    filtertePrisListe: ArrayList<Float>
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue =
    if (expandedState) 180f else 0f)

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 10.dp)
        .animateContentSize(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing
            )
        ),
        backgroundColor = Color(0xFFd9eeee),
        shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp),
        onClick = {
            expandedState = !expandedState
        },
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(1.dp)) {
            Row(modifier = Modifier
                .background(MaterialTheme.colors.primary),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(modifier = Modifier
                    .fillMaxWidth(),
                    text = "Filter",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
                IconButton(modifier = Modifier
                    .alpha(ContentAlpha.medium)
                    .rotate(rotationState),
                    onClick = { expandedState = !expandedState }) {
                    IconButton(modifier = Modifier
                        .alpha(ContentAlpha.medium)
                        .rotate(rotationState),
                        onClick = {
                            expandedState = !expandedState
                        }) {
                        Icon(modifier = Modifier,
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Drop-Down Arrow",
                        )
                    }
                }
            } //End of column
            Divider(color = MaterialTheme.colors.onPrimary, thickness = 2.dp)
            if (expandedState) {
                Row(modifier = Modifier
                    .padding(top = 2.dp)
                    .background(Color.White)
                    .fillMaxWidth(),
                )
                {
                    if (!filterLaget.value) {
                        for (butikker in butikkListe) {
                            filterListe.value.add(butikker)
                        }
                        filterLaget.value = true
                    }
                    Column() {
                        Row(
                            modifier = Modifier
                                .background(MaterialTheme.colors.secondaryVariant)
                                .padding(10.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Column() {
                                for (butikker in butikkListe) {
                                    val checked = remember { mutableStateOf(filterListe.value.contains(butikker)) }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = checked.value,
                                            onCheckedChange = {
                                                if (it) {
                                                    if (!filterListe.value.contains(butikker)) {
                                                        filterListe.value.add(butikker)
                                                        checked.value = true
                                                    }
                                                }
                                                else {
                                                    filterListe.value.remove(butikker)
                                                    checked.value = false
                                                }
                                            }
                                        )
                                        Text(text = butikker, color = MaterialTheme.colors.onSecondary)
                                    }
                                }
                            }
                        }
                        Divider(color = MaterialTheme.colors.onPrimary, thickness = 2.dp)
                    }
                }
            }
        }
    }
    Column() {
        TabellItem(
            butikkListe = butikkListe,
            valgtVare = valgtVare,
            prisjegerViewModel = prisjegerViewModel,
            filterListe = filterListe,
            filterLaget = filterLaget,
            filtertePrisListe = filtertePrisListe,
            filterteButikkListe = filterteButikkListe,
        )
    }
}

/**
 * Lager søylediagrammet for å vise forskjellen på priser visuelt
 * Kalkulerer også høyeste pris for å lage enhetene for vertikale axisen
 */
@Composable
private fun MainChart(
    filtertePrisListe: ArrayList<Float>,
    filterteButikkListe: ArrayList<String>
) {
    var høyestPris = NEGATIVE_INFINITY
    val barChartData = ArrayList<BarChartEntity>()
    for (i in filterteButikkListe.indices) {
        barChartData.add(BarChartEntity(filtertePrisListe[i],
            label = filterteButikkListe[i],
            color = MaterialTheme.colors.onPrimary))
        val hentetPris = filtertePrisListe[i]
        if (hentetPris > høyestPris) høyestPris = hentetPris
    }
    høyestPris = ((høyestPris / 10.0).roundToInt() * 10).toFloat()
    høyestPris += 5
    val verticalAxis = ArrayList<Float>()
    val dekramere = høyestPris/4
    var verAxisValue = 0.0f
    for (i in 4 downTo 0) {
        verticalAxis.add(verAxisValue)
        verAxisValue += dekramere
    }
    BarChart(
        barChartData = barChartData,
        axisColor = MaterialTheme.colors.onSecondary,
        verticalAxisValues = verticalAxis,
        horizontalAxisLabelFontSize = 12.sp,
        verticalAxisLabelFontSize = 12.sp)
}

/**
 * Sokefelt() inneholder selve tekstfeltet og startet opp Sokefeltvisningen
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Sokefelt(
    state: MutableState<TextFieldValue>,
    textFieldFocus: MutableState<Boolean>,
    vareListe: State<Array<String>?>,
    valgtVare: MutableState<String>
) {
    //Oppretter referanse til keybord
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
                        textFieldFocus.value = !textFieldFocus.value
                    }
                }
            }
            .clickable(onClick = {
            }),
        textStyle = TextStyle(color = MaterialTheme.colors.onSecondary, fontSize = 18.sp),
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
                        textFieldFocus.value = !textFieldFocus.value
                        focusManager.clearFocus()
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "test",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
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
    SokeFeltVisning(
        vareListe,
        state,
        textFieldFocus,
        valgtVare,
        focusManager
    )
}

/**
 * SokeFeltVisning() er lazy kolonne som holder varene (Sokliste)
 */
@Composable
private fun SokeFeltVisning(
    vareListe: State<Array<String>?>,
    state: MutableState<TextFieldValue>,
    textFieldFocus: MutableState<Boolean>,
    valgtVare: MutableState<String>,
    focusManager: FocusManager
) {
    val visRettListe = ArrayList<String>()
    // Kun varelinjer tilhørende inneværende liste(navn) vises
    if (textFieldFocus.value) {
        for (varer in vareListe.value!!) {
            visRettListe.add(varer)
        }
        var filtrerteVarer: ArrayList<String>
        // bygger LazyColumn - filtrerte treff eller hele lista
        LazyColumn(
            Modifier
                .fillMaxWidth()
        )
        {
            val leterEtter = state.value.text
            filtrerteVarer = if (leterEtter.isEmpty()) {
                visRettListe
            } else {
                val treffListe = ArrayList<String>()
                // filter for sammenligning av strenger. Søker på kombinasjoner av char
                // ved å lytte på søkefelt
                for (varer in visRettListe) {
                    if (varer.lowercase().contains(leterEtter.lowercase())) {
                        treffListe.add(varer)
                    }
                }
                treffListe
            }
            items(filtrerteVarer ) { filtrerte ->
                Sokliste(filtrerte, textFieldFocus, valgtVare, focusManager)
            }
        }
    }
}

/**
 * Lager individuelle items i SokeFeltVisning.
 * Har onClick som gjør sin item (vare) til valgtVare
 */
@Composable
fun Sokliste(
    filtrerte: String,
    textFieldFocus: MutableState<Boolean>,
    valgtVare: MutableState<String>,
    focusManager: FocusManager
) {
    OutlinedButton(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.secondaryVariant)
        .padding(0.dp)
        .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
        shape = RoundedCornerShape(0.dp),
        contentPadding = PaddingValues(14.dp),
        onClick =  {
            textFieldFocus.value = !textFieldFocus.value
            valgtVare.value = filtrerte
            focusManager.clearFocus()
        }) {
        Text(text = filtrerte, color = MaterialTheme.colors.onSecondary)
    }
}

/**
 * TabellItem() er funksjonen som lager tabellen for prisnavn
 * og butikknavn
 */
@Composable
private fun TabellItem(
    butikkListe: Array<String>,
    valgtVare: MutableState<String>,
    prisjegerViewModel: PrisjegerViewModel,
    filterListe: MutableState<ArrayList<String>>,
    filterLaget: MutableState<Boolean>,
    filtertePrisListe: ArrayList<Float>,
    filterteButikkListe: ArrayList<String>) {
    var lavestPris = POSITIVE_INFINITY
    filterteButikkListe.clear() //Trengs slik at det ikke blir duplikater i data
    filtertePrisListe.clear()
    for (butikker in butikkListe) {
        filterteButikkListe.add(butikker)
        val hentetPris : Float =
            prisjegerViewModel.finnPrisPrVare(butikker, valgtVare.value).toFloat()
        filtertePrisListe.add(hentetPris)
        if (hentetPris < lavestPris) lavestPris = hentetPris
    }
    Card(
        shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp),
    ) {
        Column() {
            Row(modifier = Modifier
                .background(MaterialTheme.colors.secondaryVariant)
                .padding(10.dp),
            ) {
                //Butikknavn
                Column(
                    modifier = Modifier
                        .weight(6F)
                        .padding(start = 20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.store),
                        color = MaterialTheme.colors.onSecondary,
                        fontWeight = FontWeight.Bold
                    )
                    for (butikker in butikkListe) {
                        if (filterListe.value.contains(butikker) || !filterLaget.value) {
                            TabellItemButikk(butikker)
                        }
                    }
                }
                //Pris
                Column(
                    modifier = Modifier
                        .weight(2F)
                ) {
                    Text(
                        text = stringResource(id = R.string.price),
                        color = MaterialTheme.colors.onSecondary,
                        fontWeight = FontWeight.Bold
                    )
                    for (butikker in butikkListe) {
                        val hentetPris : Float =
                            prisjegerViewModel.finnPrisPrVare(butikker, valgtVare.value).toFloat()
                        if (filterListe.value.contains(butikker) || !filterLaget.value) {
                            if (hentetPris == lavestPris) {
                                TabellItemPris(prisjegerViewModel
                                    .finnPrisPrVare(butikker, valgtVare.value), true)
                            } else {
                                TabellItemPris(prisjegerViewModel
                                    .finnPrisPrVare(butikker, valgtVare.value), false)
                            }
                        }
                    }
                } //End of column
                Spacer(Modifier.size(30.dp))
            } //End of row
            Divider(color = MaterialTheme.colors.onSecondary, thickness = 3.dp)
            Box(modifier = Modifier
                .padding(start = 30.dp, bottom = 10.dp, top = 10.dp)) {
                Text(
                    text = stringResource(id = R.string.lastUpdateLabel) +
                            prisjegerViewModel.priserPrButikk.value!!.dato,
                    color = MaterialTheme.colors.onSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
        } //End of column
    }
}

/**
 * TabellItemButikk(butikknavn) lager text for å plassere i tabellen
 */
@Composable
private fun TabellItemButikk(butikk : String) {
    Spacer(Modifier.size(10.dp))
    Text(
        text = butikk,
        color = MaterialTheme.colors.onSecondary
    )
    Divider(color = MaterialTheme.colors.onSecondary, thickness = 2.dp)
    Spacer(Modifier.size(10.dp))
}

/**
 * TabellItempris(pris) lager text for å plassere i tabellen
 */
@Composable
private fun TabellItemPris(pris: String, lavest: Boolean) {
    Spacer(Modifier.size(10.dp))
    Row() {
        Text(
            text = pris,
            color = MaterialTheme.colors.onSecondary
        )
        if (lavest) {
            Image(
                painter = painterResource(id = R.drawable.gronnhake),
                contentDescription = "Bilde av grønn hake",
                modifier = Modifier
                    .height(21.dp)
                    .padding(start = 2.dp)
            )
        }
    }
    Divider(color = MaterialTheme.colors.onSecondary, thickness = 2.dp)
    Spacer(Modifier.size(10.dp))
}
