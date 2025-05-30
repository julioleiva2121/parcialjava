package com.proyectojava.dao.impl;


import com.proyectojava.dao.AutorDao;
import com.proyectojava.model.Autor;
import com.proyectojava.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AutorDaoImpl implements AutorDao {

    @Override
    public void crear(Autor autor) {
        String sql = "INSERT INTO AUTOR (nombre) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, autor.getNombre());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        autor.setId(generatedKeys.getInt(1));
                        System.out.println("Autor creado con ID: " + autor.getId());
                    } else {
                        throw new SQLException("Fallo al crear autor, no se obtuvo ID.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al crear autor: " + e.getMessage());
            // e.printStackTrace();
        }
    }

    // READ
    @Override
    public Optional<Autor> obtenerPorId(int id) {
        String sql = "SELECT * FROM AUTOR WHERE id = ?";
        Autor autor = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    autor = new Autor();
                    autor.setId(rs.getInt("id"));
                    autor.setNombre(rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener autor por ID: " + e.getMessage());
        }
        return Optional.ofNullable(autor);
    }

    @Override
    public List<Autor> obtenerTodos() {
        String sql = "SELECT * FROM AUTOR ORDER BY nombre ASC";
        List<Autor> autores = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Autor autor = new Autor();
                autor.setId(rs.getInt("id"));
                autor.setNombre(rs.getString("nombre"));
                autores.add(autor);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los autores: " + e.getMessage());
        }
        return autores;
    }

    // UPDATE
    @Override
    public boolean actualizar(Autor autor) {
        String sql = "UPDATE AUTOR SET nombre = ? WHERE id = ?";
        boolean actualizado = false;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, autor.getNombre());
            pstmt.setInt(2, autor.getId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                actualizado = true;
                System.out.println("Autor actualizado: " + autor);
            } else {
                System.out.println("No se encontró el autor con ID " + autor.getId() + " para actualizar.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar autor: " + e.getMessage());
        }
        return actualizado;
    }

    // DELETE
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM AUTOR WHERE id = ?";
        boolean eliminado = false;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                eliminado = true;
                System.out.println("Autor con ID " + id + " eliminado.");
            } else {
                System.out.println("No se encontró el autor con ID " + id + " para eliminar.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar autor: " + e.getMessage());
        }
        return eliminado;
    }
}