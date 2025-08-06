/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

/**
 *
 * @author Jairo Herrera Romero
 */

// datos/PartidaDAO.java
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Clase de acceso a datos para registrar el resultado de las partidas.
 * Almacena quién ganó, quién perdió, la fecha y la razón del fin del combate.
 */

public class PartidaDAO {

    /**
     * Registra el resultado de una partida en la base de datos.
     *
     * @param idJugadorGanador ID del jugador ganador.
     * @param idJugadorPerdedor ID del jugador perdedor.
     * @param razon Cómo terminó la partida (ej: "vida 0", "rendición").
     * @return true si se registró correctamente, false en caso contrario.
     */
    public boolean registrarPartida(int idJugadorGanador, int idJugadorPerdedor, String razon) {
        String sql = "INSERT INTO partida (id_jugador_ganador, id_jugador_perdedor, razon) VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idJugadorGanador);
            ps.setInt(2, idJugadorPerdedor);
            ps.setString(3, razon != null ? razon : "Sin razón especificada");

            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("✅ Partida registrada: Jugador " + idJugadorGanador + " ganó contra " + idJugadorPerdedor);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al registrar la partida: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Método de prueba para validar el funcionamiento de PartidaDAO.
     */
    public static void main(String[] args) {
        PartidaDAO dao = new PartidaDAO();

        // Simulación: Jugador con ID=1 gana contra jugador con ID=2
        boolean exito = dao.registrarPartida(1, 2, "vida 0");
        if (exito) {
            System.out.println("🎉 Prueba de registro de partida exitosa.");
        } else {
            System.out.println("⚠️  La prueba falló. Revisa la conexión o la tabla 'partida'.");
        }
    }
}