package de.christcoding.beerfellow.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.christcoding.beerfellow.model.Beer
import de.christcoding.beerfellow.model.Breed
import de.christcoding.beerfellow.network.TheDogApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BeersViewModel : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private var allBreeds = MutableStateFlow(listOf<Breed>())

    var currentBeerState: CurrentBeerState by mutableStateOf(CurrentBeerState.Loading)
        private set

    var beersState: BreedState by mutableStateOf(BreedState.Loading)
        private set

    private val _breeds = MutableStateFlow(listOf<Breed>())
    val shownBreeds = searchText
        .combine(allBreeds) { query, breeds ->
            if (query.isBlank()) {
                breeds
            }
            breeds.filter { it.doesMatchSearch(query) }
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadBeers()
    }

    fun onSearchTextChanged(text: String) {
        _searchText.value = text
    }

    fun clearSearch() {
        _searchText.value = ""
    }

    private fun loadBeers() {
        viewModelScope.launch {
            beersState = try {
                val listResult = TheDogApi.retrofitService.getBreeds()
                allBreeds.emit(listResult)
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
                    allBreeds.emit(breeds)
                } catch (_: Exception) {
                }
            }
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
