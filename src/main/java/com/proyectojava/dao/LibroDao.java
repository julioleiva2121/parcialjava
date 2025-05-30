package com.proyectojava.dao;

import com.proyectojava.model.Libro;
import java.util.List;
import java.util.Optional;

public interface LibroDao {
    void crear(Libro libro);
    Optional<Libro> obtenerPorId(int id);
    List<Libro> obtenerTodos();
    boolean actualizar(Libro libro);
    boolean eliminar(int id);
    List<Libro> obtenerLibrosPorAutorId(int autorId);
}