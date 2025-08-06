/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juego;
import modelos.Personaje;
import datos.ConexionBD;               // Para conectarse a la base de datos
import datos.JugadorDAO;                 // Para gestionar operaciones CRUD de jugadores
import datos.PersonajeDAO;               // Para guardar personajes en la base de datos
import modelos.*;                       // Importa Raza, Arma, Jugador
import java.sql.*;                     // Para manejar conexiones y excepciones SQL
import java.util.*;                    // Para usar List, ArrayList, Scanner
import entidades.Arma;                //Para usar las armas 
import entidades.Raza;
import entidades.Jugador;

/**
 *
 * @author USER
 */
/**
 * Clase principal del videojuego de combate por turnos.
 * Gestiona la lógica general del juego: carga de datos, creación de personajes,
 * desarrollo del combate, y almacenamiento de resultados.
 */
public class Juego {

    // Scanner para entrada de datos por consola
    private Scanner sc = new Scanner(System.in);

    // Listas que almacenan razas y armas disponibles
    private List<Raza> razas;
    private List<Arma> armas;

    // Instancias de los personajes/jugadores
    private Personaje jugador1;
    private Personaje jugador2;

    // Distancia entre los personajes (en metros)
    private int distancia = 5;

    /**
     * Método principal que lanza la ejecución del juego.
     */
    public static void main(String[] args) {
        Juego juego = new Juego();
        try {
            juego.iniciar();
        } catch (Exception e) {
            System.out.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Método que inicia el flujo principal del juego:
     * carga datos, crea jugadores, ejecuta combate y guarda resultados.
     */
    public void iniciar() throws SQLException {
        System.out.println("️ BIENVENIDO AL VIDEOJUEGO DE COMBATE POR TURNOS ️");
        cargarDatos();
        crearJugadores();
        combate();
        guardarResultado();
    }

    /**
     * Carga razas y armas desde la base de datos.
     */
    private void cargarDatos() throws SQLException {
        String sqlRaza = "SELECT id, nombre, descripcion FROM raza";
        String sqlArma = "SELECT id, nombre, tipo, dano_minimo, dano_maximo, modificadores FROM arma";

        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement()) {

            // Cargar razas
            ResultSet rsRaza = stmt.executeQuery(sqlRaza);
            razas = new ArrayList<>();
            while (rsRaza.next()) {
                razas.add(new Raza(
                    rsRaza.getInt("id"),
                    rsRaza.getString("nombre"),
                    rsRaza.getString("descripcion")
                ));
            }
            rsRaza.close();

            // Cargar armas
            ResultSet rsArma = stmt.executeQuery(sqlArma);
            armas = new ArrayList<>();
            while (rsArma.next()) {
                armas.add(new Arma(
                    rsArma.getInt("id"),
                    rsArma.getString("nombre"),
                    rsArma.getString("tipo"),
                    rsArma.getInt("dano_minimo"),
                    rsArma.getInt("dano_maximo"),
                    rsArma.getString("modificadores")
                ));
            }
            rsArma.close();
        }

        System.out.println("✅ Datos cargados desde la base de datos.");
    }

    /**
     * Solicita los nombres de los jugadores y crea sus respectivos personajes.
     */
    private void crearJugadores() throws SQLException {
        System.out.print(" Jugador 1 - Nombre: ");
        String nombre1 = sc.nextLine();
        jugador1 = crearPersonaje(nombre1, 1);

        System.out.print(" Jugador 2 - Nombre: ");
        String nombre2 = sc.nextLine();
        jugador2 = crearPersonaje(nombre2, 2);
    }

    /**
     * Crea un personaje para un jugador determinado, permitiéndole elegir raza y arma.
     */
    private Personaje crearPersonaje(String nombre, int jugadorId) {
        System.out.println("\n" + nombre + ", elige una raza:");
        System.out.println("1. Humano  2. Elfo  3. Orco  4. Bestia");

        int razaId = sc.nextInt();
        sc.nextLine(); // Limpiar buffer

        // Buscar raza por ID
        Raza raza = obtenerRaza(razaId);
        if (raza == null) {
            System.out.println("❌ Raza inválida. Seleccionando Orco por defecto.");
            raza = obtenerRaza(3);
        }

        // Elegir arma válida según la raza
        Arma arma = seleccionarArma(raza);

        // Calcular vida inicial (puede variar según raza/arma)
        int vidaInicial = calcularVidaInicial(raza, arma);

        // Crear el personaje según su raza
        switch (razaId) {
            case 1: return new Humano(nombre, arma, raza);
            case 2: return new Elfo(nombre, arma, raza);
            case 3: return new Orco(nombre, arma, raza);
            case 4: return new Bestia(nombre, arma, raza);
            default:
                System.out.println("❌ Opción inválida. Seleccionando Orco por defecto.");
                return new Orco(nombre, arma, raza);
        }
    }

    /**
     * Busca una raza por su ID.
     */
    private Raza obtenerRaza(int id) {
        return razas.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
    }

    /**
     * Muestra las armas disponibles según la raza del personaje y permite seleccionar una.
     */
    private Arma seleccionarArma(Raza raza) {
        List<Arma> armasValidas = new ArrayList<>();

        switch (raza.getNombre()) {
            case "Humano":
                armasValidas = filtrarArmas("Escopeta", "Rifle francotirador");
                break;
            case "Elfo":
                armasValidas = filtrarArmas("Báculo Fuego", "Báculo Tierra", "Báculo Aire", "Báculo Agua");
                break;
            case "Orco":
                armasValidas = filtrarArmas("Hacha", "Martillo");
                break;
            case "Bestia":
                armasValidas = filtrarArmas("Puños", "Espada");
                break;
            default:
                System.out.println("️  Raza no soportada.");
                break;
        }

        if (armasValidas.isEmpty()) {
            System.out.println("❌ No hay armas disponibles para esta raza.");
            return armas.get(0); // Valor por defecto
        }

        // Mostrar opciones
        System.out.println("Elige tu arma:");
        for (int i = 0; i < armasValidas.size(); i++) {
            System.out.println((i + 1) + ". " + armasValidas.get(i).getNombre() + " → " + armasValidas.get(i).getModificadores());
        }

        int opcion = sc.nextInt();
        sc.nextLine(); // Limpiar buffer
        return armasValidas.get(opcion - 1);
    }

    /**
     * Filtra la lista de armas disponibles según los nombres permitidos.
     */
    private List<Arma> filtrarArmas(String... nombres) {
        return armas.stream()
                .filter(arma -> Arrays.asList(nombres).contains(arma.getNombre()))
                .toList();
    }

    /**
     * Calcula la vida inicial del personaje según su raza y arma.
     */
    private int calcularVidaInicial(Raza raza, Arma arma) {
        if (raza.getNombre().equals("Elfo") && arma.getNombre().equals("Báculo Agua")) {
            return 115; // Bonificación especial
        }
        return 100;
    }

    /**
     * Desarrolla el combate por turnos entre los dos jugadores.
     */
    private void combate() {
        System.out.println("\n ¡COMIENZA EL DUELO! ");
        int turno = 1;

        while (jugador1.getVidaActual() > 0 && jugador2.getVidaActual() > 0) {
            System.out.println("\n--- Turno " + turno + " ---");
            mostrarEstado();

            // Turno jugador 1
            if (jugador1.getVidaActual() > 0) {
                System.out.println("➡️  Turno de " + jugador1.getNombre());
                gestionarDistancia();
                if (distancia <= 1) {
                    realizarAccion(jugador1, jugador2);
                } else {
                    System.out.println(jugador1.getNombre() + " está demasiado lejos para atacar.");
                }
            }

            // Aplicar sangrado (si es Orco)
            if (jugador1 instanceof Orco) {
                ((Orco) jugador1).aplicarSangrado();
            }

            // Verificar si jugador 2 sigue vivo
            if (jugador2.getVidaActual() <= 0) break;

            // Turno jugador 2
            if (jugador2.getVidaActual() > 0) {
                System.out.println("➡️  Turno de " + jugador2.getNombre());
                gestionarDistancia();
                if (distancia <= 1) {
                    realizarAccion(jugador2, jugador1);
                } else {
                    System.out.println(jugador2.getNombre() + " está demasiado lejos para atacar.");
                }
            }

            if (jugador2 instanceof Orco) {
                ((Orco) jugador2).aplicarSangrado();
            }

            turno++;
        }
    }

    /**
     * Permite modificar la distancia entre jugadores al inicio de cada turno.
     */
    private void gestionarDistancia() {
        System.out.println("📍 Distancia actual: " + distancia + "m");
        System.out.println("1. Avanzar  2. Retroceder  3. Mantener");
        int opcion = sc.nextInt();

        switch (opcion) {
            case 1:
                distancia = Math.max(0, distancia - 1);
                System.out.println("✅ Avanzando... distancia: " + distancia + "m");
                break;
            case 2:
                distancia++;
                System.out.println("✅ Retrocediendo... distancia: " + distancia + "m");
                break;
            default:
                System.out.println("➡️  Manteniendo distancia.");
                break;
        }
    }

    /**
     * Verifica si los personajes están fuera del rango de ataque.
     */
    private boolean enDistancia() {
        return distancia > 1;
    }

    /**
     * Ejecuta la acción del personaje (atacar, sanar o defender).
     */
    private void realizarAccion(Personaje atacante, Personaje defensor) {
        System.out.println("\n" + atacante.getNombre() + ", elige acción:");
        System.out.println("1. Atacar  2. Sanar  3. Defender");

        int opcion = sc.nextInt();
        switch (opcion) {
            case 1:
                int daño = atacante.atacar(enDistancia());
                if (daño > 0) {
                    defensor.recibirDaño(daño);
                }
                break;
            case 2:
                atacante.sanar();
                break;
            case 3:
                System.out.println(atacante.getNombre() + " se defiende. (Defensa no implementada aún)");
                break;
            default:
                System.out.println("❌ Opción inválida. Pierdes el turno.");
                break;
        }
    }

    /**
     * Muestra el estado actual de ambos jugadores.
     */
    private void mostrarEstado() {
        System.out.println("\n ESTADO ACTUAL:");
        System.out.println(jugador1);
        System.out.println(jugador2);
    }

    /**
     * Guarda el resultado del combate en la base de datos (jugadores y personajes).
     */
    private void guardarResultado() {
        try (Connection conn = ConexionBD.getConnection()) {
            PersonajeDAO personajeDAO = new PersonajeDAO();
            JugadorDAO jugadorDAO = new JugadorDAO();

            // Buscar o crear jugadores en la BD
            Jugador j1 = jugadorDAO.buscarPorNombre(jugador1.getNombre());
            Jugador j2 = jugadorDAO.buscarPorNombre(jugador2.getNombre());

            if (j1 == null) {
                j1 = new Jugador(jugador1.getNombre());
                jugadorDAO.insertarJugador(j1);
            }
            if (j2 == null) {
                j2 = new Jugador(jugador2.getNombre());
                jugadorDAO.insertarJugador(j2);
            }

            // Determinar ganador
            Jugador ganador = jugador1.getVidaActual() > 0 ? j1 : j2;
            Jugador perdedor = ganador == j1 ? j2 : j1;

            System.out.println("\n ¡" + (ganador == j1 ? jugador1.getNombre() : jugador2.getNombre()) + " GANA LA PARTIDA!");

            // Guardar personajes asociados a los jugadores
            personajeDAO.guardar(jugador1, j1.getId());
            personajeDAO.guardar(jugador2, j2.getId());

            // Actualizar estadísticas
            ganador.setPartidasGanadas(ganador.getPartidasGanadas() + 1);
            perdedor.setPartidasPerdidas(perdedor.getPartidasPerdidas() + 1);

            jugadorDAO.actualizarEstadisticas(ganador);
            jugadorDAO.actualizarEstadisticas(perdedor);

            System.out.println("✅ Resultado guardado correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}