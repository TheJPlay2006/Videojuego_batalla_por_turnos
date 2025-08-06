/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;
import juego.Juego;

/**
 *
 * @author jh599
 */

public class Main {
      public static void main(String[] args) {
        Juego juego = new Juego();
        try {
            juego.iniciar();
        } catch (Exception e) {
            System.out.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
