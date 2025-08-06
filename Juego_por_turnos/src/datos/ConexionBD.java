/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// datos/ConexionBD.java
package datos;

/**
 *
 * @author Jairo Herrera Romero
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = 
        "jdbc:sqlserver://localhost\\SQLEXPRESS;" +
        "databaseName=videojuego_turnos;" +
        "integratedSecurity=true;" +
        "encrypt=false;";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver JDBC no encontrado.");
            e.printStackTrace();
            throw new SQLException("No se encontró el driver de SQL Server.", e);
        }
        return DriverManager.getConnection(URL);
    }

    // Prueba de conexión
    public static void main(String[] args) {
        System.out.println("🔌 Probando conexión a la base de datos...");
        try (Connection conn = getConnection()) {
            System.out.println("✅ ¡Conexión exitosa a videojuego_turnos!");
        } catch (SQLException e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}