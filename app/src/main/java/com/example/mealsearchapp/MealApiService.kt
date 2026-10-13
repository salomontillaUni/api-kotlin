package com.example.mealsearchapp

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz Retrofit para interactuar con los endpoints de TheMealDB.
 * Las funciones marcadas con suspend permiten invocaciones directas desde corrutinas.
 */
interface MealApiService {

    // Buscar recetas por nombre
    // GET https://www.themealdb.com/api/json/v1/1/search.php?s=arrabiata
    @GET("search.php")
    suspend fun buscarPorNombre(
        @Query("s") nombre: String
    ): MealResponse

    // Obtener receta completa por ID
    // GET https://www.themealdb.com/api/json/v1/1/lookup.php?i=52771
    @GET("lookup.php")
    suspend fun buscarPorId(
        @Query("i") id: String
    ): MealResponse

    // Obtener una receta aleatoria
    // GET https://www.themealdb.com/api/json/v1/1/random.php
    @GET("random.php")
    suspend fun recetaAleatoria(): MealResponse

    // Reto Opcional: Filtrar recetas por ingrediente principal
    // GET https://www.themealdb.com/api/json/v1/1/filter.php?i=chicken
    @GET("filter.php")
    suspend fun filtrarPorIngrediente(
        @Query("i") ingrediente: String
    ): MealResponse
}
