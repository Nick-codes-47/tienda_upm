package es.upm.etsisi.poo;

import es.upm.etsisi.poo.Handlers.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputDriver {

    private final Scanner input;
    private String inputFile = null;
    private static final String PROMPT = "tUPM> ";

    private static final String EXIT_LINE = "exit";

    public InputDriver()
    {
        input = new Scanner(System.in);
    }

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

    public Request next()
    {
        System.out.print(PROMPT); // can't use logger here because we don't want a line break
        String line;
        try {
            line = input.nextLine();
        } catch (NoSuchElementException e) { line = EXIT_LINE; }
        if (inputFile != null)
        {
            AppLogger.info(line);
        }
        return new Request(line);
    }
}
