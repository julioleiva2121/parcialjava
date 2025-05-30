# Mi Proyecto de Biblioteca en Java 📚

¡Hola! Este es mi programa de consola para manejar una pequeña biblioteca, hecho en Java con JDBC y una base de datos H2.

## Para Empezar:

**Necesitas:**
*   Java JDK (8 o superior)
*   Git

**Pasos para Ejecutar:**

1.  **Descarga (Clona) el Proyecto:**
    Abre tu terminal y usa:
    `git clone https://github.com/julioleiva2121/parcialjava
    (¡Reemplaza con tu URL de GitHub!)
    Luego, entra a la carpeta: `cd GestionBiblioteca`

3.  **Ejecuta (Opción 1 - IntelliJ IDEA):**
    *   Abre el proyecto en IntelliJ IDEA.
    *   Busca `src/main/java/com/proyectojava/main/MainApplication.java`.
    *   Haz clic derecho en `MainApplication.java` -> `Run 'MainApplication.main()'`.

4.  **Ejecuta (Opción 2 - Terminal con Gradle):**
    *   En la carpeta del proyecto, escribe:
        *   Windows: `.\gradlew run`
        *   macOS/Linux: `./gradlew run`

**¿Cómo se Usa?**
Al iniciar, se crea una base de datos local (`biblioteca_db.mv.db`). Verás un menú numérico. Escribe el número de la opción deseada y presiona Enter. Podrás gestionar autores (crear, listar, buscar, actualizar, eliminar) y libros (lo mismo, más listar por autor). La opción `0` es para salir.
