package de.christcoding.beerfellow.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.christcoding.beerfellow.R
import de.christcoding.beerfellow.model.BreedSize
import de.christcoding.beerfellow.ui.components.BeerListItem
import de.christcoding.beerfellow.ui.components.LoadingScreen
import de.christcoding.beerfellow.ui.theme.Background
import de.christcoding.beerfellow.ui.theme.Secondary
import de.christcoding.beerfellow.viewModel.BeersViewModel
import de.christcoding.beerfellow.viewModel.BreedState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeerListView(navigateToDetailsView: (String) -> Unit) {
    val vm: BeersViewModel = viewModel()
    val beersState = vm.beersState
    val searchText by vm.searchText.collectAsState()
    val shownBreeds by vm.shownBreeds.collectAsState()
    var filtersShown by remember { mutableStateOf(false) }
    val sizeFilter by vm.sizeFilter.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "BREEDS") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Secondary,
                    titleContentColor = Background
                )
            )
        }
    ) {
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
                        .background(color = Background)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp)
                    ) {
                        TextField(
                            value = searchText,
                            onValueChange = vm::onSearchTextChanged,
                            placeholder = { Text("Search") },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Secondary,
                                unfocusedTextColor = Secondary,
                                focusedContainerColor = Background,
                                unfocusedContainerColor = Background,
                                cursorColor = Secondary,
                                focusedIndicatorColor = Secondary,
                                unfocusedIndicatorColor = Secondary,
                            ),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_search_24),
                                    contentDescription = null,
                                    tint = Secondary
                                )
                            },
                            trailingIcon = {
                                if (searchText.isNotBlank()) {
                                    IconButton(onClick = { vm.clearSearch() }) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = null,
                                            tint = Secondary
                                        )
                                    }
                                }
                            },
                        )
                        IconButton(onClick = { filtersShown = !filtersShown }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_filter_list_24),
                                modifier = Modifier.size(24.dp),
                                contentDescription = null,
                                tint = Secondary
                            )
                        }
                    }
                    if (filtersShown) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { vm.onSizeFilterChanged(BreedSize.SMALL) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (sizeFilter == BreedSize.SMALL) Secondary else Background,
                                    contentColor = if (sizeFilter == BreedSize.SMALL) Background else Secondary
                                )
                            ) {
                                Text(text = "Small")
                                if (sizeFilter == BreedSize.SMALL) {
                                    IconButton(
                                        onClick = { vm.onSizeFilterChanged(BreedSize.NONE) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = null,
                                            tint = Background
                                        )
                                    }
                                }
                            }
                            Button(
                                onClick = { vm.onSizeFilterChanged(BreedSize.MEDIUM) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (sizeFilter == BreedSize.MEDIUM) Secondary else Background,
                                    contentColor = if (sizeFilter == BreedSize.MEDIUM) Background else Secondary
                                )
                            ) {
                                Text(text = "Medium")
                                if (sizeFilter == BreedSize.MEDIUM) {
                                    IconButton(
                                        onClick = { vm.onSizeFilterChanged(BreedSize.NONE) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = null,
                                            tint = Background
                                        )
                                    }
                                }
                            }
                            Button(
                                onClick = { vm.onSizeFilterChanged(BreedSize.LARGE) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (sizeFilter == BreedSize.LARGE) Secondary else Background,
                                    contentColor = if (sizeFilter == BreedSize.LARGE) Background else Secondary
                                )
                            ) {
                                Text(text = "Large")
                                if (sizeFilter == BreedSize.LARGE) {
                                    IconButton(
                                        onClick = { vm.onSizeFilterChanged(BreedSize.NONE) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = null,
                                            tint = Background
                                        )
                                    }
                                }
                            }
                        }
                    }
                    LazyColumn() {
                        items(shownBreeds) { beer ->
                            BeerListItem(
                                beer = beer,
                                showDetails = { navigateToDetailsView(beer.id.toString()) })
                        }
                    }
                }
            }
        }
    }
}