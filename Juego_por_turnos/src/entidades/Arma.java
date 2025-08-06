/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

/**
 *
 * @author Jairo Herrera Romero
 */
// entidades/Arma.java
public class Arma {
    private int id;
    private String nombre;
    private String tipo;
    private int danoMinimo;
    private int danoMaximo;
    private String modificadores;

    public Arma(int id, String nombre, String tipo, int danoMinimo, int danoMaximo, String modificadores) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.danoMinimo = danoMinimo;
        this.danoMaximo = danoMaximo;
        this.modificadores = modificadores;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public int getDanoMinimo() { return danoMinimo; }
    public int getDanoMaximo() { return danoMaximo; }
    public String getModificadores() { return modificadores; }
}