package no.usn.rygleo.prisjegermobv1.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.ui.modifier.modifierLocalConsumer
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
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahmoud.composecharts.barchart.BarChart
import com.mahmoud.composecharts.barchart.BarChartEntity
import com.mahmoud.composecharts.ui.theme.DefaultAxisColor
import no.usn.rygleo.prisjegermobv1.R
import java.lang.Float.NEGATIVE_INFINITY
import java.lang.Float.POSITIVE_INFINITY
import kotlin.math.roundToInt

/**
 * SammenligningScreen inneholder viewet for prissammenligning
 */
@Composable
fun SammenligningScreen(prisjegerViewModel: PrisjegerViewModel) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val textFieldFocus = remember { mutableStateOf(false) }
    val grafFokus = remember { mutableStateOf(false) }
    val filterLaget = remember { mutableStateOf(false) }
    val aktiverInnstillinger = remember { mutableStateOf(false) }
    val valgtVare = rememberSaveable { mutableStateOf("Ingen") }
    val vareListe = prisjegerViewModel.hentVarerAPI.observeAsState(initial = emptyArray())
    val butikkListe by prisjegerViewModel.butikkerAPI.observeAsState(initial = emptyArray())
    var filterListe = rememberSaveable { mutableStateOf(ArrayList<String>()) }
    var filterteButikkListe = rememberSaveable { mutableStateOf(ArrayList<String>()) }
    var filtertePrisListe = rememberSaveable { mutableStateOf(ArrayList<Float>()) }
    val searchForItemLabel = stringResource(id = R.string.searchForItem)

    val scrollState = rememberScrollState()

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
                /*
                Text(
                    text = prissammenligningLabel,
                    color = MaterialTheme.colors.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                )

                 */
                Sokefelt(
                    textState,
                    textFieldFocus,
                    vareListe,
                    prisjegerViewModel,
                    valgtVare
                )
                if (valgtVare.value == "Ingen") {
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
                if (valgtVare.value != "Ingen") {
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
                        Innstillinger(butikkListe,
                            filterListe = filterListe,
                            filterLaget,
                            aktiverInnstillinger,
                            valgtVare,
                            prisjegerViewModel,
                            aktiverInnstillinger,
                            grafFokus,
                            filterteButikkListe,
                            filtertePrisListe)
                        //ExpandableCard(title = "Se graf", description = "", metode2 = MainChart())
                        //"see history"
                        Button(
                            onClick = { updateGrafFocus(grafFokus) },
                            modifier = Modifier.padding(top = 10.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.onPrimary
                            )
                        ) {
                            Text(modifier = Modifier
                                .padding(10.dp),
                                text = stringResource(id = R.string.seeHistory))
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
                    onClick = { updateGrafFocus(grafFokus) },
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
                    //"gå tilbake"
                    MainChart(filtertePrisListe, filterteButikkListe)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Innstillinger(
    butikkListe: Array<String>,
    filterListe: MutableState<ArrayList<String>>,
    filterLaget: MutableState<Boolean>,
    aktiverInnstillinger: MutableState<Boolean>,
    valgtVare: MutableState<String>,
    prisjegerViewModel: PrisjegerViewModel,
    aktiverInnstillinger1: MutableState<Boolean>,
    grafFokus: MutableState<Boolean>,
    filterteButikkListe: MutableState<ArrayList<String>>,
    filtertePrisListe: MutableState<ArrayList<Float>>
){
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue =
    if (expandedState) 180f else 0f)

    //Lagrer all data på et sted
    //Da kan det gjenbrukes til søylediagrammet

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 10.dp)
        .animateContentSize(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing
            )
        ),
        //backgroundColor = MaterialTheme.colors.secondaryVariant,
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
        tabellItem(
            butikkListe = butikkListe,
            valgtVare = valgtVare,
            prisjegerViewModel = prisjegerViewModel,
            filterListe = filterListe,
            filterLaget = filterLaget,
            aktiverInnstillinger = aktiverInnstillinger,
            filtertePrisListe = filtertePrisListe,
            filterteButikkListe = filterteButikkListe,
        )
    }
}

@Composable
private fun MainChart(
    filtertePrisListe: MutableState<ArrayList<Float>>,
    filterteButikkListe: MutableState<ArrayList<String>>
) {
    var høyestPris = NEGATIVE_INFINITY
    val farger = listOf<Color>(Color.Red, Color.Blue, Color.Magenta, Color.Green, Color.Cyan, Color.Yellow, Color(0xFFFFAA84), Color(0xFF9495E4))
    val barChartData = ArrayList<BarChartEntity>()
    for (i in filterteButikkListe.value.indices) {
        System.out.println(filterteButikkListe)
        barChartData.add(BarChartEntity(filtertePrisListe.value[i], label = filterteButikkListe.value[i], color = MaterialTheme.colors.onPrimary))
        var hentetPris = filtertePrisListe.value[i]
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Sokefelt(
    state: MutableState<TextFieldValue>,
    textFieldFocus: MutableState<Boolean>,
    vareListe: State<Array<String>?>,
    prisjegerViewModel: PrisjegerViewModel,
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
                        contentDescription = "test",
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
    sokeFeltVisning(
        vareListe,
        state,
        prisjegerViewModel,
        textFieldFocus,
        valgtVare,
        focusManager
    )
}

@Composable
private fun grafBoxButton(grafFokus: MutableState<Boolean>, tekst: String) {
    Button(modifier = Modifier
        .padding(start = 20.dp, end = 20.dp, top = 10.dp),
        onClick =  {
            updateGrafFocus(grafFokus)
        }) {
        Text(modifier = Modifier
            .padding(20.dp),
            text = tekst)
    }
}

@Composable
private fun sokeFeltVisning(
    vareListe: State<Array<String>?>,
    state: MutableState<TextFieldValue>,
    prisjegerViewModel: PrisjegerViewModel,
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
            } // OBS: Må bruke både varenavn og listenavn som key for id av unike
            items(filtrerteVarer ) { filtrerte ->
                sokliste(filtrerte, textFieldFocus, valgtVare, focusManager)
            }
        }
    }
}

