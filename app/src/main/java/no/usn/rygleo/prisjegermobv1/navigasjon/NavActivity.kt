package no.usn.rygleo.prisjegermobv1.navigasjon

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import no.usn.rygleo.prisjegermobv1.R
import no.usn.rygleo.prisjegermobv1.data.NavItem
import no.usn.rygleo.prisjegermobv1.ui.*

/**
 * MainScreenView med navigasjonsskuff
 * Oppretter Scaffold og laster inn øvrig innhold i form av composables
 * Inspirert av kodemønster. Kilde: https://johncodeos.com/how-to-create-a-navigation-drawer-with-jetpack-compose/
 */
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun MainScreenView(){
    val prisjegerViewModel: PrisjegerViewModel = viewModel()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(scaffoldState = scaffoldState, scope = scope, prisjegerViewModel = prisjegerViewModel) },
        drawerBackgroundColor = Color.Transparent,
        drawerContent = {
            DrawerContent(
                scaffoldState = scaffoldState,
                scope = scope,
                navController = navController,
            )
        },
        drawerGesturesEnabled = shouldDrawerSwipe(prisjegerViewModel = prisjegerViewModel)
    ) {
        // Screen content
        NavigationGraph(navController = navController, prisjegerViewModel = prisjegerViewModel)
    }
}

/**
 * Metode som kontrollerer hvilke visninger som skal skru av gestikulasjoner for skuff.
 * Husk at tilhørende streng i prisjegerViewModel.activeNavItem må eksistere og bli satt
 */
fun shouldDrawerSwipe(prisjegerViewModel: PrisjegerViewModel): Boolean {
    return prisjegerViewModel.activeNavItem.value != "Kart"
}

/**
 * Funksjonen bygger og viser TopBar. Innhold i TopBar avhenger av skjerm som vises
 */
@Composable
fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState,
           prisjegerViewModel: PrisjegerViewModel) {
    val activeNavItem by prisjegerViewModel.activeNavItem // Nåværende visning fra ViewModel
    TopAppBar(
        title = { Text(text = prisjegerViewModel.activeNavItem.value, fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(Icons.Filled.Menu, "")
            }
        },
        actions = {
            if (activeNavItem == stringResource(id = R.string.shoppingList)) {
                IconButton(onClick = {
                    // Setter alertDialog i ViewModel til True slik at innstillinger vises
                    prisjegerViewModel.handleModus.value = !prisjegerViewModel.handleModus.value
                    prisjegerViewModel.filtrerEtterAntall.value =
                        !prisjegerViewModel.filtrerEtterAntall.value
                }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Handlemodus",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                IconButton(onClick = {
                    // Setter alertDialog i ViewModel til True slik at innstillinger vises
                    prisjegerViewModel.valgDialog.value = !prisjegerViewModel.valgDialog.value
                }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "HandlelisteInnstillinger",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    )

}

/**
 * Funksjonen bygger og viser skuffmeny med navigasjonsvalg til hovedskjermer
 * Items av class NavItem
 */
@Composable
fun DrawerContent(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController
    ) {
    val items = listOf(
        NavItem.Prissammenligning,
        NavItem.Handleliste,
        NavItem.OmOss,
        NavItem.Login,
        NavItem.Kart
    )

    Column(
        modifier = Modifier
            .padding(end = 20.dp)
            .background(MaterialTheme.colors.secondary)
            .fillMaxSize()
            .verticalScroll(state = ScrollState(2000)),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        Column(verticalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = R.drawable.prisjegerlogo),
                contentDescription = "Bilde av Prisjeger",
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxSize()
                    .padding(1.dp)
                    .align(Alignment.CenterHorizontally),
            )
            Text(
                text = "Prisjeger",
                color = MaterialTheme.colors.onPrimary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                style = TextStyle(
                    fontSize = 24.sp,
                    shadow = Shadow(
                        color = Color.Black,
                        blurRadius = 5f
                    )
                ),
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            )
            items.forEach { item ->
                DrawerItem(
                    item = item,
                    selected = currentRoute == item.screen_route,
                    onItemClick = {
                    navController.navigate(item.screen_route) {
                        // For å forhindre bygging av stakk med items, returner til start
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Forhindre nye kopier
                        launchSingleTop = true
                        // Resett state
                        restoreState = true
                    }
                    // Lukk meny
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                })
            }
        }
    }
}

/**
 * Funksjonen er en hjelpemetode for DrawerContent og bygger opp og viser items
 */
@Composable
fun DrawerItem(item: NavItem, selected: Boolean, onItemClick: (NavItem) -> Unit) {
    Card(modifier = Modifier
        .padding(horizontal = 30.dp, vertical = 10.dp)
        .fillMaxWidth()
        .background(Color.Black.copy(alpha = 0f)),
        shape = RoundedCornerShape(size = 15.dp)
    ) {
        Column(modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .clickable(onClick = { onItemClick(item) })
            .padding(top = 5.dp, bottom = 0.dp, start = 0.dp, end = 0.dp),
            horizontalAlignment = Alignment.Start)
        {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = item.icon),
                    contentDescription = stringResource(id = item.title),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                        .padding(start = 20.dp)
                )
                Spacer(modifier = Modifier.width(30.dp))
                Text(
                    text = stringResource(id = item.title),
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.onPrimary,
                    textAlign = TextAlign.Left
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Divider(color = MaterialTheme.colors.onPrimary, thickness = 3.dp)
        }
    }
}

/**
 * Funksjonen bygger navigsjonsgraf med NavHostController. Ruter til navigerbare hovedskjermer
 * ved å kalle composables.
 */
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    prisjegerViewModel: PrisjegerViewModel,
) {
    NavHost(navController, startDestination = NavItem.Prissammenligning.screen_route) {
        // SammenligningsScreen
        composable(NavItem.Prissammenligning.screen_route) {
            // Valgt skjerm settes som aktiv i ViewModel mtp. TopAppBar
            prisjegerViewModel.setAktiv(stringResource(id = R.string.priceComparison))
            SammenligningScreen(prisjegerViewModel)
        }
        // HandlelisteScreen - krever innlogging
        composable(NavItem.Handleliste.screen_route) {
            if (prisjegerViewModel.isLoggedIn.value) {
                prisjegerViewModel.loginDialog.value = false
                prisjegerViewModel.registrerDialog.value = false
                prisjegerViewModel.oppdaterAlleDataFraApi()
                prisjegerViewModel.setAktiv(stringResource(id = R.string.shoppingList))
                prisjegerViewModel.seEtterOppdateringer()
                HandlelisteScreen(prisjegerViewModel)
            } else {
                prisjegerViewModel.setAktiv("Innlogging")
                LoginScreen(prisjegerViewModel)
            }
        }
        // OmOssScreen
        composable(NavItem.OmOss.screen_route) {
            prisjegerViewModel.setAktiv(stringResource(id = R.string.aboutUs))
            OmOss()
        }
        // LoginScreen
        composable(NavItem.Login.screen_route) {
            prisjegerViewModel.setAktiv(stringResource(id = R.string.login))
            LoginScreen(prisjegerViewModel)
        }
        // KartScreen
        composable(NavItem.Kart.screen_route) {
                prisjegerViewModel.setAktiv(stringResource(id = R.string.map))
                KartScreen(prisjegerViewModel)
        }
    }
}


/**
 * Preview
 */
@RequiresApi(Build.VERSION_CODES.N)
@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {
    MainScreenView()
}
