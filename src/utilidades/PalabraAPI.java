package utilidades;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Obtiene una palabra aleatoria en español desde una API.
 */
public class PalabraAPI {

    // Palabras predeterminadas en caso de que la API no funcione
    private static final String[] PALABRAS_PREDETERMINADAS = {
            "maravilloso", "programacion", "ahorcado", "computadora", "desarrollo", "inteligencia"
    };

    /**
     * Obtiene una palabra aleatoria desde la API.
     *
     * @return Una palabra aleatoria en español.
     * @throws Exception Si hay un error al conectarse a la API.
     */
    public static String obtenerPalabra() throws Exception {

        try {

            URL url = new URL("https://random-word-api.herokuapp.com/word?lang=es");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.setConnectTimeout(2000); // timeout de 2 segundos para la conexión
            conexion.setReadTimeout(2000);    // Timeout de 2 segundos para la lectura

            BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            String respuesta = in.readLine();
            in.close();

            // La API devuelve la palabra en formato JSON: ["palabra"]
            return respuesta.substring(2, respuesta.length() - 2); // Extrae la palabra del JSON

        } catch (Exception e) {
            System.out.println("Error al obtener palabra de la API... Usando una de las palabras predeterminadas.");
            return PALABRAS_PREDETERMINADAS[new Random().nextInt(PALABRAS_PREDETERMINADAS.length)];
        }

    }
}
