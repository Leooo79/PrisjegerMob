package no.usn.rygleo.prisjegermobv1.navigasjon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
// Klasser/ Visninger/ Funksjoner:
import no.usn.rygleo.prisjegermobv1.ui.PrisjegerViewModel
import no.usn.rygleo.prisjegermobv1.ui.HandlelisteScreen
import no.usn.rygleo.prisjegermobv1.ui.LoginScreen
import no.usn.rygleo.prisjegermobv1.ui.OmOss


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
@Composable
fun MainScreenView(){
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(scaffoldState = scaffoldState, scope = scope) },
        drawerBackgroundColor = MaterialTheme.colors.primary,
        drawerContent = {
            DrawerContent(
                scaffoldState = scaffoldState,
                scope = scope,
                navController = navController)
        }
    ) {
        // Screen content
        NavigationGraph(navController = navController)
    }
}

@Composable
fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState) {
    TopAppBar(
        title = { Text(text = "Prisjeger", fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(Icons.Filled.Menu, "")
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.secondary
    )

}
@Composable
fun DrawerContent(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController) {
    val items = listOf(
        BottomNavItem.Hjem,
        BottomNavItem.Handleliste,
        BottomNavItem.Prissammenligning,
        BottomNavItem.OmOss,
        BottomNavItem.Login
    )
    Column(
        modifier = Modifier.background(MaterialTheme.colors.primary)
    ) {
        // Header
        Image(
            painter = painterResource(id = R.drawable.gaute),
            contentDescription = "Bilde av Gaute",
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(10.dp)
        )
        // Spacing
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )
        // List of navigation items
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
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
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Prisjeger",
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}
@Composable
fun DrawerItem(item: BottomNavItem, selected: Boolean, onItemClick: (BottomNavItem) -> Unit) {
    val background = if (selected) MaterialTheme.colors.onPrimary else Color.Transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(item) })
            .height(45.dp)
            .background(MaterialTheme.colors.primary)
            .padding(start = 10.dp)
    ) {
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.title,
            colorFilter = ColorFilter.tint(Color.White),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(35.dp)
                .width(35.dp)
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = item.title,
            fontSize = 18.sp,
            color = Color.White
        )
    }
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

@Composable
fun NavigationGraph(
    navController: NavHostController,
    prisjegerViewModel: PrisjegerViewModel = viewModel(),
) {

  //  val uiState2 by viewModel.uiState.collectAsState()

    NavHost(navController, startDestination = BottomNavItem.Hjem.screen_route) {
        composable(BottomNavItem.Hjem.screen_route) {
         //   HomeScreen()
          //  if (prisjegerViewModel.equals("Vellykket, data hentet"))

 visAPI(prisjegerViewModel)
        }

        // HANDLELISTE
        composable(BottomNavItem.Handleliste.screen_route) {
            if(prisjegerViewModel.isLoggedIn.value)
            HandlelisteScreen(prisjegerViewModel)
            else if   (!openDialog.value)

            {
                    AlertDialog(
                        onDismissRequest = {
                            openDialog.value = false
                        },
                        title = {
                            Text(text = "må logge inn først")
                        },
                        buttons = {
                            Row(
                                modifier = Modifier.padding(all = 8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        openDialog.value = true
                                    }
                                ) {
                                    Text("ok")
                                }
                            }
                        }
                    )
                }
       else     LoginScreen(prisjegerViewModel)
        }

        // OM OSS
        composable(BottomNavItem.Prissammenligning.screen_route) {
            AddPostScreen()
        }

        // PRISSAMMENLINGNING
        composable(BottomNavItem.OmOss.screen_route) {
            OmOss()
        }

        // LOGIN
        composable(BottomNavItem.Login.screen_route) {
            LoginScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {
    MainScreenView()
}
