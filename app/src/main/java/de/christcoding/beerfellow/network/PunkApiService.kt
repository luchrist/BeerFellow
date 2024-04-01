package de.christcoding.beerfellow.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import de.christcoding.beerfellow.model.Beer
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL = "https://api.punkapi.com/v2/"
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .build()

interface PunkApiService {
    @GET("beers")
    suspend fun getBeers(): List<Beer>
}

object PunkApi {
    val retrofitService: PunkApiService by lazy {
        retrofit.create(PunkApiService::class.java)
    }
}