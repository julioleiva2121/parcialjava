package com.proyectojava.dao;

import com.proyectojava.model.Autor;
import java.util.List;
import java.util.Optional;

public interface AutorDao {

    // CREATE
    void crear(Autor autor);

    // READ
    Optional<Autor> obtenerPorId(int id);
    List<Autor> obtenerTodos();

    // UPDATE
    boolean actualizar(Autor autor);

    // DELETE
    boolean eliminar(int id);
}