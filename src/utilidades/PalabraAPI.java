package utilidades;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Obtiene una palabra aleatoria en español desde una API.
 */
public class PalabraAPI {

    /**
     * Obtiene una palabra aleatoria desde la API.
     *
     * @return Una palabra aleatoria en español.
     * @throws Exception Si hay un error al conectarse a la API.
     */
    public static String obtenerPalabra() throws Exception {
        URL url = new URL("https://random-word-api.herokuapp.com/word?lang=es");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
        String respuesta = in.readLine();
        in.close();

        // La API devuelve la palabra en formato JSON: ["palabra"]
        return respuesta.substring(2, respuesta.length() - 2); // Extrae la palabra del JSON
    }
}