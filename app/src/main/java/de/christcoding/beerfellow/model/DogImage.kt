package de.christcoding.beerfellow.model

data class DogImage(
    val breeds: List<Breed>,
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)