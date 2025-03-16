package individual;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorAhorcadoIndividual {
    private static final int PUERTO = 5001;

    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            System.out.println("Servidor individual iniciado. Esperando conexiones...");

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress());

                ManejadorClienteIndividual manejador = new ManejadorClienteIndividual(cliente);
                new Thread(manejador).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}