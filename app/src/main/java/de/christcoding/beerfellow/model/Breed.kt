package de.christcoding.beerfellow.model

data class Breed(
    val bred_for: String?,
    val breed_group: String?,
    val height: Height,
    val id: Int,
    val life_span: String,
    val name: String,
    val origin: String?,
    val reference_image_id: String,
    val temperament: String?,
    val weight: Weight,
    var image: DogImage?
) {
    fun doesMatchSearch(query: String): Boolean {
        return name.contains(query, ignoreCase = true)
    }

    fun hasSize(size: BreedSize): Boolean {
        return when (size) {
            BreedSize.SMALL -> minHeightInCm() < 25
            BreedSize.MEDIUM -> minHeightInCm() in 25..45
            BreedSize.LARGE -> minHeightInCm() > 40
            else -> {
                true
            }
        }
    }

    private fun minHeightInCm(): Int{
        return height.metric.split(" ")[0].toInt()
    }
}

data class Height(
    val imperial: String,
    val metric: String
)

data class Weight(
    val imperial: String,
    val metric: String
)
