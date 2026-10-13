package com.example.mealsearchapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.mealsearchapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

/**
 * Pantalla principal de búsqueda de recetas con TheMealDB.
 * Implementa View Binding, Retrofit 2 con Gson, Corrutinas en lifecycleScope,
 * carga de imágenes con Coil y los retos opcionales (YouTube Intent, instrucciones
 * desplegables y filtrado por ingredientes).
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarListeners()
    }

    private fun configurarListeners() {
        // Búsqueda por nombre de receta
        binding.btnBuscar.setOnClickListener {
            val texto = binding.etReceta.text.toString().trim()
            if (texto.isEmpty()) {
                Toast.makeText(this, getString(R.string.toast_campo_vacio), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            buscarReceta(texto)
        }

        // Obtener receta sorpresa / aleatoria
        binding.btnAleatorio.setOnClickListener {
            recetaAleatoria()
        }

        // Reto opcional: Buscar por ingrediente principal
        binding.btnPorIngrediente.setOnClickListener {
            val texto = binding.etReceta.text.toString().trim()
            if (texto.isEmpty()) {
                Toast.makeText(this, getString(R.string.toast_campo_vacio), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            buscarPorIngrediente(texto)
        }
    }

    /**
     * Busca recetas que coincidan con el nombre proporcionado.
     */
    private fun buscarReceta(nombre: String) {
        lifecycleScope.launch {
            mostrarCargando(true)
            limpiar()
            try {
                val respuesta = RetrofitInstance.api.buscarPorNombre(nombre)
                val comida = respuesta.meals?.firstOrNull()
                if (comida != null) {
                    mostrarReceta(comida)
                } else {
                    mostrarError(getString(R.string.error_no_encontrado, nombre))
                }
            } catch (e: Exception) {
                mostrarError(getString(R.string.error_red, e.message ?: "Error de red"))
            } finally {
                mostrarCargando(false)
            }
        }
    }

    /**
     * Obtiene una receta aleatoria del catálogo de TheMealDB.
     */
    private fun recetaAleatoria() {
        lifecycleScope.launch {
            mostrarCargando(true)
            limpiar()
            try {
                val respuesta = RetrofitInstance.api.recetaAleatoria()
                val comida = respuesta.meals?.firstOrNull()
                if (comida != null) {
                    mostrarReceta(comida)
                    // Actualizamos el campo de búsqueda con el nombre obtenido
                    binding.etReceta.setText(comida.nombre)
                } else {
                    mostrarError("No se pudo obtener una receta aleatoria.")
                }
            } catch (e: Exception) {
                mostrarError(getString(R.string.error_red, e.message ?: "Error de red"))
            } finally {
                mostrarCargando(false)
            }
        }
    }

    /**
     * Reto opcional: Filtra por ingrediente (filter.php) y luego consulta los detalles
     * completos de la receta resultante por su ID (lookup.php).
     */
    private fun buscarPorIngrediente(ingrediente: String) {
        lifecycleScope.launch {
            mostrarCargando(true)
            limpiar()
            try {
                val respuesta = RetrofitInstance.api.filtrarPorIngrediente(ingrediente)
                val comidaBasica = respuesta.meals?.firstOrNull()
                if (comidaBasica != null) {
                    // La llamada filter.php no incluye todos los campos (instrucciones, medidas).
                    // Obtenemos los detalles completos consultando por su idMeal.
                    val detalleRespuesta = RetrofitInstance.api.buscarPorId(comidaBasica.id)
                    val comidaCompleta = detalleRespuesta.meals?.firstOrNull() ?: comidaBasica
                    mostrarReceta(comidaCompleta)
                    binding.etReceta.setText(comidaCompleta.nombre)
                } else {
                    mostrarError(getString(R.string.error_no_encontrado, ingrediente))
                }
            } catch (e: Exception) {
                mostrarError(getString(R.string.error_red, e.message ?: "Error de red"))
            } finally {
                mostrarCargando(false)
            }
        }
    }

    /**
     * Muestra toda la información de la receta en la interfaz de usuario.
     */
    private fun mostrarReceta(meal: Meal) {
        // Carga de imagen con Coil
        meal.imagenUrl?.let { url ->
            binding.ivFoto.load(url) {
                crossfade(true)
                placeholder(android.R.drawable.ic_menu_gallery)
                error(android.R.drawable.ic_dialog_alert)
            }
        }

        binding.tvNombre.text = meal.nombre
        binding.tvId.text = "ID: ${meal.id}"
        binding.tvCategoria.text = "📂 Categoría: ${meal.categoria ?: "N/A"}"
        binding.tvArea.text = "🌍 País de origen: ${meal.area ?: "N/A"}"
        binding.tvTags.text = "🏷️ Tags: ${meal.tags ?: getString(R.string.sin_tags)}"
        binding.tvIngredientes.text = meal.ingredientesListados()

        // Reto Opcional: Botón de YouTube con Intent
        configurarBotonYoutube(meal.youtube)

        // Reto Opcional: Instrucciones con botón para alternar visibilidad
        configurarInstrucciones(meal.instrucciones)

        binding.cardResultado.visibility = View.VISIBLE
    }

    /**
     * Configura el botón para abrir el enlace de YouTube en la app nativa o navegador.
     */
    private fun configurarBotonYoutube(youtubeUrl: String?) {
        if (!youtubeUrl.isNullOrBlank()) {
            binding.btnYoutube.visibility = View.VISIBLE
            binding.btnYoutube.setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, getString(R.string.error_youtube_app), Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error al abrir enlace: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            binding.btnYoutube.visibility = View.GONE
        }
    }

    /**
     * Configura la visualización de instrucciones y el botón para expandir/colapsar.
     */
    private fun configurarInstrucciones(instrucciones: String?) {
        if (!instrucciones.isNullOrBlank()) {
            binding.btnToggleInstrucciones.visibility = View.VISIBLE
            binding.tvInstrucciones.text = instrucciones.trim()
            binding.tvInstrucciones.visibility = View.GONE
            binding.btnToggleInstrucciones.text = getString(R.string.btn_mostrar_instrucciones)

            binding.btnToggleInstrucciones.setOnClickListener {
                if (binding.tvInstrucciones.visibility == View.VISIBLE) {
                    binding.tvInstrucciones.visibility = View.GONE
                    binding.btnToggleInstrucciones.text = getString(R.string.btn_mostrar_instrucciones)
                } else {
                    binding.tvInstrucciones.visibility = View.VISIBLE
                    binding.btnToggleInstrucciones.text = getString(R.string.btn_ocultar_instrucciones)
                }
            }
        } else {
            binding.btnToggleInstrucciones.visibility = View.GONE
            binding.tvInstrucciones.visibility = View.GONE
        }
    }

    /**
     * Muestra u oculta la barra de progreso mientras se realiza la petición de red.
     */
    private fun mostrarCargando(cargando: Boolean) {
        binding.progressBar.visibility = if (cargando) View.VISIBLE else View.GONE
        binding.btnBuscar.isEnabled = !cargando
        binding.btnAleatorio.isEnabled = !cargando
        binding.btnPorIngrediente.isEnabled = !cargando
    }

    /**
     * Muestra un mensaje descriptivo de error en la interfaz.
     */
    private fun mostrarError(mensaje: String) {
        binding.tvError.text = mensaje
        binding.tvError.visibility = View.VISIBLE
    }

    /**
     * Limpia los campos visuales para una nueva búsqueda.
     */
    private fun limpiar() {
        binding.tvNombre.text = ""
        binding.tvId.text = ""
        binding.tvCategoria.text = ""
        binding.tvArea.text = ""
        binding.tvTags.text = ""
        binding.tvIngredientes.text = ""
        binding.tvInstrucciones.text = ""
        binding.tvError.text = ""
        binding.tvError.visibility = View.GONE
        binding.btnYoutube.visibility = View.GONE
        binding.btnToggleInstrucciones.visibility = View.GONE
        binding.tvInstrucciones.visibility = View.GONE
        binding.cardResultado.visibility = View.GONE
        binding.ivFoto.setImageDrawable(null)
    }
}
