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
import androidx.compose.ui.graphics.Shadow
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
import com.mahmoud.composecharts.linechart.LineChart
import com.mahmoud.composecharts.linechart.LineChartEntity
import no.usn.rygleo.prisjegermobv1.R
import java.lang.Float.POSITIVE_INFINITY

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

    val prissammenligningLabel = stringResource(id = R.string.priceComparison)
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
                    .scrollable(state = scrollState, orientation = Orientation.Vertical)
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
                        aktiverInnstillinger)
                    //ExpandableCard(title = "Se graf", description = "", metode2 = MainChart())
                    //"see history"
                    grafBoxButton(grafFokus = grafFokus, tekst = stringResource(id = R.string.seeHistory))
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
            Column(modifier = Modifier
                .fillMaxSize()) {
                //"gå tilbake"
                grafBoxButton(grafFokus = grafFokus, tekst = stringResource(id = R.string.seeHistory))
                MainChart()
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
    aktiverInnstillinger1: MutableState<Boolean>
){
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
    tabellItem(
        butikkListe = butikkListe,
        valgtVare = valgtVare,
        prisjegerViewModel = prisjegerViewModel,
        filterListe = filterListe,
        filterLaget = filterLaget,
        aktiverInnstillinger = aktiverInnstillinger
    )
}

@Composable
private fun MainChart() {
    /*
    val barChartData = ArrayList<BarChartEntity>()
    barChartData.add(BarChartEntity(150.0f, Color(0xFF618A32), "1"))
    barChartData.add(BarChartEntity(450.0f, Color(0xFFC32A33), "2"))
    barChartData.add(BarChartEntity(300.0f, Color.Blue, "3"))
    barChartData.add(BarChartEntity(150.0f, Color.Cyan, "4"))
    barChartData.add(BarChartEntity(500.0f, Color.Magenta, "5"))
    BarChart(
        barChartData = barChartData,
        verticalAxisValues = listOf(0.0f, 100.0f, 200.0f, 300.0f, 400.0f, 500.0f)
    )
     */


    val lineChartData = listOf(
        LineChartEntity(10.0f, "Jan"),
        LineChartEntity(30.0f, "Feb"),
        LineChartEntity(40.0f, "Mar"),
        LineChartEntity(50.0f, "Apr"),
        LineChartEntity(20.0f, "Mai"),
    )

    LineChart(
        lineChartData = lineChartData,
        verticalAxisValues = listOf(0.0f, 25.0f, 50.0f, 75.0f),
    )
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
            cursorColor = MaterialTheme.colors.onPrimary,
            leadingIconColor = MaterialTheme.colors.onPrimary,
            trailingIconColor = MaterialTheme.colors.onPrimary,
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
                       aktiverInnstillinger: MutableState<Boolean>) {
    var lavestPris = POSITIVE_INFINITY
        for (butikker in butikkListe) {
            var hentetPris : Float = prisjegerViewModel.finnPrisPrVare(butikker, valgtVare.value).toFloat()
            if (hentetPris < lavestPris) {
                lavestPris = hentetPris
            }
    }
    Card(
        shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp),
    ) {
        Row(modifier = Modifier
            .background(MaterialTheme.colors.secondaryVariant)
            .padding(10.dp),
        ) {
            //Butikknavn
            Column(
                modifier = Modifier
                    .weight(2F)
                    .padding(start = 5.dp)
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
            Spacer(Modifier.size(30.dp))
        }
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
