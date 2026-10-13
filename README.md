# MealSearchApp 🍽️

Aplicación móvil desarrollada en **Android Nativo con Kotlin** para la búsqueda, exploración y visualización de recetas gastronómicas internacionales, consumiendo la API REST pública de [TheMealDB](https://www.themealdb.com/).

---

## 📱 Características Principales

- **Búsqueda por Nombre:** Consulta instantánea de recetas por su denominación gastronómica en inglés (ej: *Arrabiata*, *Carbonara*, *Lasagne*).
- **Filtro por Ingrediente:** Búsqueda y exploración de platillos basados en su ingrediente principal (ej: *Chicken*, *Beef*).
- **Receta Sorpresa (Aleatoria):** Descubrimiento de platillos al azar mediante el endpoint `random.php`.
- **Mapeo Completo de Ingredientes:** Desglose detallado de los hasta 20 ingredientes con sus correspondientes medidas y proporciones.
- **Instrucciones de Preparación:** Modo de preparación detallado con panel desplegable interactivo.
- **Integración Multimedia:**
  - Carga y caché asíncrono de imágenes en alta resolución mediante **Coil**.
  - Acceso directo al video tutorial de la receta mediante **Intents Implícitos** de Android hacia YouTube o navegador web.
- **Manejo Robusto de Red:** Validación de estados de carga, campos vacíos y captura de errores de conexión o recetas no encontradas.

---

## 🏗️ Arquitectura y Tecnologías

El proyecto sigue una arquitectura limpia orientada a componentes, priorizando la separación de responsabilidades y la inmutabilidad:

| Componente | Tecnología | Propósito |
| :--- | :--- | :--- |
| **Lenguaje** | Kotlin 1.9+ | Tipado estricto, inmutabilidad y funciones de extensión. |
| **Cliente HTTP** | Retrofit 2.9.0 | Definición declarativa de endpoints con proxies dinámicos. |
| **Serialización** | Gson + `@SerializedName` | Deserialización segura de JSON a objetos del dominio. |
| **Concurrencia** | Kotlin Coroutines | Ejecución asíncrona no bloqueante vinculada al ciclo de vida (`lifecycleScope`). |
| **Carga de Imágenes**| Coil 2.5.0 | Carga reactiva con soporte de memoria LruCache y downsampling. |
| **Interfaz de Usuario** | View Binding + Material Design | Enlace de vistas seguro en tiempo de compilación (Null-safe y Type-safe). |

---

## 🔌 Endpoints Consumidos (TheMealDB)

- `GET /search.php?s={nombre}` — Búsqueda por término.
- `GET /lookup.php?i={id}` — Detalle completo por ID único.
- `GET /random.php` — Obtención de receta aleatoria.
- `GET /filter.php?i={ingrediente}` — Filtro por ingrediente.

---

## ⚙️ Requisitos de Ejecución

- **Android Studio:** Ladybug / Iguana o superior.
- **JDK:** Java 17 o 21+.
- **SDK Mínimo:** API 24 (Android 7.0 Nougat).
- **SDK Objetivo:** API 35 (Android 15).
- **Conexión a Internet:** Requerida para consultar la API en vivo.

### Instrucciones de Instalación:
1. Clonar este repositorio:
   ```bash
   git clone https://github.com/tu-usuario/MealSearchApp.git
   ```
2. Abrir la carpeta del proyecto en **Android Studio**.
3. Esperar la sincronización de dependencias de Gradle.
4. Conectar un dispositivo físico con depuración USB habilitada o iniciar un emulador (AVD).
5. Presionar **Run ▶ (Shift + F10)**.

---

## 🧪 Pruebas Unitarias

El proyecto incluye pruebas unitarias para validar la lógica del formateo y filtrado de ingredientes:
```bash
./gradlew testDebugUnitTest
```
