package modelos;

import entidades.Arma;
import entidades.Raza;
import java.util.Random;

/**
 *
 * @author Jairo Herrera Romero
 */

public class Elfo extends Personaje {
    private boolean esAgua;

    public Elfo(String nombre, Arma arma) {
        super(nombre, arma.getNombre().equals("Báculo Agua") ? 115 : 100, 10, 100, arma, new Raza(2, "Elfo", "Magia elemental"));
        this.esAgua = "Báculo Agua".equals(arma.getNombre());
    }

    @Override
    public int atacar(boolean enDistancia) {
        Random r = new Random();
        int base = r.nextInt(arma.getDanoMaximo() - arma.getDanoMinimo() + 1) + arma.getDanoMinimo();

        if ("Báculo Fuego".equals(arma.getNombre())) {
            return (int) (base * 1.10);
        } else if ("Báculo Tierra".equals(arma.getNombre())) {
            return (int) (base * 1.02);
        } else if ("Báculo Aire".equals(arma.getNombre())) {
            if (enDistancia) {
                return r.nextInt(9) + 4; 
            } else {
                return base; 
            }
        }
        return base;
    }

    @Override
    public void sanar() {
        double porcentaje = esAgua ? 0.90 : 0.75;
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