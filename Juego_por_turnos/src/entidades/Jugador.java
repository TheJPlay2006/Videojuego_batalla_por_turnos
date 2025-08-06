/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

/**
 *
 * @author Jairo Herrera Romero
 */
// entities/Jugador.java
public class Jugador {
    private int id;
    private String nombre;
    private int partidasGanadas;
    private int partidasPerdidas;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.partidasGanadas = 0;
        this.partidasPerdidas = 0;
    }

    public Jugador(int id, String nombre, int partidasGanadas, int partidasPerdidas) {
        this.id = id;
        this.nombre = nombre;
        this.partidasGanadas = partidasGanadas;
        this.partidasPerdidas = partidasPerdidas;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getPartidasGanadas() { return partidasGanadas; }
    public void setPartidasGanadas(int partidasGanadas) { this.partidasGanadas = partidasGanadas; }

    public int getPartidasPerdidas() { return partidasPerdidas; }
    public void setPartidasPerdidas(int partidasPerdidas) { this.partidasPerdidas = partidasPerdidas; }

    @Override
    public String toString() {
        return "Jugador{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", ganadas=" + partidasGanadas +
                ", perdidas=" + partidasPerdidas +
                '}';
    }
}