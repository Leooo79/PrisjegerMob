package no.usn.rygleo.prisjegermobv1.navigasjon

import android.graphics.drawable.Icon
import androidx.annotation.StringRes
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
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
import no.usn.rygleo.prisjegermobv1.ui.*
// Klasser/ Visninger/ Funksjoner:


// SKAL VI BENYTTE KLASSE HER? KUN FUNKSJON?
/*
class BottomNavActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreenView()
        }
    }
}


 */

/*
@Composable
fun MainScreenView(){
    val navController = rememberNavController()
    Scaffold(
        drawerContent = {
            Text("Skufftittel")
            Divider()
        },
        topBar = { TopAppBar(navController = navController) }
    ) {

    }
}*/

// Ny MainScreenView med navigasjonsskuff
// Kilde:
// https://johncodeos.com/how-to-create-a-navigation-drawer-with-jetpack-compose/
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
        //    drawerContainerColor = Color.Transparent,

        drawerContent = {
            DrawerContent(
                scaffoldState = scaffoldState,
                scope = scope,
                navController = navController)
        },
        drawerGesturesEnabled = shouldDrawerSwipe(prisjegerViewModel = prisjegerViewModel)
    ) {
        // Screen content
        NavigationGraph(navController = navController, prisjegerViewModel = prisjegerViewModel)
    }
}

// Metode som kontrollerer hvilke visninger som skal skru av gestikulasjoner for skuff.
// Husk at tilhørende streng i prisjegerViewModel.activeNavItem må eksistere og bli satt
// fun NavigationGraph()
fun shouldDrawerSwipe(prisjegerViewModel: PrisjegerViewModel): Boolean {
    return prisjegerViewModel.activeNavItem.value != "Kart"
}

@Composable
fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState, prisjegerViewModel: PrisjegerViewModel) {
    var activeNavItem by prisjegerViewModel.activeNavItem // Nåværende visning fra ViewModel
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
                IconButton(onClick = { // Setter alertDialog i ViewModel til True slik at innstillinger vises
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
                IconButton(onClick = { // Setter alertDialog i ViewModel til True slik at innstillinger vises
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
@Composable
fun DrawerContent(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController) {
    val items = listOf(
        BottomNavItem.Prissammenligning,
        //BottomNavItem.Hjem,
        BottomNavItem.Handleliste,
        BottomNavItem.OmOss,
        BottomNavItem.Login,
        BottomNavItem.Kart

    )

    Column(
        modifier = Modifier
            .padding(end = 20.dp)
            .background(MaterialTheme.colors.secondary)
            .fillMaxSize()
            .verticalScroll(state = ScrollState(2000)),
        horizontalAlignment = Alignment.Start
    ) {
        // Header

        // Spacing
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )
        // List of navigation items
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
                DrawerItem(item = item, selected = currentRoute == item.screen_route, onItemClick = {
                    navController.navigate(item.screen_route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                    // Close drawer
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                })
            }
        } //End of column
    }
}
@Composable
fun DrawerItem(item: BottomNavItem, selected: Boolean, onItemClick: (BottomNavItem) -> Unit) {
    /*
    Spacer(modifier = Modifier.padding(10.dp) .width(20.dp))
  // TODO: Kjærsjer:  val background = if (selected) MaterialTheme.colors.onPrimary else Color.Transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(200.dp)
            .clickable(onClick = { onItemClick(item) })
            .height(45.dp)
            .background(MaterialTheme.colors.primary, shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
            .padding(start = 10.dp)
    ) {
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = stringResource(id = item.title),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(35.dp)
                .width(35.dp)
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = stringResource(id = item.title),
            fontSize = 18.sp,
            color = MaterialTheme.colors.onPrimary
        )
    }
     */
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
            }//End of row
            Spacer(modifier = Modifier.height(5.dp))
            Divider(color = MaterialTheme.colors.onPrimary, thickness = 3.dp)
        }//End of column
    } //End of card
}

/*@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Hjem,
        BottomNavItem.Handleliste,
        BottomNavItem.Prissammenligning,
        BottomNavItem.OmOss,
        BottomNavItem.Login
    )
    androidx.compose.material.BottomNavigation(
        backgroundColor = MaterialTheme.colors.secondaryVariant,
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 9.sp
                    )
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.White,
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}*/

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    prisjegerViewModel: PrisjegerViewModel,
) {

   // val openDialog = remember { mutableStateOf(true) }
  //  val uiState2 by viewModel.uiState.collectAsState()

    NavHost(navController, startDestination = BottomNavItem.Prissammenligning.screen_route) {
        composable(BottomNavItem.Prissammenligning.screen_route) {
            // Setter som aktiv i ViewModel mtp. TopAppBar
            prisjegerViewModel.setAktiv("Prissammenligning")
         //   HomeScreen()
          //  if (prisjegerViewModel.equals("Vellykket, data hentet"))

 visAPI(prisjegerViewModel)
        }

        // HANDLELISTE
        composable(BottomNavItem.Handleliste.screen_route) {
            if (prisjegerViewModel.isLoggedIn.value) {
                prisjegerViewModel.loginDialog.value = false
                prisjegerViewModel.registrerDialog.value = false
         //       openDialog.value = false
                // Setter som aktiv i ViewModel mtp. TopAppBar
                prisjegerViewModel.oppdaterAlleDataFraApi() // TODO: kjører flere ganger?
          //      prisjegerViewModel.setAktiv(stringResource(id = R.string.shoppingList))
                prisjegerViewModel.setAktiv(stringResource(id = R.string.shoppingList))
                prisjegerViewModel.seEtterOppdateringer()
                HandlelisteScreen(prisjegerViewModel)
            } else {
                prisjegerViewModel.setAktiv("Innlogging")
            //    prisjegerViewModel.openDialog.value = false
                LoginScreen(prisjegerViewModel)
            }
        }

            /*
            else if (openDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    title = {
                        Text(text = "Logg inn for å opprette handleliste")
                    },
                    buttons = {
                        Row(
                            modifier = Modifier.padding(all = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    openDialog.value = false
                                }
                            ) {
                                Text("ok")
                            }
                        }
                    }
                )
            }
            if (!openDialog.value) {
                // Setter som aktiv i ViewModel mtp. TopAppBar
                prisjegerViewModel.setAktiv("Login")
                LoginScreen(prisjegerViewModel)
            }
        }

             */

        // OM OSS
        composable(BottomNavItem.Prissammenligning.screen_route) {
            // Setter som aktiv i ViewModel mtp. TopAppBar
            prisjegerViewModel.setAktiv(stringResource(id = R.string.priceComparison))
            SammenligningScreen(prisjegerViewModel)
        }

        // PRISSAMMENLINGNING
        composable(BottomNavItem.OmOss.screen_route) {
            // Setter som aktiv i ViewModel mtp. TopAppBar
            prisjegerViewModel.setAktiv(stringResource(id = R.string.aboutUs))
            OmOss()
        }

        // LOGIN
        composable(BottomNavItem.Login.screen_route) {
            // Setter som aktiv i ViewModel mtp. TopAppBar
            prisjegerViewModel.setAktiv(stringResource(id = R.string.login))
            LoginScreen(prisjegerViewModel)
        }
        // KART
        composable(BottomNavItem.Kart.screen_route) {
                prisjegerViewModel.setAktiv(stringResource(id = R.string.map))
                KartScreen(prisjegerViewModel)

        }

    }

}



@RequiresApi(Build.VERSION_CODES.N)
@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {
    MainScreenView()
}
