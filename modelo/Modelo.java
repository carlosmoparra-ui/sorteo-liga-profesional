
package modelo;

import java.util.*;
import java.io.*;

public class Modelo {

    private Torneo torneo;
    private Random random = new Random();

    public Modelo() {
        torneo = new Torneo();

        String[] nombres = {
            "Barcelona", "Emelec", "Liga de Quito", "Aucas",
            "Deportivo Cuenca", "Independiente", "Macara", "Delfin",
            "Orense", "Mushuc Runa", "El Nacional",
            "Universidad Catolica", "Libertad", "Manta",
            "Tecnico Universitario", "Vinotinto"
        };

        for (String nombre : nombres) {
            torneo.getEquipos().add(new Equipo(nombre));
        }
    }

    // Metodo recursivo para emparejar equipos
    private ArrayList<Partido> sorteoRecursivo(
        ArrayList<Equipo> equipos, int posicion, String etapa
    ) throws SorteoInvalidoException {

        ArrayList<Partido> partidos = new ArrayList<>();

        if (posicion == equipos.size()) {
            return partidos;
        }

        if (posicion + 1 >= equipos.size()) {
            throw new SorteoInvalidoException(
                "No se pudo completar el sorteo."
            );
        }

        Partido partido = new Partido(
            etapa,
            equipos.get(posicion),
            equipos.get(posicion + 1)
        );

        partidos.add(partido);

        partidos.addAll(
            sorteoRecursivo(equipos, posicion + 2, etapa)
        );

        return partidos;
    }

    // Ejecutar todas las etapas del torneo
    public ArrayList<String> realizarSorteo()
        throws NumeroEquiposInvalidoException,
               SorteoInvalidoException {

        ArrayList<String> resultados = new ArrayList<>();

        ArrayList<Equipo> participantes =
            new ArrayList<>(torneo.getEquipos());

        if (participantes.size() != 16) {
            throw new NumeroEquiposInvalidoException(
                "El torneo necesita 16 equipos."
            );
        }

        String[] etapas = {
            "OCTAVOS DE FINAL",
            "CUARTOS DE FINAL",
            "SEMIFINAL",
            "FINAL"
        };

        for (String etapa : etapas) {

            if (participantes.size() < 2) {
                break;
            }

            Collections.shuffle(participantes);

            resultados.add("\n===== " + etapa + " =====");

            ArrayList<Partido> partidos =
                sorteoRecursivo(participantes, 0, etapa);

            ArrayList<Equipo> ganadores = new ArrayList<>();

            for (Partido p : partidos) {

                Equipo ganador = random.nextBoolean()
                    ? p.getEquipo1() : p.getEquipo2();

                p.setGanador(ganador);

                String resultado =
                    p.getEquipo1().getNombre() + " vs " +
                    p.getEquipo2().getNombre() +
                    " | Ganador: " + ganador.getNombre();

                resultados.add(resultado);
                torneo.getHistorial().add(resultado);

                ganadores.add(ganador);
            }

            participantes = ganadores;
        }

        torneo.setCampeon(participantes.get(0).getNombre());

        resultados.add("\nCAMPEON: " + torneo.getCampeon());

        return resultados;
    }

    // Guardar torneo
    public void guardar() throws IOException {

        ObjectOutputStream salida =
            new ObjectOutputStream(
                new FileOutputStream("torneo.ser")
            );

        salida.writeObject(torneo);
        salida.close();
    }

    // Cargar torneo
    public void cargar()
        throws IOException, ClassNotFoundException {

        ObjectInputStream entrada =
            new ObjectInputStream(
                new FileInputStream("torneo.ser")
            );

        torneo = (Torneo) entrada.readObject();
        entrada.close();
    }
}
