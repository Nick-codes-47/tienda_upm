package es.upm.etsisi.poo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class AppLogger {

    private static final Logger APP_LOGGER =
            LogManager.getLogger("APP");

    private AppLogger() {}

    public static void info(String message) {
        APP_LOGGER.info(message);
    }

    public static void warn(String message) {
        APP_LOGGER.warn(message);
    }

    public static void error(String message) {
        APP_LOGGER.error(message);
    }
}
