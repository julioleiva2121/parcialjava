package com.proyectojava.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {


    private static final String JDBC_URL = "jdbc:h2:./biblioteca_db;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }


    public static void initializeDatabase() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {


            String createAutorTableSQL = "CREATE TABLE IF NOT EXISTS AUTOR (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(255) NOT NULL" +
                    ")";
            statement.execute(createAutorTableSQL);
            System.out.println("Tabla AUTOR verificada/creada.");


            String createLibroTableSQL = "CREATE TABLE IF NOT EXISTS LIBRO (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "titulo VARCHAR(255) NOT NULL, " +
                    "isbn VARCHAR(50) UNIQUE, " +
                    "autor_id INT, " +
                    "FOREIGN KEY (autor_id) REFERENCES AUTOR(id) ON DELETE SET NULL" +
                    ")";
            statement.execute(createLibroTableSQL);
            System.out.println("Tabla LIBRO verificada/creada.");


        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        initializeDatabase();
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("¡Conexión a la base de datos H2 exitosa!");
            } else {
                System.out.println("Fallo al conectar a la base de datos H2.");
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}