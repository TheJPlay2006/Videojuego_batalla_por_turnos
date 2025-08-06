/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos;

/**
 *
 * @author Jairo Herrera Romero
 */

// datos/PersonajeDAO.java
import entidades.Arma;
import entidades.Raza;
import modelos.Personaje;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos para gestionar la persistencia de personajes.
 * Guarda, busca y lista personajes en la base de datos SQL Server.
 * Relaciona cada personaje con un jugador mediante id_jugador.
 */
public class PersonajeDAO {

    /**
     * Guarda un personaje en la base de datos, asociado a un jugador.
     * Requiere que la tabla 'personaje' tenga el campo 'id_jugador'.
     *
     * @param personaje El personaje a guardar.
     * @param idJugador El ID del jugador dueño del personaje.
     * @return true si se guardó correctamente, false en caso contrario.
     */
    public boolean guardar(Personaje personaje, int idJugador) {
        String sql = "INSERT INTO personaje (nombre, id_raza, fuerza, energia, vida_actual, id_arma, id_jugador) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, personaje.getNombre());
            ps.setInt(2, personaje.getRaza().getId());
            ps.setInt(3, personaje.getFuerza());
            ps.setInt(4, personaje.getEnergia());
            ps.setInt(5, personaje.getVidaActual());
            ps.setInt(6, personaje.getArma().getId());
            ps.setInt(7, idJugador);  // Clave foránea al jugador

            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("✅ Personaje '" + personaje.getNombre() + "' guardado correctamente.");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al guardar el personaje: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Busca un personaje por su nombre exacto.
     *
     * @param nombrePersonaje Nombre del personaje a buscar.
     * @return Lista con el personaje encontrado (puede estar vacía).
     */
    public List<Personaje> buscarPorNombrePersonaje(String nombrePersonaje) {
        List<Personaje> personajes = new ArrayList<>();
        String sql = "SELECT p.nombre, p.vida_actual, p.fuerza, p.energia, "
                   + "r.nombre AS raza_nombre, r.descripcion AS raza_descripcion, "
                   + "a.nombre AS arma_nombre, a.tipo AS arma_tipo, a.dano_minimo, a.dano_maximo "
                   + "FROM personaje p "
                   + "JOIN raza r ON p.id_raza = r.id "
                   + "JOIN arma a ON p.id_arma = a.id "
                   + "WHERE p.nombre = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombrePersonaje);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    int vida = rs.getInt("vida_actual");
                    int fuerza = rs.getInt("fuerza");
                    int energia = rs.getInt("energia");

                    Raza raza = new Raza(0, rs.getString("raza_nombre"), rs.getString("raza_descripcion"));
                    Arma arma = new Arma(
                        0,
                        rs.getString("arma_nombre"),
                        rs.getString("arma_tipo"),
                        rs.getInt("dano_minimo"),
                        rs.getInt("dano_maximo"),
                        ""
                    );

                    // Crear instancia temporal (abstracta)
                    Personaje temp = new Personaje(nombre, vida, fuerza, energia, arma, raza) {
                        @Override
                        public int atacar(boolean enDistancia) { return 0; }
                        @Override
                        public void sanar() {}
                        @Override
                        public void recibirDaño(int daño) {}
                    };

                    personajes.add(temp);
                    System.out.println("🔍 Personaje encontrado: " + temp);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al buscar personaje: " + e.getMessage());
            e.printStackTrace();
        }

        return personajes;
    }

    /**
     * Lista todos los personajes registrados en la base de datos.
     * Útil para depuración o estadísticas.
     */
    public void listarTodos() {
        String sql = "SELECT p.nombre, r.nombre AS raza, a.nombre AS arma, p.vida_actual "
                   + "FROM personaje p "
                   + "JOIN raza r ON p.id_raza = r.id "
                   + "JOIN arma a ON p.id_arma = a.id "
                   + "ORDER BY p.id";

        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n=== PERSONAJES REGISTRADOS ===");
            while (rs.next()) {
                System.out.println("Nombre: " + rs.getString("nombre") +
                                 " | Raza: " + rs.getString("raza") +
                                 " | Arma: " + rs.getString("arma") +
                                 " | Vida: " + rs.getInt("vida_actual"));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al listar personajes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Método de prueba para validar el funcionamiento del DAO.
     */
    public static void main(String[] args) {
        PersonajeDAO dao = new PersonajeDAO();

        // 1. Probar conexión
        System.out.println("🔌 Probando conexión a la base de datos...");
        try (Connection conn = ConexionBD.getConnection()) {
            System.out.println("✅ Conexión exitosa.");
        } catch (SQLException e) {
            System.err.println("❌ Error de conexión: " + e.getMessage());
            return;
        }

        // 2. Crear un ejemplo de personaje (ej: Humano)
        Raza raza = new Raza(1, "Humano", "Experto en armas de fuego");
        Arma arma = new Arma(1, "Escopeta", "fuego", 1, 5, "+2% daño");

        Personaje p = new Personaje("Carlos", 90, 10, 100, arma, raza) {
            @Override
            public int atacar(boolean enDistancia) { return 0; }
            @Override
            public void sanar() {}
            @Override
            public void recibirDaño(int daño) {}
        };

        // 3. Guardar personaje (suponiendo que el jugador tiene ID = 1)
        dao.guardar(p, 1);

        // 4. Buscar personaje
        dao.buscarPorNombrePersonaje("Carlos");

        // 5. Listar todos
        dao.listarTodos();
    }
}