/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author Jairo Herrera Romero
 */
// modelos/Humano.java
import entidades.Arma;
import entidades.Raza;
import java.util.Random;

public class Humano extends Personaje {
    public Humano(String nombre, Arma arma) {
        super(nombre, 100, 10, 100, arma, new Raza(1, "Humano", "Usa armas de fuego"));
    }

    @Override
    public int atacar(boolean enDistancia) {
        Random r = new Random();
        int base = r.nextInt(arma.getDanoMaximo() - arma.getDanoMinimo() + 1) + arma.getDanoMinimo();

        if (arma.getNombre().equals("Escopeta")) {
            return (int) (base * 1.02); // +2%
        } else if (arma.getNombre().equals("Rifle francotirador")) {
            if (enDistancia) {
                return r.nextInt(6) + 5; // 5-10
            } else {
                return base; // 1-5
            }
        }
        return base;
    }

    @Override
    public void sanar() {
        int danoRecibido = 100 - vidaActual;
        int curacion = (int) (danoRecibido * 0.5); // 50%
        vidaActual += curacion;
        if (vidaActual > 100) vidaActual = 100;
        System.out.println(nombre + " comió. Recuperó " + curacion + " puntos de vida.");
    }

    @Override
    public void recibirDaño(int daño) {
        vidaActual -= daño;
        if (vidaActual < 0) vidaActual = 0;
        System.out.println(nombre + " recibió " + daño + " de daño. Vida: " + vidaActual);
    }
}