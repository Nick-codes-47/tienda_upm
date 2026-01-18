package es.upm.etsisi.poo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Clase de utilidad para la gestión de logs de la aplicación.
 * <p>
 * Actúa como un envoltorio (Wrapper) estático sobre la librería Log4j,
 * proporcionando un punto centralizado para registrar mensajes con el nombre "APP".
 * Al ser una clase de utilidad y final, no debe ser instanciada.
 */
public final class AppLogger {

    // Logger de Log4j configurado con el nombre "APP"
    private static final Logger APP_LOGGER =
            LogManager.getLogger("APP");

    /**
     * Constructor privado para evitar la instanciación de la clase.
     */
    private AppLogger() {}

    /**
     * Registra un mensaje informativo (INFO level).
     *
     * @param message El mensaje a registrar.
     */
    public static void info(String message) {
        APP_LOGGER.info(message);
    }

    /**
     * Registra un mensaje de advertencia (WARN level).
     *
     * @param message El mensaje de advertencia a registrar.
     */
    public static void warn(String message) {
        APP_LOGGER.warn(message);
    }

    /**
     * Registra un mensaje de error (ERROR level).
     * Utilizado comúnmente para excepciones o fallos críticos.
     *
     * @param message El mensaje de error a registrar.
     */
    public static void error(String message) {
        APP_LOGGER.error(message);
    }

    /**
     * Registra un mensaje de debug (DEBUG level).
     * Utilizado únicamente para la correcta depuración del código
     *
     * @param message El mensaje de error a registrar.
     */
    public static void debug(String message) { APP_LOGGER.debug(message); }
}
