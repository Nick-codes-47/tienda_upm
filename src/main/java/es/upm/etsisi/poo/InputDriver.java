package es.upm.etsisi.poo;

import es.upm.etsisi.poo.Handlers.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Gestiona la entrada de datos de la aplicación.
 * <p>
 * Permite leer comandos tanto desde la entrada estándar (teclado/consola)
 * como desde un archivo de texto (modo batch/script).
 */
public class InputDriver {

    private final Scanner input;
    private String inputFile = null;
    private static final String PROMPT = "tUPM> ";

    private static final String EXIT_LINE = "exit";

    /**
     * Constructor para modo interactivo.
     * Inicializa el Scanner con {@code System.in}.
     */
    public InputDriver()
    {
        input = new Scanner(System.in);
    }

    /**
     * Constructor para modo archivo.
     * Inicializa el Scanner leyendo desde el archivo especificado.
     *
     * @param inputFile Ruta del archivo que contiene los comandos a ejecutar.
     * @throws RuntimeException Si el archivo no existe o no se puede leer.
     */
    public InputDriver(String inputFile)
            throws RuntimeException
    {
        AppLogger.info(String.format("LOG::InputDriver Received input file: %s\n", inputFile));
        this.inputFile = inputFile;

        try
        {
            input = new Scanner(new File(inputFile));
        }
        catch (FileNotFoundException exception)
        {
            throw new RuntimeException(String.format("Missing input file: %s\n", inputFile));
        }
    }

    /**
     * Lee la siguiente línea de entrada y la encapsula en un objeto {@link Request}.
     * <p>
     * Muestra el prompt si es necesario. Si se alcanza el final de la entrada (EOF),
     * devuelve automáticamente el comando de salida ("exit").
     * Si se está leyendo desde un archivo, hace un log de la línea leída para trazabilidad.
     *
     * @return Un objeto {@link Request} con la línea leída parseada.
     */
    public Request next()
    {
        System.out.print(PROMPT); // can't use logger here because we don't want a line break
        String line;
        try {
            line = input.nextLine();
        } catch (NoSuchElementException e) { line = EXIT_LINE; }
        if (inputFile != null)
        {
            AppLogger.debug(line);
        }
        return new Request(line);
    }
}
