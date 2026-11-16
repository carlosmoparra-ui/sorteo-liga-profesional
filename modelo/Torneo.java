
package modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Torneo implements Serializable {

    private ArrayList<Equipo> equipos;
    private ArrayList<String> historial;
    private String campeon;

    public Torneo() {
        equipos = new ArrayList<>();
        historial = new ArrayList<>();
    }

    public ArrayList<Equipo> getEquipos() {
        return equipos;
    }

    public ArrayList<String> getHistorial() {
        return historial;
    }

    public String getCampeon() {
        return campeon;
    }

    public void setCampeon(String campeon) {
        this.campeon = campeon;
    }
}
