/*define cómo la app se comunica con la PokeAPI, no hay logica
solo se declaran urls, parametros, tipo de datos que devuelve cada endpoint..
*/
package com.example.balanceapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Interfaz que define cómo nos comunicamos con la PokeAPI usando Retrofit
interface PokeApiService {
    // Endpoint Ejemplo: GET /pokemon?limit=100&offset=0
    @GET("pokemon") // obtener la lista del Pokemon
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 100, // Cantidad a pedir
        @Query("offset") offset: Int = 0 // Desde qué índice empezar
    ): PokemonListResponse

    // Endpoint: GET /pokemon/{id}
    @GET("pokemon/{id}") // obtener el detalle de un Pokemon
    suspend fun getPokemonDetail(
        @Path("id") id: Int // Inserta el ID en la URL
    ): PokemonDetailResponse

    companion object {
        // Configura Retrofit y devuelve una instancia lista para usar
        fun create(): PokeApiService = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/") // URL base de la API
            .addConverterFactory(GsonConverterFactory.create()) // JSON → objetos
            .build()
            .create(PokeApiService::class.java)
    }
}


