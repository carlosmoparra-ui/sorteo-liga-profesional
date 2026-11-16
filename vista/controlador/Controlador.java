
package controlador;

import modelo.Modelo;
import modelo.NumeroEquiposInvalidoException;
import modelo.SorteoInvalidoException;
import vista.Vista;
import java.io.IOException;
import java.util.ArrayList;

public class Controlador {

    private Modelo modelo;
    private Vista vista;

    public Controlador() {
        modelo = new Modelo();
        vista = new Vista();
    }

    public void iniciar() {

        vista.mostrarInicio();

        try {
            ArrayList<String> resultados =
                modelo.realizarSorteo();

            vista.mostrarResultados(resultados);

            modelo.guardar();

            vista.mostrarMensaje(
                "Torneo guardado correctamente."
            );

            modelo.cargar();

            vista.mostrarMensaje(
                "Torneo cargado correctamente."
            );

        } catch (
            NumeroEquiposInvalidoException |
            SorteoInvalidoException |
            IOException e
        ) {
            vista.mostrarMensaje(
                "Error: " + e.getMessage()
            );

        } catch (ClassNotFoundException e) {
            vista.mostrarMensaje(
                "Error al cargar el torneo."
            );
        }
    }
}
