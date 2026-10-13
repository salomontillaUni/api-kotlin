package com.example.mealsearchapp

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MealModelTest {

    @Test
    fun testIngredientesListadosConMedidasYVacios() {
        val meal = Meal(
            id = "52771",
            nombre = "Spicy Arrabiata Penne",
            categoria = "Vegetarian",
            area = "Italian",
            instrucciones = "Cook pasta...",
            imagenUrl = "https://example.com/pasta.jpg",
            youtube = "https://youtube.com/watch?v=123",
            tags = "Pasta,Curry",
            ing1 = "penne rigate",
            mea1 = "1 pound",
            ing2 = "olive oil",
            mea2 = "1/4 cup",
            ing3 = "garlic",
            mea3 = "3 cloves",
            ing4 = "chopped tomatoes",
            mea4 = "1 tin",
            ing5 = "red chile flakes",
            mea5 = "1/2 tsp",
            ing6 = "italian seasoning",
            mea6 = "1/2 tsp",
            ing7 = "basil",
            mea7 = "6 leaves",
            ing8 = "",
            mea8 = "",
            ing9 = null,
            mea9 = null,
            ing10 = "parmigiano-reggiano",
            mea10 = "spinkling"
        )

        val resultado = meal.ingredientesListados()

        assertTrue(resultado.contains("• penne rigate (1 pound)"))
        assertTrue(resultado.contains("• olive oil (1/4 cup)"))
        assertTrue(resultado.contains("• parmigiano-reggiano (spinkling)"))
        // Verificamos que no incluya el ingrediente 8 (vacío) ni el 9 (nulo)
        assertEquals(8, resultado.lines().size)
    }

    @Test
    fun testIngredienteSinMedida() {
        val meal = Meal(
            id = "1",
            nombre = "Receta Simple",
            ing1 = "sal",
            mea1 = null,
            ing2 = "pimienta",
            mea2 = ""
        )

        val resultado = meal.ingredientesListados()
        assertEquals("• sal\n• pimienta", resultado)
    }

    @Test
    fun testSinIngredientes() {
        val meal = Meal(
            id = "2",
            nombre = "Receta Vacia"
        )

        val resultado = meal.ingredientesListados()
        assertEquals("Sin ingredientes registrados.", resultado)
    }
}
