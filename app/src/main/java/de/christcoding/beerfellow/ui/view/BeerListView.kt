package de.christcoding.beerfellow.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import de.christcoding.beerfellow.R
import de.christcoding.beerfellow.ui.components.BeerListItem
import de.christcoding.beerfellow.ui.components.LoadingScreen
import de.christcoding.beerfellow.viewModel.BeersViewModel
import de.christcoding.beerfellow.viewModel.BreedState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeerListView(navigateToDetailsView: (String) -> Unit) {
    val vm: BeersViewModel = viewModel()
    val beersState = vm.beersState

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text(text = "BEER") }) }) {
        when (beersState) {
            is BreedState.Loading -> {
                LoadingScreen(modifier = Modifier.fillMaxSize())
            }

            is BreedState.Error -> {
                Text(modifier = Modifier.padding(it), text = beersState.message)
            }

            is BreedState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        //SearchBar(query = , onQueryChange = , onSearch = , active = , onActiveChange = ) {

                        // }
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_filter_list_24),
                                contentDescription = null
                            )
                        }
                    }
                    LazyRow {

                    }
                    LazyColumn (){
                        items(beersState.breeds) { beer ->
                            BeerListItem(beer = beer, showDetails = { navigateToDetailsView(beer.id.toString()) })
                        }
                    }
                }
            }
        }
    }
}