package es.upm.etsisi.poo;

import java.util.Arrays;
import java.util.Scanner;

public class CommandParser {

    public CommandParser()
    {
    }

    public void close()
    {
        input.close();
    }

    public Command nextCommand()
    {
        String[] tokens = input.next().split("\\s+"); // using spaces without living empty Strings
        return new Command(tokens[1], tokens[2], Arrays.copyOfRange(tokens, 2, tokens.length));
    }

    private final Scanner input = new Scanner(System.in);
}
