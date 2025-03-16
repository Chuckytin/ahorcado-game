package individual;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ManejadorClienteIndividual implements Runnable {
    private Socket cliente;
    private PrintWriter out;
    private BufferedReader in;

    public ManejadorClienteIndividual(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            out = new PrintWriter(cliente.getOutputStream(), true);

            // Recibir nombre del cliente
            out.println("Bienvenido al Ahorcado Individual. Introduce tu nombre:");
            String nombre = in.readLine();
            if (nombre == null) return;
            System.out.println("Jugador conectado: " + nombre);

            // Obtener una palabra aleatoria
            String palabra = utilidades.PalabraAPI.obtenerPalabra();
            StringBuilder palabraOculta = new StringBuilder(ocultarPalabra(palabra));
            boolean partidaTerminada = false;

            out.println("¡Partida individual iniciada!");
            out.println("Adivina la palabra: " + palabraOculta);

            // Bucle principal del juego
            while (!partidaTerminada) {
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

                out.println("Palabra actual: " + palabraOculta);
            }

            // Notificar que la partida ha terminado
            out.println("La partida ha terminado. Gracias por jugar. ¡Hasta la próxima!");
            out.println("FIN"); // Mensaje especial para indicar el fin del programa
            cliente.close(); // Cerrar la conexión con el cliente

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String ocultarPalabra(String palabra) {
        return palabra.replaceAll(".", "_ ");
    }
}