package multijugador;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorAhorcadoMultijugador {
    private static final int PUERTO = 5000;
    private static final List<ManejadorClienteMultijugador> jugadoresEnEspera = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            System.out.println("Servidor multijugador iniciado. Esperando conexiones...");

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress());

                ManejadorClienteMultijugador manejador = new ManejadorClienteMultijugador(cliente, jugadoresEnEspera);
                new Thread(manejador).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}