package no.usn.rygleo.prisjegermobv1.navigasjon

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
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

@Composable
fun MainScreenView(){
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) }
    ) {

        NavigationGraph(navController = navController)
    }
}
@Composable
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
}

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
            HandlelisteScreen(prisjegerViewModel)
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