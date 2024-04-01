package de.christcoding.beerfellow.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.christcoding.beerfellow.model.Beer
import de.christcoding.beerfellow.model.Breed
import de.christcoding.beerfellow.network.TheDogApi
import kotlinx.coroutines.launch

class BeersViewModel : ViewModel() {

    var currentBeerState: CurrentBeerState by mutableStateOf(CurrentBeerState.Loading)
        private set

    var beersState: BreedState by mutableStateOf(BreedState.Loading)
        private set

    init {
        loadBeers()
    }

    private fun loadBeers() {
        viewModelScope.launch {
            beersState = try {
                val listResult = TheDogApi.retrofitService.getBreeds()
                loadImages(listResult)
                BreedState.Success(listResult)
            } catch (e: Exception) {
                BreedState.Error(e.message ?: "An error occurred")
            }
        }
    }

    private fun loadImages(breeds: List<Breed>) {
        for (breed in breeds) {
            viewModelScope.launch {
                try {
                    val image = TheDogApi.retrofitService.getImage(breed.reference_image_id)
                    breed.image = image
                    beersState = BreedState.Success(breeds)
                }catch (_: Exception) {}            }
        }
    }
}

sealed interface BreedState {
    data class Success(val breeds: List<Breed>) : BreedState
    object Loading : BreedState
    data class Error(val message: String) : BreedState
}

sealed interface CurrentBeerState {
    data class Success(val beer: Beer) : CurrentBeerState
    object Loading : CurrentBeerState
    data class Error(val message: String) : CurrentBeerState
}
