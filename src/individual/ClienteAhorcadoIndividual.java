package individual;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClienteAhorcadoIndividual {
    public static void main(String[] args) {
        final String SERVIDOR = "localhost";
        final int PUERTO = 5001;

        try (Socket socket = new Socket(SERVIDOR, PUERTO)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner sc = new Scanner(System.in);

            // Recibir mensajes del servidor
            new Thread(() -> {
                try {
                    String mensajeServidor;
                    while ((mensajeServidor = in.readLine()) != null) {
                        System.out.println(mensajeServidor);

                        // Si el mensaje es "FIN", cerrar el programa
                        if (mensajeServidor.equals("FIN")) {
                            System.out.println("Cerrando el programa...");
                            System.exit(0); // Cerrar el programa
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Leer entrada del usuario y enviarla al servidor
            while (true) {
                String entradaUsuario = sc.nextLine();
                out.println(entradaUsuario);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}