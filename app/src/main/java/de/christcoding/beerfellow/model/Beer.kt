package de.christcoding.beerfellow.model

import kotlinx.serialization.Serializable

@Serializable
data class Beer(
    val id: Int = 0,
    val name: String = "",
    val tagline: String = "",
    val first_brewed: String = "",
    val description: String = "",
    val image_url: String = "",
    val abv: Double = 0.0,
    val ibu: Double = 0.0,
    val target_fg: Double = 0.0,
    val target_og: Double = 0.0,
    val ebc: Double = 0.0,
    val srm: Double = 0.0,
    val ph: Double = 0.0,
    val attenuation_level: Double = 0.0,
    val volume: Volume = Volume(),
    val boil_volume: Volume = Volume(),
    val method: Method = Method(),
    val ingredients: Ingredients = Ingredients(),
    val food_pairing: List<String> = listOf(),
    val brewers_tips: String = "",
)

@Serializable
data class Ingredients(
    val malt: List<Malt> = listOf(),
    val hops: List<Hop> = listOf(),
    val yeast: String = "",
)

@Serializable
data class Malt(
    val name: String = "",
    val amount: Volume = Volume(),
)

@Serializable
data class Hop(
    val name: String = "",
    val amount: Volume = Volume(),
    val add: String = "",
    val attribute: String = "",
)

@Serializable
data class Method(
    val mash_temp: List<MashTemp> = listOf(),
    val fermentation: Fermentation = Fermentation(),
    val twist: String? = null,
)

@Serializable
data class Fermentation(
    val temp: Volume = Volume(),
)

@Serializable
data class MashTemp(
    val temp: Volume = Volume(),
    val duration: Int = 0,
)

@Serializable
data class Volume(
    val value: Int = 0,
    val unit: String = "",
)
