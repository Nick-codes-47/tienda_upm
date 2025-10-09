package es.upm.etsisi.poo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class InputDriver {
    public InputDriver()
    {
        input = new Scanner(System.in);
    }

    public InputDriver(String inputFile)
            throws RuntimeException // TODO custom exceptions
    {
        try
        {
            input = new Scanner(new File(inputFile));
        }
        catch (FileNotFoundException exception)
        {
            throw new RuntimeException(String.format("Missing config file: %s\n", inputFile));
        }
    }

    public Request nextRequest()
    {
        System.out.print(PROMPT);
        return new Request(input.nextLine());
    }

    private final Scanner input;

    private static final String PROMPT = "tUPM> ";
}
