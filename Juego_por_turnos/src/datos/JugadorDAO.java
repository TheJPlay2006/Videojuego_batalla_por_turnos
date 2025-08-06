/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

/**
 *
 * @author Jairo Herrera Romero
 */

// datos/JugadorDAO.java
import entidades.Jugador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase de acceso a datos para la entidad Jugador.
 * Realiza operaciones CRUD básicas en la tabla 'jugador' de la base de datos.
 */
public class JugadorDAO {

    /**
     * Inserta un nuevo jugador en la base de datos.
     * @param jugador El objeto Jugador a insertar.
     * @return true si se insertó correctamente, false en caso contrario.
     */
    public boolean insertarJugador(Jugador jugador) {
        String sql = "INSERT INTO jugador (nombre, partidas_ganadas, partidas_perdidas) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, jugador.getNombre());
            ps.setInt(2, jugador.getPartidasGanadas());
            ps.setInt(3, jugador.getPartidasPerdidas());

            int filas = ps.executeUpdate();

            // Recuperar el ID generado
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        jugador.setId(rs.getInt(1));
                    }
                }
                System.out.println("✅ Jugador '" + jugador.getNombre() + "' guardado en la base de datos con ID: " + jugador.getId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar jugador: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Actualiza las estadísticas (partidas ganadas y perdidas) de un jugador existente.
     * @param jugador El jugador con datos actualizados.
     * @return true si se actualizó, false si no se encontró o hubo error.
     */
    public boolean actualizarEstadisticas(Jugador jugador) {
        String sql = "UPDATE jugador SET partidas_ganadas = ?, partidas_perdidas = ? WHERE id = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jugador.getPartidasGanadas());
            ps.setInt(2, jugador.getPartidasPerdidas());
            ps.setInt(3, jugador.getId());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("✅ Estadísticas de '" + jugador.getNombre() + "' actualizadas.");
                return true;
            } else {
                System.out.println("⚠️  No se encontró el jugador con ID: " + jugador.getId());
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar jugador: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Busca un jugador por su nombre.
     * @param nombre El nombre del jugador a buscar.
     * @return El objeto Jugador si se encuentra, null si no.
     */
    public Jugador buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM jugador WHERE nombre = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Jugador jugador = new Jugador(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("partidas_ganadas"),
                        rs.getInt("partidas_perdidas")
                    );
                    System.out.println("🔍 Jugador encontrado: " + jugador);
                    return jugador;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar jugador: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("⚠️  No se encontró el jugador: " + nombre);
        return null;
    }

    /**
     * Método principal para probar las funciones del DAO.
     */
    public static void main(String[] args) {
        JugadorDAO dao = new JugadorDAO();

        // 1. Probar conexión
        System.out.println("🔌 Probando conexión...");
        try (Connection conn = ConexionBD.getConnection()) {
            System.out.println("✅ Conexión exitosa.");
        } catch (SQLException e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
            return;
        }

        // 2. Crear y guardar un jugador
        Jugador jugador = new Jugador("Carlos");
        dao.insertarJugador(jugador);

        // 3. Simular que ganó una partida
        jugador.setPartidasGanadas(jugador.getPartidasGanadas() + 1);
        dao.actualizarEstadisticas(jugador);

        // 4. Buscar jugador
        Jugador encontrado = dao.buscarPorNombre("Carlos");
        if (encontrado != null) {
            System.out.println("📦 Datos cargados: " + encontrado);
        }
    }
}