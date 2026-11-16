
package controlador;

import modelo.*;
import vista.Vista;
import java.util.ArrayList;
import java.io.IOException;

public class Controlador {

    private Modelo modelo;
    private Vista vista;

    public Controlador() {
        modelo = new Modelo();
        vista = new Vista();
    }

    public void iniciar() {

        try {
            vista.mostrarMensaje("INICIANDO SORTEO DE LIGA PROFESIONAL");

            ArrayList<String> resultados = modelo.realizarSorteo();

            vista.mostrarResultados(resultados);

            modelo.guardar();

            vista.mostrarMensaje(
                "\nTorneo guardado correctamente en torneo.ser"
            );

            modelo.cargar();

            vista.mostrarMensaje(
                "Torneo cargado correctamente"
            );

        } catch (NumeroEquiposInvalidoException |
                 SorteoInvalidoException e) {

            vista.mostrarError(e.getMessage());

        } catch (IOException e) {

            vista.mostrarError(
                "Error al guardar o cargar el torneo: " + e.getMessage()
            );

        } catch (ClassNotFoundException e) {

            vista.mostrarError(
                "No se encontro la clase del torneo"
            );
        }
    }
}
