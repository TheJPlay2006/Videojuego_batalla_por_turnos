package modelos;

import entidades.Arma;
import entidades.Raza;
import java.util.Random;

/**
 *
 * @author Jairo Herrera Romero
 */

public class Humano extends Personaje {
    public Humano(String nombre, Arma arma) {
        super(nombre, 100, 10, 100, arma, new Raza(1, "Humano", "Usa armas de fuego"));
    }

    @Override
    public int atacar(boolean enDistancia) {
        Random r = new Random();
        int base = r.nextInt(arma.getDanoMaximo() - arma.getDanoMinimo() + 1) + arma.getDanoMinimo();

        if ("Escopeta".equals(arma.getNombre())) {
            return (int) (base * 1.02); // +2%
        } else if ("Rifle francotirador".equals(arma.getNombre())) {
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
        int curacion = (int) (danoRecibido * 0.5);
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