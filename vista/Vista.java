
package vista;

import java.util.ArrayList;

public class Vista {

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarResultados(ArrayList<String> resultados) {
        System.out.println("\n===== RESULTADOS DEL TORNEO =====");

        for (String resultado : resultados) {
            System.out.println(resultado);
        }
    }

    public void mostrarError(String error) {
        System.out.println("ERROR: " + error);
    }
}
