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

            // Recibe el nombre del cliente
            out.println("Bienvenido al Ahorcado Individual. Introduce tu nombre:");
            String nombre = in.readLine();
            if (nombre == null) return;
            System.out.println("Jugador conectado: " + nombre);

            // obtiene una palabra aleatoria
            String palabra = utilidades.PalabraAPI.obtenerPalabra();
            StringBuilder palabraOculta = new StringBuilder(ocultarPalabra(palabra));
            boolean partidaTerminada = false;
            int intentosRestantes = 7; // Contador de intentos

            out.println("¡Partida individual iniciada!");
            out.println("Adivina la palabra: " + palabraOculta);
            out.println("Tienes " + intentosRestantes + " intentos restantes.");

            // Bucle principal del juego
            while (!partidaTerminada && intentosRestantes > 0) {
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
                        intentosRestantes--; // Reducir intentos restantes
                        out.println("Incorrecto. La letra " + intento + " no está en la palabra.");
                        out.println("Te quedan " + intentosRestantes + " intentos.");
                    }
                } else {
                    if (intento.equalsIgnoreCase(palabra)) {
                        out.println("¡Felicidades! Has adivinado la palabra: " + palabra);
                        partidaTerminada = true;
                    } else {
                        intentosRestantes--; // Reducir intentos restantes
                        out.println("Incorrecto. La palabra no es " + intento + ".");
                        out.println("Te quedan " + intentosRestantes + " intentos.");
                    }
                }

                if (palabraOculta.indexOf("_") == -1) {
                    out.println("¡Felicidades! Has adivinado la palabra: " + palabra);
                    partidaTerminada = true;
                }

                if (intentosRestantes == 0) {
                    out.println("¡Oh no! Te has quedado sin intentos. La palabra era: " + palabra);
                    partidaTerminada = true;
                }

                out.println("Palabra actual: " + palabraOculta);
            }

            // Notifica que la partida ha terminado
            out.println("La partida ha terminado. Gracias por jugar. ¡Hasta la próxima!");
            out.println("FIN"); // Mensaje especial para indicar el fin del programa
            cliente.close(); // Cerrar la conexión con el cliente

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private String ocultarPalabra(String palabra) {
        return palabra.replaceAll(".", "_ ");
    }
}
