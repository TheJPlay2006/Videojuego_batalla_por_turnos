package juego;

import datos.ConexionBD;
import datos.JugadorDAO;
import datos.PartidaDAO;
import datos.PersonajeDAO;
import entidades.Arma;
import entidades.Jugador;
import entidades.Raza;
import modelos.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author Emesis
 * 
 * Clase principal del videojuego de combate por turnos.
 * Gestiona la lógica general del juego: carga de datos, creación de personajes,
 * desarrollo del combate, y almacenamiento de resultados.
 */
public class Juego {
    private Scanner sc = new Scanner(System.in);
    private List<Raza> razas;
    private List<Arma> armas;
    private Personaje jugador1;
    private Personaje jugador2;
    private int distancia = 5;
    private PartidaDAO partidaDAO = new PartidaDAO();

    public void iniciar() {
        try {
            System.out.println("🔥 BIENVENIDO AL VIDEOJUEGO DE COMBATE POR TURNOS 🔥");
            cargarDatos();
            crearJugadores();
            combate();
            guardarResultado();
        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarDatos() throws SQLException {
        String sqlRaza = "SELECT id, nombre, descripcion FROM raza";
        String sqlArma = "SELECT id, nombre, tipo, dano_minimo, dano_maximo, modificadores FROM arma";

        try (Connection conn = ConexionBD.getConnection();
             Statement stmt = conn.createStatement()) {

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

    private void crearJugadores() {
        System.out.print("🎮 Jugador 1 - Nombre: ");
        String nombre1 = sc.nextLine().trim();
        jugador1 = crearPersonaje(nombre1, 1);

        System.out.print("🎮 Jugador 2 - Nombre: ");
        String nombre2 = sc.nextLine().trim();
        jugador2 = crearPersonaje(nombre2, 2);
    }

    private Personaje crearPersonaje(String nombre, int jugadorId) {
        Raza raza = null;
        do {
            System.out.println("\n" + nombre + ", elige una raza:");
            System.out.println("1. Humano  2. Elfo  3. Orco  4. Bestia");
            if (!sc.hasNextInt()) {
                System.out.println("⚠️  Ingresa un número válido.");
                sc.next();
                continue;
            }
            int razaId = sc.nextInt();
            sc.nextLine(); // limpiar buffer
            raza = obtenerRaza(razaId);
            if (raza == null) {
                System.out.println("❌ Raza inválida. Intenta de nuevo.");
            }
        } while (raza == null);

        Arma arma = seleccionarArma(raza);
        int vidaInicial = calcularVidaInicial(raza, arma);

        switch (raza.getNombre()) {
            case "Humano":
                return new Humano(nombre, arma);
            case "Elfo":
                return new Elfo(nombre, arma);
            case "Orco":
                return new Orco(nombre, arma, raza);
            case "Bestia":
                return new Bestia(nombre, arma, raza);
            default:
                return new Orco(nombre, arma, raza);
        }
    }

    private Raza obtenerRaza(int id) {
        return razas.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
    }

    private Arma seleccionarArma(Raza raza) {
        List<Arma> armasValidas = filtrarArmasPorRaza(raza);
        if (armasValidas.isEmpty()) {
            System.out.println("❌ No hay armas disponibles para esta raza.");
            return armas.get(0);
        }

        Arma arma = null;
        do {
            System.out.println("⚔️ Elige tu arma:");
            for (int i = 0; i < armasValidas.size(); i++) {
                System.out.println((i + 1) + ". " + armasValidas.get(i).getNombre() + 
                                 " (" + armasValidas.get(i).getTipo() + ")");
            }

            if (!sc.hasNextInt()) {
                System.out.println("⚠️  Ingresa un número válido.");
                sc.next();
                continue;
            }
            int opcion = sc.nextInt();
            sc.nextLine();
            if (opcion < 1 || opcion > armasValidas.size()) {
                System.out.println("❌ Opción inválida.");
                continue;
            }
            arma = armasValidas.get(opcion - 1);
        } while (arma == null);

        return arma;
    }

    private List<Arma> filtrarArmasPorRaza(Raza raza) {
        return armas.stream()
                .filter(arma -> {
                    switch (raza.getNombre()) {
                        case "Humano":
                            return Arrays.asList("Escopeta", "Rifle francotirador").contains(arma.getNombre());
                        case "Elfo":
                            return arma.getNombre().startsWith("Báculo");
                        case "Orco":
                            return Arrays.asList("Hacha", "Martillo").contains(arma.getNombre());
                        case "Bestia":
                            return Arrays.asList("Puños", "Espada").contains(arma.getNombre());
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }

    private int calcularVidaInicial(Raza raza, Arma arma) {
        if ("Elfo".equals(raza.getNombre()) && "Báculo Agua".equals(arma.getNombre())) {
            return 115;
        }
        return 100;
    }

    private void combate() {
        System.out.println("\n⚔️ ¡COMIENZA EL DUELO! ⚔️");
        int turno = 1;

        while (jugador1.getVidaActual() > 0 && jugador2.getVidaActual() > 0) {
            System.out.println("\n--- Turno " + (turno++) + " ---");
            mostrarEstado();

            // Turno Jugador 1
            if (jugador1.getVidaActual() > 0) {
                System.out.println("👉 Turno de " + jugador1.getNombre());
                gestionarDistancia();
                if (distancia <= 1) {
                    realizarAccion(jugador1, jugador2);
                } else {
                    System.out.println("🚫 " + jugador1.getNombre() + " está demasiado lejos para atacar.");
                }
                if (jugador1 instanceof Orco) ((Orco) jugador1).aplicarSangrado();
            }

            if (jugador2.getVidaActual() <= 0) break;

            // Turno Jugador 2
            if (jugador2.getVidaActual() > 0) {
                System.out.println("👉 Turno de " + jugador2.getNombre());
                gestionarDistancia();
                if (distancia <= 1) {
                    realizarAccion(jugador2, jugador1);
                } else {
                    System.out.println("🚫 " + jugador2.getNombre() + " está demasiado lejos para atacar.");
                }
                if (jugador2 instanceof Orco) ((Orco) jugador2).aplicarSangrado();
            }
        }
    }

    private void gestionarDistancia() {
        System.out.println("📏 Distancia actual: " + distancia + "m");
        System.out.println("1. Avanzar  2. Retroceder  3. Mantener");
        int opcion = 3;
        if (sc.hasNextInt()) {
            opcion = sc.nextInt();
            sc.nextLine();
        } else {
            sc.nextLine();
        }

        switch (opcion) {
            case 1:
                distancia = Math.max(0, distancia - 1);
                System.out.println("🚶 Avanzando... distancia: " + distancia + "m");
                break;
            case 2:
                distancia++;
                System.out.println("🏃 Retrocediendo... distancia: " + distancia + "m");
                break;
            default:
                System.out.println("⏸️  Manteniendo distancia.");
                break;
        }
    }

    private void realizarAccion(Personaje atacante, Personaje defensor) {
        System.out.println("\n" + atacante.getNombre() + ", elige acción:");
        System.out.println("1. Atacar  2. Sanar  3. Defender");
        int opcion = 3;
        if (sc.hasNextInt()) {
            opcion = sc.nextInt();
            sc.nextLine();
        } else {
            sc.nextLine();
        }

        switch (opcion) {
            case 1:
                boolean enDistancia = distancia > 1;
                int daño = atacante.atacar(enDistancia);
                if (daño > 0) {
                    defensor.recibirDaño(daño);
                }
                break;
            case 2:
                atacante.sanar();
                break;
            case 3:
                System.out.println("🛡️ " + atacante.getNombre() + " se defiende (efecto no implementado).");
                break;
            default:
                System.out.println("❌ Opción inválida. Pierdes el turno.");
                break;
        }
    }

    private void mostrarEstado() {
        System.out.println("\n📊 ESTADO ACTUAL:");
        System.out.println(jugador1);
        System.out.println(jugador2);
    }

    private void guardarResultado() {
        try (Connection conn = ConexionBD.getConnection()) {
            JugadorDAO jugadorDAO = new JugadorDAO();
            PersonajeDAO personajeDAO = new PersonajeDAO();

            Jugador j1 = jugadorDAO.buscarPorNombre(jugador1.getNombre());
            if (j1 == null) {
                j1 = new Jugador(jugador1.getNombre());
                jugadorDAO.insertarJugador(j1);
            }

            Jugador j2 = jugadorDAO.buscarPorNombre(jugador2.getNombre());
            if (j2 == null) {
                j2 = new Jugador(jugador2.getNombre());
                jugadorDAO.insertarJugador(j2);
            }

            Jugador ganador = jugador1.getVidaActual() > 0 ? j1 : j2;
            Jugador perdedor = ganador == j1 ? j2 : j1;

            System.out.println("\n🏆 ¡" + (ganador == j1 ? jugador1.getNombre() : jugador2.getNombre()) + " GANA LA PARTIDA! 🏆");

            personajeDAO.guardar(jugador1, j1.getId());
            personajeDAO.guardar(jugador2, j2.getId());

            ganador.setPartidasGanadas(ganador.getPartidasGanadas() + 1);
            perdedor.setPartidasPerdidas(perdedor.getPartidasPerdidas() + 1);

            jugadorDAO.actualizarEstadisticas(ganador);
            jugadorDAO.actualizarEstadisticas(perdedor);

            partidaDAO.registrarPartida(ganador.getId(), perdedor.getId(), "Vida 0");

            System.out.println("✅ Resultado guardado en la base de datos.");
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar el resultado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}