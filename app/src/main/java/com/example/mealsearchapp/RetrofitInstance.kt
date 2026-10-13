package com.example.mealsearchapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente singleton de Retrofit configurado para TheMealDB.
 * Utiliza inicialización perezosa (lazy) para instanciar el servicio solo cuando sea necesario.
 */
object RetrofitInstance {

    // La URL base incluye el prefijo '1' que corresponde a la clave API pública de pruebas
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    val api: MealApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealApiService::class.java)
    }
}
