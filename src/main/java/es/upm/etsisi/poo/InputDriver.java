package es.upm.etsisi.poo;

import es.upm.etsisi.poo.Handlers.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class InputDriver {
    public InputDriver()
    {
        input = new Scanner(System.in);
    }

    public InputDriver(String inputFile)
            throws RuntimeException
    {
//        System.err.printf("LOG::InputDriver Received input file: %s\n", inputFile);
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
        System.out.print(PROMPT);
        String line = input.nextLine();
        if (inputFile != null)
        {
            System.out.println(line);
        }
        return new Request(line);
    }

    private final Scanner input;
    private String inputFile = null;
    private static final String PROMPT = "tUPM> ";
}
