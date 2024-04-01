package de.christcoding.beerfellow.ui.navigation

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.christcoding.beerfellow.ui.view.BeerDetailView
import de.christcoding.beerfellow.ui.view.BeerListView

@Composable
fun Navigation(context: Context,
               navController: NavHostController = rememberNavController(),
               padding: PaddingValues = PaddingValues(0.dp)
) {

    val startDestination = Screen.BeersListScreen.route

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.BeersListScreen.route) {
            BeerListView{id: String -> navController.navigate("${Screen.BeerDetailScreen.route}/$id")}
        }
        composable("${Screen.BeerDetailScreen.route}/{beerId}") {
            BeerDetailView(it.arguments?.getString("beerId") ?: "0") { navController.popBackStack() }
        }
    }
}