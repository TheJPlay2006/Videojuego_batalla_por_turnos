/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author Jairo Herrera Romero
 */
// modelos/Elfo.java
import entidades.Arma;
import entidades.Raza;
import java.util.Random;

public class Elfo extends Personaje {
    private boolean esAgua;

    public Elfo(String nombre, Arma arma) {
        super(nombre, arma.getTipo().equals("agua") ? 115 : 100, 10, 100, arma, new Raza(2, "Elfo", "Magia elemental"));
        this.esAgua = arma.getTipo().equals("agua");
    }

    @Override
    public int atacar(boolean enDistancia) {
        Random r = new Random();
        int base = r.nextInt(arma.getDanoMaximo() - arma.getDanoMinimo() + 1) + arma.getDanoMinimo();

        if (arma.getTipo().equals("fuego")) {
            return (int) (base * 1.10); // +10%
        } else if (arma.getTipo().equals("tierra")) {
            return (int) (base * 1.02); // +2%
        } else if (arma.getTipo().equals("aire") && enDistancia) {
            return r.nextInt(9) + 4; // 4-12
        }
        return base;
    }

    @Override
    public void sanar() {
        double porcentaje = esAgua ? 0.90 : 0.75; // 90% si es agua
        int vidaMax = esAgua ? 115 : 100;
        int danoRecibido = vidaMax - vidaActual;
        int curacion = (int) (danoRecibido * porcentaje);
        vidaActual += curacion;
        if (vidaActual > vidaMax) vidaActual = vidaMax;
        System.out.println(nombre + " lanzó sanación. Recuperó " + curacion + " puntos de vida.");
    }

    @Override
    public void recibirDaño(int daño) {
        vidaActual -= daño;
        if (vidaActual < 0) vidaActual = 0;
        System.out.println(nombre + " recibió " + daño + " de daño. Vida: " + vidaActual);
    }
}