@Composable
fun sokliste(
    filtrerte: String,
    textFieldFocus: MutableState<Boolean>,
    valgtVare: MutableState<String>,
    focusManager: FocusManager
)
{
    OutlinedButton(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.secondaryVariant)
        .padding(0.dp)
        .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
        shape = RoundedCornerShape(0.dp),
        contentPadding = PaddingValues(14.dp),
        onClick =  {
            updateTextFieldFocus(textFieldFocus)
            valgtVare.value = filtrerte
            focusManager.clearFocus()
        }) {
        Text(text = filtrerte, color = MaterialTheme.colors.onSecondary)
    }
}

@Composable
private fun tabellItem(
                       butikkListe: Array<String>,
                       valgtVare: MutableState<String>,
                       prisjegerViewModel: PrisjegerViewModel,
                       filterListe: MutableState<ArrayList<String>>,
                       filterLaget: MutableState<Boolean>,
                       aktiverInnstillinger: MutableState<Boolean>,
                       filtertePrisListe: MutableState<ArrayList<Float>>,
                       filterteButikkListe: MutableState<ArrayList<String>>) {
    var lavestPris = POSITIVE_INFINITY
    var høyestPris = NEGATIVE_INFINITY
    filterteButikkListe.value.clear() //Trengs slik at det ikke blir duplikater i data
    filtertePrisListe.value.clear()
        for (butikker in butikkListe) {
            filterteButikkListe.value.add(butikker)
            var hentetPris : Float = prisjegerViewModel.finnPrisPrVare(butikker, valgtVare.value).toFloat()
            filtertePrisListe.value.add(hentetPris)
            if (hentetPris < lavestPris) lavestPris = hentetPris
            if (hentetPris > høyestPris) høyestPris = hentetPris
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
                            tabellItemButikk(butikker)
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
                        var hentetPris : Float = prisjegerViewModel.finnPrisPrVare(butikker, valgtVare.value).toFloat()
                        if (filterListe.value.contains(butikker) || !filterLaget.value) {
                            if (hentetPris == lavestPris) {
                                tabellItemPris(prisjegerViewModel.finnPrisPrVare(butikker, valgtVare.value), true)
                            } else {
                                tabellItemPris(prisjegerViewModel.finnPrisPrVare(butikker, valgtVare.value), false)
                            }
                        }
                    }
                }
                /*
                //Dato
                Column(
                    modifier = Modifier
                        .weight(1F)
                        .padding(end = 0.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.date),
                        color = MaterialTheme.colors.onSecondary,
                        fontWeight = FontWeight.Bold
                    )

                }
                 */
                Spacer(Modifier.size(30.dp))
            } //End of row
            Divider(color = MaterialTheme.colors.onSecondary, thickness = 3.dp)
            Box(modifier = Modifier
                .padding(start = 30.dp, bottom = 10.dp, top = 10.dp)) {
                Text(
                    text = stringResource(id = R.string.lastUpdateLabel) + prisjegerViewModel.priserPrButikk.value!!.dato,
                    color = MaterialTheme.colors.onSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
        } //End of column
    }
}

@Composable
private fun tabellItemButikk(butikk : String) {
    Spacer(Modifier.size(10.dp))
    Text(
        text = butikk,
        color = MaterialTheme.colors.onSecondary
    )
    Divider(color = MaterialTheme.colors.onSecondary, thickness = 2.dp)
    Spacer(Modifier.size(10.dp))
}

@Composable
private fun tabellItemPris(pris: String, lavest: Boolean) {
    Spacer(Modifier.size(10.dp))
    Row() {
        Text(
            text = "$pris",
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

@Composable
private fun tabellItemDato(dato : String) {
    Spacer(Modifier.size(10.dp))
    Text(
        text = dato,
        color = MaterialTheme.colors.onSecondary
    )
    Divider(color = MaterialTheme.colors.onSecondary, thickness = 2.dp)
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

private fun updateGrafFocus(grafFokus: MutableState<Boolean>) {
    if (!grafFokus.value) {
        grafFokus.value = true
    }
    else if (grafFokus.value) {
        grafFokus.value = false
    }
}
