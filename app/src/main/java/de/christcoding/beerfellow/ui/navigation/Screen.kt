package de.christcoding.beerfellow.ui.navigation

sealed class Screen(val route: String) {
    object BeersListScreen : Screen("beers_list_screen")
    object BeerDetailScreen : Screen("beer_detail_screen")
}