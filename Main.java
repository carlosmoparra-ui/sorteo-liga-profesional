
import java.util.*;
import java.io.*;

// MODELO
class Equipo implements Serializable {
    String nombre;

    public Equipo(String nombre) {
        this.nombre = nombre;
    }
}

class Partido implements Serializable {
    String etapa;
    Equipo equipo1, equipo2, ganador;

    public Partido(String etapa, Equipo e1, Equipo e2) {
        this.etapa = etapa;
        this.equipo1 = e1;
        this.equipo2 = e2;
    }
}

class Torneo implements Serializable {
    ArrayList<Equipo> equipos = new ArrayList<>();
    ArrayList<String> historial = new ArrayList<>();
    String campeon;
}

class NumeroEquiposInvalidoException extends Exception {
    public NumeroEquiposInvalidoException(String mensaje) {
        super(mensaje);
    }
}

class SorteoInvalidoException extends Exception {
    public SorteoInvalidoException(String mensaje) {
        super(mensaje);
    }
}

class Modelo {
    Torneo torneo = new Torneo();
    Random random = new Random();

    public Modelo() {
        String[] nombres = {
            "Barcelona", "Emelec", "Liga de Quito", "Aucas",
            "Deportivo Cuenca", "Independiente", "Macara", "Delfin",
            "Orense", "Mushuc Runa", "El Nacional", "Universidad Catolica",
            "Libertad", "Manta", "Tecnico Universitario", "Vinotinto"
        };

        for (String nombre : nombres) {
            torneo.equipos.add(new Equipo(nombre));
        }
    }

    public ArrayList<Partido> sorteoRecursivo(
        ArrayList<Equipo> equipos, int posicion, String etapa
    ) throws SorteoInvalidoException {

        ArrayList<Partido> partidos = new ArrayList<>();

        if (posicion >= equipos.size()) {
            return partidos;
        }

        if (posicion + 1 >= equipos.size()) {
            throw new SorteoInvalidoException(
                "No se pudo completar el emparejamiento."
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

    public void realizarSorteo()
        throws NumeroEquiposInvalidoException,
               SorteoInvalidoException {

        ArrayList<Equipo> participantes =
            new ArrayList<>(torneo.equipos);

        String[] etapas = {
            "OCTAVOS DE FINAL",
            "CUARTOS DE FINAL",
            "SEMIFINAL",
            "FINAL"
        };

        if (participantes.size() != 16) {
            throw new NumeroEquiposInvalidoException(
                "El torneo debe comenzar con 16 equipos."
            );
        }

        for (String etapa : etapas) {
            if (participantes.size() < 2) break;

            if ((participantes.size() &
                (participantes.size() - 1)) != 0) {
                throw new NumeroEquiposInvalidoException(
                    "Cantidad de equipos invalida."
                );
            }

            Collections.shuffle(participantes);

            ArrayList<Partido> partidos =
                sorteoRecursivo(participantes, 0, etapa);

            ArrayList<Equipo> ganadores = new ArrayList<>();

            System.out.println("\n========== " + etapa + " ==========");

            for (Partido p : partidos) {
                p.ganador = random.nextBoolean()
                    ? p.equipo1 : p.equipo2;

                System.out.println(
                    p.equipo1.nombre + " vs " +
                    p.equipo2.nombre
                );

                System.out.println("Ganador: " + p.ganador.nombre);

                torneo.historial.add(
                    etapa + ": " + p.equipo1.nombre +
                    " vs " + p.equipo2.nombre +
                    " | Ganador: " + p.ganador.nombre
                );

                ganadores.add(p.ganador);
            }

            participantes = ganadores;
        }

        torneo.campeon = participantes.get(0).nombre;
        System.out.println("\nCAMPEON DEL TORNEO: " + torneo.campeon);
    }

    public void guardar() throws IOException {
        ObjectOutputStream salida = new ObjectOutputStream(
            new FileOutputStream("torneo.ser")
        );

        salida.writeObject(torneo);
        salida.close();
    }

    public void cargar() throws IOException, ClassNotFoundException {
        ObjectInputStream entrada = new ObjectInputStream(
            new FileInputStream("torneo.ser")
        );

        torneo = (Torneo) entrada.readObject();
        entrada.close();
    }
}

// VISTA
class Vista {
    public void mostrarInicio() {
        System.out.println("================================");
        System.out.println("     SORTEO DE LIGA PROFESIONAL");
        System.out.println("================================");
        System.out.println("Participantes: 16 equipos");
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}

// CONTROLADOR
class Controlador {
    Modelo modelo = new Modelo();
    Vista vista = new Vista();

    public void iniciar() {
        vista.mostrarInicio();

        try {
            modelo.realizarSorteo();
            modelo.guardar();
            vista.mostrarMensaje(
                "\nTorneo guardado correctamente en torneo.ser"
            );

        } catch (NumeroEquiposInvalidoException |
                 SorteoInvalidoException |
                 IOException e) {

            vista.mostrarMensaje("Error: " + e.getMessage());
        }

        try {
            modelo.cargar();
            vista.mostrarMensaje(
                "Torneo cargado correctamente."
            );

        } catch (IOException | ClassNotFoundException e) {
            vista.mostrarMensaje(
                "Error al cargar: " + e.getMessage()
            );
        }
    }
}

// PROGRAMA PRINCIPAL
public class Main {
    public static void main(String[] args) {
        Controlador controlador = new Controlador();
        controlador.iniciar();
    }
}
