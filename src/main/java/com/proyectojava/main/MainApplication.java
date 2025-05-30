package com.proyectojava.main;

import com.proyectojava.dao.AutorDao;
import com.proyectojava.dao.LibroDao;
import com.proyectojava.dao.impl.AutorDaoImpl;
import com.proyectojava.dao.impl.LibroDaoImpl;
import com.proyectojava.model.Autor;
import com.proyectojava.model.Libro;
import com.proyectojava.util.DatabaseConnection;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.InputMismatchException;

public class MainApplication {

    private static final AutorDao autorDao = new AutorDaoImpl();
    private static final LibroDao libroDao = new LibroDaoImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();
        System.out.println("\n¡Bienvenido a la Gestión de Biblioteca!");
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerOpcionNumerica();
            switch (opcion) {
                case 1: gestionarAutores(); break;
                case 2: gestionarLibros(); break;
                case 0: System.out.println("Saliendo de la aplicación..."); break;
                default: System.out.println("Opción no válida. Por favor, intente de nuevo."); break;
            }
        } while (opcion != 0);
        scanner.close();
        System.out.println("Aplicación finalizada.");
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║   GESTIÓN DE BIBLIOTECA      ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║ 1. Gestionar Autores         ║");
        System.out.println("║ 2. Gestionar Libros          ║");
        System.out.println("║ 0. Salir                     ║");
        System.out.println("╚══════════════════════════════╝");
        System.out.print("Seleccione una opción: ");
    }

    private static void mostrarMenuAutores() {
        System.out.println("\n--- Menú Gestión de Autores ---");
        System.out.println("1. Crear Autor");
        System.out.println("2. Listar Todos los Autores");
        System.out.println("3. Buscar Autor por ID");
        System.out.println("4. Actualizar Autor");
        System.out.println("5. Eliminar Autor");
        System.out.println("0. Volver al Menú Principal");
        System.out.print("Seleccione una opción: ");
    }

    private static void mostrarMenuLibros() {
        System.out.println("\n--- Menú Gestión de Libros ---");
        System.out.println("1. Crear Libro");
        System.out.println("2. Listar Todos los Libros");
        System.out.println("3. Buscar Libro por ID");
        System.out.println("4. Actualizar Libro");
        System.out.println("5. Eliminar Libro");
        System.out.println("6. Listar Libros por Autor");
        System.out.println("0. Volver al Menú Principal");
        System.out.print("Seleccione una opción: ");
    }

    private static int leerOpcionNumerica() {
        try {
            int opcionLeida = scanner.nextInt();
            scanner.nextLine();
            return opcionLeida;
        } catch (InputMismatchException e) {
            System.out.println("Error: Entrada inválida. Debe ingresar un número.");
            scanner.nextLine();
            return -1;
        }
    }

    private static void gestionarAutores() {
        int opcionSubMenu;
        do {
            mostrarMenuAutores();
            opcionSubMenu = leerOpcionNumerica();
            switch (opcionSubMenu) {
                case 1: crearAutor(); break;
                case 2: listarAutores(); break;
                case 3: buscarAutorPorId(); break;
                case 4: actualizarAutor(); break;
                case 5: eliminarAutor(); break;
                case 0: System.out.println("Volviendo al menú principal..."); break;
                default: System.out.println("Opción de autor no válida."); break;
            }
        } while (opcionSubMenu != 0);
    }

    private static void crearAutor() {
        System.out.println("\n--- Crear Nuevo Autor ---");
        System.out.print("Ingrese el nombre del autor: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("El nombre del autor no puede estar vacío. Operación cancelada.");
            return;
        }
        Autor nuevoAutor = new Autor(nombre);
        autorDao.crear(nuevoAutor);
        if (nuevoAutor.getId() > 0) {
            System.out.println("Confirmación App: Autor '" + nuevoAutor.getNombre() + "' creado con ID: " + nuevoAutor.getId() + ".");
        } else {
            System.out.println("Confirmación App: No se pudo crear el autor (verifique logs del DAO).");
        }
    }

    private static void listarAutores() {
        System.out.println("\n--- Lista de Autores ---");
        List<Autor> autores = autorDao.obtenerTodos();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            for (Autor autor : autores) {
                System.out.println(autor);
            }
        }
    }

    private static void buscarAutorPorId() {
        System.out.println("\n--- Buscar Autor por ID ---");
        System.out.print("Ingrese el ID del autor a buscar: ");
        int id = leerOpcionNumerica();
        if (id == -1) return;
        Optional<Autor> autorOpt = autorDao.obtenerPorId(id);
        if (autorOpt.isPresent()) {
            System.out.println("Autor encontrado: " + autorOpt.get());
        } else {
            System.out.println("Autor con ID " + id + " no encontrado.");
        }
    }

    private static void actualizarAutor() {
        System.out.println("\n--- Actualizar Autor ---");
        System.out.print("Ingrese el ID del autor a actualizar: ");
        int id = leerOpcionNumerica();
        if (id == -1) return;
        Optional<Autor> autorOpt = autorDao.obtenerPorId(id);
        if (!autorOpt.isPresent()) {
            System.out.println("Autor con ID " + id + " no encontrado. No se puede actualizar.");
            return;
        }
        Autor autorParaActualizar = autorOpt.get();
        System.out.print("Ingrese el nuevo nombre para el autor '" + autorParaActualizar.getNombre() + "' (actual ID: "+ autorParaActualizar.getId() +"): ");
        String nuevoNombre = scanner.nextLine().trim();
        if (nuevoNombre.isEmpty()) {
            System.out.println("El nuevo nombre no puede estar vacío. Operación cancelada.");
            return;
        }
        autorParaActualizar.setNombre(nuevoNombre);
        if (autorDao.actualizar(autorParaActualizar)) {
            System.out.println("Confirmación App: Autor actualizado a '" + nuevoNombre + "'.");
        } else {
            System.out.println("Confirmación App: No se pudo actualizar el autor (verifique logs del DAO, puede que el autor ya no exista).");
        }
    }

    private static void eliminarAutor() {
        System.out.println("\n--- Eliminar Autor ---");
        System.out.print("Ingrese el ID del autor a eliminar: ");
        int id = leerOpcionNumerica();
        if (id == -1) return;
        Optional<Autor> autorOpt = autorDao.obtenerPorId(id);
        if (!autorOpt.isPresent()) {
            System.out.println("Autor con ID " + id + " no encontrado. No se puede eliminar.");
            return;
        }
        List<Libro> librosDelAutor = libroDao.obtenerLibrosPorAutorId(id);
        if (!librosDelAutor.isEmpty()) {
            System.out.println("¡ADVERTENCIA! El autor con ID " + id + " ('" + autorOpt.get().getNombre() +"') tiene " + librosDelAutor.size() + " libro(s) asociado(s).");
            System.out.println("Si elimina este autor, dichos libros quedarán sin autor asignado (su campo autor_id se establecerá a NULL).");
        }
        System.out.print("¿Está seguro de que desea eliminar al autor '" + autorOpt.get().getNombre() + "' con ID " + id + "? (s/N): ");
        String confirmacion = scanner.nextLine().trim().toLowerCase();
        if (confirmacion.equals("s")) {
            if (autorDao.eliminar(id)) {
                System.out.println("Confirmación App: Autor eliminado.");
            } else {
                System.out.println("Confirmación App: No se pudo eliminar el autor (verifique logs del DAO).");
            }
        } else {
            System.out.println("Eliminación cancelada.");
        }
    }

    private static void gestionarLibros() {
        int opcionSubMenu;
        do {
            mostrarMenuLibros();
            opcionSubMenu = leerOpcionNumerica();
            switch (opcionSubMenu) {
                case 1: crearLibro(); break;
                case 2: listarLibros(); break;
                case 3: buscarLibroPorId(); break;
                case 4: actualizarLibro(); break;
                case 5: eliminarLibro(); break;
                case 6: listarLibrosPorAutor(); break;
                case 0: System.out.println("Volviendo al menú principal..."); break;
                default: System.out.println("Opción de libro no válida."); break;
            }
        } while (opcionSubMenu != 0);
    }

    private static void crearLibro() {
        System.out.println("\n--- Crear Nuevo Libro ---");
        System.out.print("Ingrese el título del libro: ");
        String titulo = scanner.nextLine().trim();
        if (titulo.isEmpty()) {
            System.out.println("El título no puede estar vacío. Operación cancelada.");
            return;
        }
        System.out.print("Ingrese el ISBN del libro: ");
        String isbn = scanner.nextLine().trim();
        if (isbn.isEmpty()) {
            System.out.println("El ISBN no puede estar vacío. Operación cancelada.");
            return;
        }
        System.out.print("Ingrese el ID del autor para este libro: ");
        int autorId = leerOpcionNumerica();
        if (autorId == -1) return;
        if (autorId <= 0) {
            System.out.println("ID de autor no válido. Debe ser un número positivo. Operación cancelada.");
            return;
        }
        Optional<Autor> autorExistente = autorDao.obtenerPorId(autorId);
        if (!autorExistente.isPresent()) {
            System.out.println("Error: El autor con ID " + autorId + " no existe en la base de datos. No se puede crear el libro.");
            return;
        }
        System.out.println("Asignando libro al autor: " + autorExistente.get().getNombre() + " (ID: " + autorId + ")");
        Libro nuevoLibro = new Libro(titulo, isbn, autorId);
        libroDao.crear(nuevoLibro);
        if (nuevoLibro.getId() > 0) {
            System.out.println("Confirmación App: Libro '" + nuevoLibro.getTitulo() + "' creado con ID: " + nuevoLibro.getId() + ".");
        } else {
            System.out.println("Confirmación App: No se pudo crear el libro (verifique logs del DAO, ¿ISBN duplicado?).");
        }
    }

    private static void listarLibros() {
        System.out.println("\n--- Lista de Libros ---");
        List<Libro> libros = libroDao.obtenerTodos();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            for (Libro libro : libros) {
                System.out.print(libro);
                Optional<Autor> autorDelLibro = autorDao.obtenerPorId(libro.getAutorId());
                if (autorDelLibro.isPresent()) {
                    System.out.println(" (Autor: " + autorDelLibro.get().getNombre() + ")");
                } else if (libro.getAutorId() != 0) {
                    System.out.println(" (ID de Autor: " + libro.getAutorId() + " - Autor no encontrado o eliminado)");
                } else {
                    System.out.println(" (Autor no asignado)");
                }
            }
        }
    }

    private static void buscarLibroPorId() {
        System.out.println("\n--- Buscar Libro por ID ---");
        System.out.print("Ingrese el ID del libro a buscar: ");
        int id = leerOpcionNumerica();
        if (id == -1) return;
        Optional<Libro> libroOpt = libroDao.obtenerPorId(id);
        if (libroOpt.isPresent()) {
            Libro libroEncontrado = libroOpt.get();
            System.out.print("Libro encontrado: " + libroEncontrado);
            Optional<Autor> autorDelLibro = autorDao.obtenerPorId(libroEncontrado.getAutorId());
            if (autorDelLibro.isPresent()) {
                System.out.println(" (Autor: " + autorDelLibro.get().getNombre() + ")");
            } else if (libroEncontrado.getAutorId() != 0) {
                System.out.println(" (ID de Autor: " + libroEncontrado.getAutorId() + " - Autor no encontrado o eliminado)");
            } else {
                System.out.println(" (Autor no asignado)");
            }
        } else {
            System.out.println("Libro con ID " + id + " no encontrado.");
        }
    }

    private static void actualizarLibro() {
        System.out.println("\n--- Actualizar Libro ---");
        System.out.print("Ingrese el ID del libro a actualizar: ");
        int id = leerOpcionNumerica();
        if (id == -1) return;
        Optional<Libro> libroOpt = libroDao.obtenerPorId(id);
        if (!libroOpt.isPresent()) {
            System.out.println("Libro con ID " + id + " no encontrado. No se puede actualizar.");
            return;
        }
        Libro libroParaActualizar = libroOpt.get();
        System.out.println("Actualizando libro: " + libroParaActualizar);
        System.out.print("Ingrese el nuevo título (actual: '" + libroParaActualizar.getTitulo() + "', dejar vacío para no cambiar): ");
        String nuevoTitulo = scanner.nextLine().trim();
        if (!nuevoTitulo.isEmpty()) {
            libroParaActualizar.setTitulo(nuevoTitulo);
        }
        System.out.print("Ingrese el nuevo ISBN (actual: '" + libroParaActualizar.getIsbn() + "', dejar vacío para no cambiar): ");
        String nuevoIsbn = scanner.nextLine().trim();
        if (!nuevoIsbn.isEmpty()) {
            libroParaActualizar.setIsbn(nuevoIsbn);
        }
        System.out.print("Ingrese el nuevo ID del autor (actual: " + libroParaActualizar.getAutorId() + ", dejar vacío o 0 para mantener/desasignar): ");
        String nuevoAutorIdStr = scanner.nextLine().trim();
        if (!nuevoAutorIdStr.isEmpty()) {
            try {
                int nuevoAutorId = Integer.parseInt(nuevoAutorIdStr);
                if (nuevoAutorId > 0) {
                    Optional<Autor> autorExistente = autorDao.obtenerPorId(nuevoAutorId);
                    if (!autorExistente.isPresent()) {
                        System.out.println("Error: El autor con ID " + nuevoAutorId + " no existe. El autor del libro no se cambiará.");
                    } else {
                        System.out.println("Cambiando autor a: " + autorExistente.get().getNombre() + " (ID: " + nuevoAutorId + ")");
                        libroParaActualizar.setAutorId(nuevoAutorId);
                    }
                } else if (nuevoAutorId == 0 && libroParaActualizar.getAutorId() != 0) {
                    System.out.println("Advertencia: Se desasignará el autor del libro (autor_id será NULL).");
                    libroParaActualizar.setAutorId(0);
                } else if (nuevoAutorId < 0) {
                    System.out.println("ID de autor inválido. No se cambiará el autor.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida para el ID del autor. No se cambiará el autor.");
            }
        }
        if (libroDao.actualizar(libroParaActualizar)) {
            System.out.println("Confirmación App: Libro actualizado.");
        } else {
            System.out.println("Confirmación App: No se pudo actualizar el libro (verifique logs del DAO, ¿ISBN duplicado?).");
        }
    }

    private static void eliminarLibro() {
        System.out.println("\n--- Eliminar Libro ---");
        System.out.print("Ingrese el ID del libro a eliminar: ");
        int id = leerOpcionNumerica();
        if (id == -1) return;
        Optional<Libro> libroOpt = libroDao.obtenerPorId(id);
        if (!libroOpt.isPresent()) {
            System.out.println("Libro con ID " + id + " no encontrado. No se puede eliminar.");
            return;
        }
        System.out.print("¿Está seguro de que desea eliminar el libro '" + libroOpt.get().getTitulo() + "' con ID " + id + "? (s/N): ");
        String confirmacion = scanner.nextLine().trim().toLowerCase();
        if (confirmacion.equals("s")) {
            if (libroDao.eliminar(id)) {
                System.out.println("Confirmación App: Libro eliminado.");
            } else {
                System.out.println("Confirmación App: No se pudo eliminar el libro (verifique logs del DAO).");
            }
        } else {
            System.out.println("Eliminación cancelada.");
        }
    }

    private static void listarLibrosPorAutor() {
        System.out.println("\n--- Listar Libros por Autor ---");
        System.out.print("Ingrese el ID del autor para ver sus libros: ");
        int autorId = leerOpcionNumerica();
        if (autorId == -1) return;
        Optional<Autor> autorOpt = autorDao.obtenerPorId(autorId);
        if (!autorOpt.isPresent()) {
            System.out.println("El autor con ID " + autorId + " no existe.");
            return;
        }
        System.out.println("--- Libros del Autor: " + autorOpt.get().getNombre() + " (ID: " + autorId + ") ---");
        List<Libro> libros = libroDao.obtenerLibrosPorAutorId(autorId);
        if (libros.isEmpty()) {
            System.out.println("Este autor no tiene libros registrados.");
        } else {
            for (Libro libro : libros) {
                System.out.println(libro);
            }
        }
    }
}