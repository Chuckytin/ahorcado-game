# Juego del Ahorcado - Multijugador e Individual

Este proyecto es una implementación con dos modos de juego: **multijugador** e **individual**. 
El servidor maneja la lógica del juego, mientras que los clientes se conectan para jugar.

## Estructura del Proyecto

El proyecto está organizado en las siguientes carpetas:

- **individual**: Contiene el código para el modo de juego individual.
- **multijugador**: Contiene el código para el modo de juego multijugador.
- **utilidades**: Contiene la clase `PalabraAPI`, que obtiene una palabra aleatoria desde una API.
- **out**: Contiene los archivos compilados (`.class`).

## Requisitos

- Java JDK 8 o superior.
- Conexión a Internet (para obtener palabras aleatorias desde la API).

## Cómo Ejecutar el Proyecto

### 1. Compilar el Proyecto

Abre una terminal en la raíz del proyecto y ejecuta el siguiente comando para compilar todo el código:

```bash
javac -d out src/individual/*.java src/multijugador/*.java src/utilidades/*.java
```

Esto generará los archivos (`.class`) en la carpeta (`.out`).

### 2. Ejecutar el Servidor Multijugador o Individual

Para ejecutar el servidor multijugador o individual, abre una terminal y navega a la carpeta out. Luego, ejecuta:

```bash
java -cp out multijugador.ServidorAhorcadoMultijugador
```

```bash
java -cp out individual.ServidorAhorcadoIndividual
```

También puedes ejecutar el Servidor desde tu **Entorno de Desarrollo**.

### 3. Ejecutar el Cliente Multijugador o Individual

Para jugar en modo multijugador, abre dos terminales adicionales (una para cada jugador) o una solo si vas a jugar en modo individual:

```bash
java -cp out multijugador.ClienteAhorcadoMultijugador
```

```bash
java -cp out individual.ClienteAhorcadoIndividual
```

## Cómo Jugar

### Modo Multijugador

1. El servidor espera a que se conecten dos jugadores.

2. Una vez que ambos jugadores están conectados, el servidor elige una palabra aleatoria.

3. Los jugadores toman turnos para adivinar letras o la palabra completa.

4. El juego termina cuando un jugador adivina la palabra o se quedan sin intentos.

5. El servidor notifica a los jugadores quién ganó y cierra la conexión.

### Modo Individual

1. El servidor elige una palabra aleatoria.

2. El jugador intenta adivinar letras o la palabra completa.

3. El juego termina cuando el jugador adivina la palabra o se queda sin intentos.

4. El servidor notifica al jugador el resultado y cierra la conexión.
