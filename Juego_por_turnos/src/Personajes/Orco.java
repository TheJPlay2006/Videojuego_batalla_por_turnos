/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Personajes;

import java.util.Random;

/**
 *
 * @author Emesis
 */
public class Orco extends Personaje{
    private boolean sangrando = false;
    private int turnoSangrado = 0;
    private Random rand = new Random();
    
    public Orco(String nombre, Arma arma, Raza raza) {
        super(nombre, 100, 10, 100, arma, raza);
    }
    
   @Override
    public int atacar(boolean enDistancia) {
        if (arma.getNombre().equals("Hacha")) {
            int daño = rand.nextInt(5) + 1; // 1-5
            System.out.println(getNombre() + " ataca con hacha por " + daño + " puntos.");
            // Aplica sangrado
            sangrando = true;
            turnoSangrado = 2;
            System.out.println("¡El oponente está sangrando! -3 vida por 2 turnos.");
            return daño;
        } else if (arma.getNombre().equals("Martillo")) {
            int daño = rand.nextInt(5) + 1;
            System.out.println(getNombre() + " golpea con martillo por " + daño + " puntos.");
            return daño;
        }
        return 0;
    }

    @Override
    public void sanar() {
        int curacion = (int) (getVidaActual() * 0.25); // 25%
        setVidaActual(getVidaActual() + curacion);
        System.out.println(getNombre() + " se cura con poción: +" + curacion + " vida.");
        System.out.println("¡Efecto acumulado! +15% de curación en el próximo turno.");
    }

    @Override
    public void recibirDaño(int daño) {
        setVidaActual(getVidaActual() - daño);
        System.out.println(getNombre() + " recibe " + daño + " de daño. Vida actual: " + getVidaActual());
    }
    // Método para aplicar sangrado (lo llama el sistema de combate)
    public void aplicarSangrado() {
        if (sangrando && turnoSangrado > 0) {
            setVidaActual(getVidaActual() - 3);
            turnoSangrado--;
            System.out.println(getNombre() + " pierde 3 puntos por sangrado. Turnos restantes: " + turnoSangrado);
            if (turnoSangrado == 0) sangrando = false;
        }
    }
}
