/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import entidades.Arma;
import entidades.Raza;

/**
 *
 * @author Jairo Herrera Romero
 */
// modelos/Personaje.java
public abstract class Personaje {
    protected String nombre;
    protected int vidaActual;
    protected int fuerza;
    protected int energia;
    protected Arma arma;
    protected Raza raza;

    public Personaje(String nombre, int vidaActual, int fuerza, int energia, Arma arma, Raza raza) {
        this.nombre = nombre;
        this.vidaActual = vidaActual;
        this.fuerza = fuerza;
        this.energia = energia;
        this.arma = arma;
        this.raza = raza;
    }

    public abstract int atacar(boolean enDistancia);
    public abstract void sanar();
    public abstract void recibirDaño(int daño);

    // Getters
    public String getNombre() { return nombre; }
    public int getVidaActual() { return vidaActual; }
    public int getFuerza() { return fuerza; }
    public int getEnergia() { return energia; }
    public Arma getArma() { return arma; }
    public Raza getRaza() { return raza; }

    public void setVidaActual(int vidaActual) { this.vidaActual = vidaActual; }
    public void setEnergia(int energia) { this.energia = energia; }

    @Override
    public String toString() {
        return nombre + " (" + raza.getNombre() + ") - Vida: " + vidaActual + ", Arma: " + arma.getNombre();
    }
}
