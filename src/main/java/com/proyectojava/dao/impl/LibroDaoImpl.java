package com.proyectojava.dao.impl;

import com.proyectojava.dao.LibroDao;
import com.proyectojava.model.Libro;
import com.proyectojava.util.DatabaseConnection;

import java.sql.*; // Para Connection, PreparedStatement, ResultSet, SQLException, Statement
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibroDaoImpl implements LibroDao {

    // --- MÉTODO CREAR ---
    @Override
    public void crear(Libro libro) {
        // La columna en la base de datos para el ID del autor es 'autor_id'
        String sql = "INSERT INTO LIBRO (titulo, isbn, autor_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, libro.getTitulo());
            pstmt.setString(2, libro.getIsbn());
            pstmt.setInt(3, libro.getAutorId()); // Obtenemos el autorId del objeto Libro

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                // Si la inserción fue exitosa, obtenemos el ID generado para el libro
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        libro.setId(generatedKeys.getInt(1)); // Asignamos el nuevo ID al objeto libro
                        System.out.println("Libro creado con ID: " + libro.getId());
                    } else {
                        // Esto no debería ocurrir si la inserción fue exitosa y la tabla tiene AUTO_INCREMENT
                        throw new SQLException("Fallo al crear libro, no se obtuvo ID generado.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al crear libro: " + e.getMessage());
            // Verificamos si el error es por ISBN duplicado (restricción UNIQUE)
            // El código de error '23505' es común para violaciones de unicidad en H2 y PostgreSQL.
            if (e.getSQLState() != null && e.getSQLState().equals("23505")) {
                System.err.println("Detalle: El ISBN '" + libro.getIsbn() + "' ya existe en la base de datos.");
            }
            // Podrías querer lanzar una excepción personalizada aquí o manejarlo de otra forma.
            // e.printStackTrace(); // Para ver la traza completa del error durante el desarrollo
        }
    }

    // --- MÉTODO OBTENER POR ID ---
    @Override
    public Optional<Libro> obtenerPorId(int id) {
        String sql = "SELECT id, titulo, isbn, autor_id FROM LIBRO WHERE id = ?";
        Libro libro = null; // Empezamos con libro nulo

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id); // Establecemos el ID en la consulta

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) { // Si se encontró una fila
                    libro = new Libro();
                    libro.setId(rs.getInt("id"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setIsbn(rs.getString("isbn"));
                    libro.setAutorId(rs.getInt("autor_id")); // Leer de la columna 'autor_id'
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener libro por ID " + id + ": " + e.getMessage());
        }
        // Optional.ofNullable creará un Optional vacío si libro sigue siendo null,
        // o un Optional con el libro si se encontró.
        return Optional.ofNullable(libro);
    }

    // --- MÉTODO OBTENER TODOS ---
    @Override
    public List<Libro> obtenerTodos() {
        String sql = "SELECT id, titulo, isbn, autor_id FROM LIBRO ORDER BY titulo ASC";
        List<Libro> libros = new ArrayList<>(); // Lista para almacenar los libros

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement(); // Statement simple, no hay parámetros
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) { // Iterar sobre cada fila del resultado
                Libro libro = new Libro();
                libro.setId(rs.getInt("id"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setIsbn(rs.getString("isbn"));
                libro.setAutorId(rs.getInt("autor_id"));
                libros.add(libro); // Añadir el libro a la lista
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los libros: " + e.getMessage());
        }
        return libros; // Devolver la lista (puede estar vacía si no hay libros)
    }

    // --- MÉTODO ACTUALIZAR ---
    @Override
    public boolean actualizar(Libro libro) {
        String sql = "UPDATE LIBRO SET titulo = ?, isbn = ?, autor_id = ? WHERE id = ?";
        boolean actualizado = false; // Bandera para saber si se actualizó

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, libro.getTitulo());
            pstmt.setString(2, libro.getIsbn());
            pstmt.setInt(3, libro.getAutorId());
            pstmt.setInt(4, libro.getId()); // El ID va en la cláusula WHERE

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) { // Si al menos una fila fue afectada
                actualizado = true;
                System.out.println("Libro actualizado: " + libro);
            } else {
                System.out.println("No se encontró el libro con ID " + libro.getId() + " para actualizar.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar libro con ID " + libro.getId() + ": " + e.getMessage());
            if (e.getSQLState() != null && e.getSQLState().equals("23505")) {
                System.err.println("Detalle: El ISBN '" + libro.getIsbn() + "' ya pertenece a otro libro.");
            }
        }
        return actualizado;
    }

    // --- MÉTODO ELIMINAR ---
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM LIBRO WHERE id = ?";
        boolean eliminado = false;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                eliminado = true;
                System.out.println("Libro con ID " + id + " eliminado.");
            } else {
                System.out.println("No se encontró el libro con ID " + id + " para eliminar.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar libro con ID " + id + ": " + e.getMessage());
            // Aquí no esperamos violación de ISBN, pero podría haber otros errores.
        }
        return eliminado;
    }

    // --- MÉTODO OBTENER LIBROS POR AUTOR ID ---
    @Override
    public List<Libro> obtenerLibrosPorAutorId(int autorId) {
        String sql = "SELECT id, titulo, isbn, autor_id FROM LIBRO WHERE autor_id = ? ORDER BY titulo ASC";
        List<Libro> libros = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, autorId); // Establecemos el autor_id en la consulta

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Libro libro = new Libro();
                    libro.setId(rs.getInt("id"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setIsbn(rs.getString("isbn"));
                    libro.setAutorId(rs.getInt("autor_id")); // Este será igual al autorId que pasamos
                    libros.add(libro);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener libros para el autor ID " + autorId + ": " + e.getMessage());
        }
        return libros;
    }
}