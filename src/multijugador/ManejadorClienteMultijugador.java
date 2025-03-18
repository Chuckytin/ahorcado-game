package multijugador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ManejadorClienteMultijugador implements Runnable {
    private Socket cliente;
    private String nombre;
    private PrintWriter out;
    private BufferedReader in;
    private static List<ManejadorClienteMultijugador> jugadoresEnEspera;
    private static String palabra;
    private static StringBuilder palabraOculta;
    private static int turno = 0;
    private static boolean partidaTerminada = false;

    public ManejadorClienteMultijugador(Socket cliente, List<ManejadorClienteMultijugador> jugadoresEnEspera) {
        this.cliente = cliente;
        this.jugadoresEnEspera = jugadoresEnEspera;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            out = new PrintWriter(cliente.getOutputStream(), true);

            // Recibe nombre del cliente
            out.println("Bienvenido al Ahorcado Multijugador. Introduce tu nombre:");
            nombre = in.readLine();
            if (nombre == null) return;
            System.out.println("Jugador conectado: " + nombre);

            // Añadir jugador a la cola de espera
            synchronized (jugadoresEnEspera) {
                jugadoresEnEspera.add(this);
                out.println("Esperando a que se unan más jugadores...");

                // Si hay menos de 2 jugadores se espera
                if (jugadoresEnEspera.size() < 2) {
                    out.println("Necesitamos al menos 2 jugadores para comenzar. Por favor, espera...");
                    jugadoresEnEspera.wait(); // Esperar a que se conecte otro jugador
                }

                // Cuando hay 2 jugadores se inicia la partida
                if (jugadoresEnEspera.size() >= 2) {
                    palabra = utilidades.PalabraAPI.obtenerPalabra();
                    palabraOculta = new StringBuilder(ocultarPalabra(palabra));
                    partidaTerminada = false;
                    jugadoresEnEspera.notifyAll(); // Notificar a todos los jugadores
                }
            }

            // Inicia partida multijugador
            iniciarPartidaMultijugador();

        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            try {
                cliente.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }

    private void iniciarPartidaMultijugador() {
        try {
            out.println("¡Partida multijugador iniciada!");

            while (!partidaTerminada) {
                synchronized (jugadoresEnEspera) {
                    if (jugadoresEnEspera.indexOf(this) == turno) {
                        out.println("Es tu turno, " + nombre + ". Adivina la palabra: " + palabraOculta);
                        out.println("Introduce una letra o la palabra completa:");

                        String intento = in.readLine();
                        if (intento == null || intento.isEmpty()) {
                            out.println("Entrada no válida. Introduce una letra o la palabra completa.");
                            continue;
                        }

                        if (intento.length() == 1) {
                            boolean acierto = false;
                            for (int i = 0; i < palabra.length(); i++) {
                                if (palabra.charAt(i) == intento.charAt(0)) {
                                    palabraOculta.setCharAt(i * 2, intento.charAt(0));
                                    acierto = true;
                                }
                            }

                            if (acierto) {
                                out.println("¡Correcto! La letra " + intento + " está en la palabra.");
                            } else {
                                out.println("Incorrecto. La letra " + intento + " no está en la palabra.");
                            }
                        } else {
                            if (intento.equalsIgnoreCase(palabra)) {
                                out.println("¡Felicidades! Has adivinado la palabra: " + palabra);
                                partidaTerminada = true;
                            } else {
                                out.println("Incorrecto. La palabra no es " + intento + ".");
                            }
                        }

                        if (palabraOculta.indexOf("_") == -1) {
                            out.println("¡Felicidades! Has adivinado la palabra: " + palabra);
                            partidaTerminada = true;
                        }

                        turno = (turno + 1) % jugadoresEnEspera.size();

                        if (!partidaTerminada) {
                            for (ManejadorClienteMultijugador jugador : jugadoresEnEspera) {
                                jugador.out.println("Palabra actual: " + palabraOculta);
                            }
                        }
                    } else {
                        out.println("Es el turno de " + jugadoresEnEspera.get(turno).nombre + ". Espera tu turno.");
                    }
                }

                Thread.sleep(1000);
            }

            // Notifica a ambos jugadores que la partida ha terminado
            synchronized (jugadoresEnEspera) {
                for (ManejadorClienteMultijugador jugador : jugadoresEnEspera) {
                    jugador.out.println("La partida ha terminado. El ganador es: " + nombre);
                    jugador.out.println("La palabra era: " + palabra);
                    jugador.out.println("Gracias por jugar. ¡Hasta la próxima!");
                    jugador.out.println("FIN"); // Mensaje especial para indicar el fin del programa
                    jugador.cliente.close(); // Cerrar la conexión con el cliente
                }
            }

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private String ocultarPalabra(String palabra) {
        return palabra.replaceAll(".", "_ ");
    }
}
