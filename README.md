# RandomUser

## Descripción General
RandomUser es una aplicación Android desarrollada en Kotlin, diseñada para mostrar perfiles de usuarios aleatorios. Utiliza una arquitectura modular para una mejor escalabilidad y mantenimiento.

## Módulos
- **App:** Código principal de la aplicación.
- **Data:** Manejo y almacenamiento de datos.
- **Domain:** Lógica de negocio.
- **Services:** Gestión de servicios externos y APIs.

## Arquitectura
Este proyecto emplea un enfoque de arquitectura limpia, separando preocupaciones para una mejor capacidad de prueba y fácil comprensión. Implementa patrones de diseño como MVVM y MVI. Se comunica entre capas por medio de Interfaces.

## Composición
- **Arquitectura:** Arquitectura limpia modular.
- **Patrones:** Singleton, MVVM y MVI.
- **Componentes:**
  - DiffUtil para la gestión de cambios en el RecyclerView.
  - Inyección de dependencias con HILT
  - Control de paginación con DataStore
  - Control de vistas con Navigation
  - Llamadas REST con Retrofit, OkHttp, y Moshi
  - Diseño con Material
  - Carga de imágenes con Glide
  - Test Unitarios con JUnit4 y MockK
  - Shimmer para la carga de datos

- **Librerías**
  - Dagger HILT
  - Retrofit
  - OkHttp
  - Moshi
  - JetPack DataStore
  - Timber
  - Facebook Shimmer
  - Glide
  - JUnit4
  - MockK

## Comenzando
1. Descargue el proyecto
2. Ábralo con Android Studio
3. Acceda a su fichero "local.properties" (si no le aparece, créelo en la ruta principal del proyecto) y agrege su API KEY de Google Maps con este formato:
   **GOOGLE_MAPS_API_KEY=XXXXXXXXXXXXXXXXXXX**
4.  Ejecute la aplicación en un emulador o dispositivo físico.

## Licencia
Este proyecto tiene licencia Apache 2.0.

## Contacto
Este proyecto ha sido desarrollado por [Álvaro Estrada](https://www.linkedin.com/in/aest85/).

## Agradecimientos
Los datos han sido extraidos de la API [Random User API](https://randomuser.me/)


Para más detalles, visita el [repositorio de GitHub](https://github.com/AlvaroEstrada/RandomUser/tree/master).
