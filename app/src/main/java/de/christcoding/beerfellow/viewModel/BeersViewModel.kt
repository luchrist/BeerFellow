package de.christcoding.beerfellow.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.christcoding.beerfellow.model.Breed
import de.christcoding.beerfellow.model.BreedSize
import de.christcoding.beerfellow.network.TheDogApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BeersViewModel : ViewModel() {

    var title by mutableStateOf("BREEDS")

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _sizeFilter = MutableStateFlow(BreedSize.NONE)
    val sizeFilter = _sizeFilter.asStateFlow()

    private var allBreeds = MutableStateFlow(listOf<Breed>())

    var currentBeerState: CurrentBeerState by mutableStateOf(CurrentBeerState.Loading)
        private set

    var beersState: BreedState by mutableStateOf(BreedState.Loading)
        private set

    val shownBreeds = sizeFilter
        .combine(allBreeds) { size, breeds ->
            if (size == BreedSize.NONE) {
                breeds
            } else
                breeds.filter { it.hasSize(size) }
        }
        .combine(_searchText) { breeds, query ->
            if (query.isBlank()) {
                breeds
            } else
                breeds.filter { it.doesMatchSearch(query) }
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onSizeFilterChanged(size: BreedSize) {
        _sizeFilter.value = size
    }

    init {
        loadBeers()
    }

    fun onSearchTextChanged(text: String) {
        _searchText.value = text
    }

    fun clearSearch() {
        _searchText.value = ""
    }

    private var firstLoading = mutableStateOf(true)

    fun loadBreed(breedId: String) {
        if (!firstLoading.value) {
            return
        }
        firstLoading.value = false
        viewModelScope.launch {
            currentBeerState = try {
                val beer = TheDogApi.retrofitService.getBreed(breedId)
                loadImage(beer)
                title = beer.name
                CurrentBeerState.Success(beer)
            } catch (e: Exception) {
                CurrentBeerState.Error(e.message ?: "An error occurred")
            }
        }
    }

    private fun loadImage(breed: Breed) {
        viewModelScope.launch {
            try {
                val image = TheDogApi.retrofitService.getImage(breed.reference_image_id)
                breed.image = image
                currentBeerState = CurrentBeerState.Success(breed)
            } catch (_: Exception) {
            }
        }
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
    data class Success(val breed: Breed) : CurrentBeerState
    object Loading : CurrentBeerState
    data class Error(val message: String) : CurrentBeerState
}
