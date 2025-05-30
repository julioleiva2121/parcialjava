package com.proyectojava.model;

import java.util.Date; // Importamos Date para el ejemplo de fecha (opcional)

public class Libro {
    private int id;
    private String titulo;
    private String isbn;
    private int autorId; // Clave foránea para relacionar con Autor



    public Libro() {
    }


    public Libro(String titulo, String isbn, int autorId) {
        this.titulo = titulo;
        this.isbn = isbn;
        this.autorId = autorId;
    }


    public Libro(int id, String titulo, String isbn, int autorId) {
        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
        this.autorId = autorId;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getAutorId() {
        return autorId;
    }

    public void setAutorId(int autorId) {
        this.autorId = autorId;
    }

    /*
    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }
    */


    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", isbn='" + isbn + '\'' +
                ", autorId=" + autorId +
                // (fechaPublicacion != null ? ", fechaPublicacion=" + fechaPublicacion : "") + // Si añades fecha
                '}';
    }
}