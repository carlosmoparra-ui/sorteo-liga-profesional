
package vista;

import java.util.ArrayList;

public class Vista {

    public void mostrarInicio() {
        System.out.println("==============================");
        System.out.println("   SORTEO DE LIGA PROFESIONAL");
        System.out.println("==============================");
        System.out.println("Participantes: 16 equipos");
    }

    public void mostrarResultados(ArrayList<String> resultados) {
        for (String resultado : resultados) {
            System.out.println(resultado);
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}
