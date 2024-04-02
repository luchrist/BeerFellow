package de.christcoding.beerfellow.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.christcoding.beerfellow.model.Breed
import de.christcoding.beerfellow.model.DogImage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://api.thedogapi.com/v1/"
private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

private val client = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(client)
    .build()

interface TheDogApiService {
    @GET("breeds")
    suspend fun getBreeds(): List<Breed>

    @GET("breeds/{id}")
    suspend fun getBreed(@Path("id") id: String): Breed

    @GET("images/{id}")
    suspend fun getImage(@Path("id") id: String): DogImage
}

object TheDogApi {
    val retrofitService: TheDogApiService by lazy {
        retrofit.create(TheDogApiService::class.java)
    }
}