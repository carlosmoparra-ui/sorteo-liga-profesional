
package modelo;

import java.util.*;
import java.io.*;

public class Modelo {

    private Torneo torneo;

    public Modelo() {
        torneo = new Torneo();

        String[] nombres = {
            "Barcelona", "Emelec", "Liga de Quito",
            "Independiente", "Aucas", "Deportivo Cuenca",
            "El Nacional", "Delfin", "Macara",
            "Mushuc Runa", "Orense", "Universidad Catolica",
            "Tecnico Universitario", "Libertad",
            "Cumbaya", "Imbabura"
        };

        for (String nombre : nombres) {
            torneo.getEquipos().add(new Equipo(nombre));
        }
    }

    // Metodo recursivo para realizar los emparejamientos
    public ArrayList<Partido> sorteoRecursivo(
            ArrayList<Equipo> equipos, int posicion, String etapa)
            throws SorteoInvalidoException {

        ArrayList<Partido> partidos = new ArrayList<>();

        if (posicion >= equipos.size()) {
            return partidos;
        }

        if (posicion + 1 >= equipos.size()) {
            throw new SorteoInvalidoException(
                "No se pueden realizar los emparejamientos");
        }

        Partido partido = new Partido(
            etapa, equipos.get(posicion), equipos.get(posicion + 1));

        partidos.add(partido);

        partidos.addAll(
            sorteoRecursivo(equipos, posicion + 2, etapa));

        return partidos;
    }

    public ArrayList<String> realizarSorteo()
            throws NumeroEquiposInvalidoException,
                   SorteoInvalidoException {

        ArrayList<Equipo> participantes =
            new ArrayList<>(torneo.getEquipos());

        if (participantes.size() != 16) {
            throw new NumeroEquiposInvalidoException(
                "El torneo necesita exactamente 16 equipos");
        }

        ArrayList<String> resultados = new ArrayList<>();

        String[] etapas = {
            "Octavos de final",
            "Cuartos de final",
            "Semifinal",
            "Final"
        };

        Collections.shuffle(participantes);

        for (String etapa : etapas) {

            ArrayList<Partido> partidos =
                sorteoRecursivo(participantes, 0, etapa);

            ArrayList<Equipo> ganadores = new ArrayList<>();

            for (Partido partido : partidos) {

                Equipo ganador;

                if (Math.random() < 0.5) {
                    ganador = partido.getEquipo1();
                } else {
                    ganador = partido.getEquipo2();
                }

                partido.setGanador(ganador);

                resultados.add(
                    etapa + ": " +
                    partido.getEquipo1().getNombre() +
                    " vs " +
                    partido.getEquipo2().getNombre() +
                    " | Ganador: " + ganador.getNombre()
                );

                ganadores.add(ganador);
            }

            participantes = ganadores;
        }

        torneo.setCampeon(participantes.get(0).getNombre());

        resultados.add("CAMPEON: " + torneo.getCampeon());

        torneo.getHistorial().addAll(resultados);

        return resultados;
    }

    // Guardar torneo mediante serializacion
    public void guardar() throws IOException {

        ObjectOutputStream salida = new ObjectOutputStream(
            new FileOutputStream("torneo.ser"));

        salida.writeObject(torneo);
        salida.close();
    }

    // Cargar torneo mediante deserializacion
    public void cargar()
            throws IOException, ClassNotFoundException {

        ObjectInputStream entrada = new ObjectInputStream(
            new FileInputStream("torneo.ser"));

        torneo = (Torneo) entrada.readObject();
        entrada.close();
    }
}
