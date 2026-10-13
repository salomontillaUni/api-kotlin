package com.example.mealsearchapp

import com.google.gson.annotations.SerializedName

/**
 * Respuesta raíz devuelta por la API REST de TheMealDB.
 * Contiene la lista de recetas encontradas o null si la búsqueda no arrojó resultados.
 */
data class MealResponse(
    @SerializedName("meals")
    val meals: List<Meal>?
)

/**
 * Modelo de datos que representa una receta completa de TheMealDB.
 * Mapea las propiedades JSON a nombres descriptivos en español mediante @SerializedName.
 * Incluye el reto opcional con los 20 ingredientes y medidas individuales de la API.
 */
data class Meal(
    @SerializedName("idMeal")
    val id: String,

    @SerializedName("strMeal")
    val nombre: String,

    @SerializedName("strCategory")
    val categoria: String? = null,

    @SerializedName("strArea")
    val area: String? = null,

    @SerializedName("strInstructions")
    val instrucciones: String? = null,

    @SerializedName("strMealThumb")
    val imagenUrl: String? = null,

    @SerializedName("strYoutube")
    val youtube: String? = null,

    @SerializedName("strTags")
    val tags: String? = null,

    // Ingredientes (TheMealDB los almacena en campos individuales del 1 al 20)
    @SerializedName("strIngredient1") val ing1: String? = null,
    @SerializedName("strIngredient2") val ing2: String? = null,
    @SerializedName("strIngredient3") val ing3: String? = null,
    @SerializedName("strIngredient4") val ing4: String? = null,
    @SerializedName("strIngredient5") val ing5: String? = null,
    @SerializedName("strIngredient6") val ing6: String? = null,
    @SerializedName("strIngredient7") val ing7: String? = null,
    @SerializedName("strIngredient8") val ing8: String? = null,
    @SerializedName("strIngredient9") val ing9: String? = null,
    @SerializedName("strIngredient10") val ing10: String? = null,
    @SerializedName("strIngredient11") val ing11: String? = null,
    @SerializedName("strIngredient12") val ing12: String? = null,
    @SerializedName("strIngredient13") val ing13: String? = null,
    @SerializedName("strIngredient14") val ing14: String? = null,
    @SerializedName("strIngredient15") val ing15: String? = null,
    @SerializedName("strIngredient16") val ing16: String? = null,
    @SerializedName("strIngredient17") val ing17: String? = null,
    @SerializedName("strIngredient18") val ing18: String? = null,
    @SerializedName("strIngredient19") val ing19: String? = null,
    @SerializedName("strIngredient20") val ing20: String? = null,

    // Medidas correspondientes a cada ingrediente (del 1 al 20)
    @SerializedName("strMeasure1") val mea1: String? = null,
    @SerializedName("strMeasure2") val mea2: String? = null,
    @SerializedName("strMeasure3") val mea3: String? = null,
    @SerializedName("strMeasure4") val mea4: String? = null,
    @SerializedName("strMeasure5") val mea5: String? = null,
    @SerializedName("strMeasure6") val mea6: String? = null,
    @SerializedName("strMeasure7") val mea7: String? = null,
    @SerializedName("strMeasure8") val mea8: String? = null,
    @SerializedName("strMeasure9") val mea9: String? = null,
    @SerializedName("strMeasure10") val mea10: String? = null,
    @SerializedName("strMeasure11") val mea11: String? = null,
    @SerializedName("strMeasure12") val mea12: String? = null,
    @SerializedName("strMeasure13") val mea13: String? = null,
    @SerializedName("strMeasure14") val mea14: String? = null,
    @SerializedName("strMeasure15") val mea15: String? = null,
    @SerializedName("strMeasure16") val mea16: String? = null,
    @SerializedName("strMeasure17") val mea17: String? = null,
    @SerializedName("strMeasure18") val mea18: String? = null,
    @SerializedName("strMeasure19") val mea19: String? = null,
    @SerializedName("strMeasure20") val mea20: String? = null
) {
    /**
     * Junta y formatea los ingredientes no vacíos con viñetas.
     * Si la medida está presente y no está en blanco, se adjunta entre paréntesis:
     * Ejemplo: "• penne rigate (1 pound)"
     * Si no hay medida, se muestra únicamente el ingrediente:
     * Ejemplo: "• penne rigate"
     * Filtra los elementos nulos o en blanco mediante !it.isNullOrBlank().
     */
    fun ingredientesListados(): String {
        val ingredientes = listOf(
            ing1, ing2, ing3, ing4, ing5, ing6, ing7, ing8, ing9, ing10,
            ing11, ing12, ing13, ing14, ing15, ing16, ing17, ing18, ing19, ing20
        )
        val medidas = listOf(
            mea1, mea2, mea3, mea4, mea5, mea6, mea7, mea8, mea9, mea10,
            mea11, mea12, mea13, mea14, mea15, mea16, mea17, mea18, mea19, mea20
        )

        val lista = mutableListOf<String>()
        for (i in ingredientes.indices) {
            val ing = ingredientes[i]
            if (!ing.isNullOrBlank()) {
                val med = medidas.getOrNull(i)?.trim()
                if (!med.isNullOrBlank()) {
                    lista.add("• ${ing.trim()} ($med)")
                } else {
                    lista.add("• ${ing.trim()}")
                }
            }
        }

        return if (lista.isEmpty()) "Sin ingredientes registrados." else lista.joinToString("\n")
    }
}
