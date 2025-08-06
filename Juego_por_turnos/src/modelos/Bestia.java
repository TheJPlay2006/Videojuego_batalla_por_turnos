/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.util.Random;
import entidades.Arma;
import entidades.Raza;

/**
 *
 * @author Emesis
 */
public class Bestia extends Personaje{
    private Random rand = new Random();
    
    public Bestia(String nombre, Arma arma, Raza raza) {
        super(nombre, 100, 12, 80, arma, raza);
    }
    
    @Override
    public int atacar(boolean enDistancia) {
        if (arma.getNombre().equals("Puños")) {
            System.out.println(getNombre() + " ataca con puños por 25 puntos.");
            // El atacante pierde 10 de vida
            setVidaActual(getVidaActual() - 10);
            System.out.println(getNombre() + " se lastima: -10 vida.");
            return 25;
        } else if (arma.getNombre().equals("Espada")) {
            int daño = rand.nextInt(10) + 1; // 1-10
            System.out.println(getNombre() + " ataca con espada por " + daño + " puntos.");
            return daño;
        }
        return 0;
    }

    @Override
    public void sanar() {
        int curacion = (int) (getVidaActual() * 0.45); // 45%
        setVidaActual(getVidaActual() + curacion);
        System.out.println(getNombre() + " duerme y se recupera: +" + curacion + " vida.");
    }

    @Override
    public void recibirDaño(int daño) {
        setVidaActual(getVidaActual() - daño);
        System.out.println(getNombre() + " recibe " + daño + " de daño. Vida actual: " + getVidaActual());
    }
}

