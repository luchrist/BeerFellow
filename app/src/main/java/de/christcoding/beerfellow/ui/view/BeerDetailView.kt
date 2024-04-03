package de.christcoding.beerfellow.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import de.christcoding.beerfellow.R
import de.christcoding.beerfellow.ui.components.LoadingScreen
import de.christcoding.beerfellow.ui.theme.Background
import de.christcoding.beerfellow.ui.theme.Primary
import de.christcoding.beerfellow.ui.theme.Secondary
import de.christcoding.beerfellow.viewModel.BeersViewModel
import de.christcoding.beerfellow.viewModel.CurrentBeerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeerDetailView(beerId: String, popBackStack: () -> Boolean) {
    val vm: BeersViewModel = viewModel()
    val breedState = vm.currentBeerState
    vm.loadBreed(beerId)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = vm.title) },
                navigationIcon = {
                    IconButton(onClick = { popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Secondary,
                    titleContentColor = Background,
                    navigationIconContentColor = Background
                )
            )
        }
    ) {
        when (breedState) {
            is CurrentBeerState.Loading -> {
                LoadingScreen(modifier = Modifier.fillMaxSize().background(color = Background))
            }

            is CurrentBeerState.Error -> {
                Text(modifier = Modifier.padding(it), text = breedState.message)
            }

            is CurrentBeerState.Success -> {
                val breed = breedState.breed
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Background)
                ) {
                    if (breed.image != null)
                        AsyncImage(
                            model = breed.image!!.url,
                            contentDescription = "Beer Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.5f),
                            placeholder = painterResource(R.drawable.baseline_cloud_off_24),
                            error = painterResource(
                                R.drawable.baseline_cloud_off_24
                            )
                        )
                    else
                        Icon(
                            painter = painterResource(R.drawable.baseline_cloud_off_24),
                            contentDescription = "Beer Image",
                            modifier = Modifier.fillMaxWidth()
                        )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = breed.name,
                            modifier = Modifier.padding(8.dp),
                            fontSize = 25.sp,
                            color = Secondary
                        )
                        if (breed.origin != null)
                            Text(
                                text = breed.origin,
                                modifier = Modifier.padding(8.dp),
                                color = Secondary
                            )
                        Text(
                            text = "Size Category: ${breed.getSize()}",
                            modifier = Modifier.padding(8.dp),
                            color = Secondary
                        )
                        Text(
                            text = "Size: ${breed.height.metric} cm",
                            modifier = Modifier.padding(8.dp),
                            color = Secondary
                        )
                        Text(
                            text = "Weight: ${breed.weight.metric} kg",
                            modifier = Modifier.padding(8.dp),
                            color = Secondary
                        )

                        Text(
                            text = "Life expectancy: ${breed.life_span}",
                            modifier = Modifier.padding(8.dp),
                            color = Secondary
                        )
                    }
                    Button(
                        onClick = { /*TODO*/ },
                        colors = ButtonDefaults.buttonColors(containerColor = Secondary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                    ) {
                        Text(
                            text = "Adopt me",
                            color = Primary,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}